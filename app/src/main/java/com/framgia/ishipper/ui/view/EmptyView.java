package com.framgia.ishipper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framgia.ishipper.R;

/**
 * Created by HungNT on 12/23/16.
 */

public class EmptyView extends LinearLayout {
    private TextView mTvHint;
    private ImageView mImgHint;
    private String mTextHint;
    private int mImgHintResId;

    public EmptyView(Context context) {
        super(context);
        setWillNotDraw(false);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);
        try {
            mTextHint = typedArray.getString(R.styleable.EmptyView_textHint);
            mImgHintResId = typedArray.getResourceId(R.styleable.EmptyView_imageHintSrc, 0);
        } finally {
            typedArray.recycle();
        }
    }

    public void setTextHint(String hint) {
        mTextHint = hint;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mTvHint.setText(mTextHint);
        if (mImgHintResId == 0) {
            mImgHint.setVisibility(GONE);
        } else {
            mImgHint.setVisibility(VISIBLE);
            mImgHint.setImageResource(mImgHintResId);
        }
    }

    public void setImgHint(int imgHintResId) {
        mImgHintResId = imgHintResId;
        invalidate();
        requestLayout();
    }

    private void init() {
        inflate(getContext(), R.layout.view_empty, this);
        mTvHint = (TextView) findViewById(R.id.tvHint);
        mImgHint = (ImageView) findViewById(R.id.imgHint);
    }

    public void active(boolean active) {
        setVisibility(active ? VISIBLE : GONE);
    }
}
