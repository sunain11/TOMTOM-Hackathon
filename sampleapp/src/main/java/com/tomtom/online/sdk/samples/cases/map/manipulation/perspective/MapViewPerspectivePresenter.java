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
package com.tomtom.online.sdk.samples.cases.map.manipulation.perspective;

import android.content.Context;

import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.model.MapModeType;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

public class MapViewPerspectivePresenter extends BaseFunctionalExamplePresenter {


    @Override
    public void bind(final FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        if (!view.isMapRestored()) {
            centerOnAmsterdam();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new MapViewPerspectiveFunctionalExample();
    }

    @Override
    public void cleanup() {
        tomtomMap.getUiSettings().setMapModeType(MapModeType.MODE_2D);
    }

    public void show2DMode() {
        //tag::doc_change_map_to_2D[]
        tomtomMap.set2DMode();
        //end::doc_change_map_to_2D[]
    }

    public void show3DMode() {
        //tag::doc_change_map_to_3D[]
        tomtomMap.set3DMode();
        //end::doc_change_map_to_3D[]
    }

    public void centerOnAmsterdam() {
        tomtomMap.centerOn(Locations.AMSTERDAM_LOCATION.getLatitude(), Locations.AMSTERDAM_LOCATION.getLongitude(), DEFAULT_ZOOM_LEVEL, MapConstants.ORIENTATION_NORTH);
    }
}
