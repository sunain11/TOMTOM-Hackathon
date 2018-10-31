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
package com.tomtom.online.sdk.samples.cases.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.utils.LanguageSelector;
import com.tomtom.online.sdk.samples.utils.views.RadioModifierView;

public class LanguageSelectorSearchFragment extends SearchFragment {

    public LanguageSelectorSearchFragment() {
        searchPresenter = new LanguageSelectorSearchFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initSearchModifiersView(View view) {
        searchModifiersView = (RadioModifierView) view.findViewById(R.id.search_modifier_tabs);
        searchModifiersView.setRadioNames(
                LanguageSelector.EN.getDecoratedName(),
                LanguageSelector.FR.getDecoratedName(),
                LanguageSelector.DE.getDecoratedName(),
                LanguageSelector.ES.getDecoratedName());
        searchModifiersView.selectItem(RadioModifierView.ModifierButton.LEFT);
        searchModifiersView.setSearchModifiersLViewListener(this);
        searchModifiersView.setFirstMiddleRadioVisible(true);
        searchModifiersView.setSecondMiddleRadioVisible(true);
        searchModifiersView.setEnabled(false);
    }

    @Override
    public void onLeftButtonClicked() {
        performAction(LanguageSelector.EN.getCode());
    }

    @Override
    public void onRightButtonClicked() {
        performAction(LanguageSelector.FR.getCode());
    }

    @Override
    public void onFirstMiddleButtonClicked() {
        performAction(LanguageSelector.DE.getCode());
    }

    @Override
    public void onSecondMiddleButtonClicked() {
        performAction(LanguageSelector.ES.getCode());
    }

    private void performAction(String code) {
        if (isValidSearchInput()) {
            return;
        }

        disableToggleButtons();
        searchPresenter.performSearch(getSearchFieldText(), code);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchPresenter.onResume();
        //Override default values
        setActionBarSubtitle(R.string.empty);
        setActionBarTitle(R.string.language_selector_search);
    }

}
