package com.example.stoycho.phonebook.models;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;

/**
 * Created by Stoycho on 10/22/2016.
 */

public class GenderDialog extends Dialog implements View.OnClickListener{

    private String      mGender;
    private TextView    mAllTxt;
    private TextView    mMaleTxt;
    private TextView    mFemaleTxt;

    public GenderDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gender_dialog);

        mAllTxt     = (TextView)    findViewById(R.id.all);
        mMaleTxt    = (TextView)    findViewById(R.id.male);
        mFemaleTxt  = (TextView)    findViewById(R.id.female);

        mAllTxt.setOnClickListener(this);
        mMaleTxt.setOnClickListener(this);
        mFemaleTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View clickedView) {
        int id = clickedView.getId();
        switch (id)
        {
            case R.id.all:
                mGender = mAllTxt.getText().toString();
                break;
            case R.id.male:
                mGender = mMaleTxt.getText().toString();
                break;
            case R.id.female:
                mGender = mFemaleTxt.getText().toString();
                break;
        }
        dismiss();
    }

    public String getGender()
    {
        return mGender;
    }
}
