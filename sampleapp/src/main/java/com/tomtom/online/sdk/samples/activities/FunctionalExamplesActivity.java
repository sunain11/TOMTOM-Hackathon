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

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.BaseGpsPositionIndicator;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.BuildConfig;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.fragments.CurrentLocationFragment;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.BackButtonDelegate;

import java.util.Locale;

import timber.log.Timber;

//tag::doc_implement_tt_interfaces[]
public class FunctionalExamplesActivity extends AppCompatActivity
        implements BackButtonDelegate.BackButtonDelegateCallback, ActionBarModel {
    //end::doc_implement_tt_interfaces[]

    public static final String CURRENT_EXAMPLE_KEY = "CURRENT_EXAMPLE";

    public static final int EMPTY_EXAM = 1000;
    private int currentExampleId = EMPTY_EXAM;

    private BackButtonDelegate backButtonDelegate;
    private FunctionalExamplesNavigationManager functionalExamplesNavigationManager;

    //tag::doc_implement_on_map_ready_callback[]
    private OnMapReadyCallback onMapReadyCallback =
            new OnMapReadyCallback() {
                @Override
                public void onMapReady(TomtomMap map) {
                    //Map is ready here
                    tomtomMap = map;
                    tomtomMap.setMyLocationEnabled(true);
                    tomtomMap.setLanguage(Locale.getDefault().getLanguage());
                }
            };
    //end::doc_implement_on_map_ready_callback[]

    private TomtomMap tomtomMap;
    private MapFragment mapFragment;
    static TextView speedtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate()");
        super.onCreate(savedInstanceState);
        inflateActivity();
        //tag::doc_initialise_map[]
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getAsyncMap(onMapReadyCallback);
        speedtv=(TextView)findViewById(R.id.speed_text);
        //end::doc_initialise_map[]

        restoreState(savedInstanceState);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            setCurrentExample(new CurrentLocationFragment(), EMPTY_EXAM);
            return;
        }

        currentExampleId = savedInstanceState.getInt(CURRENT_EXAMPLE_KEY);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.functional_example_control_container);
        if (fragment != null) {
            setCurrentExample((FunctionalExampleFragment) fragment, currentExampleId);
        }
    }


    private void inflateActivity() {
        setContentView(R.layout.activity_main);
        initManagers();
        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_EXAMPLE_KEY, currentExampleId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!backButtonDelegate.onBackPressed(drawer)) {
            // The event was not consumed by the delegate
            // Then proceed with standard procedure.
            super.onBackPressed();
        }
    }

    @Override
    public boolean exitFromFunctionalExample(FunctionalExampleFragment newFragment, int newExampleId) {
        if (!closePreviousFunctionalExample()) {
            return false;
        }

        if (currentExampleId == EMPTY_EXAM) {
            super.onBackPressed();
        }

        setCurrentExample(newFragment, EMPTY_EXAM);
        return true;
    }

    public boolean closePreviousFunctionalExample() {
        FunctionalExampleFragment currentFragment = (FunctionalExampleFragment) getSupportFragmentManager()
                .findFragmentById(R.id.functional_example_control_container);
        return currentFragment.onBackPressed();
    }

    @Override
    public boolean isManeuversOrSearchFragmentOnTop() {
        return false;
    }

    private void initManagers() {
        functionalExamplesNavigationManager = new FunctionalExamplesNavigationManager(this);
        backButtonDelegate = new BackButtonDelegate(this);
    }

    private void initViews() {
        initDrawerAndToolbar();
        initNavigationView();
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(functionalExamplesNavigationManager);
        navigationView.setKeepScreenOn(true);
        updateAppVersion(navigationView);
    }

    private void initDrawerAndToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = initToggleForNavigationDrawer(toolbar, drawer);
        drawer.addDrawerListener(toggle);
    }

    @NonNull
    private ActionBarDrawerToggle initToggleForNavigationDrawer(Toolbar toolbar, final DrawerLayout drawer) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        };

        toggle.setDrawerIndicatorEnabled(true);
        toggle.setToolbarNavigationClickListener(new ToolbarNavigationClickListener(drawer));

        toggle.syncState();
        return toggle;
    }

    static class ToolbarNavigationClickListener implements View.OnClickListener {
        private DrawerLayout drawer;

        public ToolbarNavigationClickListener(DrawerLayout drawer) {

            this.drawer = drawer;
        }

        @Override
        public void onClick(View view) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public void setActionBarTitle(int titleId) {
        getSupportActionBar().setTitle(getString(titleId));
    }

    @Override
    public void setActionBarSubtitle(int subtitleId) {
        getSupportActionBar().setSubtitle(getString(subtitleId));
    }

    public void setCurrentExample(FunctionalExampleFragment currentExample, int itemId) {
        currentExampleId = itemId;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.functional_example_control_container, (Fragment) currentExample, currentExample.getFragmentTag())
                .commit();
    }

    private void updateAppVersion(NavigationView navigationView) {
        TextView appVersionTextView = navigationView.getHeaderView(0).findViewById(R.id.app_version);
        appVersionTextView.setText(BuildConfig.VERSION_NAME);
    }

    //tag::doc_map_permissions[]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        tomtomMap.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //end::doc_map_permissions[]

    /**
     * Custom GPS position indicator that forces accuracy to 0.
     */
    @SuppressWarnings("unused")
    //tag::doc_custom_gps_position_indicator
    static class CustomGpsPositionIndicator extends BaseGpsPositionIndicator {

        //To use this class, call setCustomGpsPositionIndicator on TomtomMap:
        //tomtomMap.setGpsPositionIndicator(new CustomGpsPositionIndicator());
        
        @Override
        public void setLocation(Location location) {
            setLocation(new LatLng(location), 0.0, 0.0);
            if(location.hasSpeed())
            {
                speedtv.setText(""+location.getSpeed());
            }
        }

        @Override
        public void setLocation(LatLng latLng, double bearingInDegrees, double accuracyInMeters) {
            super.show();
            super.setDimmed(false);
            super.setLocation(latLng, 0.0, 0.0);
        }
    }
    //end::doc_custom_gps_position_indicator

}
