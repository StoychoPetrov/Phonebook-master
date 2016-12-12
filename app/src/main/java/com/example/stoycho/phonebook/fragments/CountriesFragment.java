package com.example.stoycho.phonebook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.stoycho.phonebook.Interfaces.OnRecyclerItemClick;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.activities.HomeActivity;
import com.example.stoycho.phonebook.adapters.CountriesRecyclerAdapter;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.models.Country;

import java.util.ArrayList;
import java.util.List;

public class CountriesFragment extends Fragment implements TextWatcher,View.OnClickListener,OnRecyclerItemClick {

    private EditText                        mSearchEdb;
    private RecyclerView                    mCountriesRecycler;
    private CountriesRecyclerAdapter        mCountriesAdapter;
    private List<Country>                   mCountries;
    private TextView                        mSearchTxt;
    private View                            mDividerView;
    private CountriesDatabaseCommunication  mCountriesDatabaseCommunication;

    public  final static String COUNTRY_BACKSTACK_NAME      = "country_backstack";
    public  final static String COIUNTRIES_FRAGMENT_TAG     = "countryFragment";
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

        mCountries          = new ArrayList<>();
        mCountriesAdapter   = new CountriesRecyclerAdapter(mCountries);

        mCountriesRecycler.setHasFixedSize(true);
        mCountriesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCountriesRecycler.setItemAnimator(new DefaultItemAnimator());
        mCountriesRecycler.setAdapter(mCountriesAdapter);

        if(getArguments() == null) {
            mSearchTxt.setVisibility(View.GONE);
            mDividerView.setVisibility(View.GONE);
        }

        loadCountriesList();
        return root;
    }

    private void initUI(View root)
    {
        mSearchEdb                      = (EditText)root.findViewById(R.id.search);
        mCountriesRecycler              = (RecyclerView) root.findViewById(R.id.countriesRecycler);
        mSearchTxt                      = (TextView) root.findViewById(R.id.all);
        mDividerView                        = root.findViewById(R.id.divider);
        mCountriesDatabaseCommunication = CountriesDatabaseCommunication.getInstance(getActivity());
    }

    private void setListeners()
    {
        mSearchEdb.addTextChangedListener(this);
        mSearchTxt.setOnClickListener(this);
        mCountriesAdapter.setOnItemClickListener(this);
    }

    private void loadCountriesList()
    {
        mCountries = mCountriesDatabaseCommunication.selectAllCountriesFromDatabase(CountriesDatabaseCommunication.SELECT_ALL_COUNTRIES,null);
        mCountriesAdapter.setCountries(mCountries);
        mCountriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence input, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence input, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable input) {
        mCountries = mCountriesDatabaseCommunication.selectAllCountriesFromDatabase(CountriesDatabaseCommunication.SELECT_SEARCH_PLACES,input.toString());
        mCountriesAdapter.setCountries(mCountries);
        mCountriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View clickedView) {
        int id = clickedView.getId();
        switch (id)
        {
            case R.id.all:
                selectAll();
                break;
        }
    }

    private void selectAll()
    {
        getActivity().getIntent().putExtra(FILTER_COUNTRY_KEY, new Country());
        getFragmentManager().popBackStack();
    }

    @Override
    public void onRecyclerItemClickListener(View view, int position) {
        List<Fragment> fragments = getFragmentManager().getFragments();
        RegistrationFragment registrationFragment = (RegistrationFragment) getFragmentManager().findFragmentByTag(RegistrationFragment.REGISTRATION_FRAGMENT_TAG);                  // check if RegistrationFragment is already exist.

        if(registrationFragment != null) {
            getFragmentManager().popBackStack(HomeActivity.REGISTRATION_BACKSTACK_NAME,0);                                                                                          // RegistrationFragment is exist and pop fragments while RegistrationFragment become visible.
            registrationFragment.setSelectedCountry(mCountries.get(position));
        }
        else if(fragments.size() > 1) {
            getActivity().getIntent().putExtra(FILTER_COUNTRY_KEY, mCountries.get(position));                                                                                       // Put extra in activity intent.
            while (fragments.size() == 0)
                getFragmentManager().popBackStack();                                                                                                                                // pop all fragments
        }

        View viewFocus = getActivity().getCurrentFocus();

        if (viewFocus != null) {                                                                                                                                                    // check if any edittext is focused and close keyboard if it is opened.
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }
    }
}
