package com.framgia.ishipper.net.data;

import com.framgia.ishipper.model.Invoice;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinhduc on 18/08/2016.
 */
public class FilterInvoiceData {

    @SerializedName("invoices") private List<Invoice> mInvoiceList = new ArrayList<>();

    public List<Invoice> getInvoiceList() {
        return mInvoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        mInvoiceList = invoiceList;
    }
}
