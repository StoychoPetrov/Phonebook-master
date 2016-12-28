package com.example.stoycho.phonebook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stoycho.phonebook.Interfaces.OnRecyclerItemClick;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.models.User;

import java.util.List;

/**
 * Created by stoycho.petrov on 09/12/2016.
 */

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private List<User>          mUsers;
    private OnRecyclerItemClick mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView        mUserNameTxt;
        private ImageButton     mCallButton;

        private ViewHolder(View itemView)  {
            super(itemView);
            mUserNameTxt            = (TextView)    itemView.findViewById(R.id.user_name);
            mCallButton             = (ImageButton) itemView.findViewById(R.id.call_button);

            itemView.setOnClickListener(this);
            mCallButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           int id = view.getId();
            switch (id)
            {
                case R.id.user_item:
                    callListener(view);
                    break;
            }
        }

        private void callListener(View view)
        {
            if(mItemClickListener != null)
                mItemClickListener.onRecyclerItemClickListener(view,getAdapterPosition());
        }
    }

    public UsersRecyclerAdapter(List<User> users) {
        mUsers          = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User    user    = mUsers.get(position);
        holder.mUserNameTxt.setText(user.getFirstName());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void setUsersAndCountries(List<User> users)
    {
        mUsers      = users;
    }

    public void setOnItemClickListener(OnRecyclerItemClick itemClickListener)
    {
        mItemClickListener = itemClickListener;
    }
}