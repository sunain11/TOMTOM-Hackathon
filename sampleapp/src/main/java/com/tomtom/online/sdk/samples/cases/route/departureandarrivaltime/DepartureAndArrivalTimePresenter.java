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
package com.tomtom.online.sdk.samples.cases.route.departureandarrivaltime;

import android.support.annotation.VisibleForTesting;

import com.tomtom.online.sdk.routing.data.InstructionsType;
import com.tomtom.online.sdk.routing.data.Report;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.RoutePlannerPresenter;
import com.tomtom.online.sdk.samples.cases.RoutingUiListener;
import com.tomtom.online.sdk.samples.routes.AmsterdamToRotterdamRouteConfig;
import com.tomtom.online.sdk.samples.routes.RouteConfigExample;

import org.joda.time.DateTime;

import java.util.Date;

public class DepartureAndArrivalTimePresenter extends RoutePlannerPresenter {

    public DepartureAndArrivalTimePresenter(RoutingUiListener viewModel) {
        super(viewModel);
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new DepartureAndArrivalTimeFunctionalExample();
    }

    @Override
    public RouteConfigExample getRouteConfig() {
        return new AmsterdamToRotterdamRouteConfig();
    }


    public void clearRoute() {
        tomtomMap.clearRoute();
    }

    public void displayArrivalAtRoute(DateTime arrivalDateTime) {
        viewModel.showRoutingInProgressDialog();
        showRoute(getArrivalRouteQuery(arrivalDateTime.toDate()));
    }

    @VisibleForTesting
    protected RouteQuery getArrivalRouteQuery(Date arrivalTime) {
        //tag::doc_route_arrival_time[]
        RouteQuery queryBuilder = RouteQueryBuilder.create(getRouteConfig().getOrigin(), getRouteConfig().getDestination())
                .withReport(Report.EFFECTIVE_SETTINGS)
                .withInstructionsType(InstructionsType.TEXT)
                .withArriveAt(arrivalTime)
                .withConsiderTraffic(false).build();
        //end::doc_route_arrival_time[]
        return queryBuilder;
    }

    public void displayDepartureAtRoute(DateTime departureDate) {
        viewModel.showRoutingInProgressDialog();
        showRoute(getDepartureRouteQuery(departureDate.toDate()));
    }

    protected RouteQuery getDepartureRouteQuery(Date departureTime) {
        //tag::doc_route_departure_time[]
        return RouteQueryBuilder.create(getRouteConfig().getOrigin(), getRouteConfig().getDestination())
                .withReport(Report.EFFECTIVE_SETTINGS)
                .withInstructionsType(InstructionsType.TEXT)
                .withDepartAt(departureTime)
                .withConsiderTraffic(false).build();
        //end::doc_route_departure_time[]
    }



}
