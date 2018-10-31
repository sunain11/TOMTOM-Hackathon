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
package com.tomtom.online.sdk.samples.cases.map.markers.clustering;

import android.support.annotation.NonNull;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.BaseMarkerBalloon;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.map.markers.balloons.TypedBalloonViewAdapter;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;
import com.tomtom.online.sdk.samples.utils.formatter.LatLngFormatter;

import java.util.List;

public class MarkersClusteringPresenter extends BaseFunctionalExamplePresenter {

    private static final double ZOOM_LEVEL_FOR_EXAMPLE = 7.5;
    private static final int DEFAULT_MAP_PADDING = 0;

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        if (!view.isMapRestored()) {
            createMarkers();
            centerOnMarkers();
        }
        confMapPadding();
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new MarkersClusteringFunctionalExample();
    }

    @Override
    public void cleanup() {
        if (!isMapReady()) {
            return;
        }
        tomtomMap.removeMarkers();
        tomtomMap.getMarkerSettings().setMarkersClustering(false);
        tomtomMap.setPadding(DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING,
                DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING);
    }

    private boolean isMapReady() {
        return tomtomMap != null;
    }

    @Override
    protected void confMapPadding() {
        int offsetBig = view.getContext().getResources().getDimensionPixelSize(R.dimen.offset_super_big);

        int actionBarHeight = view.getContext().getResources().getDimensionPixelSize(
                android.support.v7.appcompat.R.dimen.abc_action_bar_default_height_material);

        int padding = actionBarHeight + offsetBig;
        tomtomMap.setPadding(padding, padding, padding, padding);
    }

    private void createMarkers() {

        //tag::doc_enable_markers_clustering[]
        tomtomMap.getMarkerSettings().setMarkersClustering(true);
        //end::doc_enable_markers_clustering[]

        List<LatLng> amsterdamLocations = Locations.randomLocation(Locations.AMSTERDAM_LOCATION, 90, 0.5f);
        List<LatLng> rotterdamLocations = Locations.randomLocation(Locations.ROTTERDAM_LOCATION, 150, 0.1f);

        addMarkers(amsterdamLocations);
        addMarkers(rotterdamLocations);
    }

    private void addMarkers(List<LatLng> positions) {
        for (LatLng position : positions) {
            //tag::doc_add_marker_to_cluster[]
            MarkerBuilder markerBuilder = new MarkerBuilder(position)
                    .shouldCluster(true)
                    .markerBalloon(new SimpleMarkerBalloon(positionToText(position)));
            //end::doc_add_marker_to_cluster[]
            tomtomMap.addMarker(markerBuilder);
        }
    }

    @NonNull
    private String positionToText(LatLng position) {
        return LatLngFormatter.toSimpleString(position);
    }

    public void centerOnMarkers() {
        tomtomMap.zoomToAllMarkers();
    }

}
