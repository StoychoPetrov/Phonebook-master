package com.example.stoycho.phonebook.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stoycho.phonebook.Interfaces.OnRecyclerItemClick;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.UsersRecyclerAdapter;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.database.UsersDatabaseCommunication;
import com.example.stoycho.phonebook.fragments.CountriesFragment;
import com.example.stoycho.phonebook.fragments.RegistrationFragment;
import com.example.stoycho.phonebook.models.Country;
import com.example.stoycho.phonebook.models.GenderDialog;
import com.example.stoycho.phonebook.models.InformationDialog;
import com.example.stoycho.phonebook.models.User;
import com.example.stoycho.phonebook.tasks.DownloadData;
import com.example.stoycho.phonebook.utils.Constants;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity implements View.OnClickListener,FragmentManager.OnBackStackChangedListener,OnRecyclerItemClick,RadioGroup.OnCheckedChangeListener,View.OnTouchListener,TextWatcher {

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
    private ImageButton                 mFilterButton;
    private RadioGroup                  mGenderRadioGroup;

    private int                         mFirstTouchPositionY;
    private int                         mFilterCurrentTopMargin;
    private int                         mFilterLayoutStartTopMargin;
    private boolean                     mStartAnimation;

    private final static String ALL_COUNTRIES_ARE_SELECTED              = "all_selected";
    private final        int    MY_PERMISSIONS_REQUEST_CALL_PHONE       = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerAdapter);

        setListeners();
        loadCountries();
        loadUsers();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        checkPermissions();

        mFilterLayout.measure(0,0);
        RelativeLayout.LayoutParams filterLayoutParams = (RelativeLayout.LayoutParams) mFilterLayout.getLayoutParams();

        mFilterLayoutStartTopMargin     =   filterLayoutParams.topMargin;
        filterLayoutParams.topMargin    =   -mFilterLayout.getMeasuredHeight();
        mFilterLayout.setLayoutParams(filterLayoutParams);
    }

    private void initUI()
    {
        mNewContactButton       = (FloatingActionButton)    findViewById(R.id.add_user);
        mRecyclerView           = (RecyclerView)            findViewById(R.id.recycleView);
        mFilterLayout           = (RelativeLayout)          findViewById(R.id.filter_layout);
        mTitleTxt               = (TextView)                findViewById(R.id.title);
        mEmptyTxt               = (TextView)                findViewById(R.id.empty_txt);
        mFilterButton           = (ImageButton)             findViewById(R.id.filter_button);
        mCountryEdb             = (EditText)                findViewById(R.id.country_editbox);
        mSearchCountryEdb       = (EditText)                findViewById(R.id.search);
        mGenderRadioGroup       = (RadioGroup)              findViewById(R.id.gender_radio);

        mRecyclerAdapter        = new UsersRecyclerAdapter(this,mRecyclerView,mUsers);
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
    }

    private void animateFilter(float topMargin, float increaseHeight)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(topMargin,increaseHeight);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ((RelativeLayout.LayoutParams)mFilterLayout.getLayoutParams()).topMargin = ((Float) valueAnimator.getAnimatedValue()).intValue();
                mFilterLayout.requestLayout();
            }
        });
        valueAnimator.start();
    }

    private void checkPermissions()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
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
                visibleView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void loadCountries()
    {
        String                               urlForGetCountries           = getString(R.string.countries_api);      // api for countries names
        final CountriesDatabaseCommunication countryDatabaseCommunication = CountriesDatabaseCommunication.getInstance(this);
        DownloadData                         data                         = new DownloadData(urlForGetCountries)         // make request to api for countries names
        {
            @Override
            protected void onPostExecute(String countriesJson) {
            super.onPostExecute(countriesJson);
                List<Country> countries = Country.parceCountriesFromJson(countriesJson);

                for(Country country : countries)
                {
                   countryDatabaseCommunication.saveInDatabase(country);
                }
            }
        };

        if(countryDatabaseCommunication.getCountOfCountries() == 0)
            data.execute();
    }

    private void loadUsers()
    {
        mCountries      = new ArrayList<>();
        mUsers          = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,UsersAndCountruesDatabaseComunication.WITHOUT_COUNTRY_ID,null,null);  // make query for all users with their countries from Users table and Countries table

        mRecyclerAdapter.setUsersAndCountries(mUsers,mCountries);
        mRecyclerAdapter.notifyDataSetChanged();

        if(mUsers.size() > 0) {
            mFilterButton.setVisibility(View.VISIBLE);
            mEmptyTxt.setVisibility(View.GONE);
        }
        else {
            mFilterButton.setVisibility(View.GONE);
            mEmptyTxt.setVisibility(View.VISIBLE);
        }
    }

    private void refreshUsers(User user,Country country)
    {
        mCountries.add(country);
        mUsers.add(user);
        mRecyclerAdapter.notifyDataSetChanged();

        if(mUsers.size() > 0) {
            mFilterButton.setVisibility(View.VISIBLE);
            mEmptyTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View clickedView) {
        int id = clickedView.getId();

        switch (id)
        {
            case R.id.add_user:
                onAddUser();
                mTitleTxt.setText(getString(R.string.registration));
                break;
            case R.id.filter_button:
                if(((RelativeLayout.LayoutParams)mFilterLayout.getLayoutParams()).topMargin < mFilterLayoutStartTopMargin)
                    animateFilter(-mFilterLayout.getHeight(),mFilterLayoutStartTopMargin);
                else
                    animateFilter(mFilterLayoutStartTopMargin,-mFilterLayout.getHeight());
                break;
            case R.id.country_editbox:
                onSelectCountry();
                break;
        }
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
        mCountries              = new ArrayList<>();

        int checkedButtonId     =                   mGenderRadioGroup.getCheckedRadioButtonId();
        String gender           = ((RadioButton)    mGenderRadioGroup.findViewById(checkedButtonId)).getText().toString();

        mUsers                  = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,country.getCountryName() != null ? country.getId():-1, !gender.equals(getString(R.string.all)) ? gender : null,null);

        if(country.getCountryName() == null) {
            mCountryEdb.setText(getString(R.string.all));
            mSelectedFilterCountry = null;
        }
        else
            mCountryEdb.setText(country.getCountryName());

        mRecyclerAdapter.setUsersAndCountries(mUsers,mCountries);
        mRecyclerAdapter.notifyDataSetChanged();

        checkSizeOfContactsAndSetMessage();
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

    private void deleteUserAlert(final int position)         //Show confirmation dialog for delete user.
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.deleteEntry))
                .setMessage(getString(R.string.deleteInfo))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(position);
                    }
                })
                .setNegativeButton(android.R.string.no,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteUser(int position)                                                       //Delete user from database and listview
    {
        UsersDatabaseCommunication usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(this);

        if(usersDatabaseCommunication.deleteUserFromDatabase(mUsers.get(position))) {           //delete user from database, method return true if the query is successful
            mUsers.remove(position);
            mCountries.remove(position);
            mRecyclerAdapter.notifyDataSetChanged();

            if(mUsers.size() == 0) {
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
        registrationFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,0,0,android.R.anim.fade_out).replace(R.id.replace_layout,registrationFragment,Constants.REGISTRATION_FRAGMENT_TAG)
                .addToBackStack(Constants.REGISTRATION_BACKSTACK_NAME).commit();

        mTitleTxt.setText(getString(R.string.editContact));
        onPause();
    }

    @Override
    public void onBackStackChanged() {
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

        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            mNewContactButton.setVisibility(View.GONE);
            mFilterButton.setVisibility(View.GONE);
        }
        else {
            mNewContactButton.setVisibility(View.VISIBLE);
            mFilterButton.setVisibility(View.VISIBLE);
        }

        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

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

            mSearchCountryEdb.setText("");
        }
    }

    @Override
    public void onRecyclerItemClickListener(View view, int position) {
        int clickedViewId = view.getId();
        switch (clickedViewId)
        {
            case R.id.delete:
                deleteUserAlert(position);
                break;
            case R.id.edit:
                onEdit(position);
                break;
            case R.id.call_button:
                String phoneNumber = getString(R.string.plus) + mCountries.get(position).getCallingCode() +  mUsers.get(position).getPhoneNumber();
                callToNumber(phoneNumber);
                break;
            case R.id.info_button:
                InformationDialog dialog = new InformationDialog(this,mCountries.get(position),mUsers.get(position));
                dialog.show();
                break;
        }
    }

    private void callToNumber(String number)
    {
        if(number != null && !number.equals("")) {

            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));

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
        mCountries      = new ArrayList<>();
        String gender   = checkedButton.getText().toString();
        int countryId   = Constants.INVALID_ROW_INDEX;

        if(mSelectedFilterCountry != null)
            countryId = mSelectedFilterCountry.getId();

        mUsers = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,countryId,!gender.equals(getString(R.string.all)) ? gender : null,null);
        mRecyclerAdapter.setUsersAndCountries(mUsers,mCountries);
        mRecyclerAdapter.notifyDataSetChanged();
        checkSizeOfContactsAndSetMessage();
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

        int topMarginView                           = layoutParams.topMargin;

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                mFirstTouchPositionY = touchPositionY - lParams.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                if(touchPositionY - mFirstTouchPositionY < topMarginView) {
                    layoutParams.topMargin  = touchPositionY - mFirstTouchPositionY;
                    mFilterCurrentTopMargin = touchPositionY - mFirstTouchPositionY;
                    view.setLayoutParams(layoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mFilterCurrentTopMargin <= -mFilterLayout.getHeight() / 2)
                    animateFilter(mFilterCurrentTopMargin, -mFilterLayout.getHeight());
                else {
                    animateFilter(mFilterCurrentTopMargin, mFilterLayoutStartTopMargin);
                }
                break;
        }
        view.invalidate();
        return true;
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
    }
}
