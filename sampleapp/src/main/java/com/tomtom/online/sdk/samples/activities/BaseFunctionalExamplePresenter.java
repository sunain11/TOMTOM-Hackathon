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

import com.tomtom.online.sdk.common.util.Contextable;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.DimensionUtils;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseFunctionalExamplePresenter implements FunctionalExamplePresenter, Contextable {

    private static final int NETWORK_THREADS_NUMBER = 4;
    public static final float DEFAULT_ZOOM_TRAFFIC_LEVEL = 12.0f; //works from 11.1
    protected static final int DEFAULT_MAP_PADDING = 0;

    protected TomtomMap tomtomMap;
    protected FunctionalExampleFragment view;
    protected Scheduler networkScheduler = Schedulers.from(Executors.newFixedThreadPool(NETWORK_THREADS_NUMBER));

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {

        this.view = view;
        tomtomMap = map;

        alignCompassButton(view, map);
        alignCurrentLocationButton(view, map);
        
        view.setActionBarTitle(getModel().getPlayableTitle());
        view.setActionBarSubtitle(getModel().getPlayableSubtitle());

        confMapPadding();
    }

    @Override
    public void cleanup() {
        resetCompassButton(view, tomtomMap);
        resetMapPadding();
    }

    public abstract FunctionalExampleModel getModel();

    @Override
    public void alignCompassButton(FunctionalExampleFragment view, TomtomMap map) {
    }

    @Override
    public void resetCompassButton(FunctionalExampleFragment view, TomtomMap tomtomMap) {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume(Context context) {
    }

    @Override
    public void alignCurrentLocationButton(FunctionalExampleFragment view, TomtomMap tomtomMap) {
        int currentLocationButtonBottomMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.current_location_default_margin_bottom);
        int currentLocationButtonLeftMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.compass_default_margin_start);

        tomtomMap.getUiSettings().getCurrentLocationView()
                .setMargins(currentLocationButtonLeftMargin, 0, 0,
                        currentLocationButtonBottomMargin + getCurrentLocationBottomMarginDelta(view));
    }

    @Override
    public int getCurrentLocationBottomMarginDelta(FunctionalExampleFragment view) {
        return DimensionUtils.fromDpToPx(DEFAULT_CURRENT_LOCATION_BUTTON_BOTTOM_MARGIN,
                getContext().getResources().getDisplayMetrics());
    }

    protected void confMapPadding() {

        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.offset_extra_big);

        tomtomMap.setPadding(padding, padding, padding, padding);
    }

    protected void resetMapPadding(){
        tomtomMap.setPadding(DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING,
                DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING);
    }

    @Override
    public Context getContext() {
        return view.getContext();
    }

    @Override
    public void onStop() {

    }
}
