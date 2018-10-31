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
import com.tomtom.online.sdk.map.RouteBuilder;
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

import java.util.List;

import timber.log.Timber;

public class ChevronTrackingPresenter extends BaseFunctionalExamplePresenter {

    private static final LatLng ROUTE_ORIGIN = new LatLng(51.772756, 19.423065);
    private static final LatLng ROUTE_DESTINATION = new LatLng(51.773136, 19.4233983);
    private static final ImmutableList<LatLng> ROUTE_WAYPOINTS = ImmutableList.of(
            new LatLng(51.780990, 19.451229),
            new LatLng(51.786451, 19.449562),
            new LatLng(51.791383, 19.420641)
    );
    private final static LatLng DEFAULT_MAP_POSITION = new LatLng(51.776495, 19.440739);

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
    }

    private void createChevron() {
        Icon activeIcon = Icon.Factory.fromResources(getContext(), R.drawable.chevron_color, CHEVRON_ICON_SCALE);
        Icon inactiveIcon = Icon.Factory.fromResources(getContext(), R.drawable.chevron_shadow, CHEVRON_ICON_SCALE);
        //tag::doc_create_chevron[]
        ChevronBuilder chevronBuilder = ChevronBuilder.create(activeIcon, inactiveIcon);
        chevron = tomtomMap.getDrivingSettings().addChevron(chevronBuilder);
        //end::doc_create_chevron[]
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
