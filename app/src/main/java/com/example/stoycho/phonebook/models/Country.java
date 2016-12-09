package com.example.stoycho.phonebook.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Country implements Parcelable {

    private int     mId;
    private String  mCountryName;
    private String  mCallingCode;

    public Country() {}

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

    public Country(Parcel in)
    {
        String[] data = new String[3];
        in.readStringArray(data);
        this.mId            = Integer.parseInt(data[0]);
        this.mCountryName   = data[1];
        this.mCallingCode   = data[2];
    }

    private void parcefromJson(JSONObject country) throws JSONException {
        JSONArray countryCodes  = country.getJSONArray("callingCodes");
        this.mCountryName       = country.getString("name");
        this.mCallingCode       = countryCodes.length() > 0 ? countryCodes.getString(0) : null;
    }

    public JSONObject parceToJson()
    {
        JSONObject countryJson = new JSONObject();
        try {
            countryJson.put("name",this.mCountryName);
            countryJson.put("callingCode",this.mCallingCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countryJson;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{String.valueOf(this.mId),this.mCountryName,this.getCallingCode()});
    }

    public static final Parcelable.Creator CREATOR = new ClassLoaderCreator() {
        @Override
        public Object createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return null;
        }

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Country(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Country[i];
        }
    };
}