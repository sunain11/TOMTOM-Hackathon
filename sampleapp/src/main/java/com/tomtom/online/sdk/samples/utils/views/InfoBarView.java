/**
 * Copyright (c) 2015-2018 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be used
 * for internal evaluation purposes or commercial use strictly subject to separate licensee
 * agreement between you and TomTom. If you are the licensee, you are only permitted to use
 * this Software in accordance with the terms of your license agreement. If you are not the
 * licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */
package com.tomtom.online.sdk.samples.utils.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomtom.online.sdk.samples.R;

public class InfoBarView extends RelativeLayout {

    private static final String BUNDLE_KEY_SUPER_STATE = "SUPER_STATE";
    private static final String BUNDLE_KEY_VIEW_VISIBILITY = "VIEW_VISIBILITY";
    private static final String BUNDLE_KEY_VIEW_LEFT_TEXT = "VIEW_LEFT_TEXT";
    private static final String BUNDLE_KEY_VIEW_RIGHT_TEXT = "VIEW_RIGHT_TEXT";
    private static final String BUNDLE_KEY_VIEW_LEFT_IMAGE_RES_ID = "VIEW_LEFT_IMAGE_RES_ID";
    private static final String BUNDLE_KEY_VIEW_LEFT_IMAGE_VISIBILITY = "VIEW_LEFT_IMAGE_VISIBILITY";

    private TextView leftTextView;
    private TextView rightTextView;
    private ImageView leftImageView;

    public InfoBarView(Context context) {
        super(context);
        init(context);
    }

    public InfoBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InfoBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.info_bar_view, this, true);
        leftTextView = findViewById(R.id.left_text);
        rightTextView = findViewById(R.id.right_text);
        leftImageView = findViewById(R.id.left_icon);
    }

    private boolean isVisible() {
        return getVisibility() == View.VISIBLE;
    }

    private boolean isLeftIconVisible() {
        return leftImageView.getVisibility() == View.VISIBLE;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putBoolean(BUNDLE_KEY_VIEW_VISIBILITY, isVisible());
        bundle.putString(BUNDLE_KEY_VIEW_LEFT_TEXT, leftTextView.getText().toString());
        bundle.putString(BUNDLE_KEY_VIEW_RIGHT_TEXT, rightTextView.getText().toString());
        bundle.putBoolean(BUNDLE_KEY_VIEW_LEFT_IMAGE_VISIBILITY, isLeftIconVisible());
        if (leftImageView.getTag() != null) {
            bundle.putInt(BUNDLE_KEY_VIEW_LEFT_IMAGE_RES_ID, (Integer) leftImageView.getTag());
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            leftTextView.setText(bundle.getString(BUNDLE_KEY_VIEW_LEFT_TEXT));
            rightTextView.setText(bundle.getString(BUNDLE_KEY_VIEW_RIGHT_TEXT));
            int imageResId = bundle.getInt(BUNDLE_KEY_VIEW_LEFT_IMAGE_RES_ID, 0);
            if (imageResId > 0) {
                setLeftIcon(imageResId);
            }
            setVisibility(bundle.getBoolean(BUNDLE_KEY_VIEW_VISIBILITY) ? View.VISIBLE : View.GONE);
            leftImageView.setVisibility(bundle.getBoolean(BUNDLE_KEY_VIEW_LEFT_IMAGE_VISIBILITY) ? View.VISIBLE : View.GONE);
            state = bundle.getParcelable(BUNDLE_KEY_SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    public void setLeftText(String text) {
        leftTextView.setText(text);
    }

    public void setRightText(String text) {
        rightTextView.setText(text);
    }

    public void setLeftIcon(int resId) {
        leftImageView.setImageResource(resId);
        leftImageView.setTag(resId);
    }

    public void hideLeftIcon() {
        leftImageView.setVisibility(View.GONE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

}
