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
package com.tomtom.online.sdk.samples.cases.search.geometry;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.CircleBuilder;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.PolygonBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.api.SearchError;
import com.tomtom.online.sdk.search.api.geometry.GeometrySearchResultListener;
import com.tomtom.online.sdk.search.data.geometry.Geometry;
import com.tomtom.online.sdk.search.data.geometry.GeometrySearchQuery;
import com.tomtom.online.sdk.search.data.geometry.GeometrySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.geometry.GeometrySearchResponse;
import com.tomtom.online.sdk.search.data.geometry.GeometrySearchResult;
import com.tomtom.online.sdk.search.data.geometry.query.CircleGeometry;
import com.tomtom.online.sdk.search.data.geometry.query.PolygonGeometry;

import java.util.ArrayList;
import java.util.List;

public class GeometrySearchPresenter extends BaseFunctionalExamplePresenter {

    private static final LatLng MAP_CENTER_FOR_EXAMPLE = new LatLng(52.3691851, 4.8505632);
    private static final int ZOOM_LEVEL_FOR_EXAMPLE = 10;

    private static final int OVERLAYS_COLOR = Color.rgb(255, 0, 0);
    private static final float OVERLAYS_OPACITY = 0.5f;

    private static final LatLng CIRCLE_CENTER = new LatLng(52.3639871, 4.7953232);
    private static final int CIRCLE_RADIUS = 2000;

    private static final List<LatLng> POLYGON_POINTS = ImmutableList.of(
            new LatLng(52.37874, 4.90482),
            new LatLng(52.37664, 4.92559),
            new LatLng(52.37497, 4.94877),
            new LatLng(52.36805, 4.97246),
            new LatLng(52.34918, 4.95993),
            new LatLng(52.34016, 4.95169),
            new LatLng(52.32894, 4.91392),
            new LatLng(52.34048, 4.88611),
            new LatLng(52.33953, 4.84388),
            new LatLng(52.37067, 4.8432),
            new LatLng(52.38492, 4.84663),
            new LatLng(52.40011, 4.85058),
            new LatLng(52.38995, 4.89075)
    );

    private static final int SEARCH_RESULTS_LIMIT = 50;

    protected Context context;
    protected FunctionalExampleFragment fragment;

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        context = view.getContext();
        fragment = view;

        if (!view.isMapRestored()) {
            centerOnDefaultLocation();
            showShapesOnMap();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new GeometrySearchFunctionalExample();
    }

    @Override
    public void cleanup() {
        tomtomMap.clear();
    }

    public void centerOnDefaultLocation() {
        tomtomMap.centerOn(CameraPosition.builder(MAP_CENTER_FOR_EXAMPLE)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(ZOOM_LEVEL_FOR_EXAMPLE)
                .build());
    }

    private void showShapesOnMap() {
        tomtomMap.getOverlaySettings().addOverlay(
                CircleBuilder.create()
                        .position(CIRCLE_CENTER)
                        .radius(CIRCLE_RADIUS)
                        .color(OVERLAYS_COLOR)
                        .opacity(OVERLAYS_OPACITY)
                        .fill(true)
                        .build()
        );

        tomtomMap.getOverlaySettings().addOverlay(
                PolygonBuilder.create()
                        .coordinates(POLYGON_POINTS)
                        .color(OVERLAYS_COLOR)
                        .opacity(OVERLAYS_OPACITY)
                        .build()
        );
    }

    public void performSearch(String term) {

        fragment.disableOptionsView();

        //tag::doc_geometry_search_request[]
        List<Geometry> geometries = new ArrayList<>();
        geometries.add(new Geometry(new PolygonGeometry(POLYGON_POINTS)));
        geometries.add(new Geometry(new CircleGeometry(CIRCLE_CENTER, CIRCLE_RADIUS)));

        GeometrySearchQuery query = new GeometrySearchQueryBuilder(term, geometries)
                .withLimit(SEARCH_RESULTS_LIMIT).build();

        SearchApi searchAPI = OnlineSearchApi.create(context);
        searchAPI.geometrySearch(query, searchResultListener);
        //end::doc_geometry_search_request[]
    }

    private GeometrySearchResultListener searchResultListener = new GeometrySearchResultListener() {
        @Override
        public void onSearchResult(GeometrySearchResponse geometrySearchResponse) {
            if (isResultEmpty(geometrySearchResponse)) {
                onSearchError(new SearchError("No results"));
                return;
            }
            fragment.enableOptionsView();
            tomtomMap.getMarkerSettings().removeMarkers();
            for (GeometrySearchResult result : geometrySearchResponse.getResults()) {
                createMarker(result.getPoi().getName(), result.getPosition());
            }
        }

        boolean isResultEmpty(GeometrySearchResponse geometrySearchResponse) {
            return geometrySearchResponse.getResults() == null || geometrySearchResponse.getResults().isEmpty();
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
