package com.example.stoycho.phonebook.activities;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity implements View.OnClickListener,DialogInterface.OnDismissListener,FragmentManager.OnBackStackChangedListener,OnRecyclerItemClick {

    private RecyclerView                mRecyclerView;
    private UsersRecyclerAdapter        mRecyclerAdapter;
    private List<Country>               mCountries;
    private List<User>                  mUsers;
    private RelativeLayout              mSelectCountryLayout;
    private RelativeLayout              mSelectGenderLayout;
    private TextView                    mNewContactTxt;
    private TextView                    mFilterCountryTxt;
    private TextView                    mFilterGenderTxt;
    private TextView                    mTitleTxt;
    private GenderDialog                mGenderDialog;
    private Country                     mSelectedFilterCountry;
    private LinearLayout                mFilterLayout;

    private final static String ALL_COUNTRIES_ARE_SELECTED  = "all_selected";
    private final static String BUNDLE_USER_KEY             = "user";
    private final static String BUNDLE_COUNTRY_KEY          = "country";
    private final static String REFRESH_USERS_KEY           =  "refresh_users";
    private final static String UPDATE_USER                 = "update_user";
    private final static String FILTER_COUNTRY_KEY          = "filter_country";
    private final static String BUNDLE_POSITION_KEY         = "position";
    public  final static String REGISTRATION_BACKSTACK_NAME = "registration";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter        = new UsersRecyclerAdapter(this,mCountries,mUsers);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerAdapter);

        setListeners();
        loadCountries();
        loadUsers();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    private void initUI()
    {
        mNewContactTxt          = (TextView)        findViewById(R.id.add_user);
        mRecyclerView           = (RecyclerView)    findViewById(R.id.recycleView);
        mSelectCountryLayout    = (RelativeLayout)  findViewById(R.id.select_country);
        mSelectGenderLayout     = (RelativeLayout)  findViewById(R.id.select_gender);
        mFilterCountryTxt       = (TextView)        findViewById(R.id.filter_country);
        mFilterGenderTxt        = (TextView)        findViewById(R.id.filter_gender);
        mFilterLayout           = (LinearLayout)    findViewById(R.id.search_bar);
        mTitleTxt               = (TextView)        findViewById(R.id.title);
        mGenderDialog           = new GenderDialog(this);
    }

    private void setListeners()
    {
        mNewContactTxt.setOnClickListener(this);
        mSelectCountryLayout.setOnClickListener(this);
        mSelectGenderLayout.setOnClickListener(this);
        mRecyclerAdapter.setOnItemClickListener(this);
        mGenderDialog.setOnDismissListener(this);
    }

    private void loadCountries()
    {
        String                               urlForGetCountries           = getString(R.string.countries_api);      // api for countries names
        final CountriesDatabaseCommunication countryDatabaseCommunication = CountriesDatabaseCommunication.getInstance(this);
        DownloadData data = new DownloadData(urlForGetCountries)                                                        // make request to api for countries names
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
        if(mUsers.size() > 0)
            mFilterLayout.setVisibility(View.VISIBLE);
    }

    private void refreshUsers(User user,Country country)
    {
        mCountries.add(country);
        mUsers.add(user);
        mRecyclerAdapter.notifyDataSetChanged();

        if(mFilterLayout.getVisibility() == View.GONE)
            mFilterLayout.setVisibility(View.VISIBLE);
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
            case R.id.select_country:
                onSelectCountry();
                mTitleTxt.setText(getString(R.string.countries));
                break;
            case R.id.select_gender:
                mGenderDialog.show();
                break;
        }
    }

    private void onSelectCountry()                                                      //start CountriesFragment
    {
        CountriesFragment countriesFragment = new CountriesFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ALL_COUNTRIES_ARE_SELECTED,true);
        countriesFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down,0,0,R.anim.slide_up)
                .add(R.id.replace_layout,countriesFragment).addToBackStack(null).commit();
    }

    private void onAddUser()                                                           //start RegisterFragment
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.replace_layout,new RegistrationFragment(),RegistrationFragment.REGISTRATION_FRAGMENT_TAG)
                .addToBackStack(REGISTRATION_BACKSTACK_NAME).commit();
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
        String gender           = mFilterGenderTxt.getText().toString();
        mUsers                  = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,country.getCountryName() != null ? country.getId():-1, !gender.equals("All") ? gender : null,null);

        if(country.getCountryName() == null)
            mFilterCountryTxt.setText(getString(R.string.all));
        else
            mFilterCountryTxt.setText(country.getCountryName());

       mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(((GenderDialog)dialog).getGender() != null) {
            mFilterGenderTxt.setText(((GenderDialog) dialog).getGender());
            mCountries = new ArrayList<>();
            String gender = mFilterGenderTxt.getText().toString();
            int countryId ;
            if(mSelectedFilterCountry != null)
                countryId = mSelectedFilterCountry.getId();
            else
                countryId = -1;
            mUsers = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,countryId,!gender.equals("All") ? gender : null,null);
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() == 0)
            mTitleTxt.setText(getString(R.string.contacts));
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
            if(mUsers.size() == 0)
                mFilterLayout.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.successDelete), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,getString(R.string.notSuccessDelete),Toast.LENGTH_SHORT).show();
    }

    private void onEdit(int position)                                            //start RegistrationFragment for editing user information
    {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        Bundle bundle = new Bundle();
        User user = mUsers.get(position);
        Country country = mCountries.get(position);
        bundle.putParcelable("country",country);
        bundle.putParcelable("user",user);
        registrationFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.replace_layout,registrationFragment,RegistrationFragment.REGISTRATION_FRAGMENT_TAG)
                .addToBackStack(REGISTRATION_BACKSTACK_NAME).commit();
        mTitleTxt.setText(getString(R.string.editContact));
        onPause();
    }

    private void setTitle()
    {
        mTitleTxt.setText(getString(R.string.contacts));
    }

    @Override
    public void onBackStackChanged() {
        if (getIntent() != null && getIntent().hasExtra(BUNDLE_COUNTRY_KEY) && getIntent().hasExtra(BUNDLE_USER_KEY))   // listener for backstack changing, in case there is intent with user and country for update, updating user and remove intent
        {
            User    user    = getIntent().getExtras().getParcelable(BUNDLE_USER_KEY);
            Country country = getIntent().getExtras().getParcelable(BUNDLE_COUNTRY_KEY);
            if(getIntent().hasExtra(REFRESH_USERS_KEY))
                refreshUsers(user,country);
            else if(getIntent().hasExtra(UPDATE_USER))
                updateUser(user,country,getIntent().getExtras().getInt(BUNDLE_POSITION_KEY));
            getIntent().removeExtra(BUNDLE_COUNTRY_KEY);
            getIntent().removeExtra(BUNDLE_USER_KEY);
            setTitle();
        }
        else if(getIntent().getExtras() != null && getIntent().hasExtra(FILTER_COUNTRY_KEY))
        {
            setFilterCountry((Country) getIntent().getParcelableExtra(FILTER_COUNTRY_KEY));
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
            case R.id.user_item:
                InformationDialog dialog = new InformationDialog(this,mCountries.get(position),mUsers.get(position));
                dialog.show();
                break;
        }
    }
}
