package com.example.stoycho.phonebook.communicationClasses.commands;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.view.View;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.activities.HomeActivity;
import com.example.stoycho.phonebook.utils.GlobalData;
import com.example.stoycho.phonebook.utils.Utils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseComponentCommunication implements StanzaListener {
    protected static final String           TAG_COMMAND     = "command";
    protected static final String           TAG_OS          = "os";
    protected static final String           TAG_VERSION     = "version";
    protected static final String           TAG_STATUS      = "status";
    protected static final String           TAG_IP          = "ip";
    protected static final String           TAG_LANGUAGE    = "language";
    protected static final String           TAG_SESSION     = "session";
    protected static final String           TAG_ORIGINATOR  = "originator";

    protected onAnswerReceiveListener       mListener;
    protected Context                       mContext;
    protected String                        mComponent;
    protected int                           mCommand;
    protected int                           mStatus;
    protected JSONObject                    mInputParameters;

    protected View                          mProgressBackground;

    protected boolean                       mHideProgressOnComplete = true;
    protected boolean                       mShowProgressBar        = true;
    protected boolean                       mShowErrorToast         = true;
    protected boolean                       mProceedErrorAnswer     = false;

    private final int                       TIME_OUT_IN_MILISECONDS         = 30000;
    private boolean                         mReceiveCommand                 = false;

    private XMPPConnection                  mXmppConnection;

    private String                          mStanzaId;

    public String getmStanzaId() {
        return mStanzaId;
    }

    public interface onAnswerReceiveListener {
        void onAnswerReceived(int command, String body);
    }

    public BaseComponentCommunication(Context context, onAnswerReceiveListener listener, int command){
        mContext        = context;
        mCommand        = command;
        mComponent      = Utils.FINANCE_OPERATIONS_COMPONENT;
        mListener       = listener;
        mXmppConnection = Utils.getInstance().getmXmppConnection();
    }

    public void sendCommand(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Message ack = new Message(mComponent, Message.Type.chat);
                mStanzaId = ack.getStanzaId();

                if (!Utils.isNetworkConnectionAvailable(mContext)) {
                    Utils.showErrorToast(mContext, mContext.getString(R.string.no_internet_connection));
                    return;
                }

                if ( mXmppConnection == null  || !mXmppConnection.isAuthenticated()){
                    if( mShowErrorToast )
                        Utils.showErrorToast(mContext, mContext.getString(R.string.connection_problem));
                    return;
                }

                mXmppConnection.addAsyncStanzaListener(BaseComponentCommunication.this, new StanzaTypeFilter(Message.class));

                    try {
                        String ip = Utils.getLocalIpAddress();
                        String language = "en";

                        if (ip == null || ip.equalsIgnoreCase("")) {
                            Utils.showErrorToast(mContext, mContext.getString(R.string.connection_problem));
                            return;
                        }

                        mInputParameters.put(TAG_COMMAND, mCommand);
                        mInputParameters.put(TAG_OS, Integer.parseInt(Utils.ANDROID));
                        mInputParameters.put(TAG_VERSION, (double) mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode);
                        mInputParameters.put(TAG_IP, ip);
                        mInputParameters.put(TAG_LANGUAGE, language);
                        mInputParameters.put(TAG_SESSION, GlobalData.getInstance().getSession());
                        mInputParameters.put(TAG_ORIGINATOR, Utils.MYPOS_ORIGINATOR);

                        ack.setBody(mInputParameters.toString());
                        mXmppConnection.sendStanza(ack);
                        checkForTimeout();
                    } catch (PackageManager.NameNotFoundException | SmackException.NotConnectedException | JSONException | NullPointerException e) {
                        e.printStackTrace();
                        mXmppConnection.removeAsyncStanzaListener(BaseComponentCommunication.this);
                        if (mShowErrorToast)
                            Utils.showErrorToast(mContext, mContext.getString(R.string.connection_problem));
                    }
            }
        });
        thread.start();
    }

    @Override
    public void processPacket(Stanza stanza) throws SmackException.NotConnectedException {
        if( !stanza.getFrom().equalsIgnoreCase(mComponent) || ((Message)stanza).getType().equals(Message.Type.error) || !mStanzaId.equalsIgnoreCase(stanza.getStanzaId()))
            return;

        JSONObject jsonObject;
        try {
            mReceiveCommand = true;
            jsonObject = new JSONObject(((Message)stanza).getBody());
            if( jsonObject.getInt(TAG_COMMAND) != mCommand )
                return;

            mStatus = jsonObject.getInt(TAG_STATUS);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        mXmppConnection.removeAsyncStanzaListener(this);

        if( mHideProgressOnComplete )

        try {
            if ( jsonObject.getInt(TAG_STATUS) != Response.RESPOND_STATUS_SUCCESS) {
                if( jsonObject.getInt(TAG_STATUS) == Response.RESPOND_STATUS_INVALID_SESSION ){
                    try {
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                        Utils.showErrorToast(mContext, mContext.getString(R.string.your_session_has_expired));
                        return;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if( mShowErrorToast )
                    onError(jsonObject.getInt(TAG_STATUS));

                if( !mProceedErrorAnswer )
                    return;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        processData(stanza);

        if( mListener != null )
            mListener.onAnswerReceived(mCommand, ((Message) stanza).getBody());
    }

    protected void onError(int status){
        Utils.showErrorToast(mContext, Response.getErrorMessage(mContext, status, mCommand));
    }

    protected void processData(Stanza stanza){

    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setmInputParameters(JSONObject inputParameters) {
        mInputParameters = inputParameters;
    }

    public void setmHideProgressOnComplete(boolean mHideProgressOnComplete) {
        this.mHideProgressOnComplete = mHideProgressOnComplete;
    }

    public void setmShowErrorToast(boolean mShowErrorToast) {
        this.mShowErrorToast = mShowErrorToast;
    }

    public void setmProceedErrorAnswer(boolean mProceedErrorAnswer) {
        this.mProceedErrorAnswer = mProceedErrorAnswer;
    }

    public boolean ismShowProgressBar() {
        return mShowProgressBar;
    }

    public void setmShowProgressBar(boolean mShowProgressBar) {
        this.mShowProgressBar = mShowProgressBar;
    }

    public void setmXmppConnection(XMPPConnection mXmppConnection) {
        this.mXmppConnection = mXmppConnection;
    }

    public void setCustomProgressBackground(View progressBackground){
        mProgressBackground = progressBackground;
    }

    private void checkForTimeout() {
        long startTime = SystemClock.uptimeMillis();
        while( startTime + TIME_OUT_IN_MILISECONDS > SystemClock.uptimeMillis()) {
            if (mReceiveCommand) {
                mReceiveCommand = false;
                return;
            }
        }

        Utils.showErrorToast(mContext, mContext.getString(R.string.service_temporarily_unavailable));
    }
}

