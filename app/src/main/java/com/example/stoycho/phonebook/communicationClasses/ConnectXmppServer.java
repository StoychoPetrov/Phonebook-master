package com.example.stoycho.phonebook.communicationClasses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.stoycho.phonebook.activities.HomeActivity;
import com.example.stoycho.phonebook.loggerClasses.ConnectionStatusLogger;
import com.example.stoycho.phonebook.loggerClasses.DataLogger;
import com.example.stoycho.phonebook.utils.Utils;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.sasl.core.SCRAMSHA1Mechanism;
import org.jivesoftware.smack.sm.StreamManagementException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class ConnectXmppServer {

    private Context                             mContext;
    private String                              mUsername;
    private String                              mPassword;

    private onXmppConnectionListener            mConnListener;
    private ConnectionListener                  mConnectionListener;
    private XMPPTCPConnection                   mXmppConnection;
    private boolean                             mIsReconnecting                 = false;
    private static ConnectXmppServer            instance;

    public static synchronized ConnectXmppServer getInstance(Context context) {
        if (instance == null) {
            SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(context);
            String username                 = preferences.getString(Utils.PREFERENCES_JID, "");

            username = username.replace('@','-');
            username = username.replace('.','-');

            String password                 = preferences.getString(Utils.PREFERENCES_PASS, "");

            if( password.equalsIgnoreCase("") )
                return null;

            instance = new ConnectXmppServer(context, username, password);
            Utils.getInstance().setmConnectXmppServer(instance);
        }
        return instance;
    }

    public static synchronized ConnectXmppServer getInstance(Context context, String username, String password) {
        if (instance == null) {
            username = username.replace('@','-').replace('.','-');

            if( password.equalsIgnoreCase("") )
                return null;

            instance = new ConnectXmppServer(context, username, password);
            Utils.getInstance().setmConnectXmppServer(instance);
        }
        return instance;
    }

    public interface onXmppConnectionListener {
        void onConnect();
        void onError();
    }

    public void setOnXmppConnectionEstablishListener (onXmppConnectionListener listener){
        mConnListener = listener;
    }

    public ConnectXmppServer(Context context, String username, String password){
        mContext        = context;
        mUsername       = username;
        mPassword       = password;
    }

    public void connect() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if( mXmppConnection != null && mXmppConnection.isConnected() )
                        mXmppConnection.disconnect();

                    setConnection();
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                try {
                    mXmppConnection.connect();

                    mXmppConnection.login(mUsername, mPassword, "Phone");

                } catch (XMPPException | SmackException | IOException e) {
                    e.printStackTrace();
                    mConnListener.onError();
                }
            }
        });
        thread.start();
    }

    private synchronized void setConnection() throws XmppStringprepException {
        XMPPTCPConnectionConfiguration connConfig = XMPPTCPConnectionConfiguration.builder()
                .setHost(Utils.HOST)
                .setPort(Utils.PORT)
                .setServiceName(Utils.HOST)
                .setConnectTimeout(10000)
                .setSendPresence(false)
                .build();

        XMPPTCPConnection.setUseStreamManagementDefault(true);
        SASLAuthentication.registerSASLMechanism(new SCRAMSHA1Mechanism());

        mXmppConnection = new XMPPTCPConnection(connConfig);
        mXmppConnection.setPacketReplyTimeout(5000);
        mXmppConnection.setUseStreamManagement(true);
        mXmppConnection.setPreferredResumptionTime(0);
        mXmppConnection.setUseStreamManagementResumption(false);
        mXmppConnection.setFromMode(XMPPConnection.FromMode.USER);
        mXmppConnection.setReplyToUnknownIq(true);

        Utils.getInstance().setmXmppConnection(mXmppConnection);

        addConnectionListener();

        logDataPackets();
    }

    StanzaListener mStanzaReceiveListener;
    StanzaListener mStanzaSentListener;

    private void logDataPackets(){
        if ( mStanzaReceiveListener != null )
            mXmppConnection.removeAsyncStanzaListener(mStanzaReceiveListener);
        if ( mStanzaSentListener != null )
            mXmppConnection.removePacketSendingListener(mStanzaSentListener);

        mStanzaReceiveListener = new StanzaListener() {
            @Override
            public void processPacket(Stanza stanza) throws SmackException.NotConnectedException {
                DataLogger.log("MESSAGE RECEIVED", stanza.toString().getBytes().length, stanza.toString());
                Log.d("MESSAGE RECEIVED", stanza.toString().replace("&quot;", "'").replace("&apos;", "'"));
            }
        };
        mXmppConnection.addAsyncStanzaListener(mStanzaReceiveListener, new StanzaTypeFilter(Message.class));

        mStanzaSentListener = new StanzaListener() {
            @Override
            public void processPacket(Stanza stanza) throws SmackException.NotConnectedException {
                DataLogger.log("MESSAGE SENT", stanza.toString().getBytes().length, stanza.toString());
                Log.d("MESSAGE SENT", stanza.toString().replace("&quot;", "'").replace("&apos;", "'"));
            }
        };
        mXmppConnection.addPacketSendingListener(mStanzaSentListener, new StanzaTypeFilter(Message.class));
    }


    public void addConnectionListener(){
        if ( mConnectionListener != null )
            mXmppConnection.removeConnectionListener(mConnectionListener);

        mConnectionListener = new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {
                ConnectionStatusLogger.log("XMPP Connection", "CONNECTED");
                Log.i("XMPPChatDemoActivity", "SUCCESSFUL connected" );
            }

            @Override
            public void authenticated(XMPPConnection xmppConnection, boolean b) {
                ConnectionStatusLogger.log("XMPP Connection", "AUTHENTICATED");
                Log.i("XMPPChatDemoActivity", "SUCCESSFUL LOG IN" );
                onAuthenticatedComplete();
            }

            @Override
            public void connectionClosed() {
                ConnectionStatusLogger.log("XMPP Connection", "CONNECTION CLOSED");
                Log.i("XMPPChatDemoActivity", "CONNECTION CLOSED");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                ConnectionStatusLogger.log("XMPP Connection", "CONNECTION CLOSED");
                Log.i("XMPPChatDemoActivity", "CONNECTION CLOSED WITH ERROR" );

                if( e.toString().contains("Replaced by new connection")){
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                }
                else
                    reconnect();
            }

            @Override
            public void reconnectionSuccessful() {
                ConnectionStatusLogger.log("XMPP Connection", "RECONNECTED");
                Log.i("XMPPChatDemoActivity", "RECONNECTED SUCCESSFUL" );
            }

            @Override
            public void reconnectingIn(int seconds) {
                ConnectionStatusLogger.log("XMPP Connection", "RECONNECTING SECONDS: " + seconds);
                Log.i("XMPPChatDemoActivity", "RECONNECTING SECONDS: " + seconds );
            }

            @Override
            public void reconnectionFailed(Exception ex) {
                ConnectionStatusLogger.log("XMPP Connection", "RECONNECTION FAILED");
                Log.i("XMPPChatDemoActivity", "RECONNECTION FAILED");
                reconnect();
            }
        };
        mXmppConnection.addConnectionListener(mConnectionListener);
    }


    private void onAuthenticatedComplete(){
        try {
            if (mConnListener != null)
                mConnListener.onConnect();

            if( mXmppConnection.isSmAvailable() && mXmppConnection.isSmEnabled()) {
                mXmppConnection.requestSmAcknowledgement();
                mXmppConnection.sendSmAcknowledgement();
            }
        } catch (SmackException.NotConnectedException | StreamManagementException.StreamManagementNotEnabledException e) {
            e.printStackTrace();
            reconnect();
        }
    }

    public void reconnect(){
        if ( !mIsReconnecting ) {
            mIsReconnecting = true;
            long sleepTime = 2000;

            while ( mXmppConnection == null || !mXmppConnection.isConnected() || !mXmppConnection.isAuthenticated() ) {
                try {
                    if ( mXmppConnection == null ){
                        setConnection();
                    }

                    if( !mXmppConnection.isConnected() )
                        mXmppConnection.connect();

                    if ( !mXmppConnection.isAuthenticated() )
                        mXmppConnection.login(mUsername, mPassword, "Phone");

                } catch (SmackException | IOException | XMPPException | NullPointerException ex) {
                    ex.printStackTrace();

                    try {
                        ConnectionStatusLogger.log("XMPP Connection", "WAIT " + sleepTime/1000 + " SECONDS before next reconnection try");
                        Log.i("XMPPChatDemoActivity", "WAIT " + sleepTime/1000 + " SECONDS");
                        Thread.sleep(sleepTime);
                        if( sleepTime < 60000)
                            sleepTime += 1000;
                        ConnectionStatusLogger.log("XMPP Connection", "RECONNECTION TRY");
                        Log.i("XMPPChatDemoActivity", "TRY AGAIN");
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }  catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
            }
            mIsReconnecting = false;
        }
    }

    public String getUsername(){
        return mUsername;
    }

    public String getPassword(){
        return mPassword;
    }

    public void setUsername(String username){
        mUsername = username.replace('@','-').replace('.','-');
    }

    public void setPassword(String password){
        mPassword = password;
    }

    public XMPPTCPConnection getXmppConnection(){
        return mXmppConnection;
    }
}