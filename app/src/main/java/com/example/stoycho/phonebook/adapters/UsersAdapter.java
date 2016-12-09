package com.example.stoycho.phonebook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.activities.HomeActivity;
import com.example.stoycho.phonebook.models.Country;
import com.example.stoycho.phonebook.models.User;

import java.util.List;

/**
 * Created by Stoycho on 10/22/2016.
 */

public class UsersAdapter extends BaseAdapter{

    private Context         mContext;
    private List<User>      mUsers;
    private List<Country>   mCountries;
    private LayoutInflater  mInflater;

    public UsersAdapter(Context context, List<User> users, List<Country> countries)
    {
        this.mContext        = context;
        this.mUsers          = users;
        this.mCountries      = countries;
        this.mInflater       = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView == null)
            convertView = mInflater.inflate(R.layout.user_item, parent, false);

        User user           = mUsers.get(position);
        Country country     = mCountries.get(position);

        TextView name           = (TextView)    convertView.findViewById(R.id.user_name);
        TextView phone          = (TextView)    convertView.findViewById(R.id.user_phone);
        final ImageView delete  = (ImageView)   convertView.findViewById(R.id.delete);
        final ImageView edit    = (ImageView)   convertView.findViewById(R.id.edit);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView)parent).performItemClick(view,position,0);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView)parent).performItemClick(view,position,0);
            }
        });

        name.setText(user.getFirstName());
        String phoneNumber = mContext.getString(R.string.plus) + country.getCallingCode() + user.getPhoneNumber();
        phone.setText(phoneNumber);

        return convertView;
    }

    public void setUsersAndCountries(List<User> users, List<Country> countries)
    {
        this.mUsers = users;
        this.mCountries = countries;
    }
}
