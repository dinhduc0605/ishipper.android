package com.framgia.ishipper.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.presentation.profile.UserInfoFragment;
import com.framgia.ishipper.ui.adapter.GeneralFragmentPagerAdapter;
import com.framgia.ishipper.presentation.profile.UserReviewFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dinhduc on 14/09/2016.
 */
public class UserInfoDialogFragment extends DialogFragment {

    private static final String KEY_USER = "User";
    private User mUser;
    @BindView(R.id.img_info_dialog_avatar) CircularImageView mImgAvatar;
    @BindView(R.id.tv_info_dialog_name) TextView mTvName;
    @BindView(R.id.rtb_info_dialog_) AppCompatRatingBar mRatingBar;
    @BindView(R.id.tv_info_dialog_order_number) TextView mTvOrderNumber;
    @BindView(R.id.tv_info_dialog_success_order) TextView mTvSuccessOrder;
    @BindView(R.id.tbl_info_dialog) TabLayout mTabLayout;
    @BindView(R.id.vpg_info_dialog) ViewPager mViewPager;

    public static UserInfoDialogFragment newInstance(User user) {
        UserInfoDialogFragment dialogFragment = new UserInfoDialogFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_dialog, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void initView() {
        // Viewpager
        GeneralFragmentPagerAdapter adapter = new GeneralFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(UserInfoFragment.newInstance(mUser), getResources().getString(R.string.info_dialog_info_tab));
        adapter.addFragment(UserReviewFragment.newInstance(mUser), getResources().getString(R.string.info_dialog_review));
        mViewPager.setAdapter(adapter);

        mRatingBar.setRating((float) mUser.getRate());
        mTvName.setText(mUser.getName());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.tv_info_dialog_close)
    public void onClick() {
        dismiss();
    }
}
