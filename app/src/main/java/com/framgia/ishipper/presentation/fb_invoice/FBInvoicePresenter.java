package com.framgia.ishipper.presentation.fb_invoice;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;

/**
 * Created by dinhduc on 21/11/2016.
 */

public class FBInvoicePresenter implements FBInvoiceContract.Presenter {
    private FBInvoiceContract.View mView;
    private BaseToolbarActivity mActivity;

    public FBInvoicePresenter(FBInvoiceContract.View view, BaseToolbarActivity activity) {
        mView = view;
        mActivity = activity;
    }

    @Override
    public void showPinnedInvoice() {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_container, FBInvoiceFragment.newInstance(false))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backToFacebookInvoice() {
        mActivity.getSupportFragmentManager().popBackStack();
    }
}
