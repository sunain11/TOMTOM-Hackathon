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
package com.tomtom.online.sdk.samples.cases.route.waypoints;

import android.support.annotation.VisibleForTesting;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.RouteStyle;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.routing.data.RouteResponse;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.RoutePlannerPresenter;
import com.tomtom.online.sdk.samples.cases.RoutingUiListener;
import com.tomtom.online.sdk.samples.routes.AmsterdamToBerlinRouteConfig;
import com.tomtom.online.sdk.samples.routes.RouteConfigExample;
import com.tomtom.online.sdk.samples.utils.Locations;

import java.util.ArrayList;
import java.util.List;

public class RouteWaypointsPresenter extends RoutePlannerPresenter {

    private static final int DEFAULT_ZOOM_LEVEL = 4;

    private static final LatLng BRUSSELS = new LatLng(50.854751, 4.305694);
    private static final LatLng HAMBURG = new LatLng(53.560096, 9.788492);
    private static final LatLng ZURICH = new LatLng(47.385150, 8.476178);
    private List<LatLng> wayPoints;


    public RouteWaypointsPresenter(RoutingUiListener viewModel) {
        super(viewModel);
        wayPoints = new ArrayList<>();
    }

    @Override
    public void cleanup() {
        clearMap();
        super.cleanup();
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new RouteWaypointsFunctionalExample();
    }

    @Override
    public RouteConfigExample getRouteConfig() {
        return new AmsterdamToBerlinRouteConfig();
    }

    public void bestOrder() {
        clearMap();
        addDefaultWaypoints();
        displayRoute(getRouteQuery(true));
    }

    private void addDefaultWaypoints() {
        addWaypoint(HAMBURG);
        addWaypoint(ZURICH);
        addWaypoint(BRUSSELS);
    }

    public void initialOrder() {
        clearMap();
        addDefaultWaypoints();
        displayRoute(getRouteQuery(false));
    }

    public void noWaypoints() {
        clearMap();
        displayRoute(getRouteQuery(false));
    }

    private void clearMap() {
        wayPoints.clear();
        tomtomMap.clear();
    }

    @Override
    public void centerOnDefaultLocation() {
        LatLng location = Locations.AMSTERDAM_BERLIN_CENTER_LOCATION;
        tomtomMap.centerOn(CameraPosition.builder(location)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(DEFAULT_ZOOM_LEVEL)
                .build());
    }

    protected void displayRoute(RouteQuery routeQuery) {
        viewModel.showRoutingInProgressDialog();
        tomtomMap.clearRoute();
        showRoute(routeQuery);
    }

    @Override
    protected void displayRoutes(RouteResponse routeResponse) {
        for (LatLng waypoint : wayPoints) {
            tomtomMap.addMarker(new MarkerBuilder(waypoint));
        }
        super.displayRoutes(routeResponse);
    }

    @VisibleForTesting
    protected RouteQuery getRouteQuery(boolean computeBestOrder) {
        //tag::doc_route_waypoints[]
        LatLng[] wayPointsArray = wayPoints.toArray(new LatLng[wayPoints.size()]);
        RouteQuery routeQuery = RouteQueryBuilder.create(getRouteConfig().getOrigin(), getRouteConfig().getDestination())
                .withWayPoints(wayPointsArray)
                .withComputeBestOrder(computeBestOrder)
                .withConsiderTraffic(false).build();
        //end::doc_route_waypoints[]
        return routeQuery;
    }

    private void addWaypoint(LatLng latLng) {
        wayPoints.add(latLng);
    }

    void setWayPoints(List<LatLng> points) {
        wayPoints = points;
    }

    List<LatLng> getWayPoints() {
        return wayPoints;
    }

}
