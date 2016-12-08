package com.example.stoycho.phonebook.activities;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.UsersAdapter;
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

public class HomeActivity extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener,DialogInterface.OnDismissListener {

    private ListView        mListWithUsers;
    private List<Country>   mCountries;
    private List<User>      mUsers;
    private UsersAdapter    mUserAdapter;
    private RelativeLayout  mSelectCountryLayout;
    private RelativeLayout  mSelectGenderLayout;
    private TextView        mNewContactTxt;
    private TextView        mFilterCountryTxt;
    private TextView        mFilterGenderTxt;
    private TextView        mTitleTxt;
    private GenderDialog    mGenderDialog;
    private Country         mSelectedFilterCountry;
    private LinearLayout    mFilterLayout;
    private final static String ALL_COUNTRIES_ARE_SELECTED = "all_selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        setListeners();
        loadCountries();
        loadUsers();
    }

    private void initUI()
    {
        mNewContactTxt          = (TextView)        findViewById(R.id.add_user);
        mListWithUsers          = (ListView)        findViewById(R.id.listForUsers);
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
        mNewContactTxt      .setOnClickListener(this);
        mListWithUsers      .setOnItemClickListener(this);
        mSelectCountryLayout.setOnClickListener(this);
        mSelectGenderLayout .setOnClickListener(this);
        mGenderDialog.setOnDismissListener(this);
    }

    private void loadCountries()
    {
        String urlForGetCountries = getString(R.string.countries_api);                                              // api for countries names
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
        if(countryDatabaseCommunication.selectAllCountriesFromDatabase(this,CountriesDatabaseCommunication.SELECT_ALL_COUNTRIES,null).size() == 0)
            data.execute();
    }

    private void loadUsers()
    {
        mCountries      = new ArrayList<>();
        mUsers          = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,UsersAndCountruesDatabaseComunication.WITHOUT_COUNTRY_ID,null,null);  // make query for all users with their countries from Users table and Countries table
        mUserAdapter    = new UsersAdapter(this,mUsers,mCountries);
        mListWithUsers.setAdapter(mUserAdapter);

        if(mUsers.size() > 0)
            mFilterLayout.setVisibility(View.VISIBLE);
    }

    public void refreshUsers(User user,Country country)
    {
        mCountries.     add(country);
        mUsers.         add(user);
        mUserAdapter.   setUsersAndCountries(mUsers,mCountries);
        mUserAdapter.   notifyDataSetChanged();

        if(mFilterLayout.getVisibility() == View.GONE)
            mFilterLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

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
                .addToBackStack(null).commit();
    }

    public void updateUser(User user,Country country,int position)                     //update user in ListView
    {
        mUsers      .set(position,user);
        mCountries  .set(position,country);
        mUserAdapter.setUsersAndCountries(mUsers,mCountries);
        mUserAdapter.notifyDataSetChanged();
    }

    public void setFilterCountry(Country countryName)
    {
        mSelectedFilterCountry = countryName;
        mCountries = new ArrayList<>();
        if(countryName == null)
            mFilterCountryTxt.setText(getString(R.string.all));
        else
            mFilterCountryTxt.setText(countryName.getCountryName());
        String gender = mFilterGenderTxt.getText().toString();
        mUsers = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,countryName != null ? countryName.getId():-1, !gender.equals("All") ? gender : null,null);
        mUserAdapter.setUsersAndCountries(mUsers, mCountries);
        mUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int clickedViewId = view.getId();
        switch (clickedViewId)
        {
            case R.id.delete:
                deleteUserAlert(position);
                break;
            case R.id.edit:
                onEdit(position);
                break;
        }
        InformationDialog dialog = new InformationDialog(this,mCountries.get(position),mUsers.get(position));
        dialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(((GenderDialog)dialog).getmGender() != null) {
            mFilterGenderTxt.setText(((GenderDialog) dialog).getmGender());
            mCountries = new ArrayList<>();
            String gender = mFilterGenderTxt.getText().toString();
            int countryId ;
            if(mSelectedFilterCountry != null)
                countryId = mSelectedFilterCountry.getId();
            else
                countryId = -1;
            mUsers = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(mCountries,countryId,!gender.equals("All") ? gender : null,null);
            mUserAdapter.setUsersAndCountries(mUsers,mCountries);
            mUserAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() == 0)
            mTitleTxt.setText(getString(R.string.contacts));
    }

    public void deleteUserAlert(final int position)         //Show confirmation dialog for delete user.
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.deleteEntry))
                .setMessage(getString(R.string.deleteInfo))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(position);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteUser(int position)                                                       //Delete user from database and listview
    {
        UsersDatabaseCommunication usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(this);

        if(usersDatabaseCommunication.deleteUserFromDatabase(mUsers.get(position))) {           //delete user from database, method return true if the query is successful
            mUsers.remove(position);
            mCountries.remove(position);
            mUserAdapter.setUsersAndCountries(mUsers,mCountries);
            mUserAdapter.notifyDataSetChanged();
            if(mUsers.size() == 0)
                mFilterLayout.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.successDelete), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,getString(R.string.notSuccessDelete),Toast.LENGTH_SHORT).show();
    }

    public void onEdit(int position)                                            //start RegistrationFragment for editing user information
    {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        Bundle bundle = new Bundle();
        User user = mUsers.get(position);
        Country country = mCountries.get(position);
        bundle.putParcelable("country",country);
        bundle.putParcelable("user",user);
        registrationFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.replace_layout,registrationFragment,RegistrationFragment.REGISTRATION_FRAGMENT_TAG)
                .addToBackStack(null).commit();
        mTitleTxt.setText(getString(R.string.editContact));
    }

    public void setmTitle()
    {
        mTitleTxt.setText(getString(R.string.contacts));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mTitleTxt.setText(getString(R.string.contacts));
    }
}
