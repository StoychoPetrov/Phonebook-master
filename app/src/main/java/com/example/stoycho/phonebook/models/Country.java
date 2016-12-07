package com.example.stoycho.phonebook.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Country {

    private int mId;
    private String mCountryName;
    private String mCallingCode;

    public Country() {
    }

    public Country(String countryName, String mCallingCode) {
        this.mCountryName   = countryName;
        this.mCallingCode   = mCallingCode;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String countryName) {
        this.mCountryName = countryName;
    }

    public String getCallingCode() {
        return mCallingCode;
    }

    public void setCallingCode(String mCallingCode) {
        this.mCallingCode = mCallingCode;
    }

    private void parcefromJson(JSONObject country) throws JSONException {
        JSONArray countryCodes = country.getJSONArray("mCallingCodes");
        this.mCountryName = country.getString("name");
        this.mCallingCode = countryCodes.length() > 0 ? countryCodes.getString(0) : null;
    }

    public static List<Country> parceCountriesFromJson(String countriesForParce) {
        List<Country> countries = new ArrayList<>();
        try {
            JSONArray countriesJson = new JSONArray(countriesForParce);
            for (int i = 0; i < countriesJson.length(); i++) {
                Country country = new Country();
                country.parcefromJson(countriesJson.getJSONObject(i));
                countries.add(country);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countries;
    }
}