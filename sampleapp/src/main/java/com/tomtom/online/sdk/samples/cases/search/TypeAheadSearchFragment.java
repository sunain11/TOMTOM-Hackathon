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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.utils.views.RadioModifierView;

public class TypeAheadSearchFragment extends SearchFragment {

    public TypeAheadSearchFragment() {
        searchPresenter = new TypeAheadSearchFragmentPresenter(this);
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
        searchModifiersView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        searchTextView.removeTextChangedListener(searchTextWatcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchTextView.addTextChangedListener(searchTextWatcher);
        searchPresenter.enableSearchUI();
        //Override default values
        setActionBarSubtitle(R.string.type_ahead_search_description);
        setActionBarTitle(R.string.type_ahead_search);
    }

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No use here
        }

        @Override
        public void onTextChanged(CharSequence queryPart, int start, int before, int count) {
            searchPresenter.performSearch(queryPart.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            // No use here
        }
    };

}
