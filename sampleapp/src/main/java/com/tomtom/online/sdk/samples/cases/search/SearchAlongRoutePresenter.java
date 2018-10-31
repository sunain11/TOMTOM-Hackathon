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
package com.tomtom.online.sdk.samples.cases.search;

import android.content.Context;
import android.widget.Toast;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.InstructionsType;
import com.tomtom.online.sdk.routing.data.Report;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.routing.data.RouteType;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.RoutePlannerPresenter;
import com.tomtom.online.sdk.samples.cases.RoutingUiListener;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.routes.AmsterdamToHaarlemRouteConfig;
import com.tomtom.online.sdk.samples.routes.RouteConfigExample;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.api.SearchError;
import com.tomtom.online.sdk.search.api.alongroute.AlongRouteSearchResultListener;
import com.tomtom.online.sdk.search.data.alongroute.AlongRouteSearchQueryBuilder;
import com.tomtom.online.sdk.search.data.alongroute.AlongRouteSearchResponse;
import com.tomtom.online.sdk.search.data.alongroute.AlongRouteSearchResult;

import timber.log.Timber;

public class SearchAlongRoutePresenter extends RoutePlannerPresenter {

    private static final int SEARCH_MAX_DETOUR_TIME = 3600;
    private static final int SEARCH_MAX_LIMIT = 10;

    protected Context context;
    protected FunctionalExampleFragment fragment;

    public SearchAlongRoutePresenter(RoutingUiListener viewModel) {
        super(viewModel);
    }

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        context = view.getContext();
        fragment = view;
        displayRoute();
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new SearchAlongRouteFunctionalExample();
    }

    void displayRoute() {
        viewModel.showRoutingInProgressDialog();
        showRoute(getRouteQuery());
        tomtomMap.clearRoute();
    }

    protected RouteQuery getRouteQuery() {
        RouteQuery queryBuilder = RouteQueryBuilder.create(getRouteConfig().getOrigin(), getRouteConfig().getDestination())
                .withMaxAlternatives(0)
                .withReport(Report.EFFECTIVE_SETTINGS)
                .withInstructionsType(InstructionsType.TEXT)
                .withRouteType(RouteType.FASTEST).build();
        return queryBuilder;
    }

    @Override
    public RouteConfigExample getRouteConfig() {
        return new AmsterdamToHaarlemRouteConfig();
    }

    public void performSearch(String term) {

        fragment.disableOptionsView();

        if (routesMap.isEmpty()) {
            Timber.d("performSearch(): no routes available");
            return;
        }

        FullRoute route = (FullRoute) routesMap.values().toArray()[0];

        //tag::doc_search_along_route_request[]
        AlongRouteSearchQueryBuilder query = new AlongRouteSearchQueryBuilder(
                term,
                route.getCoordinates(),
                SEARCH_MAX_DETOUR_TIME
        );
        query.withLimit(SEARCH_MAX_LIMIT);

        SearchApi searchAPI = OnlineSearchApi.create(context);
        searchAPI.alongRouteSearch(query.build(), alongRouteSearchCallback);
        //end::doc_search_along_route_request[]
    }

    private AlongRouteSearchResultListener alongRouteSearchCallback = new AlongRouteSearchResultListener() {
        @Override
        public void onSearchResult(AlongRouteSearchResponse alongRouteSearchResponse) {
            tomtomMap.getMarkerSettings().removeMarkers();
            for (AlongRouteSearchResult result : alongRouteSearchResponse.getResults()) {
                createMarker(result.getPoi().getName(), result.getPosition());
            }
            fragment.enableOptionsView();
        }

        @Override
        public void onSearchError(SearchError error) {
            view.showInfoText(error.getMessage(), Toast.LENGTH_LONG);
            tomtomMap.getMarkerSettings().removeMarkers();
            fragment.enableOptionsView();
        }
    };

    private void createMarker(String name, LatLng position) {
        MarkerBuilder markerBuilder = new MarkerBuilder(position)
                .markerBalloon(new SimpleMarkerBalloon(name));
        tomtomMap.addMarker(markerBuilder);
    }

}
