package com.example.stoycho.phonebook.models;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;

/**
 * Created by Stoycho on 10/22/2016.
 */

public class InformationDialog extends Dialog {

    private Context mContext;
    private Country mCountry;
    private User    mUser;

    protected InformationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public InformationDialog(Context context,Country country, User user) {
        super(context);
        this.mContext   = context;
        this.mCountry   = country;
        this.mUser      = user;
    }

    public InformationDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.information_dialog);
        TextView firstNameTxt      = (TextView) findViewById(R.id.first_name);
        TextView lastNameTxt       = (TextView) findViewById(R.id.last_name);
        TextView countryTxt        = (TextView) findViewById(R.id.country);
        TextView emailTxt          = (TextView) findViewById(R.id.email);
        TextView phoneNumberTxt    = (TextView) findViewById(R.id.phone_number);
        TextView genderTxt         = (TextView) findViewById(R.id.gender);

        String phone = mContext.getString(R.string.plus) + mCountry.getCallingCode() + mUser.getPhoneNumber();

        firstNameTxt.  setText(mUser.getFirstName());
        lastNameTxt.   setText(mUser.getLastName());
        countryTxt.    setText(mCountry.getCountryName());
        emailTxt.      setText(mUser.getEmail());
        phoneNumberTxt.setText(phone);
        genderTxt.     setText(mUser.getGender());
    }
}
