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
package com.tomtom.online.sdk.samples.cases.map.layers.styles;

import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

public class CustomMapStylePresenter extends BaseFunctionalExamplePresenter {

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        if (!view.isMapRestored()) {
            centerOnAmsterdam();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new CustomMapStyleFunctionalExample();
    }

    @Override
    public void cleanup() {
        showBaseStyle();
    }

    public void centerOnAmsterdam() {

        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH);

    }

    public void showNightStyle() {
        //tag::doc_set_style[]
        tomtomMap.getUiSettings().setStyleUrl("asset://styles/night.json");
        //end::doc_set_style[]
    }

    public void showBaseStyle() {
        tomtomMap.getUiSettings().setStyleUrl("asset://styles/mapssdk-default-style.json");
    }
}
