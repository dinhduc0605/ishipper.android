package com.framgia.ishipper.presentation.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.util.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {
    private static final String KEY_USER = "User";
    @BindView(R.id.tv_user_info_phone) TextView mTvPhone;
    @BindView(R.id.tv_user_info_plate) TextView mTvPlate;
    @BindView(R.id.tv_user_info_address) TextView mTvAddress;
    private User mUser;

    public static UserInfoFragment newInstance(User user) {
        UserInfoFragment dialogFragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_USER, user);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getArguments().getParcelable(KEY_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mTvPhone.setText(String.valueOf(mUser.getPhoneNumber()));

        if (CommonUtils.stringIsValid(mUser.getAddress())) {
            mTvAddress.setText(String.valueOf(mUser.getAddress()));
        } else {
            mTvAddress.setText(R.string.all_na);
        }

        if (mUser.isShop()) {
            mTvPlate.setVisibility(View.GONE);
            return;
        }

        if (CommonUtils.stringIsValid(mUser.getPlateNumber())) {
            mTvPlate.setText(String.valueOf(mUser.getPlateNumber()));
        } else {
            mTvPlate.setText(R.string.all_na);
        }
    }
}
