package com.example.stoycho.phonebook.communicationClasses.commands;

import android.content.Context;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lyubomir.babev on 20/04/2016.
 */
public class CommandLogin extends BaseComponentCommunication {
    private static String   TAG_USERNAME        = "email";
    private static String   TAG_PASSWORD        = "password";
    private static String   TAG_SESSION         = "session";
    private static String   TAG_TEMP_PASS       = "temp_pass";

    private String          mSession;
    private boolean         mTempPass;

    public CommandLogin(Context context, onAnswerReceiveListener listener) {
        super(context, listener, AllCommands.COMMAND_LOGIN);
        setmInputParameters(new JSONObject());
    }

    public void setInputParameters(String username, String password){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TAG_USERNAME  , username);
            jsonObject.put(TAG_PASSWORD   , password);
            setmInputParameters(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processData(Stanza stanza){
        super.processData(stanza);
        String body = ((Message)stanza).getBody();
        try{
            JSONObject jsonObject   = new JSONObject(body);

            mSession    = jsonObject.getString(TAG_SESSION);
            mTempPass   = jsonObject.getBoolean(TAG_TEMP_PASS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSession(){
        return mSession;
    }

    public boolean getTempPass(){
        return mTempPass;
    }
}