package com.framgia.ishipper.model;

/**
 * Created by HungNT on 9/20/16.
 */
public class FbInvoice {

    private String mUserName;
    private String mContent;
    private String mPhoneNumber;
    private String mPostUrl;
    private String mCreatedTime;
    private boolean isPinned;

    public String getPostUrl() {
        return mPostUrl;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getContent() {
        return mContent;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getCreatedTime() {
        return mCreatedTime;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public static FbInvoice getSample() {
        FbInvoice invoice = new FbInvoice();
        invoice.mUserName = "Nguyen Van Nam";
        invoice.mPhoneNumber = "0987667788";
        invoice.mContent = "Em cần ship luôn 1 file tài liệu từ 98 hoàng quốc việt đi Số 15 lô B " +
                "khu đô thị mới Đại Kim đường Nguyễn Cảnh Dị cạnh cây xăng. " +
                "K ứng ship 50k. Ai đi đc giúp em ko ạ.";
        invoice.mCreatedTime = "14:30";
        return invoice;
    }
}
