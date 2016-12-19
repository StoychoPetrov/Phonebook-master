package com.example.stoycho.phonebook.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stoycho.phonebook.Interfaces.OnRecyclerItemClick;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.models.Country;
import com.example.stoycho.phonebook.models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by stoycho.petrov on 09/12/2016.
 */

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private Context             mContext;
    private List<User>          mUsers;
    private RecyclerView        mRecyclerView;
    private OnRecyclerItemClick mItemClickListener;

    private final static int    BUTTONS_LAYOUT_VISIBLE_HEIGHT   = 40;
    private final static int    BUTTONS_LAYOUT_GONE_HEIGHT      = 0;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView        mUserNameTxt;
        private ImageButton     mEditButton;
        private ImageButton     mDeleteButton;
        private ImageButton     mCallButton;
        private ImageButton     mInfoButton;
        private LinearLayout    mUsersButtonsLayput;

        private ViewHolder(View itemView)  {
            super(itemView);
            mUserNameTxt            = (TextView)    itemView.findViewById(R.id.user_name);
            mEditButton             = (ImageButton) itemView.findViewById(R.id.edit);
            mDeleteButton           = (ImageButton) itemView.findViewById(R.id.delete);
            mCallButton             = (ImageButton) itemView.findViewById(R.id.call_button);
            mInfoButton             = (ImageButton) itemView.findViewById(R.id.info_button);
            mUsersButtonsLayput     = (LinearLayout)itemView.findViewById(R.id.user_buttons_layout);


            itemView.setOnClickListener(this);
            mEditButton.setOnClickListener(this);
            mDeleteButton.setOnClickListener(this);
            mInfoButton.setOnClickListener(this);
            mCallButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           int id = view.getId();
            switch (id)
            {
                case R.id.edit:
                case R.id.call_button:
                case R.id.info_button:
                case R.id.delete:
                    callListener(view);
                    break;
                case R.id.user_item:
                    onItemClick();
                    break;
            }
        }

        private void callListener(View view)
        {
            if(mItemClickListener != null)
                mItemClickListener.onRecyclerItemClickListener(view,getAdapterPosition());
        }

        private void onItemClick()
        {
            float increaseHeight;
            if(mUsersButtonsLayput.getHeight() == 0)
                increaseHeight = BUTTONS_LAYOUT_VISIBLE_HEIGHT * mContext.getResources().getDisplayMetrics().density;
            else
                increaseHeight = BUTTONS_LAYOUT_GONE_HEIGHT;
            List<Animator> listAnimator      = new ArrayList<>();
            AnimatorSet         set                     = new AnimatorSet();
            ValueAnimator       animateClickedItem      = createAnimator(mUsersButtonsLayput,increaseHeight);
            listAnimator.add(animateClickedItem);
            for(int i = 0; i < getItemCount(); i++)
            {
                if(i != getAdapterPosition()) {
                    View            item    =                   mRecyclerView.getChildAt(i);
                    LinearLayout    buttons = (LinearLayout)    item.findViewById(R.id.user_buttons_layout);

                    if(buttons.getHeight() != BUTTONS_LAYOUT_GONE_HEIGHT)
                    {
                        ValueAnimator hideLayout = createAnimator(buttons,BUTTONS_LAYOUT_GONE_HEIGHT);
                        listAnimator.add(hideLayout);
                    }
                }
            }
            set.playTogether(listAnimator);
            set.start();
        }

        private ValueAnimator createAnimator(final View view, float increaseHeight)
        {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(view.getHeight(),increaseHeight);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    view.getLayoutParams().height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    view.requestLayout();
                }
            });
            return valueAnimator;
        }
    }

    public UsersRecyclerAdapter(Context context,RecyclerView recyclerView, List<User> users) {
        mContext        = context;
        mUsers          = users;
        mRecyclerView   = recyclerView;
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

    public void setUsersAndCountries(List<User> users, List<Country> countries)
    {
        mUsers      = users;
    }

    public void setOnItemClickListener(OnRecyclerItemClick itemClickListener)
    {
        mItemClickListener = itemClickListener;
    }
}