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
package com.tomtom.online.sdk.samples.cases.route.modes;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tomtom.online.sdk.routing.data.TravelMode;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.RoutePlannerFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class RouteTravelModesFragment extends RoutePlannerFragment<RouteTravelModesPresenter> {

    public static final String ROUTE_TRAVEL_MODE = "route_travel_mode";
    private TravelMode travelMode;

    @Override
    protected RouteTravelModesPresenter createPresenter() {
        return new RouteTravelModesPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (travelMode != null) {
            presenter.setTravelMode(travelMode);
        }
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.btn_text_travel_mode_car);
        view.addOption(R.string.btn_text_travel_mode_truck);
        view.addOption(R.string.btn_text_travel_mode_pedestrian);
    }

    @Override
    public void onChange(final boolean[] oldValues, final boolean[] newValues) {
        if (newValues[0]) {
            presenter.startTravelModeCar();
        } else if (newValues[1]) {
            presenter.startTravelModeTruck();
        } else if (newValues[2]) {
            presenter.startTravelModePedestrian();
        }
    }

    @Override
    public void onRestoreSavedInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreSavedInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            travelMode = (TravelMode) savedInstanceState.getSerializable(ROUTE_TRAVEL_MODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ROUTE_TRAVEL_MODE, presenter.getTravelMode());
    }

}
