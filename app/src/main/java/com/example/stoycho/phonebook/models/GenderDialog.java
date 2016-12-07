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

    private Context     mContext;
    private String      mGender;
    private TextView    mAllTxt;
    private TextView    mMaleTxt;
    private TextView    mFemaleTxt;

    public GenderDialog(Context context) {
        super(context);
        mContext = context;
    }

    public GenderDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected GenderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gender_dialog);

        mAllTxt     = (TextView) findViewById(R.id.all);
        mMaleTxt    = (TextView) findViewById(R.id.male);
        mFemaleTxt  = (TextView) findViewById(R.id.female);
        RelativeLayout mAllLayout = (RelativeLayout) findViewById(R.id.allLayout);
        RelativeLayout maleLayout = (RelativeLayout) findViewById(R.id.maleLayout);
        RelativeLayout femaleLayout = (RelativeLayout) findViewById(R.id.femaleLayout);
        mAllLayout.setOnClickListener(this);
        maleLayout.setOnClickListener(this);
        femaleLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.allLayout:
                mGender = mAllTxt.getText().toString();
                break;
            case R.id.maleLayout:
                mGender = mMaleTxt.getText().toString();
                break;
            case R.id.femaleLayout:
                mGender = mFemaleTxt.getText().toString();
                break;
        }
        dismiss();
    }

    public String getmGender()
    {
        return mGender;
    }
}
