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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tomtom.online.sdk.samples.R;

public class RadioModifierView extends LinearLayout {


    public enum ModifierButton {
        LEFT(R.id.leftButton),
        RIGHT(R.id.rightButton),
        FIRST_MIDDLE(R.id.firstMiddleButton),
        SECOND_MIDDLE(R.id.secondMiddleButton);

        public int id;

        ModifierButton(int id) {
            this.id = id;
        }
    }

    private ModifierButton currentlySelectedButton;

    RadioModifierListener searchModifiersLViewListener;

    public void setSearchModifiersLViewListener(RadioModifierListener searchModifiersLViewListener) {
        this.searchModifiersLViewListener = searchModifiersLViewListener;
    }

    private Button leftBtn, firstMiddleBtn, secondMiddleBtn, rightBtn;

    private boolean firstMiddleRadioVisible;

    private boolean secondMiddleRadioVisible;

    public void selectItem(ModifierButton button) {
        deselectAll();
        currentlySelectedButton = button;
        findViewById(button.id).setSelected(true);
    }

    public void clickItem(ModifierButton button){
        selectItem(button);
        findViewById(button.id).performClick();
    }

    private void deselectAll() {
        leftBtn.setSelected(false);
        firstMiddleBtn.setSelected(false);
        secondMiddleBtn.setSelected(false);
        rightBtn.setSelected(false);
        currentlySelectedButton = null;
    }

    public ModifierButton getSelectedItem() {
        return currentlySelectedButton;
    }

    public RadioModifierView(Context context) {
        super(context);
        init(context, null);
    }

    public RadioModifierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void initClickListeners() {
        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAll();
                leftBtn.setSelected(true);
                currentlySelectedButton = ModifierButton.LEFT;
                if (searchModifiersLViewListener != null) {
                    searchModifiersLViewListener.onLeftButtonClicked();
                }
            }
        });

        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAll();
                rightBtn.setSelected(true);
                currentlySelectedButton = ModifierButton.RIGHT;
                if (searchModifiersLViewListener != null) {
                    searchModifiersLViewListener.onRightButtonClicked();
                }
            }
        });

        firstMiddleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAll();
                firstMiddleBtn.setSelected(true);
                currentlySelectedButton = ModifierButton.FIRST_MIDDLE;
                if (searchModifiersLViewListener != null) {
                    searchModifiersLViewListener.onFirstMiddleButtonClicked();
                }
            }
        });

        secondMiddleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAll();
                secondMiddleBtn.setSelected(true);
                currentlySelectedButton = ModifierButton.SECOND_MIDDLE;
                if (searchModifiersLViewListener != null) {
                    searchModifiersLViewListener.onSecondMiddleButtonClicked();
                }
            }
        });
    }

    private void initMiddleButtons(Context context, AttributeSet attrs) {
        if(attrs == null){
            return;
        }

        TypedArray radioModifierViewAttrs = context.obtainStyledAttributes(attrs, R.styleable.RadioModifierView);

        setupFirstMiddleButton(radioModifierViewAttrs);
        setupSecondMiddleButton(radioModifierViewAttrs);

        radioModifierViewAttrs.recycle();
    }

    private void setupFirstMiddleButton(TypedArray radioModifierViewAttrs) {
        int firstMiddleButtonVisibility = radioModifierViewAttrs.getIndex(0);

        firstMiddleRadioVisible = radioModifierViewAttrs.getBoolean(firstMiddleButtonVisibility, true);
        if (!firstMiddleRadioVisible) {
            firstMiddleBtn.setVisibility(GONE);
        }
    }

    private void setupSecondMiddleButton(TypedArray radioModifierViewAttrs) {
        int secondMiddleButtonVisibility = radioModifierViewAttrs.getIndex(1);

        secondMiddleRadioVisible = radioModifierViewAttrs.getBoolean(secondMiddleButtonVisibility, true);
        if (!secondMiddleRadioVisible) {
            secondMiddleBtn.setVisibility(GONE);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        findViews(context);
        initClickListeners();
        initMiddleButtons(context, attrs);
    }

    private void findViews(Context context) {
        inflate(context, R.layout.view_radio_modifier, this);
        leftBtn = (Button) findViewById(R.id.leftButton);
        firstMiddleBtn = (Button) findViewById(R.id.firstMiddleButton);
        secondMiddleBtn = (Button) findViewById(R.id.secondMiddleButton);
        rightBtn = (Button) findViewById(R.id.rightButton);
    }

    public void setRadioNames(String leftRadioButtonName, String rightRadioButtonName) {
        leftBtn.setText(leftRadioButtonName);
        rightBtn.setText(rightRadioButtonName);
    }

    public void setRadioNames(String leftRadioButtonName, String rightRadioButtonName, String middleRadioButtonName) {
        setRadioNames(leftRadioButtonName, rightRadioButtonName);
        firstMiddleBtn.setText(middleRadioButtonName);
    }

    public void setRadioNames(String leftRadioButtonName, String rightRadioButtonName, String firstMiddleRadioButtonName, String secondMiddleRadioButtonName) {
        setRadioNames(leftRadioButtonName, rightRadioButtonName, firstMiddleRadioButtonName);
        secondMiddleBtn.setText(secondMiddleRadioButtonName);
    }

    @Override
    public void setEnabled(boolean enabled) {
        leftBtn.setEnabled(enabled);
        firstMiddleBtn.setEnabled(enabled);
        secondMiddleBtn.setEnabled(enabled);
        rightBtn.setEnabled(enabled);
    }

    public void setFirstMiddleRadioVisible(boolean visible){
        firstMiddleBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setSecondMiddleRadioVisible(boolean visible){
        secondMiddleBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public ModifierButton findButtonByCaption(String caption) {
        if (leftBtn.getText().toString().contains(caption)) {
            return ModifierButton.LEFT;
        } else if (rightBtn.getText().toString().contains(caption)) {
            return ModifierButton.RIGHT;
        } else if (firstMiddleBtn.getText().toString().contains(caption)) {
            return ModifierButton.FIRST_MIDDLE;
        } else if (secondMiddleBtn.getText().toString().contains(caption)) {
            return ModifierButton.SECOND_MIDDLE;
        }
        return null;
    }


    public void selectButtonByCaption(String caption) {
        selectItem(findButtonByCaption(caption));
    }

    public boolean isLeftBtnSelected(){
        return leftBtn.isSelected();
    }

    public boolean isRightBtnSelected(){
        return rightBtn.isSelected();
    }

    public boolean isFirstMiddleBtnSelected() {
        return firstMiddleBtn.isSelected();
    }

    public boolean isSecondMiddleBtnSelected() {
        return secondMiddleBtn.isSelected();
    }

    public interface RadioModifierListener {
        void onLeftButtonClicked();

        void onRightButtonClicked();

        void onFirstMiddleButtonClicked();

        void onSecondMiddleButtonClicked();
    }

}
