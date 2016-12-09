package com.example.stoycho.phonebook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.activities.HomeActivity;
import com.example.stoycho.phonebook.adapters.CountriesAdapter;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.models.Country;
import com.example.stoycho.phonebook.database.Database;

import java.util.List;

public class CountriesFragment extends Fragment implements TextWatcher, AdapterView.OnItemClickListener,View.OnClickListener {

    private EditText                        mSearchEdb;
    private ListView                        mCountriesList;
    private List<Country>                   mCountries;
    private CountriesAdapter                mAdapter;
    private TextView                        mSearchTxt;
    private View                            mDivider;
    private CountriesDatabaseCommunication  mCountriesDatabaseCommunication;

    public  final static String COIUNTRIES_FRAGMENT_TAG = "countryFragment";
    private final static String FILTER_COUNTRY_KEY          = "filter_country";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_countries, container, false);

        initUI(root);
        setListeners();
        if(getArguments() == null) {
            mSearchTxt.setVisibility(View.GONE);
            mDivider.setVisibility(View.GONE);
        }
        loadCountriesList();
        return root;
    }

    private void initUI(View root)
    {
        mSearchEdb                      = (EditText)root.findViewById(R.id.search);
        mCountriesList                  = (ListView)root.findViewById(R.id.countriesList);
        mSearchTxt                      = (TextView) root.findViewById(R.id.all);
        mDivider                        = root.findViewById(R.id.divider);
        mCountriesDatabaseCommunication = CountriesDatabaseCommunication.getInstance(getActivity());
    }

    private void setListeners()
    {
        mSearchEdb.     addTextChangedListener(this);
        mCountriesList. setOnItemClickListener(this);
        mSearchTxt.     setOnClickListener(this);
    }

    private void loadCountriesList()
    {
        mCountries =    mCountriesDatabaseCommunication.selectAllCountriesFromDatabase(getActivity(),CountriesDatabaseCommunication.SELECT_ALL_COUNTRIES,null);
        mAdapter =      new CountriesAdapter(getActivity(),mCountries);
        mCountriesList.setAdapter(mAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mCountries = mCountriesDatabaseCommunication.selectAllCountriesFromDatabase(getActivity(),CountriesDatabaseCommunication.SELECT_SEARCH_PLACES,s.toString());
        mAdapter.setCountries(mCountries);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<Fragment> fragments = getFragmentManager().getFragments();
        RegistrationFragment fragment = (RegistrationFragment) getFragmentManager().findFragmentByTag(RegistrationFragment.REGISTRATION_FRAGMENT_TAG);
        if(fragment != null) {
           getFragmentManager().popBackStack(HomeActivity.REGISTRATION_BACKSTACK_NAME,0);
            fragment.setSelectedCountry(mCountries.get(position));
        }
        else if(fragments.size() == 1)
            getActivity().getIntent().putExtra(FILTER_COUNTRY_KEY,mCountries.get(position));

        getFragmentManager().popBackStack();
        View viewFocus = getActivity().getCurrentFocus();
        if (viewFocus != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.all:
                selectAll();
                break;
        }
    }

    private void selectAll()
    {
        getActivity().getIntent().putExtra(FILTER_COUNTRY_KEY,"");
        getFragmentManager().popBackStack();
    }
}
