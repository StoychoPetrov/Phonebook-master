package com.example.stoycho.phonebook.communicationClasses.commands;

import android.content.Context;

import com.example.stoycho.phonebook.models.Country;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by kamen.troshev on 10.10.2016 г..
 */
public class CommandGetActiveCountries extends BaseComponentCommunication {

    private final static String TAG_COUNTRIES       = "countries";
    private final static String TAG_RISK_LEVEL      = "risk_level";

    private final static int INDEX_COUNTRY_CODE     = 0;
    private final static int INDEX_PHONE_CODE       = 1;
    private final static int INDEX_COUNTRY_NAME     = 2;

    public final static int RISK_TYPE_ALL           = 0;
    public final static int RISK_TYPE_NO_RISK       = 1;
    public final static int RISK_TYPE_RISK          = 2;


    private ArrayList<Country> mCountries = new ArrayList<>();

    public CommandGetActiveCountries(Context context, onAnswerReceiveListener listener) {
        super(context, listener, AllCommands.COMMAND_GET_ACTIVE_COUNTRIES);
        setmInputParameters(new JSONObject());
    }

    public void setInputParameters(int riskType){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TAG_RISK_LEVEL  , riskType);
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

            deserializeCountries(jsonObject.getJSONArray(TAG_COUNTRIES));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deserializeCountries(JSONArray jsonArrayCountries){
        for( int i = 0; i < jsonArrayCountries.length(); i++ ){
            try {
                JSONArray country = jsonArrayCountries.getJSONArray(i);
                Country countryModel = new Country();
                countryModel.setCountryName(country.getString(INDEX_COUNTRY_NAME));
                countryModel.setCallingCode(country.getString(INDEX_PHONE_CODE));
                mCountries.add(countryModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Country> getCountries() {
        return mCountries;
    }
}

