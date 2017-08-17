package com.framgia.ishipper.presentation.favorite;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.ui.listener.OnRemoveUserListener;
import com.framgia.ishipper.widget.dialog.ConfirmDialog;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.widget.dialog.UserInfoDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vuduychuong1994 on 9/15/16.
 */
public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUserList;
    OnRemoveUserListener mRemoveUserListener;


    public FavoriteListAdapter(Context context, List<User> userList, OnRemoveUserListener listener) {
        mContext = context;
        mUserList = userList;
        mRemoveUserListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_black_list_shipper, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.mTextName.setText(user.getName());

    }

    public void add(User user) {
        mUserList.add(0, user);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        if (mUserList != null) return mUserList.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUserName) TextView mTextName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.btnRemove, R.id.layout_container})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnRemove:
                    mRemoveUserListener.onRemove(mUserList.get(getAdapterPosition()), getAdapterPosition());
                    break;
                case R.id.layout_container:
                    UserInfoDialogFragment
                            dialogFragment = UserInfoDialogFragment.newInstance(mUserList.get(getAdapterPosition()));
                    dialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "dialog");
                    break;
            }
        }
    }
}
