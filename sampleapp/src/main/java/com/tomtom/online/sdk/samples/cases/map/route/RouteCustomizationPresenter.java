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
package com.tomtom.online.sdk.samples.cases.map.route;

import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.VisibleForTesting;

import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.RouteStyle;
import com.tomtom.online.sdk.map.RouteStyleBuilder;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.RoutePlannerPresenter;
import com.tomtom.online.sdk.samples.cases.RoutingUiListener;
import com.tomtom.online.sdk.samples.routes.AmsterdamToOsloRouteConfig;
import com.tomtom.online.sdk.samples.routes.RouteConfigExample;

public class RouteCustomizationPresenter extends RoutePlannerPresenter {

    private static final double DEFAULT_SCALE_FOR_SAMPLE_APP_ICONS = 4.5;

    public RouteCustomizationPresenter(RoutingUiListener viewModel) {
        super(viewModel);
    }


    @Override
    public FunctionalExampleModel getModel() {
        return new RouteCustomizationFunctionalExample();
    }

    void displayRoute(RouteStyle routeStyle, Icon startIcon, Icon endIcon) {
        tomtomMap.clearRoute();
        viewModel.showRoutingInProgressDialog();
        showRoute(getRouteQuery(), routeStyle, startIcon, endIcon);
    }

    @VisibleForTesting
    protected RouteQuery getRouteQuery() {
        return RouteQueryBuilder.create(getRouteConfig().getOrigin(), getRouteConfig().getDestination())
                .withMaxAlternatives(0)
                .withConsiderTraffic(false)
                .build();
    }

    @Override
    public RouteConfigExample getRouteConfig() {
        return new AmsterdamToOsloRouteConfig();
    }

    public void startRoutingWithCustomRoute() {

        RouteStyle routeStyle = createCustomRouteStyle();

        Icon startIcon = prepareRouteIconFromDrawable(R.drawable.ic_map_route_departure, DEFAULT_ICON_SCALE);
        Icon endIcon = prepareRouteIconFromDrawable(R.drawable.ic_map_fav, DEFAULT_SCALE_FOR_SAMPLE_APP_ICONS);

        displayRoute(routeStyle, startIcon, endIcon);
    }

    @VisibleForTesting
    RouteStyle createCustomRouteStyle() {
        return
                //tag::doc_create_custom_route_style[]
                RouteStyleBuilder.create()
                        .withWidth(2.0)
                        .withFillColor(Color.BLACK)
                        .withOutlineColor(Color.RED)
                        .build();
        //end::doc_create_custom_route_style[]
    }

    public void startRoutingWithBasicRoute() {
        Icon startIcon = prepareRouteIconFromDrawable(R.drawable.ic_map_route_departure, DEFAULT_ICON_SCALE);
        Icon endIcon = prepareRouteIconFromDrawable(R.drawable.ic_map_route_destination, DEFAULT_ICON_SCALE);

        displayRoute(RouteStyle.DEFAULT_ROUTE_STYLE, startIcon, endIcon);
    }

    private Icon prepareRouteIconFromDrawable(@DrawableRes int routeIcon, double scale) {
        return Icon.Factory.fromResources(view.getContext(), routeIcon, scale);
    }
}