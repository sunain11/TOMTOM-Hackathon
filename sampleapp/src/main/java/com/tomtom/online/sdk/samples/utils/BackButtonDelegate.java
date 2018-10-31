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
package com.tomtom.online.sdk.samples.utils;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.tomtom.online.sdk.samples.fragments.CurrentLocationFragment;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;

public final class BackButtonDelegate {

    private final BackButtonDelegateCallback backButtonDelegateCallback;

    public BackButtonDelegate(final BackButtonDelegateCallback callback) {
        backButtonDelegateCallback = callback;
    }

    public boolean onBackPressed(final DrawerLayout drawer) {
        boolean eventConsumed;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            eventConsumed = true;
        } else if (backButtonDelegateCallback.isManeuversOrSearchFragmentOnTop()) {
            eventConsumed = false;
        } else {
            backButtonDelegateCallback.exitFromFunctionalExample(new CurrentLocationFragment(), 1000);
            eventConsumed = true;
        }

        return eventConsumed;
    }

    public interface BackButtonDelegateCallback {
        boolean exitFromFunctionalExample(final FunctionalExampleFragment nextExample, int itemId);
        boolean isManeuversOrSearchFragmentOnTop();
    }

}
