package com.example.stoycho.phonebook.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.stoycho.phonebook.database.Database;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stoycho on 10/20/2016.
 */

public class User implements Parcelable{

    private int     mId;
    private String  mFirstName;
    private String  mLastName;
    private int     mCountryId;
    private String  mEmail;
    private String  mPhoneNumber;
    private String  mGender;

    public User() {
    }

    public User(String firstName, String lastName, int countryId, String email, String phoneNumber, String gender) {
        this.mFirstName     = firstName;
        this.mLastName      = lastName;
        this.mCountryId     = countryId;
        this.mEmail         = email;
        this.mPhoneNumber   = phoneNumber;
        this.mGender        = gender;
    }

    public User(Parcel in)
    {
        String[] data = new String[7];
        in.readStringArray(data);
        this.mId            = Integer.parseInt(data[0]);
        this.mFirstName     = data[1];
        this.mLastName      = data[2];
        this.mCountryId     = Integer.parseInt(data[3]);
        this.mEmail         = data[4];
        this.mPhoneNumber   = data[5];
        this.mGender        = data[6];
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public int getCountry() {
        return mCountryId;
    }

    public void setCountry(int countryId) {
        this.mCountryId = countryId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String male) {
        this.mGender = male;
    }

    private JSONObject parseToJson()
    {
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("id",this.mId);
            userJson.put("firstName",this.mFirstName);
            userJson.put("lastName",this.mLastName);
            userJson.put("countryId",this.mCountryId);
            userJson.put("email",this.mEmail);
            userJson.put("phoneNumber",this.mPhoneNumber);
            userJson.put("gender",this.mGender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userJson;
    }

    private User parceFromJson(JSONObject object)
    {
        User user = new User();
        try {
            user.setId(object.getInt("id"));
            user.setFirstName(object.getString("firstName"));
            user.setLastName(object.getString("lastName"));
            user.setCountry(object.getInt("countryId"));
            user.setEmail(object.getString("email"));
            user.setPhoneNumber(object.getString("phoneNumber"));
            user.setGender(object.getString("gender"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {String.valueOf(this.mId),this.mFirstName,this.mLastName, String.valueOf(this.mCountryId),this.mEmail,this.mPhoneNumber,this.mPhoneNumber,this.mGender});
    }

    public static final Parcelable.Creator CREATOR = new ClassLoaderCreator() {
        @Override
        public Object createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return null;
        }

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new User[i];
        }
    };
}
