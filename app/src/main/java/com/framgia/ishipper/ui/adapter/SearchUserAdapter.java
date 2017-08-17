package com.framgia.ishipper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.framgia.ishipper.R;
import com.framgia.ishipper.model.User;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {
    private List<User> mUserList;

    // Check the state of search is blacklist or favorite
    private OnSearchItemCallback mCallback;

    public SearchUserAdapter(List<User> invoiceList, OnSearchItemCallback callback) {
        mUserList = invoiceList;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        User item = mUserList.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if (mUserList == null)
            return 0;
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUserName) TextView tvName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.btnAdd, R.id.itemSearchUser})
        public void onClick(View view) {
            if (view.getId() == R.id.btnAdd) {
                mCallback.onAddButtonClick(mUserList.get(getAdapterPosition()));
            } else if (view.getId() == R.id.itemSearchUser) {
                mCallback.onItemClick(mUserList.get(getAdapterPosition()));
            }
        }


        public void bindData(User invoice) {
            tvName.setText(invoice.getName());
        }
    }

    public interface OnSearchItemCallback {
        void onAddButtonClick(User data);

        void onItemClick(User data);
    }
}
