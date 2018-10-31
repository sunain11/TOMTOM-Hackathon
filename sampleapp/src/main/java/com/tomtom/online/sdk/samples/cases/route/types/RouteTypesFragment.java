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
package com.tomtom.online.sdk.samples.cases.route.types;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tomtom.online.sdk.routing.data.RouteType;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.RoutePlannerFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class RouteTypesFragment extends RoutePlannerFragment<RouteTypesPresenter> {

    public static final String ROUTE_TYPE = "route_type";
    private RouteType routeType;

    @Override
    protected RouteTypesPresenter createPresenter() {
        return new RouteTypesPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (routeType != null) {
            presenter.setType(routeType);
        }
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.btn_text_fastest);
        view.addOption(R.string.btn_text_shortest);
        view.addOption(R.string.btn_text_eco);
    }

    @Override
    public void onChange(final boolean[] oldValues, final boolean[] newValues) {
        if (newValues[0]) {
            presenter.startRoutingFastest();
        } else if (newValues[1]) {
            presenter.startRoutingShortest();
        } else if (newValues[2]) {
            presenter.startRoutingEco();
        }
    }

    @Override
    public void onRestoreSavedInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreSavedInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            routeType = (RouteType) savedInstanceState.getSerializable(ROUTE_TYPE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ROUTE_TYPE, presenter.getType());
    }

}
