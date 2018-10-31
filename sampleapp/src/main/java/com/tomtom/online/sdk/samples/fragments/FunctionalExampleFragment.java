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
package com.tomtom.online.sdk.samples.fragments;

import android.content.Context;
import android.support.annotation.StringRes;

import com.tomtom.online.sdk.samples.activities.ActionBarModel;
import com.tomtom.online.sdk.samples.activities.InfoViewModel;
import com.tomtom.online.sdk.samples.activities.OptionsViewModel;

public interface FunctionalExampleFragment extends ActionBarModel, InfoViewModel, OptionsViewModel {

    /**
     * Method created just once when position menu chosen.
     */
    void onMenuItemSelected();

    boolean onBackPressed();

    String getString(@StringRes int textId);

    Context getContext();

    boolean isMapRestored();

    String getFragmentTag();

}
