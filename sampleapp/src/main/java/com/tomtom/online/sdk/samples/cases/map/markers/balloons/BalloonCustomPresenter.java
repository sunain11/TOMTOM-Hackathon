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
package com.tomtom.online.sdk.samples.cases.map.markers.balloons;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.BaseMarkerBalloon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.Marker;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.SingleLayoutBalloonViewAdapter;
import com.tomtom.online.sdk.map.TextBalloonViewAdapter;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

public class BalloonCustomPresenter extends BaseFunctionalExamplePresenter {

    private final static LatLng location = Locations.AMSTERDAM_LOCATION;

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        if (!view.isMapRestored()) {
            centerMapOnLocation();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new BalloonFunctionalExample();
    }

    @Override
    public void cleanup() {
        tomtomMap.getMarkerSettings().setMarkerBalloonViewAdapter(new TypedBalloonViewAdapter());
        tomtomMap.removeMarkers();
    }

    public void createSimpleBalloon() {
        centerMapOnLocation();
        tomtomMap.removeMarkers();
        //tag::doc_marker_balloon_model[]
        BaseMarkerBalloon markerBalloon = new BaseMarkerBalloon();
        markerBalloon.addProperty("key", "value");
        //end::doc_marker_balloon_model[]
        //tag::doc_init_popup_layout[]
        tomtomMap.getMarkerSettings().setMarkerBalloonViewAdapter(new TextBalloonViewAdapter());
        SimpleMarkerBalloon balloon = new SimpleMarkerBalloon("Welcome to TomTom");
        MarkerBuilder markerBuilder = new MarkerBuilder(Locations.AMSTERDAM_LOCATION)
                .markerBalloon(balloon);

        Marker m = tomtomMap.addMarker(markerBuilder);
        balloon.setText("Welcome to TomTom");
        //end::doc_init_popup_layout[]
        m.select();
    }

    public void createCustomBalloon() {
        centerMapOnLocation();
        tomtomMap.removeMarkers();

        //tag::doc_show_popup_layout[]
        tomtomMap.getMarkerSettings().setMarkerBalloonViewAdapter(new SingleLayoutBalloonViewAdapter(R.layout.custom_balloon_layout));
        MarkerBuilder markerBuilder = new MarkerBuilder(Locations.AMSTERDAM_LOCATION)
                .markerBalloon(new BaseMarkerBalloon());
        Marker m = tomtomMap.addMarker(markerBuilder);
        //end::doc_show_popup_layout[]
        m.select();

    }

    public void centerMapOnLocation() {
        tomtomMap.centerOn(
                location.getLatitude(),
                location.getLongitude(),
                DEFAULT_ZOOM_LEVEL, MapConstants.ORIENTATION_NORTH);
    }

}
