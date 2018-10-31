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
package com.tomtom.online.sdk.samples.cases.route.supportingpoints;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.Route;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.RoutePlannerPresenter;
import com.tomtom.online.sdk.samples.cases.RoutingUiListener;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.routes.RouteConfigExample;

import java.util.List;

public class RouteSupportingPointsPresenter extends RoutePlannerPresenter {

    private final LatLng EXAMPLE_DESTINATION = new LatLng(40.209408, -8.423741);
    private final LatLng EXAMPLE_ORIGIN = new LatLng(40.10995732392718, -8.501433134078981);

    private final List<LatLng> SUPPORTING_POINTS = ImmutableList.of(
            new LatLng(40.10995732392718, -8.501433134078981),
            new LatLng(40.11115121590874, -8.500000834465029),
            new LatLng(40.11089684892725, -8.497683405876161),
            new LatLng(40.11192251642396, -8.498423695564272),
            new LatLng(40.209408, -8.423741)
    );

    public RouteSupportingPointsPresenter(RoutingUiListener viewModel) {
        super(viewModel);
    }

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        tomtomMap.addOnRouteClickListener(onRouteClickListener);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        tomtomMap.removeOnRouteClickListener(onRouteClickListener);
    }

    @Override
    public void centerOnDefaultLocation() {
        tomtomMap.centerOn(CameraPosition.builder(new LatLng(EXAMPLE_ORIGIN.getLatitude(), EXAMPLE_ORIGIN.getLongitude()))
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(DEFAULT_ZOOM_FOR_EXAMPLE)
                .build());
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new RouteSupportingPointsFunctionalExample();
    }

    public void startRouting(int minDeviationDistance) {
        tomtomMap.clearRoute();
        viewModel.showRoutingInProgressDialog();
        showRoute(getRouteQuery(minDeviationDistance));
    }

    @VisibleForTesting
    protected RouteQuery getRouteQuery(int minDeviationDistance) {

        //tag::doc_route_supporting_points[]
        RouteQuery queryBuilder = RouteQueryBuilder.create(EXAMPLE_ORIGIN, EXAMPLE_DESTINATION)
                .withMaxAlternatives(1)
                .withMinDeviationTime(0)
                .withSupportingPoints(SUPPORTING_POINTS)
                .withMinDeviationDistance(minDeviationDistance)
                .withConsiderTraffic(false).build();
        //end::doc_route_supporting_points[]
        return queryBuilder;
    }

    @Override
    public RouteConfigExample getRouteConfig() {
        return null;
    }

    private TomtomMapCallback.OnRouteClickListener onRouteClickListener = new TomtomMapCallback.OnRouteClickListener() {
        @Override
        public void onRouteClick(@NonNull Route route) {
            long routeId = route.getId();
            tomtomMap.getRouteSettings().setRoutesInactive();
            tomtomMap.getRouteSettings().setRouteActive(routeId);
            FullRoute fullRoute = routesMap.get(routeId);
            displayInfoAboutRoute(fullRoute);
        }
    };

}
