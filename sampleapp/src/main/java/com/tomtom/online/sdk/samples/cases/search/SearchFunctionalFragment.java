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

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.ActionBarModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;

public class SearchFunctionalFragment extends Fragment implements FunctionalExampleFragment {

    protected ActionBarModel actionBar;

    @Override
    public void onResume() {
        super.onResume();
        actionBar = (ActionBarModel) getActivity();

        setActionBarTitle(R.string.address_search);
        setActionBarSubtitle(R.string.address_search_description);
    }

    @Override
    public void setActionBarTitle(@StringRes int titleId) {
        actionBar.setActionBarTitle(titleId);
    }

    @Override
    public void setActionBarSubtitle(@StringRes int subtitleId) {
        actionBar.setActionBarSubtitle(subtitleId);
    }

    @Override
    public void onMenuItemSelected() {

    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public boolean isMapRestored() {
        return false;
    }

    @Override
    public String getFragmentTag() {
        return getClass().getName();
    }

    @Override
    public void showInfoText(String text, long duration) {

    }

    @Override
    public void showInfoText(int text, long duration) {

    }

    @Override
    public void enableOptionsView() {

    }

    @Override
    public void disableOptionsView() {

    }
}
