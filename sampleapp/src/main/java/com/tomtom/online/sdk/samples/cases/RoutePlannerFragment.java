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
package com.tomtom.online.sdk.samples.cases;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.Summary;
import com.tomtom.online.sdk.samples.utils.CheckedButtonCleaner;
import com.tomtom.online.sdk.samples.utils.formatter.DistanceFormatter;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Map;

public abstract class RoutePlannerFragment<T extends RoutePlannerPresenter> extends ExampleFragment<T>
        implements CheckedButtonCleaner, RoutingUiListener {

    private static final String KEY_ROUTES = "KEY_ROUTES";
    private static final String KEY_SELECTED_ROUTE = "KEY_ROUTES_SELECTED";
    private static final String KEY_ROUTING_IN_PROGRESS = "ROUTING_IN_PROGRESS";

    private RoutePlanningInProgressDialog routingInProgressDialog = new RoutePlanningInProgressDialog();
    protected final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private boolean routeInProgress = false;
    private int selectedKey;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onRestoreSavedInstanceState(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setCheckedButtonCleaner(this);
    }

    @Override
    public void cleanCheckedButtons() {
        optionsView.unSelectAll();
    }


    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {

    }

    @Override
    public void repeatRequestWhenNotFinished() {
        if (routeInProgress) {
            optionsView.selectItem(selectedKey, true);
            optionsView.performClickSelected();
        }
    }

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {

    }

    @Override
    public void showRoutingInProgressDialog() {
        routeInProgress = true;
        optionsView.setEnabled(false);
        routingInProgressDialog.show(getFragmentManager());
    }

    @Override
    public void hideRoutingInProgressDialog() {
        routingInProgressDialog.dismiss();
        optionsView.setEnabled(true);
    }

    @Override
    public void routeUpdated(FullRoute route) {
        getInfoBarView().show();
        Summary routeSummary = route.getSummary();

        int distance = routeSummary.getLengthInMeters();
        getInfoBarView().setRightText(DistanceFormatter.format(distance));

        String arrivalTime = timeFormat.format(routeSummary.getArrivalTime());
        getInfoBarView().setLeftText(arrivalTime + (route.getTag() == null ? " " : " " + route.getTag()));

        routeInProgress = false;
    }


    @Override
    public void showError(@StringRes int message) {
        showError(getString(message));
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(getContext()).setMessage(message).show();
    }

    public void onRestoreSavedInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            routeInProgress = savedInstanceState.getBoolean(KEY_ROUTING_IN_PROGRESS);
            selectedKey = savedInstanceState.getInt(KEY_SELECTED_ROUTE);
            if (routeInProgress) {
                getInfoBarView().hide();
                return;
            }
            restoreRoute(savedInstanceState);
        }
    }

    @SuppressWarnings("unchecked")
    private void restoreRoute(@NonNull Bundle savedInstanceState) {
        Serializable routeObj = savedInstanceState.getSerializable(KEY_ROUTES);
        if (routeObj instanceof Map) {
            Map<Long, FullRoute> routes = (Map<Long, FullRoute>) routeObj;
            presenter.setRoutesMap(routes);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_ROUTES, (Serializable) presenter.getRoutesMap());
        outState.putBoolean(KEY_ROUTING_IN_PROGRESS, routeInProgress);
        outState.putInt(KEY_SELECTED_ROUTE, optionsView.getFirstSelectedItem());
        super.onSaveInstanceState(outState);
    }

}
