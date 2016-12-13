package com.example.stoycho.phonebook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stoycho.phonebook.Interfaces.OnRecyclerItemClick;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.models.Country;
import com.example.stoycho.phonebook.models.User;

import java.util.List;

/**
 * Created by stoycho.petrov on 09/12/2016.
 */

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private Context             mContext;
    private List<User>          mUsers;
    private List<Country>       mCountries;
    private OnRecyclerItemClick mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView    mUserNameTxt;
        private TextView    mUserPhoneTxt;
        private ImageButton mEditButton;
        private ImageButton mDeleteButton;

        private ViewHolder(View itemView) {
            super(itemView);
            mUserNameTxt    = (TextView)    itemView.findViewById(R.id.user_name);
            mUserPhoneTxt   = (TextView)    itemView.findViewById(R.id.user_phone);
            mEditButton     = (ImageButton) itemView.findViewById(R.id.edit);
            mDeleteButton   = (ImageButton) itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(this);
            mEditButton.setOnClickListener(this);
            mDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null)
                mItemClickListener.onRecyclerItemClickListener(view,getAdapterPosition());
        }
    }

    public UsersRecyclerAdapter(Context context,List<Country> countries, List<User> users) {
        mContext    = context;
        mCountries  = countries;
        mUsers      = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User    user    = mUsers.get(position);
        Country country = mCountries.get(position);

        String phoneNumber = mContext.getString(R.string.plus) + country.getCallingCode() + user.getPhoneNumber();

        holder.mUserNameTxt.setText(user.getFirstName());
        holder.mUserPhoneTxt.setText(phoneNumber);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void setUsersAndCountries(List<User> users, List<Country> countries)
    {
        mUsers      = users;
        mCountries  = countries;
    }

    public void setOnItemClickListener(OnRecyclerItemClick itemClickListener)
    {
        mItemClickListener = itemClickListener;
    }
}