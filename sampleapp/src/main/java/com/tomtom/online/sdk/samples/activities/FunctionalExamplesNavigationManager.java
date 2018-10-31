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
package com.tomtom.online.sdk.samples.activities;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.FunctionalExamplesFactory;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;

public class FunctionalExamplesNavigationManager implements NavigationView.OnNavigationItemSelectedListener {

    private final FunctionalExamplesActivity functionalExamplesActivity;

    public FunctionalExamplesNavigationManager(final FunctionalExamplesActivity functionalExamplesActivity) {
        this.functionalExamplesActivity = functionalExamplesActivity;
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) functionalExamplesActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        closeDrawer();

        functionalExamplesActivity.closePreviousFunctionalExample();

        setNewFunctionalExample(item.getItemId());
        return true;
    }

    private void setNewFunctionalExample(int itemID) {
        final FunctionalExampleFragment functionalExampleFragment = new FunctionalExamplesFactory().create(itemID);
        functionalExamplesActivity.setCurrentExample(functionalExampleFragment, itemID);
        functionalExampleFragment.onMenuItemSelected();
    }

}