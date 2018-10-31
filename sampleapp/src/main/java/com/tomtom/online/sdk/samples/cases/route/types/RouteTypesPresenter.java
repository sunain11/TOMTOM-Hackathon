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
package com.tomtom.online.sdk.samples.cases.route.types;

import android.support.annotation.VisibleForTesting;

import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.routing.data.InstructionsType;
import com.tomtom.online.sdk.routing.data.Report;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.routing.data.RouteType;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.RoutePlannerPresenter;
import com.tomtom.online.sdk.samples.cases.RoutingUiListener;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.routes.AmsterdamToRotterdamRouteConfig;
import com.tomtom.online.sdk.samples.routes.RouteConfigExample;

public class RouteTypesPresenter extends RoutePlannerPresenter {

    private RouteType type;

    public RouteTypesPresenter(RoutingUiListener viewModel) {
        super(viewModel);
    }

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        if (type != null) {
            displayRoute(type);
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new RouteTypesFunctionalExample();
    }


    void displayRoute(RouteType routeType) {
        tomtomMap.clearRoute();
        viewModel.showRoutingInProgressDialog();
        showRoute(getRouteQuery(routeType));
    }

    @VisibleForTesting
    protected RouteQuery getRouteQuery(RouteType routeType) {
        //tag::doc_route_type[]
        RouteQueryBuilder queryBuilder = RouteQueryBuilder.create(getRouteConfig().getOrigin(), getRouteConfig().getDestination())
                .withMaxAlternatives(0)
                .withReport(Report.EFFECTIVE_SETTINGS)
                .withInstructionsType(InstructionsType.TEXT)
                .withRouteType(routeType)
                .withConsiderTraffic(false);
        return queryBuilder.build();
        //end::doc_route_type[]
    }

    @Override
    public RouteConfigExample getRouteConfig() {
        return new AmsterdamToRotterdamRouteConfig();
    }


    public void startRoutingShortest() {
        displayRoute(RouteType.SHORTEST);
    }

    public void startRoutingEco() {
        displayRoute(RouteType.ECO);
    }

    public void startRoutingFastest() {
        displayRoute(RouteType.FASTEST);
    }

    public RouteType getType() {
        return type;
    }

    public void setType(RouteType type) {
        this.type = type;
    }

}
