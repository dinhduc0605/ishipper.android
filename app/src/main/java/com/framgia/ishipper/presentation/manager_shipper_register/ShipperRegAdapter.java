package com.framgia.ishipper.presentation.manager_shipper_register;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
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

/**
 * Created by vuduychuong1994 on 7/20/16.
 */
public class ShipperRegAdapter extends RecyclerView.Adapter<ShipperRegAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mShipperList;
    private OnItemClickShipperRegListener mListener;
    private OnClickAcceptShipperListener mAcceptShipperListener;

    public ShipperRegAdapter(Context context, List<User> shipperList) {
        mContext = context;
        mShipperList = shipperList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipper, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mShipper = mShipperList.get(position);
        holder.mTextName.setText(holder.mShipper.getName());
        holder.mRtbRatingUser.setRating((float) holder.mShipper.getRate());
        //TODO update shipper information
//        holder.mTextDistance.setText(holder.mShipper.getDistance());
//        holder.mTextCountRating.setText(String.valueOf(holder.mShipper.getCountRating()));

    }

    @Override
    public int getItemCount() {
        if (mShipperList == null)
            return 0;
        return mShipperList.size();
    }

    public OnItemClickShipperRegListener getListener() {
        return mListener;
    }

    public void setListener(OnItemClickShipperRegListener listener) {
        mListener = listener;
    }

    public OnClickAcceptShipperListener getAcceptShipperListener() {
        return mAcceptShipperListener;
    }

    public void setAcceptShipperListener(OnClickAcceptShipperListener acceptShipperListener) {
        mAcceptShipperListener = acceptShipperListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public User mShipper;
        @BindView(R.id.text_name) TextView mTextName;
        @BindView(R.id.text_count_order) TextView mTextCountOrder;
        @BindView(R.id.text_distance) TextView mTextDistance;
        @BindView(R.id.text_count_rating) TextView mTextCountRating;
        @BindView(R.id.rtb_rating_user) AppCompatRatingBar mRtbRatingUser;
        @BindView(R.id.btn_accept_shipper) TextView mBtnAcceptShipper;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
            mView.setOnClickListener(this);
            mBtnAcceptShipper.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == mView.getId() && mListener != null) {
                mListener.onItemClickShipperRegListener(mShipper);
            }
            if (view.getId() == R.id.btn_accept_shipper && mAcceptShipperListener != null) {
                mAcceptShipperListener.onClickAcceptShipperListener(mShipper);
            }
        }
    }

    public interface OnItemClickShipperRegListener {
        void onItemClickShipperRegListener(User shipper);
    }

    public interface OnClickAcceptShipperListener {
        void onClickAcceptShipperListener(User shipper);
    }
}
