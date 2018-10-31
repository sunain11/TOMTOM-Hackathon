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
package com.tomtom.online.sdk.samples.cases.route.reachablerange;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.widget.Toast;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.data.reachablerange.ReachableRangeResponse;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.PolygonBuilder;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.routing.OnlineRoutingApi;
import com.tomtom.online.sdk.routing.ReachableRangeResultListener;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

import java.util.Arrays;

public class ReachableRangePresenter extends BaseFunctionalExamplePresenter {

    protected static final int DEFAULT_MAP_PADDING = 0;
    private static final int OVERLAYS_COLOR = Color.rgb(255, 0, 0);
    private static final float OVERLAYS_OPACITY = 0.5f;

    private ReachableRangeQueryFactory reachableRangeQueryFactory;
    private RoutingApi routingApi;

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        routingApi = provideOnlineRoutingApi(view);
        reachableRangeQueryFactory = provideReachableRangeQueryFactory();
        if (!view.isMapRestored()) {
            centerOnDefaultLocation();
        }
        confMapPadding();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        getTomtomMap().getOverlaySettings().removeOverlays();
        getTomtomMap().getMarkerSettings().removeMarkers();
        getTomtomMap().setPadding(DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING,
                DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING);
    }

    @NonNull
    @VisibleForTesting
    protected ReachableRangeQueryFactory provideReachableRangeQueryFactory() {
        return new ReachableRangeQueryFactory();
    }

    @NonNull
    @VisibleForTesting
    protected RoutingApi provideOnlineRoutingApi(FunctionalExampleFragment view) {
        return OnlineRoutingApi.create(view.getContext());
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new ReachableRangeFunctionalExample();
    }

    public void centerOnDefaultLocation() {
        tomtomMap.centerOn(CameraPosition.builder(Locations.AMSTERDAM_CENTER_LOCATION)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(MapConstants.DEFAULT_ZOOM_LEVEL)
                .build());
    }

    public void startReachableRangeCalculationForElectric() {
        routingApi.findReachableRange(reachableRangeQueryFactory.createReachableRangeQueryForElectric(),
                reachableRangeResultListener);
    }

    public void startReachableRangeCalculationForFuel() {
        //tag::doc_route_api_call[]
        routingApi.findReachableRange(
                reachableRangeQueryFactory.createReachableRangeQueryForCombustion(),
                reachableRangeResultListener);
        //end::doc_route_api_call[]
    }

    public void startReachableRangeCalculationFor2HLimit() {
        routingApi.findReachableRange(reachableRangeQueryFactory.createReachableRangeQueryForElectricLimitTo2Hours(),
                reachableRangeResultListener);
    }

    @VisibleForTesting
    protected void drawPolygonFromReachableRangeResponse(ReachableRangeResponse response) {
        getTomtomMap().getOverlaySettings().addOverlay(
                PolygonBuilder.create()
                        .coordinates(Arrays.asList(response.getResult().getBoundary()))
                        .color(OVERLAYS_COLOR)
                        .opacity(OVERLAYS_OPACITY)
                        .build()
        );
    }

    @VisibleForTesting
    protected void createMarker(LatLng position) {
        getTomtomMap().addMarker(new MarkerBuilder(position));
    }

    @VisibleForTesting
    //tag::doc_reachable_range_result_listener[]
    protected ReachableRangeResultListener reachableRangeResultListener = new ReachableRangeResultListener() {
        @Override
        public void onReachableRangeResponse(ReachableRangeResponse response) {
            doActionOnReachableRangeResponse(response);
        }

        @Override
        public void onReachableRangeError(Throwable error) {
            doActionOnReachableRangeError();
        }
    };
    //end::doc_reachable_range_result_listener[]

    private void doActionOnReachableRangeError() {
        getView().showInfoText("Error occurred!", Toast.LENGTH_LONG);
        getView().enableOptionsView();
    }

    private void doActionOnReachableRangeResponse(ReachableRangeResponse response) {
        drawPolygonFromReachableRangeResponse(response);
        createMarkersOnTheReachableRangeEdges(response);
    }

    protected void confMapPadding() {
        int offsetBig = getView().getContext().getResources().getDimensionPixelSize(R.dimen.offset_extra_big);

        int actionBarHeight = getView().getContext().getResources().getDimensionPixelSize(
                android.support.v7.appcompat.R.dimen.abc_action_bar_default_height_material);

        int padding = actionBarHeight + offsetBig;

        getTomtomMap().setPadding(padding, offsetBig, padding, offsetBig);
    }

    protected void confMapCenter() {
        getTomtomMap().addMarker(new MarkerBuilder(Locations.AMSTERDAM_CENTER_LOCATION)
                .icon(Icon.Factory.fromResources(getView().getContext(), R.drawable.ic_favourites)));
    }

    @VisibleForTesting
    TomtomMap getTomtomMap() {
        return tomtomMap;
    }

    @VisibleForTesting
    FunctionalExampleFragment getView() {
        return view;
    }

    private void createMarkersOnTheReachableRangeEdges(ReachableRangeResponse response) {
        if (!response.hasError()) {
            for (LatLng latLng : response.getResult().getBoundary()) {
                createMarker(latLng);
            }
            getTomtomMap().zoomToAllMarkers();
        }
    }
}
