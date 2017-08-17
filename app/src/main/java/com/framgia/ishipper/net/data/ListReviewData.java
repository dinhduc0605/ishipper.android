package com.framgia.ishipper.net.data;

import com.framgia.ishipper.model.ReviewUser;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dinhduc on 16/09/2016.
 */
public class ListReviewData {
    @SerializedName("reviews") private List<ReviewUser> mReviewUsers;

    public List<ReviewUser> getReviewUsers() {
        return mReviewUsers;
    }

    public void setReviewUsers(List<ReviewUser> reviewUsers) {
        mReviewUsers = reviewUsers;
    }
}
