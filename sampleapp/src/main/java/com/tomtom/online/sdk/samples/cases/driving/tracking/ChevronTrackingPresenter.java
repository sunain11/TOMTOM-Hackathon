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
package com.tomtom.online.sdk.samples.cases.driving.tracking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.common.service.ServiceException;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.Chevron;
import com.tomtom.online.sdk.map.ChevronBuilder;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.RouteBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.routing.OnlineRoutingApi;
import com.tomtom.online.sdk.routing.RouteCallback;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.routing.data.RouteResponse;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.driving.RouteSimulator;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;
import com.tomtom.online.sdk.samples.utils.formatter.LatLngFormatter;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.data.alongroute.AlongRouteSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ChevronTrackingPresenter extends BaseFunctionalExamplePresenter {

    private static final LatLng ROUTE_ORIGIN = new LatLng(52.565023, -0.384929);
    private static final LatLng ROUTE_DESTINATION = new LatLng(53.593992, -1.216541);
    private static final ImmutableList<LatLng> ROUTE_WAYPOINTS = ImmutableList.of(
            new LatLng(53.09414211 , -0.7890264570000001),
            new LatLng(52.89772978 , -0.6644283879999999)
    );
    private final static LatLng DEFAULT_MAP_POSITION = new LatLng(55.097623, -1.660812);

    private final static int NO_ANIMATION_TIME = 0;
    private final static double CHEVRON_ICON_SCALE = 2.5;
    private final static double DEFAULT_MAP_ZOOM_LEVEL_FOR_EXAMPLE = 12.5;
    private final static double DEFAULT_MAP_ZOOM_LEVEL_FOR_DRIVING = 17.5;
    private final static double DEFAULT_MAP_PITCH_LEVEL_FOR_DRIVING = 50.0;
    private static final class ChevronUpdater implements RouteSimulator.RouteSimulatorEventListener {
        private Handler uiHandler;
        private Chevron chevron;

        public ChevronUpdater(Chevron chevron) {
            Timber.d(" ChevronUpdater " + this + " chevron " + chevron);
            uiHandler = new Handler();
            this.chevron = chevron;
        }

        @Override
        public void onNewRoutePointVisited(final LatLng point, final double bearing) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    chevron.show();
                    chevron.setDimmed(false);
                    chevron.setLocation(point, bearing, 0.0f);
                }
            });
        }
    }


    private RouteSimulator routeSimulator;
    private Chevron chevron;

    @Override
    public void bind(final FunctionalExampleFragment view, final TomtomMap map) {
        super.bind(view, map);

        if (view.isMapRestored() || !tomtomMap.getDrivingSettings().getChevrons().isEmpty()) {
            restoreChevron();
            this.routeSimulator = new RouteSimulator(new ChevronUpdater(chevron));
            restoreSimulator();
            restoreRouteOverview();
        } else {
            centerOnDefaultLocation();
            planBatchRoutes();
            createChevron();
            this.routeSimulator = new RouteSimulator(new ChevronUpdater(chevron));
        }

        //We do not want blue location dot on this example
        tomtomMap.setMyLocationEnabled(false);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        //We want the blue dot back when we exit this example
        tomtomMap.setMyLocationEnabled(true);
        stopSimulator();
        stopTracking();
        clearMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopSimulator();
        Timber.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop");
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new ChevronTrackingFunctionalExample();
    }

    private void centerOnDefaultLocation() {
        tomtomMap.centerOn(
                DEFAULT_MAP_POSITION.getLatitude(),
                DEFAULT_MAP_POSITION.getLongitude(),
                DEFAULT_MAP_ZOOM_LEVEL_FOR_EXAMPLE,
                MapConstants.ORIENTATION_NORTH
        );
    }

    private void planBatchRoutes() {
        RoutingApi routingApi = OnlineRoutingApi.create(getContext());
        routingApi.planRoute(getRouteQuery(), routeCallback);
    }

    private RouteQuery getRouteQuery() {
        return RouteQueryBuilder.create(ROUTE_ORIGIN, ROUTE_DESTINATION)
                .withWayPointsList(ROUTE_WAYPOINTS.asList())
                .build();
    }

    private FullRoute getFirstRoute(RouteResponse routeResponse) {
        return routeResponse.getRoutes().get(0);
    }

    private LatLng getRouteOrigin(FullRoute route) {
        return route.getCoordinates().get(0);
    }

    private void showRoute(FullRoute route) {
        tomtomMap.addRoute(new RouteBuilder(route.getCoordinates()));
        tomtomMap.getRouteSettings().displayRoutesOverview();
        createmarkers();
    }
    public void createmarkers()
    {
        tomtomMap.getMarkerSettings().setMarkersClustering(true);
       // List<LatLng> aLocations = Locations.randomLocation(new LatLng(55.930290, -2.355107), 90, 0.5f);
        List<LatLng> aLocations=new ArrayList<>();
        aLocations.add(new LatLng( 53.01153714 , -0.763764978 ));
        aLocations.add(new LatLng( 52.99906832 , -0.766356273 ));
        aLocations.add(new LatLng( 52.74239044 , -0.602817583 ));
        aLocations.add(new LatLng( 52.67870194 , -0.540061449 ));
        aLocations.add(new LatLng( 52.73210173 , -0.5994440089999999 ));
        aLocations.add(new LatLng( 52.735622 , -0.600634577 ));
        aLocations.add(new LatLng( 52.65727106 , -0.515641239 ));
        aLocations.add(new LatLng( 52.90500925 , -0.672233735 ));
        aLocations.add(new LatLng( 53.31298389 , -1.029305549 ));
        aLocations.add(new LatLng( 53.14463238 , -0.8037543009999999 ));
        aLocations.add(new LatLng( 52.64953963 , -0.508511918 ));
        aLocations.add(new LatLng( 52.89585536 , -0.657647768 ));
        aLocations.add(new LatLng( 53.26507723 , -0.945230149 ));
        aLocations.add(new LatLng( 52.93211217 , -0.684199698 ));
        aLocations.add(new LatLng( 53.10528573 , -0.806489329 ));
        aLocations.add(new LatLng( 53.3530519 , -1.037560201 ));
        aLocations.add(new LatLng( 52.859568700000004 , -0.62906098 ));
        aLocations.add(new LatLng( 53.04098743 , -0.7776890540000001 ));
        aLocations.add(new LatLng( 53.23995643 , -0.900894735 ));
        aLocations.add(new LatLng( 53.36784056 , -1.054660658 ));
        aLocations.add(new LatLng( 52.76945701 , -0.61097832 ));
        aLocations.add(new LatLng( 52.63946748 , -0.494075616 ));
        aLocations.add(new LatLng( 53.40525957 , -1.080907461 ));
        aLocations.add(new LatLng( 53.02897771 , -0.7698870109999999 ));
        aLocations.add(new LatLng( 52.64341603 , -0.500591109 ));
        aLocations.add(new LatLng( 52.89772978 , -0.6644283879999999 ));
        aLocations.add(new LatLng( 52.89611047 , -0.66432951 ));
        aLocations.add(new LatLng( 52.89906778 , -0.663495242 ));
        aLocations.add(new LatLng( 53.09411931 , -0.786787026 ));
        aLocations.add(new LatLng( 53.09382533 , -0.7844059 ));
        aLocations.add(new LatLng( 53.09414211 , -0.7890264570000001 ));
        aLocations.add(new LatLng( 53.22909981 , -0.893382833 ));
        aLocations.add(new LatLng( 53.369380299999996 , -1.056129532 ));
        aLocations.add(new LatLng( 53.078883499999996 , -0.773779655 ));
        aLocations.add(new LatLng( 52.98299015 , -0.750429248 ));
        aLocations.add(new LatLng( 53.06405228 , -0.765246784 ));
        aLocations.add(new LatLng( 53.08584799 , -0.777911722 ));
        aLocations.add(new LatLng( 53.07928818 , -0.769438834 ));
        aLocations.add(new LatLng( 52.96535757 , -0.6989734240000001 ));
        aLocations.add(new LatLng( 52.98631595 , -0.7589733359999999 ));
        aLocations.add(new LatLng( 53.01153714 , -0.763764978 ));
        aLocations.add(new LatLng( 52.99906832 , -0.766356273 ));
        aLocations.add(new LatLng( 52.74239044 , -0.602817583 ));
        aLocations.add(new LatLng( 52.67870194 , -0.540061449 ));
        aLocations.add(new LatLng( 52.73210173 , -0.5994440089999999 ));
        aLocations.add(new LatLng( 52.735622 , -0.600634577 ));
        aLocations.add(new LatLng( 52.65727106 , -0.515641239 ));
        aLocations.add(new LatLng( 53.31298389 , -1.029305549 ));
        aLocations.add(new LatLng( 53.14463238 , -0.8037543009999999 ));
        aLocations.add(new LatLng( 52.64953963 , -0.508511918 ));
        aLocations.add(new LatLng( 52.89585536 , -0.657647768 ));
        aLocations.add(new LatLng( 53.26507723 , -0.945230149 ));
        aLocations.add(new LatLng( 53.10528573 , -0.806489329 ));
        aLocations.add(new LatLng( 53.3530519 , -1.037560201 ));
        aLocations.add(new LatLng( 52.859568700000004 , -0.62906098 ));
        aLocations.add(new LatLng( 53.23995643 , -0.900894735 ));
        aLocations.add(new LatLng( 53.36784056 , -1.054660658 ));
        aLocations.add(new LatLng( 52.76945701 , -0.61097832 ));
        aLocations.add(new LatLng( 52.63946748 , -0.494075616 ));
        aLocations.add(new LatLng( 53.40525957 , -1.080907461 ));
        aLocations.add(new LatLng( 52.64341603 , -0.500591109 ));
        aLocations.add(new LatLng( 52.89772978 , -0.6644283879999999 ));
        aLocations.add(new LatLng( 52.89611047 , -0.66432951 ));
        aLocations.add(new LatLng( 52.89906778 , -0.663495242 ));
        aLocations.add(new LatLng( 53.09411931 , -0.786787026 ));
        aLocations.add(new LatLng( 53.09382533 , -0.7844059 ));
        aLocations.add(new LatLng( 53.09414211 , -0.7890264570000001 ));
        aLocations.add(new LatLng( 53.22909981 , -0.893382833 ));
        aLocations.add(new LatLng( 53.369380299999996 , -1.056129532 ));
        aLocations.add(new LatLng( 53.078883499999996 , -0.773779655 ));
        aLocations.add(new LatLng( 52.98299015 , -0.750429248 ));
        aLocations.add(new LatLng( 53.08584799 , -0.777911722 ));
        aLocations.add(new LatLng( 53.07928818 , -0.769438834 ));
        aLocations.add(new LatLng( 52.96535757 , -0.6989734240000001 ));
        aLocations.add(new LatLng( 52.98631595 , -0.7589733359999999 ));
        aLocations.add(new LatLng( 53.01153714 , -0.763764978 ));
        aLocations.add(new LatLng( 52.99906832 , -0.766356273 ));
        aLocations.add(new LatLng( 52.74239044 , -0.602817583 ));
        aLocations.add(new LatLng( 52.67870194 , -0.540061449 ));
        aLocations.add(new LatLng( 53.06405228 , -0.765246784 ));
        aLocations.add(new LatLng( 52.91680296 , -0.6777776 ));
        aLocations.add(new LatLng( 52.73210173 , -0.5994440089999999 ));
        aLocations.add(new LatLng( 52.91760954 , -0.684029509 ));
        aLocations.add(new LatLng( 52.93211217 , -0.684199698 ));
        aLocations.add(new LatLng( 52.90500925 , -0.672233735 ));
        aLocations.add(new LatLng( 52.65727106 , -0.515641239 ));
        aLocations.add(new LatLng( 53.31298389 , -1.029305549 ));
        aLocations.add(new LatLng( 53.14463238 , -0.8037543009999999 ));
        aLocations.add(new LatLng( 52.64953963 , -0.508511918 ));
        aLocations.add(new LatLng( 52.89585536 , -0.657647768 ));
        aLocations.add(new LatLng( 53.26507723 , -0.945230149 ));
        aLocations.add(new LatLng( 53.10528573 , -0.806489329 ));
        aLocations.add(new LatLng( 53.3530519 , -1.037560201 ));
        aLocations.add(new LatLng( 52.859568700000004 , -0.62906098 ));
        aLocations.add(new LatLng( 53.23995643 , -0.900894735 ));
        aLocations.add(new LatLng( 53.36784056 , -1.054660658 ));
        aLocations.add(new LatLng( 52.76945701 , -0.61097832 ));
        aLocations.add(new LatLng( 52.63946748 , -0.494075616 ));
        aLocations.add(new LatLng( 53.40525957 , -1.080907461 ));
        aLocations.add(new LatLng( 52.64341603 , -0.500591109 ));
        aLocations.add(new LatLng( 52.89772978 , -0.6644283879999999 ));
        aLocations.add(new LatLng( 52.89611047 , -0.66432951 ));
        aLocations.add(new LatLng( 52.89906778 , -0.663495242 ));
        aLocations.add(new LatLng( 53.09411931 , -0.786787026 ));
        aLocations.add(new LatLng( 53.09382533 , -0.7844059 ));
        aLocations.add(new LatLng( 53.09414211 , -0.7890264570000001 ));
        aLocations.add(new LatLng( 53.22909981 , -0.893382833 ));
        aLocations.add(new LatLng( 53.369380299999996 , -1.056129532 ));
        aLocations.add(new LatLng( 53.078883499999996 , -0.773779655 ));
        aLocations.add(new LatLng( 52.98299015 , -0.750429248 ));
        aLocations.add(new LatLng( 52.73527335 , -0.6015640320000001 ));
        aLocations.add(new LatLng( 52.73516427 , -0.599938302 ));
        aLocations.add(new LatLng( 52.72850173 , -0.599115097 ));
        aLocations.add(new LatLng( 52.73092469 , -0.598741244 ));
        aLocations.add(new LatLng( 53.08584799 , -0.777911722 ));
        aLocations.add(new LatLng( 53.07928818 , -0.769438834 ));
        aLocations.add(new LatLng( 52.96535757 , -0.6989734240000001 ));
        aLocations.add(new LatLng( 52.98631595 , -0.7589733359999999 ));
        aLocations.add(new LatLng( 53.01153714 , -0.763764978 ));
        aLocations.add(new LatLng( 52.99906832 , -0.766356273 ));
        aLocations.add(new LatLng( 52.74239044 , -0.602817583 ));
        aLocations.add(new LatLng( 52.67870194 , -0.540061449 ));
        aLocations.add(new LatLng( 53.06405228 , -0.765246784 ));
        aLocations.add(new LatLng( 52.91680296 , -0.6777776 ));
        aLocations.add(new LatLng( 52.73210173 , -0.5994440089999999 ));
        aLocations.add(new LatLng( 52.91760954 , -0.684029509 ));
        aLocations.add(new LatLng( 52.93211217 , -0.684199698 ));
        aLocations.add(new LatLng( 52.90500925 , -0.672233735 ));
        aLocations.add(new LatLng( 52.65727106 , -0.515641239 ));
        aLocations.add(new LatLng( 53.31298389 , -1.029305549 ));
        aLocations.add(new LatLng( 53.14463238 , -0.8037543009999999 ));
        aLocations.add(new LatLng( 52.64953963 , -0.508511918 ));
        aLocations.add(new LatLng( 52.89585536 , -0.657647768 ));
        aLocations.add(new LatLng( 53.26507723 , -0.945230149 ));
        aLocations.add(new LatLng( 53.10528573 , -0.806489329 ));
        aLocations.add(new LatLng( 53.3530519 , -1.037560201 ));
        aLocations.add(new LatLng( 52.859568700000004 , -0.62906098 ));
        aLocations.add(new LatLng( 53.23995643 , -0.900894735 ));
        aLocations.add(new LatLng( 53.36784056 , -1.054660658 ));
        aLocations.add(new LatLng( 52.76945701 , -0.61097832 ));
        aLocations.add(new LatLng( 52.63946748 , -0.494075616 ));
        aLocations.add(new LatLng( 53.40525957 , -1.080907461 ));
        aLocations.add(new LatLng( 52.64341603 , -0.500591109 ));
        aLocations.add(new LatLng( 52.89772978 , -0.6644283879999999 ));
        aLocations.add(new LatLng( 52.89611047 , -0.66432951 ));
        aLocations.add(new LatLng( 52.89906778 , -0.663495242 ));
        aLocations.add(new LatLng( 53.09411931 , -0.786787026 ));
        aLocations.add(new LatLng( 53.09382533 , -0.7844059 ));
        aLocations.add(new LatLng( 53.09414211 , -0.7890264570000001 ));
        aLocations.add(new LatLng( 53.22909981 , -0.893382833 ));
        aLocations.add(new LatLng( 53.369380299999996 , -1.056129532 ));
        aLocations.add(new LatLng( 53.078883499999996 , -0.773779655 ));
        aLocations.add(new LatLng( 52.98299015 , -0.750429248 ));
        aLocations.add(new LatLng( 52.73527335 , -0.6015640320000001 ));
        aLocations.add(new LatLng( 52.73516427 , -0.599938302 ));
        aLocations.add(new LatLng( 52.72850173 , -0.599115097 ));
        aLocations.add(new LatLng( 52.73092469 , -0.598741244 ));
        aLocations.add(new LatLng( 53.08584799 , -0.777911722 ));
        aLocations.add(new LatLng( 53.07928818 , -0.769438834 ));
        aLocations.add(new LatLng( 52.96535757 , -0.6989734240000001 ));
        aLocations.add(new LatLng( 52.98631595 , -0.7589733359999999 ));
        aLocations.add(new LatLng( 53.01153714 , -0.763764978 ));
        aLocations.add(new LatLng( 52.99906832 , -0.766356273 ));
        aLocations.add(new LatLng( 52.74239044 , -0.602817583 ));
        aLocations.add(new LatLng( 52.67870194 , -0.540061449 ));
        aLocations.add(new LatLng( 53.06405228 , -0.765246784 ));
        aLocations.add(new LatLng( 52.91680296 , -0.6777776 ));
        aLocations.add(new LatLng( 52.73210173 , -0.5994440089999999 ));
        aLocations.add(new LatLng( 52.91760954 , -0.684029509 ));
        aLocations.add(new LatLng( 52.93211217 , -0.684199698 ));
        aLocations.add(new LatLng( 52.90500925 , -0.672233735 ));
        aLocations.add(new LatLng( 52.65727106 , -0.515641239 ));
        aLocations.add(new LatLng( 53.31298389 , -1.029305549 ));
        aLocations.add(new LatLng( 53.14463238 , -0.8037543009999999 ));
        aLocations.add(new LatLng( 52.64953963 , -0.508511918 ));
        aLocations.add(new LatLng( 52.89585536 , -0.657647768 ));
        aLocations.add(new LatLng( 53.26507723 , -0.945230149 ));
        aLocations.add(new LatLng( 53.10528573 , -0.806489329 ));
        aLocations.add(new LatLng( 53.3530519 , -1.037560201 ));
        aLocations.add(new LatLng( 52.859568700000004 , -0.62906098 ));
        aLocations.add(new LatLng( 53.23995643 , -0.900894735 ));
        aLocations.add(new LatLng( 53.36784056 , -1.054660658 ));
        aLocations.add(new LatLng( 52.76945701 , -0.61097832 ));
        aLocations.add(new LatLng( 52.63946748 , -0.494075616 ));
        aLocations.add(new LatLng( 53.40525957 , -1.080907461 ));
        aLocations.add(new LatLng( 52.64341603 , -0.500591109 ));
        aLocations.add(new LatLng( 52.89772978 , -0.6644283879999999 ));
        aLocations.add(new LatLng( 52.89611047 , -0.66432951 ));
        aLocations.add(new LatLng( 52.89906778 , -0.663495242 ));
        aLocations.add(new LatLng( 53.09411931 , -0.786787026 ));
        aLocations.add(new LatLng( 53.09382533 , -0.7844059 ));
        aLocations.add(new LatLng( 53.09414211 , -0.7890264570000001 ));
        aLocations.add(new LatLng( 53.22909981 , -0.893382833 ));
        aLocations.add(new LatLng( 53.369380299999996 , -1.056129532 ));
        aLocations.add(new LatLng( 53.078883499999996 , -0.773779655 ));
        aLocations.add(new LatLng( 52.98299015 , -0.750429248 ));
        aLocations.add(new LatLng( 52.73527335 , -0.6015640320000001 ));
        aLocations.add(new LatLng( 52.73516427 , -0.599938302 ));
        aLocations.add(new LatLng( 52.72850173 , -0.599115097 ));
        aLocations.add(new LatLng( 52.73092469 , -0.598741244 ));
        aLocations.add(new LatLng( 53.08584799 , -0.777911722 ));
        aLocations.add(new LatLng( 53.07928818 , -0.769438834 ));
        aLocations.add(new LatLng( 52.96535757 , -0.6989734240000001 ));
        aLocations.add(new LatLng( 52.98631595 , -0.7589733359999999 ));
        aLocations.add(new LatLng( 53.01153714 , -0.763764978 ));
        aLocations.add(new LatLng( 52.99906832 , -0.766356273 ));
        aLocations.add(new LatLng( 52.74239044 , -0.602817583 ));
        aLocations.add(new LatLng( 52.67870194 , -0.540061449 ));
        aLocations.add(new LatLng( 53.06405228 , -0.765246784 ));
        aLocations.add(new LatLng( 52.91680296 , -0.6777776 ));
        aLocations.add(new LatLng( 52.73210173 , -0.5994440089999999 ));
        aLocations.add(new LatLng( 52.91760954 , -0.684029509 ));
        aLocations.add(new LatLng( 52.93211217 , -0.684199698 ));
        aLocations.add(new LatLng( 52.90500925 , -0.672233735 ));
        aLocations.add(new LatLng( 52.65727106 , -0.515641239 ));
        aLocations.add(new LatLng( 53.31298389 , -1.029305549 ));
        aLocations.add(new LatLng( 53.14463238 , -0.8037543009999999 ));
        aLocations.add(new LatLng( 52.64953963 , -0.508511918 ));
        aLocations.add(new LatLng( 52.89585536 , -0.657647768 ));
        aLocations.add(new LatLng( 53.26507723 , -0.945230149 ));
        aLocations.add(new LatLng( 53.10528573 , -0.806489329 ));
        aLocations.add(new LatLng( 53.3530519 , -1.037560201 ));
        aLocations.add(new LatLng( 52.859568700000004 , -0.62906098 ));
        aLocations.add(new LatLng( 53.23995643 , -0.900894735 ));
        aLocations.add(new LatLng( 53.36784056 , -1.054660658 ));
        aLocations.add(new LatLng( 52.76945701 , -0.61097832 ));
        aLocations.add(new LatLng( 52.63946748 , -0.494075616 ));
        aLocations.add(new LatLng( 52.64341603 , -0.500591109 ));
        aLocations.add(new LatLng( 52.89772978 , -0.6644283879999999 ));
        aLocations.add(new LatLng( 52.89611047 , -0.66432951 ));
        aLocations.add(new LatLng( 52.89906778 , -0.663495242 ));
        aLocations.add(new LatLng( 53.09411931 , -0.786787026 ));
        aLocations.add(new LatLng( 53.09382533 , -0.7844059 ));
        aLocations.add(new LatLng( 53.09414211 , -0.7890264570000001 ));
        aLocations.add(new LatLng( 53.22909981 , -0.893382833 ));
        aLocations.add(new LatLng( 53.369380299999996 , -1.056129532 ));
        aLocations.add(new LatLng( 53.078883499999996 , -0.773779655 ));
        aLocations.add(new LatLng( 52.98299015 , -0.750429248 ));
        aLocations.add(new LatLng( 52.73527335 , -0.6015640320000001 ));
        aLocations.add(new LatLng( 52.73516427 , -0.599938302 ));
        aLocations.add(new LatLng( 52.72850173 , -0.599115097 ));
        aLocations.add(new LatLng( 52.73092469 , -0.598741244 ));
        aLocations.add(new LatLng( 53.08584799 , -0.777911722 ));
        aLocations.add(new LatLng( 53.07928818 , -0.769438834 ));
        aLocations.add(new LatLng( 52.96535757 , -0.6989734240000001 ));
        aLocations.add(new LatLng( 52.98631595 , -0.7589733359999999 ));
        aLocations.add(new LatLng( 53.01153714 , -0.763764978 ));
        aLocations.add(new LatLng( 52.99906832 , -0.766356273 ));
        aLocations.add(new LatLng( 52.74239044 , -0.602817583 ));
        aLocations.add(new LatLng( 52.67870194 , -0.540061449 ));
        aLocations.add(new LatLng( 53.06405228 , -0.765246784 ));
        aLocations.add(new LatLng( 52.91680296 , -0.6777776 ));
        aLocations.add(new LatLng( 52.73210173 , -0.5994440089999999 ));
        aLocations.add(new LatLng( 52.91760954 , -0.684029509 ));
        aLocations.add(new LatLng( 52.93211217 , -0.684199698 ));
        aLocations.add(new LatLng( 52.90500925 , -0.672233735 ));
        aLocations.add(new LatLng( 52.65727106 , -0.515641239 ));
        aLocations.add(new LatLng( 53.31298389 , -1.029305549 ));
        aLocations.add(new LatLng( 53.14463238 , -0.8037543009999999 ));
        aLocations.add(new LatLng( 52.64953963 , -0.508511918 ));
        aLocations.add(new LatLng( 52.89585536 , -0.657647768 ));
        aLocations.add(new LatLng( 53.26507723 , -0.945230149 ));
        aLocations.add(new LatLng( 53.10528573 , -0.806489329 ));
        aLocations.add(new LatLng( 53.3530519 , -1.037560201 ));
        aLocations.add(new LatLng( 52.859568700000004 , -0.62906098 ));
        aLocations.add(new LatLng( 53.23995643 , -0.900894735 ));
        aLocations.add(new LatLng( 53.36784056 , -1.054660658 ));
        aLocations.add(new LatLng( 52.76945701 , -0.61097832 ));
        aLocations.add(new LatLng( 52.63946748 , -0.494075616 ));
        aLocations.add(new LatLng( 52.64341603 , -0.500591109 ));
        aLocations.add(new LatLng( 52.89772978 , -0.6644283879999999 ));
        aLocations.add(new LatLng( 52.89611047 , -0.66432951 ));
        aLocations.add(new LatLng( 52.89906778 , -0.663495242 ));
        aLocations.add(new LatLng( 53.09411931 , -0.786787026 ));
        aLocations.add(new LatLng( 53.09382533 , -0.7844059 ));
        aLocations.add(new LatLng( 53.09414211 , -0.7890264570000001 ));
        aLocations.add(new LatLng( 53.22909981 , -0.893382833 ));
        aLocations.add(new LatLng( 53.369380299999996 , -1.056129532 ));
        aLocations.add(new LatLng( 53.078883499999996 , -0.773779655 ));
        aLocations.add(new LatLng( 52.98299015 , -0.750429248 ));
        aLocations.add(new LatLng( 52.73527335 , -0.6015640320000001 ));
        aLocations.add(new LatLng( 52.73516427 , -0.599938302 ));
        aLocations.add(new LatLng( 52.72850173 , -0.599115097 ));
        aLocations.add(new LatLng( 52.73092469 , -0.598741244 ));
        aLocations.add(new LatLng( 53.08584799 , -0.777911722 ));
        aLocations.add(new LatLng( 53.07928818 , -0.769438834 ));
        aLocations.add(new LatLng( 52.96535757 , -0.6989734240000001 ));
        aLocations.add(new LatLng( 52.98631595 , -0.7589733359999999 ));
        aLocations.add(new LatLng( 53.01153714 , -0.763764978 ));
        aLocations.add(new LatLng( 52.99906832 , -0.766356273 ));
        aLocations.add(new LatLng( 52.74239044 , -0.602817583 ));
        aLocations.add(new LatLng( 52.67870194 , -0.540061449 ));
        aLocations.add(new LatLng( 53.06405228 , -0.765246784 ));
        aLocations.add(new LatLng( 52.91680296 , -0.6777776 ));
        aLocations.add(new LatLng( 52.73210173 , -0.5994440089999999 ));
        aLocations.add(new LatLng( 52.91760954 , -0.684029509 ));
        aLocations.add(new LatLng( 52.93211217 , -0.684199698 ));
        aLocations.add(new LatLng( 52.90500925 , -0.672233735 ));
        addMarkers(aLocations);
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
    private void createChevron() {
        Icon activeIcon = Icon.Factory.fromResources(getContext(), R.drawable.chevron_color, CHEVRON_ICON_SCALE);
        Icon inactiveIcon = Icon.Factory.fromResources(getContext(), R.drawable.chevron_shadow, CHEVRON_ICON_SCALE);
        //tag::doc_create_chevron[]
        ChevronBuilder chevronBuilder = ChevronBuilder.create(activeIcon, inactiveIcon);
        chevron = tomtomMap.getDrivingSettings().addChevron(chevronBuilder);
        //end::doc_create_chevron[]
    }

    public void accident()
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
        dlgAlert.setMessage("Application has detected an accidental condition.\nSending Alert message....");
        dlgAlert.setTitle("SmartDrive");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                    }
                });
        dlgAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    private void restoreChevron() {
        //Chevron is stored inside Maps SDK
        chevron = tomtomMap.getDrivingSettings().getChevrons().get(0);
    }


    private void restoreSimulator() {
        //Route and chevron are stored inside Maps SDK
        routeSimulator.start(getFirstAvailableRouteCoordinates(), chevron.getLocation());
    }

    private List<LatLng> getFirstAvailableRouteCoordinates() {
        return tomtomMap.getRoutes().get(0).getCoordinates();
    }

    private void restoreRouteOverview() {
        if (tomtomMap.getDrivingSettings().isTracking()) {
            tomtomMap.getRouteSettings().displayRoutesOverview();
        }
    }

    private void startSimulator(List<LatLng> routePoints) {
        routeSimulator.start(routePoints);
    }

    private void stopSimulator() {
        if (routeSimulator != null) {
            routeSimulator.stop();
        } else {
            Timber.d("Cannot stop routeSimulator");
        }
    }

    public void startTracking() {
        tomtomMap.centerOn(
                CameraPosition.builder(tomtomMap.getCenterOfMap())
                        .animationDuration(NO_ANIMATION_TIME)
                        .pitch(DEFAULT_MAP_PITCH_LEVEL_FOR_DRIVING)
                        .zoom(DEFAULT_MAP_ZOOM_LEVEL_FOR_DRIVING)
                        .build()
        );
        //tag::doc_start_chevron_tracking[]
        tomtomMap.getDrivingSettings().startTracking(chevron);
        //end::doc_start_chevron_tracking[]
    }

    public void stopTracking() {
        //tag::doc_stop_chevron_tracking[]
        tomtomMap.getDrivingSettings().stopTracking();
        //end::doc_stop_chevron_tracking[]
        tomtomMap.getRouteSettings().displayRoutesOverview();
    }

    private void clearMap() {
        tomtomMap.getDrivingSettings().removeChevrons();
        tomtomMap.clear();
    }

    private RouteCallback routeCallback = new RouteCallback() {
        @Override
        public void onRoutePlannerResponse(@NonNull RouteResponse routeResult) {
            FullRoute route = getFirstRoute(routeResult);
            chevron.setLocation(getRouteOrigin(route).toLocation());
            startSimulator(route.getCoordinates());
            showRoute(route);
        }

        @Override
        public void onRoutePlannerError(ServiceException exception) {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}
