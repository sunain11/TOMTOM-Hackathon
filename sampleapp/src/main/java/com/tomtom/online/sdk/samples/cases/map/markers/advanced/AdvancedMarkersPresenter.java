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
package com.tomtom.online.sdk.samples.cases.map.markers.advanced;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.Marker;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.map.markers.balloons.TypedBalloonViewAdapter;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;
import com.tomtom.online.sdk.samples.utils.formatter.LatLngFormatter;

import java.util.List;

import timber.log.Timber;

public class AdvancedMarkersPresenter extends BaseFunctionalExamplePresenter {

    private static final String SAMPLE_GIF_ASSET_PATH = "images/racing_car.gif";

    private static final String TOAST_MESSAGE = "%s : %f, %f";
    private static final int TOAST_DURATION_MS = 2000; //milliseconds

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);

        tomtomMap.getMarkerSettings().setMarkerBalloonViewAdapter(new TypedBalloonViewAdapter());

        //tag::doc_register_draggable_marker_listener[]
        tomtomMap.getMarkerSettings().addOnMarkerDragListener(onMarkerDragListener);
        //end::doc_register_draggable_marker_listener[]

        if (!view.isMapRestored()) {
            centerMapOnLocation();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new AdvancedMarkersFunctionalExample();
    }

    @Override
    public void cleanup() {
        if (!isMapReady()) {
            return;
        }
        tomtomMap.removeMarkers();
    }

    @Override
    public void onPause() {
        if (!isMapReady()) {
            return;
        }
        tomtomMap.removeOnMarkerDragListeners();
    }

    public void createAnimatedMarkers() {
        resetMap();
        List<LatLng> list = Locations.randomLocation(Locations.AMSTERDAM_LOCATION, 5, 0.2f);
        for (LatLng position : list) {
            //tag::doc_create_animated_marker[]
            MarkerBuilder markerBuilder = new MarkerBuilder(position)
                .icon(Icon.Factory.fromAssets(view.getContext(), SAMPLE_GIF_ASSET_PATH));
            tomtomMap.addMarker(markerBuilder);
            //end::doc_create_animated_marker[]
        }
    }

    public void createDraggableMarkers() {
        resetMap();
        List<LatLng> list = Locations.randomLocation(Locations.AMSTERDAM_LOCATION, 5, 0.2f);
        for (LatLng position : list) {
            //tag::doc_create_simple_draggable_marker[]
            MarkerBuilder markerBuilder = new MarkerBuilder(position)
                    .markerBalloon(new SimpleMarkerBalloon(positionToText(position)))
                    .draggable(true);
            tomtomMap.addMarker(markerBuilder);
            //end::doc_create_simple_draggable_marker[]
        }
    }

    //tag::doc_create_draggable_marker_listener[]
    TomtomMapCallback.OnMarkerDragListener onMarkerDragListener = new TomtomMapCallback.OnMarkerDragListener() {
        @Override
        public void onStartDragging(@NonNull Marker marker) {
            Timber.d("onMarkerDragStart(): " + marker.toString());
            displayMessage(R.string.marker_dragging_start_message, marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
        }

        @Override
        public void onStopDragging(@NonNull Marker marker) {
            Timber.d("onMarkerDragEnd(): " + marker.toString());
            displayMessage(R.string.marker_dragging_end_message, marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
        }

        @Override
        public void onDragging(@NonNull Marker marker) {
            Timber.d("onMarkerDragging(): " + marker.toString());
        }
    };
    //end::doc_create_draggable_marker_listener[]


    @NonNull
    private String positionToText(LatLng position) {
        return LatLngFormatter.toSimpleString(position);
    }

    private void displayMessage(@StringRes int titleId, double lat, double lon) {
        String title = view.getContext().getString(titleId);
        Timber.d("Functional Example on %s", title);
        String message = String.format(java.util.Locale.getDefault(),
                TOAST_MESSAGE,
                title,
                lat,
                lon);

        view.showInfoText(message, TOAST_DURATION_MS);
    }

    private boolean isMapReady() {
        return tomtomMap != null;
    }

    private void resetMap(){
        tomtomMap.removeMarkers();
        centerMapOnLocation();
    }

    private void centerMapOnLocation() {
        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH
        );

    }

}
