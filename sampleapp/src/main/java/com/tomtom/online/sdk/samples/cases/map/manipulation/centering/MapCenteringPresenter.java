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
package com.tomtom.online.sdk.samples.cases.map.manipulation.centering;

import android.location.Location;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

public class MapCenteringPresenter extends BaseFunctionalExamplePresenter {

    private final LatLng defaultLocation = Locations.AMSTERDAM_LOCATION;


    @Override
    public void bind(final FunctionalExampleFragment view, final TomtomMap map) {
        super.bind(view, map);
        if (!view.isMapRestored()) {
            centerMapOnLocation();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new MapCenteringFunctionalExample();
    }

    private void centerMapOnLocation() {
        tomtomMap.centerOn(
                defaultLocation.getLatitude(),
                defaultLocation.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH
        );
    }

    @Override
    public void cleanup() {
    }

    public void centerOnAmsterdam() {

        view.setActionBarSubtitle(R.string.amsterdam_city_name);

        //tag::doc_map_center_on_amsterdam[]
        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH);

        //end::doc_map_center_on_amsterdam[]
    }

    public void centerOnBerlin() {

        view.setActionBarSubtitle(R.string.berlin_city_name);

        //tag::doc_map_center_on_berlin[]
        CameraPosition cameraPosition = CameraPosition
                .builder(Locations.BERLIN_LOCATION)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(DEFAULT_ZOOM_LEVEL)
                .build();
        tomtomMap.centerOn(cameraPosition);
        //end::doc_map_center_on_berlin[]
    }

    public void centerOnLondon() {
        view.setActionBarSubtitle(R.string.map_center_london);

        //tag::doc_map_center_on_london[]
        CameraPosition cameraPosition = CameraPosition
                .builder(Locations.LONDON_LOCATION)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(DEFAULT_ZOOM_LEVEL)
                .build();
        tomtomMap.centerOn(cameraPosition);
        //end::doc_map_center_on_london[]
    }

    @SuppressWarnings("unused")
    public void turnOnMyLocation(){
        //tag::doc_my_location_enabled[]
        tomtomMap.setMyLocationEnabled(true);
        //end::doc_my_location_enabled[]
    }
    @SuppressWarnings("unused")
    public Location getCurrentLocation(){
        //tag::doc_get_current_location[]
        Location location = tomtomMap.getUserLocation();
        //end::doc_get_current_location[]
        return location;
    }


}
