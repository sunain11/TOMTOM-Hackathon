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

package com.tomtom.online.sdk.samples.cases.map.geopoliticalview;

import android.content.Context;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;

public class MapGeopoliticalViewPresenter extends BaseFunctionalExamplePresenter {

    private final LatLng ISRAEL_REGION = new LatLng(32.009929, 34.843555);
    private final static int DEFAULT_MAP_ZOOM_LEVEL_FOR_EXAMPLE = 7;

    final static String ISRAEL_LOCAL_VIEW = "IL";
    final static String INTERNATIONAL_VIEW = "Unified";
    final static String DEFAULT_VIEW = "";

    @Override
    public void bind(final FunctionalExampleFragment view, final TomtomMap map) {
        super.bind(view, map);
        if (!view.isMapRestored()) {
            centerMapOnDefaultLocation();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new MapGeopoliticalViewFunctionalExample();
    }

    private void centerMapOnDefaultLocation() {
        tomtomMap.centerOn(
                ISRAEL_REGION.getLatitude(),
                ISRAEL_REGION.getLongitude(),
                DEFAULT_MAP_ZOOM_LEVEL_FOR_EXAMPLE,
                MapConstants.ORIENTATION_NORTH
        );
    }

    @Override
    public void cleanup() {
        tomtomMap.setGeopoliticalView(DEFAULT_VIEW);
    }

    public void simulateIsrael() {
        centerMapOnDefaultLocation();
        //tag::doc_map_geopolitical_view[]
        //ISRAEL_LOCAL_VIEW = "IL"
        tomtomMap.setGeopoliticalView(ISRAEL_LOCAL_VIEW);
        //end::doc_map_geopolitical_view[]
    }

    public void simulateUnified() {
        centerMapOnDefaultLocation();
        tomtomMap.setGeopoliticalView(INTERNATIONAL_VIEW);
    }

}