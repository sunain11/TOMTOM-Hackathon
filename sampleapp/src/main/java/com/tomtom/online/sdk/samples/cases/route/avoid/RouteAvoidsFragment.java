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
package com.tomtom.online.sdk.samples.cases.route.avoid;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tomtom.online.sdk.routing.data.Avoid;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.RoutePlannerFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class RouteAvoidsFragment extends RoutePlannerFragment<RouteAvoidsPresenter> {

    public static final String ROUTE_AVOID = "route_avoid";
    private Avoid avoidOnRoute;

    @Override
    protected RouteAvoidsPresenter createPresenter() {
        return new RouteAvoidsPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (avoidOnRoute != null) {
            presenter.setAvoid(avoidOnRoute);
        }
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.btn_text_avoid_motorways);
        view.addOption(R.string.btn_text_avoid_tolls);
        view.addOption(R.string.btn_text_avoid_ferries);
    }

    @Override
    public void onChange(final boolean[] oldValues, final boolean[] newValues) {
        presenter.centerOnDefaultLocation();
        if (newValues[0]) {
            presenter.startRoutingAvoidMotorways();
        } else if (newValues[1]) {
            presenter.startRoutingAvoidTollRoads();
        } else if (newValues[2]) {
            presenter.startRoutingAvoidFerries();
        }
    }

    @Override
    public void onRestoreSavedInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreSavedInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            avoidOnRoute = (Avoid) savedInstanceState.getSerializable(ROUTE_AVOID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ROUTE_AVOID, presenter.getAvoid());
    }

}
