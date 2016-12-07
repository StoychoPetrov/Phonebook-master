package com.example.stoycho.phonebook.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.stoycho.phonebook.database.Database;

/**
 * Created by Stoycho on 10/20/2016.
 */

public class User {

    private int mId;
    private String mFirstName;
    private String mLastName;
    private int mCountryId;
    private String mEmail;
    private String mPhoneNumber;
    private String mGender;

    public User() {
    }

    public User(String firstName, String lastName, int countryId, String email, String phoneNumber, String gender) {
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mCountryId = countryId;
        this.mEmail = email;
        this.mPhoneNumber = phoneNumber;
        this.mGender = gender;
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
}
