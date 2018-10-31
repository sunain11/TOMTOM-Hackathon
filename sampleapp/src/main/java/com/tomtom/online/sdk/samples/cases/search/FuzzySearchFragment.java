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
import com.tomtom.online.sdk.samples.utils.views.RadioModifierView;

public class FuzzySearchFragment extends SearchFragment {

    public FuzzySearchFragment() {
        searchPresenter = new FuzzySearchFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        searchTextView.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    protected void initSearchModifiersView(View view) {
        searchModifiersView = (RadioModifierView) view.findViewById(R.id.search_modifier_tabs);
        searchModifiersView.setRadioNames(getString(R.string.fuzzy_max_first_label), getString(R.string.fuzzy_max_third_label), getString(R.string.fuzzy_max_second_label));
        searchModifiersView.setSearchModifiersLViewListener(this);
        searchModifiersView.setFirstMiddleRadioVisible(true);
        searchModifiersView.setSecondMiddleRadioVisible(false);
        searchModifiersView.selectItem(RadioModifierView.ModifierButton.LEFT);
        searchModifiersView.setEnabled(true);
    }

    private void searchFuzzyByMaxLevel(int maxLevel) {
        disableToggleButtons();
        ((FuzzySearchFragmentPresenter) searchPresenter).performFuzzySearch(searchTextView.getText().toString(), maxLevel);
    }

    @Override
    public void onLeftButtonClicked() {
        searchFuzzyByMaxLevel(1);
    }

    @Override
    public void onRightButtonClicked() {
        searchFuzzyByMaxLevel(2);
    }

    @Override
    public void onFirstMiddleButtonClicked() {
        searchFuzzyByMaxLevel(3);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchPresenter.enableSearchUI();
        //Override default values
        setActionBarSubtitle(R.string.fuzzy_search_description);
        setActionBarTitle(R.string.fuzzy_search);
    }
}
