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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tomtom.online.sdk.samples.R;

public class OptionsButtonsView extends LinearLayout {


    public enum SelectMode {
        SINGLE,
        MULTI
    }

    public interface OptionsChangeValue {
        void onChange(boolean[] oldValues, boolean[] newValues);
    }

    public static final String SAVE_STATE_SUPER = "SAVE_STATE_SUPER";
    public static final String SAVE_STATE_SELECTS = "SAVE_STATE_SELECTS";
    public static final String SAVE_STATE_MODE = "SAVE_STATE_MODE";

    private SelectMode selectMode = SelectMode.SINGLE;
    private OptionsChangeValue listener;

    public OptionsButtonsView(Context context) {
        super(context);
        init();
    }

    public OptionsButtonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OptionsButtonsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionsButtonsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setListener(OptionsChangeValue listener) {
        this.listener = listener;
    }

    public void setSelectMode(SelectMode selectMode) {
        this.selectMode = selectMode;
    }

    public void selectItem(int index, boolean selected) {
        getChildAt(index).setSelected(selected);
    }

    public void unSelectAll() {
        for(int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setSelected(false);
        }
    }

    public boolean isSelected(int index) {
        return getChildAt(index).isSelected();
    }

    public void addOption(@StringRes int resId) {
        addOption(getResources().getString(resId));
    }

    public void addOption(String name) {
        Button button = buildButton(name);
        button.setOnClickListener(new SelectClickListener(this, button));
        addView(button);
    }

    public boolean isSingleMode() {
        return selectMode == SelectMode.SINGLE;
    }


    static class SelectClickListener implements OnClickListener {
        private OptionsButtonsView optionsButtonsView;
        private Button button;

        public SelectClickListener(OptionsButtonsView optionsButtonsView, Button button) {
            this.optionsButtonsView = optionsButtonsView;
            this.button = button;
        }

        @Override
        public void onClick(View v) {
            boolean[] state = optionsButtonsView.getSelectionState();
            if (optionsButtonsView.isSingleMode()) {
                optionsButtonsView.unSelectAll();
                button.setSelected(true);
            } else {
                button.setSelected(!button.isSelected());
            }
            optionsButtonsView.notifyListener(state, optionsButtonsView.getSelectionState());
        }
    }

    protected Button buildButton(String name) {
        Button button = new Button(new ContextThemeWrapper(getContext(), R.style.ControlButton),
                                         null, R.style.ControlButton);
        LayoutParams params = new LayoutParams((int)getResources().getDimension(R.dimen.control_button_width),
                (int)getResources().getDimension(R.dimen.control_button_height));
        int margins = (int) getResources().getDimension(R.dimen.control_button_margin);
        params.setMargins(margins, margins, margins, margins);
        button.setLayoutParams(params);
        button.setText(name);
        return button;
    }

    private void notifyListener(boolean[] oldState, boolean[] newState) {
        if(listener != null) {
            listener.onChange(oldState, newState);
        }
    }

    public boolean[] getSelectionState() {
        boolean[] result = new boolean[getChildCount()];
        for(int i = 0; i < getChildCount(); i++) {
            result[i] = getChildAt(i).isSelected();
        }
        return result;
    }

    public int getFirstSelectedItem() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    //save view state

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVE_STATE_SUPER, super.onSaveInstanceState());
        bundle.putBooleanArray(SAVE_STATE_SELECTS, getSelectionState());
        bundle.putInt(SAVE_STATE_MODE, selectMode.ordinal());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) { // implicit null check
            Bundle bundle = (Bundle) state;
            boolean[] restore = bundle.getBooleanArray(SAVE_STATE_SELECTS);
            updateRestoreState(restore);
            selectMode = SelectMode.values()[bundle.getInt(SAVE_STATE_MODE)];
            state = bundle.getParcelable(SAVE_STATE_SUPER);
        }
        super.onRestoreInstanceState(state);
    }

    private void updateRestoreState(boolean[] restore) {
        for(int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setSelected(restore[i]);
        }
    }

    public void performClickSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).isSelected()) {
                getChildAt(i).performClick();
            }
        }
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for(int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setEnabled(enabled);
        }
    }
}
