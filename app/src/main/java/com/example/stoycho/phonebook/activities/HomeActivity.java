package com.example.stoycho.phonebook.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stoycho.phonebook.Interfaces.OnRecyclerItemClick;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.UsersRecyclerAdapter;
import com.example.stoycho.phonebook.communicationClasses.ConnectXmppServer;
import com.example.stoycho.phonebook.communicationClasses.commands.AllCommands;
import com.example.stoycho.phonebook.communicationClasses.commands.BaseComponentCommunication;
import com.example.stoycho.phonebook.communicationClasses.commands.CommandGetActiveCountries;
import com.example.stoycho.phonebook.communicationClasses.commands.CommandLogin;
import com.example.stoycho.phonebook.communicationClasses.commands.Response;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.database.UsersDatabaseCommunication;
import com.example.stoycho.phonebook.fragments.CountriesFragment;
import com.example.stoycho.phonebook.fragments.RegistrationFragment;
import com.example.stoycho.phonebook.models.Country;
import com.example.stoycho.phonebook.models.User;
import com.example.stoycho.phonebook.utils.Constants;
import com.example.stoycho.phonebook.utils.GlobalData;
import com.example.stoycho.phonebook.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HomeActivity extends FragmentActivity implements View.OnClickListener,FragmentManager.OnBackStackChangedListener,OnRecyclerItemClick,RadioGroup.OnCheckedChangeListener,View.OnTouchListener,TextWatcher,ConnectXmppServer.onXmppConnectionListener,BaseComponentCommunication.onAnswerReceiveListener {

    private RecyclerView                mRecyclerView;
    private UsersRecyclerAdapter        mRecyclerAdapter;
    private List<Country>               mCountries;
    private List<User>                  mUsers;
    private FloatingActionButton        mNewContactButton;
    private TextView                    mTitleTxt;
    private TextView                    mEmptyTxt;
    private EditText                    mCountryEdb;
    private EditText                    mSearchCountryEdb;
    private Country                     mSelectedFilterCountry;
    private RelativeLayout              mFilterLayout;
    private RelativeLayout              mBar;
    private ImageButton                 mFilterButton;
    private ImageButton                 mSearchButton;
    private ImageButton                 mCloseButton;
    private RadioGroup                  mGenderRadioGroup;
    private Button                      mResetButton;
    private Button                      mApplyButton;

    private String                      mFilterGender;
    private int                         mFirstTouchPositionY;
    private int                         mFilterCurrentTopMargin;
    private int                         mFilterLayoutStartTopMargin;
    private boolean                     mStartAnimation;
    private boolean                     mNotCountrySearching;
    private CommandLogin                mCommandLogin;
    private CommandGetActiveCountries   mCommandGetActiveCountries;
    private int                         mSelectedItemPosition;

    private final static int    PICK_IMAGE                              = 1;
    private final static String ALL_COUNTRIES_ARE_SELECTED              = "all_selected";
    private final        int    MY_PERMISSIONS_REQUEST_CALL_PHONE       = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        setConnection();

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerAdapter);

        setListeners();
        loadUsers();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        checkPermissions();

        mFilterLayout.measure(0,0);
        mBar.measure(0,0);
        RelativeLayout.LayoutParams filterLayoutParams = (RelativeLayout.LayoutParams) mFilterLayout.getLayoutParams();

        mFilterLayoutStartTopMargin         = filterLayoutParams.bottomMargin;
        filterLayoutParams.bottomMargin     = getResources().getDisplayMetrics().heightPixels - mBar.getMeasuredHeight();
        filterLayoutParams.topMargin        = mBar.getMeasuredHeight() - getResources().getDisplayMetrics().heightPixels;
        mFilterLayout.setLayoutParams(filterLayoutParams);

    }

    private void setConnection()
    {
        ConnectXmppServer connectXmppServer = ConnectXmppServer.getInstance(this,Utils.USERNAME_TEST,Utils.PASSWORD_TEST);
        if(connectXmppServer != null) {
            connectXmppServer.setUsername(Utils.USERNAME_TEST);
            connectXmppServer.setPassword(Utils.PASSWORD_TEST);
            connectXmppServer.setOnXmppConnectionEstablishListener(this);
            connectXmppServer.connect();
        }
    }

    private void initUI()
    {
        mNewContactButton       = (FloatingActionButton)    findViewById(R.id.add_user);
        mRecyclerView           = (RecyclerView)            findViewById(R.id.recycleView);
        mFilterLayout           = (RelativeLayout)          findViewById(R.id.filter_layout);
        mBar                    = (RelativeLayout)          findViewById(R.id.bar);
        mTitleTxt               = (TextView)                findViewById(R.id.title);
        mEmptyTxt               = (TextView)                findViewById(R.id.empty_txt);
        mFilterButton           = (ImageButton)             findViewById(R.id.filter_button);
        mSearchButton           = (ImageButton)             findViewById(R.id.search_button);
        mCloseButton            = (ImageButton)             findViewById(R.id.close_button);
        mCountryEdb             = (EditText)                findViewById(R.id.country_editbox);
        mSearchCountryEdb       = (EditText)                findViewById(R.id.search);
        mGenderRadioGroup       = (RadioGroup)              findViewById(R.id.gender_radio);
        mResetButton            = (Button)                  findViewById(R.id.reset_button);
        mApplyButton            = (Button)                  findViewById(R.id.apply_button);

        mRecyclerAdapter        = new UsersRecyclerAdapter(this,mUsers);
    }

    private void setListeners()
    {
        mNewContactButton.setOnClickListener(this);
        mRecyclerAdapter.setOnItemClickListener(this);
        mFilterButton.setOnClickListener(this);
        mCountryEdb.setOnClickListener(this);
        mGenderRadioGroup.setOnCheckedChangeListener(this);
        mFilterLayout.setOnTouchListener(this);
        mSearchCountryEdb.addTextChangedListener(this);
        mSearchButton.setOnClickListener(this);
        mApplyButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);
    }

    private void animateFilter(float topMargin, float increaseHeight)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(topMargin,increaseHeight);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ((RelativeLayout.LayoutParams)mFilterLayout.getLayoutParams()).bottomMargin = ((Float) valueAnimator.getAnimatedValue()).intValue();
                ((RelativeLayout.LayoutParams)mFilterLayout.getLayoutParams()).topMargin = -((Float) valueAnimator.getAnimatedValue()).intValue();
                mFilterLayout.requestLayout();
            }
        });

        valueAnimator.start();

        if(increaseHeight == mFilterLayoutStartTopMargin)
            mNewContactButton.setVisibility(View.GONE);
        else
            mNewContactButton.setVisibility(View.VISIBLE);
    }

    private void checkPermissions()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }
    }

    private void replaceElementsWithFade(final View visibleView, final View goneView)
    {

        AlphaAnimation fadeInAnimation      = new AlphaAnimation(0,1);
        AlphaAnimation fadeOutAnimation     = new AlphaAnimation(1,0);

        fadeOutAnimation.setDuration(getResources().getInteger(R.integer.fade_animation_duration));
        fadeOutAnimation.setInterpolator(new LinearInterpolator());

        fadeInAnimation.setDuration(getResources().getInteger(R.integer.fade_animation_duration));
        fadeInAnimation.setInterpolator(new LinearInterpolator());
        fadeInAnimation.setStartOffset(getResources().getInteger(R.integer.fade_animation_start_offset));

        visibleView.startAnimation(fadeOutAnimation);
        goneView.startAnimation(fadeInAnimation);

        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goneView.setVisibility(View.VISIBLE);
                mStartAnimation = false;

                if(goneView.getId() == R.id.search) {
                    mSearchCountryEdb.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mSearchCountryEdb, InputMethodManager.SHOW_IMPLICIT);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(visibleView.getId() == R.id.search)
                {
                    mSearchCountryEdb.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchCountryEdb.getWindowToken(), 0);
                }

                visibleView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void loadUsers()
    {
        mCountries      = new ArrayList<>();
        mUsers          = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,UsersAndCountruesDatabaseComunication.WITHOUT_COUNTRY_ID,null,null,mSearchCountryEdb.getText().toString());  // make query for all users with their countries from Users table and Countries table

        mRecyclerAdapter.setUsersAndCountries(mUsers);
        mRecyclerAdapter.notifyDataSetChanged();

        if(mUsers.size() > 0) {
            mFilterButton.setVisibility(View.VISIBLE);
            mSearchButton.setVisibility(View.VISIBLE);
            mEmptyTxt.setVisibility(View.GONE);
        }
        else {
            mFilterButton.setVisibility(View.GONE);
            mSearchButton.setVisibility(View.GONE);
            mEmptyTxt.setVisibility(View.VISIBLE);
        }
    }

    private void refreshUsers(User user,Country country)
    {
        mCountries.add(country);
        mUsers.add(user);
        mRecyclerAdapter.notifyDataSetChanged();

        if(mUsers.size() > 0) {
            mSearchButton.setVisibility(View.VISIBLE);
            mFilterButton.setVisibility(View.VISIBLE);
            mEmptyTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View clickedView) {
        int id = clickedView.getId();
        RelativeLayout.LayoutParams filterParams = (RelativeLayout.LayoutParams) mFilterLayout.getLayoutParams();

        switch (id)
        {
            case R.id.add_user:
                onAddUser();
                mTitleTxt.setText(getString(R.string.registration));
                break;
            case R.id.filter_button:
                if(filterParams.bottomMargin > mFilterLayoutStartTopMargin)
                    animateFilter(filterParams.bottomMargin,mFilterLayoutStartTopMargin);
                else
                    animateFilter(mFilterLayoutStartTopMargin,getResources().getDisplayMetrics().heightPixels - mBar.getHeight());
                break;
            case R.id.country_editbox:
                onSelectCountry();
                break;
            case R.id.search_button:
                replaceElementsWithFade(mTitleTxt, mSearchCountryEdb);
                replaceElementsWithFade(mSearchButton,mCloseButton);
                break;
            case R.id.reset_button:
                resetFilter();
                break;
            case R.id.apply_button:
                applyFilter();
                animateFilter(filterParams.bottomMargin,getResources().getDisplayMetrics().heightPixels - mBar.getHeight());
                break;
            case R.id.close_button:
                replaceElementsWithFade(mCloseButton,mSearchButton);
                replaceElementsWithFade(mSearchCountryEdb, mTitleTxt);
                break;
        }
    }

    private void applyFilter()
    {
        int filterCountryId;
        int checkedButtonId     = mGenderRadioGroup.getCheckedRadioButtonId();

        String gender           = ((RadioButton)    mGenderRadioGroup.findViewById(checkedButtonId)).getText().toString();

        if(mSelectedFilterCountry != null && mSelectedFilterCountry.getCountryName() != null)
            filterCountryId = mSelectedFilterCountry.getId();
        else
            filterCountryId = Constants.INVALID_ROW_INDEX;

        if(gender.equals(getString(R.string.all)))
           gender = null;

        mUsers                  = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,filterCountryId, gender,null,mSearchCountryEdb.getText().toString());

        mRecyclerAdapter.setUsersAndCountries(mUsers);
        mRecyclerAdapter.notifyDataSetChanged();

        checkSizeOfContactsAndSetMessage();

    }

    private void resetFilter()
    {
        mCountryEdb.setText(getString(R.string.all));
        mGenderRadioGroup.check(mGenderRadioGroup.getChildAt(0).getId());

        mFilterGender           = null;
        mSelectedFilterCountry  = null;
    }

    private void onSelectCountry()                                                      //start CountriesFragment
    {
        CountriesFragment countriesFragment = new CountriesFragment();
        Bundle            bundle            = new Bundle();

        bundle.putBoolean(ALL_COUNTRIES_ARE_SELECTED,true);
        countriesFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down,0,0,R.anim.slide_up)
                .add(R.id.replace_layout,countriesFragment,Constants.COIUNTRIES_FRAGMENT_TAG).addToBackStack(Constants.COUNTRY_BACKSTACK_NAME).commit();
    }

    private void onAddUser()                                                           //start RegisterFragment
    {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,0,0,android.R.anim.fade_out).replace(R.id.replace_layout,new RegistrationFragment(),Constants.REGISTRATION_FRAGMENT_TAG)
                .addToBackStack(Constants.REGISTRATION_BACKSTACK_NAME).commit();
    }

    private void updateUser(User user,Country country,int position)                     //update user in ListView
    {
        mUsers.set(position,user);
        mCountries.set(position,country);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    private void setFilterCountry(Country country)                                     //The contacts are selected by country
    {
        mSelectedFilterCountry  = country;

        if(country.getCountryName() == null) {
            mCountryEdb.setText(getString(R.string.all));
            mSelectedFilterCountry = null;
        }
        else
            mCountryEdb.setText(country.getCountryName());
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            mTitleTxt.setText(getString(R.string.contacts));
            mSearchCountryEdb.setText("");
        }
    }

    private void deleteUser(int position)                                                       //Delete user from database and listview
    {
        UsersDatabaseCommunication usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(this);

        if(usersDatabaseCommunication.deleteUserFromDatabase(mUsers.get(position))) {           //delete user from database, method return true if the query is successful
            mUsers.remove(position);
            mCountries.remove(position);
            mRecyclerAdapter.notifyDataSetChanged();

            if(mUsers.size() == 0) {
                mSearchButton.setVisibility(View.GONE);
                mFilterButton.setVisibility(View.GONE);
                mEmptyTxt.setVisibility(View.VISIBLE);
            }

            Toast.makeText(this, getString(R.string.successDelete), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,getString(R.string.notSuccessDelete),Toast.LENGTH_SHORT).show();
    }

    private void onEdit(int position)                                            //start RegistrationFragment for editing user information
    {
        RegistrationFragment    registrationFragment    = new RegistrationFragment();
        Bundle                  bundle                  = new Bundle();
        User                    user                    = mUsers.get(position);
        Country                 country                 = mCountries.get(position);

        bundle.putParcelable(Constants.BUNDLE_COUNTRY_KEY,country);
        bundle.putParcelable(Constants.BUNDLE_USER_KEY,user);
        bundle.putInt(Constants.BUNDLE_POSITION_KEY,position);

        registrationFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,0,0,android.R.anim.fade_out).replace(R.id.replace_layout,registrationFragment,Constants.REGISTRATION_FRAGMENT_TAG)
                .addToBackStack(Constants.REGISTRATION_BACKSTACK_NAME).commit();

        mTitleTxt.setText(getString(R.string.contact_information));
        onPause();
    }

    @Override
    public void onBackStackChanged() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

        if (getIntent() != null && getIntent().hasExtra(Constants.BUNDLE_COUNTRY_KEY) && getIntent().hasExtra(Constants.BUNDLE_USER_KEY))   // listener for backstack changing, in case there is intent with user and country for update, updating user and remove intent
        {
            User    user    = getIntent().getExtras().getParcelable(Constants.BUNDLE_USER_KEY);
            Country country = getIntent().getExtras().getParcelable(Constants.BUNDLE_COUNTRY_KEY);

            if(getIntent().hasExtra(Constants.INTENT_REFRESH_USERS_KEY)) {
                refreshUsers(user, country);
                getIntent().removeExtra(Constants.INTENT_REFRESH_USERS_KEY);
            }
            else if(getIntent().hasExtra(Constants.INTENT_UPDATE_USER_KEY)) {
                updateUser(user, country, getIntent().getExtras().getInt(Constants.BUNDLE_POSITION_KEY));
                getIntent().removeExtra(Constants.INTENT_UPDATE_USER_KEY);
            }

            getIntent().removeExtra(Constants.BUNDLE_COUNTRY_KEY);
            getIntent().removeExtra(Constants.BUNDLE_USER_KEY);

            mTitleTxt.setText(getString(R.string.contacts));
        }
        else if(getIntent().getExtras() != null && getIntent().hasExtra(Constants.INTENT_FILTER_COUNTRY_KEY))
        {
            setFilterCountry((Country) getIntent().getParcelableExtra(Constants.INTENT_FILTER_COUNTRY_KEY));
            getIntent().removeExtra(Constants.INTENT_FILTER_COUNTRY_KEY);
        }

        if(getSupportFragmentManager().getBackStackEntryCount() > 0 && !getSupportFragmentManager().getBackStackEntryAt(backStackCount-1).getName().equals(Constants.FILTER_BACKSTACK_NAME)) {
            mNewContactButton.setVisibility(View.GONE);
            mSearchButton.setVisibility(View.GONE);
            mFilterButton.setVisibility(View.GONE);
        }
        else {
            if(((RelativeLayout.LayoutParams)mFilterLayout.getLayoutParams()).bottomMargin > 0)
                mNewContactButton.setVisibility(View.VISIBLE);
            if(mUsers.size() > 0) {
                mFilterButton.setVisibility(View.VISIBLE);
                mSearchButton.setVisibility(View.VISIBLE);
            }
        }

        if(getSupportFragmentManager().getBackStackEntryCount() > 0 && getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getName() != null
                && getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getName().equals(Constants.COUNTRY_BACKSTACK_NAME) && !mStartAnimation)
        {
            mStartAnimation = true;
            replaceElementsWithFade(mTitleTxt,mSearchCountryEdb);
        }
        else if(!mStartAnimation)
        {
            if(mSearchCountryEdb.getVisibility() == View.VISIBLE)
                replaceElementsWithFade(mSearchCountryEdb,mTitleTxt);

            mNotCountrySearching = true;
            mSearchCountryEdb.setText("");
        }
        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.INTENT_DELETE_USER_POSITION))
        {
            deleteUser(getIntent().getExtras().getInt(Constants.INTENT_DELETE_USER_POSITION));
            getIntent().removeExtra(Constants.INTENT_DELETE_USER_POSITION);
        }
    }

    @Override
    public void onRecyclerItemClickListener(View view, int position) {
        int clickedViewId = view.getId();
        switch (clickedViewId)
        {
            case R.id.call_button:
                String phoneNumber = getString(R.string.plus) + mCountries.get(position).getCallingCode() +  mUsers.get(position).getPhoneNumber();
                callToNumber(phoneNumber);
                break;
            case R.id.user_item:
                onEdit(position);
                break;
            case R.id.user_image:
                selectImageForUser(position);
                break;
        }
    }

    private void selectImageForUser(int position)
    {
        mSelectedItemPosition = position;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK)
        {
            if (data != null)
            {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                if(cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap      bitmap          = BitmapFactory.decodeFile(picturePath);
                    View        item            = mRecyclerView.getChildAt(mSelectedItemPosition);

                    ImageView   image           = (ImageView) item.findViewById(R.id.user_image);
                    image.setImageBitmap(bitmap);

                    UsersDatabaseCommunication  usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(this);
                    usersDatabaseCommunication.updateImageInDatabase(mUsers.get(mSelectedItemPosition).getId(),picturePath);
                }
            }
        }
        mSelectedItemPosition = Constants.INVALID_ROW_INDEX;
    }

    private void callToNumber(String number)
    {
        if(number != null && !number.equals("")) {

            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(getString(R.string.tel) + number));

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            } else
                checkPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.permission_denied_phone_call, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
        RadioButton checkedButton = (RadioButton) radioGroup.findViewById(checkedButtonId);
        mFilterGender   = checkedButton.getText().toString();

        if(mFilterGender.equals(getString(R.string.all)))
            mFilterGender          = null;
    }

    private void checkSizeOfContactsAndSetMessage()
    {
        if(mUsers.size() == 0)
            mEmptyTxt.setVisibility(View.VISIBLE);
        else
            mEmptyTxt.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        final int touchPositionY                    = (int) motionEvent.getRawY();

        RelativeLayout.LayoutParams layoutParams    = (RelativeLayout.LayoutParams) view
                .getLayoutParams();

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                mFirstTouchPositionY = touchPositionY - lParams.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                if(mFirstTouchPositionY - touchPositionY > 0) {
                    layoutParams.bottomMargin = mFirstTouchPositionY - touchPositionY;
                    layoutParams.topMargin = touchPositionY - mFirstTouchPositionY;
                    mFilterCurrentTopMargin = mFirstTouchPositionY - touchPositionY;
                    view.setLayoutParams(layoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mFilterCurrentTopMargin > mFilterLayout.getHeight() / 3)
                    animateFilter(mFilterCurrentTopMargin, mFilterLayout.getHeight());
                else {
                    animateFilter(mFilterCurrentTopMargin, mFilterLayoutStartTopMargin);
                }
                break;
        }
        view.invalidate();
        return true;
    }

    private void getActiveCounties(){
        mCommandGetActiveCountries = new CommandGetActiveCountries(this, this);
        mCommandGetActiveCountries.setInputParameters(CommandGetActiveCountries.RISK_TYPE_ALL);
        mCommandGetActiveCountries.setmHideProgressOnComplete(false);
        mCommandGetActiveCountries.sendCommand();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence,  int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        CountriesFragment countriesFragment = (CountriesFragment) getSupportFragmentManager().findFragmentByTag(Constants.COIUNTRIES_FRAGMENT_TAG);
        if(countriesFragment != null && countriesFragment.isVisible())
        {
            countriesFragment.searchCountries(mSearchCountryEdb.getText().toString());
        }
        else if(getSupportFragmentManager().getBackStackEntryCount() == 0)
        {
            int filterCountryId;
            if(mSelectedFilterCountry != null && mSelectedFilterCountry.getCountryName() != null && !mSelectedFilterCountry.getCountryName().equals(""))
                filterCountryId = mSelectedFilterCountry.getId();
            else
                filterCountryId = Constants.INVALID_ROW_INDEX;

            if(!mNotCountrySearching) {
                mUsers = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries, filterCountryId, mFilterGender, null, mSearchCountryEdb.getText().toString());
                mRecyclerAdapter.setUsersAndCountries(mUsers);
                mRecyclerAdapter.notifyDataSetChanged();

                checkSizeOfContactsAndSetMessage();
            }
            else
                mNotCountrySearching = false;
        }
    }

    private void sendLoginCommand()
    {
        mCommandLogin = new CommandLogin(this, this);
        mCommandLogin.setInputParameters(Utils.USERNAME_TEST, Utils.md5(Utils.PASSWORD_TEST));
        mCommandLogin.setmShowProgressBar(false);
        mCommandLogin.setmProceedErrorAnswer(true);
        mCommandLogin.setmShowErrorToast(false);
        mCommandLogin.sendCommand();
    }

    @Override
    public void onConnect() {
        sendLoginCommand();
    }

    @Override
    public void onError() {

    }

    @Override
    public void onAnswerReceived(int command, String body) {
        final   CountriesDatabaseCommunication countryDatabaseCommunication   = CountriesDatabaseCommunication.getInstance(this);

        if ((mCommandLogin.getStatus() == Response.RESPOND_STATUS_SUCCESS || mCommandLogin.getStatus() == Response.RESPOND_STATUS_NO_DEFAULT_WALLET) && command == AllCommands.COMMAND_LOGIN) {
            GlobalData.getInstance().setSession(mCommandLogin.getSession());
            GlobalData.getInstance().setmSessionStartMillis(Calendar.getInstance().getTimeInMillis());

            if(countryDatabaseCommunication.getCountOfCountries() == 0)
                getActiveCounties();
        }
        else if( command == AllCommands.COMMAND_GET_ACTIVE_COUNTRIES){

            mCountries  = mCommandGetActiveCountries.getCountries();

            for(Country country : mCountries)
            {
                countryDatabaseCommunication.saveInDatabase(country);
            }
        }
    }
}
