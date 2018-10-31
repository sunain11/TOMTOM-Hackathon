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
package com.tomtom.online.sdk.samples.cases.search.batch;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.CircleBuilder;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;
import com.tomtom.online.sdk.search.BatchableSearchResponse;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.api.SearchError;
import com.tomtom.online.sdk.search.api.batch.BatchSearchResultListener;
import com.tomtom.online.sdk.search.data.batch.BatchSearchQuery;
import com.tomtom.online.sdk.search.data.batch.BatchSearchQueryBuilder;
import com.tomtom.online.sdk.search.data.batch.BatchSearchResponse;
import com.tomtom.online.sdk.search.data.batch.BatchableSearchResponseVisitorAdapter;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQuery;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResponse;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResult;
import com.tomtom.online.sdk.search.data.geometry.Geometry;
import com.tomtom.online.sdk.search.data.geometry.GeometrySearchQuery;
import com.tomtom.online.sdk.search.data.geometry.GeometrySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.geometry.GeometrySearchResponse;
import com.tomtom.online.sdk.search.data.geometry.GeometrySearchResult;
import com.tomtom.online.sdk.search.data.geometry.query.CircleGeometry;

import java.util.ArrayList;
import java.util.List;

public class BatchSearchPresenter extends BaseFunctionalExamplePresenter {

    private static final int ZOOM_LEVEL_FOR_EXAMPLE = 10;
    private static final int GEOMETRY_RADIUS = 4000;

    private FunctionalExampleFragment fragment;

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);

        fragment = view;

        if (!view.isMapRestored()) {
            centerOnDefaultLocation();
        }

        tomtomMap.getMarkerSettings().setMarkersClustering(true);
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new BatchSearchFunctionalExample();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        tomtomMap.clear();
    }

    private void centerOnDefaultLocation() {
        tomtomMap.centerOn(CameraPosition.builder(Locations.AMSTERDAM_CENTER_LOCATION)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(ZOOM_LEVEL_FOR_EXAMPLE)
                .build());
    }

    private FuzzySearchQueryBuilder getBaseFuzzyQuery(String category) {
        FuzzySearchQueryBuilder queryBuilder = FuzzySearchQueryBuilder.create(category)
                .withCategory(true);
        return queryBuilder;
    }

    private FuzzySearchQuery getAmsterdamQuery(String category) {
        FuzzySearchQueryBuilder query = getBaseFuzzyQuery(category);
        query.withPosition(Locations.AMSTERDAM_CENTER_LOCATION);
        query.withLimit(10);
        return query.build();
    }

    private FuzzySearchQuery getHaarlemQuery(String category) {
        FuzzySearchQueryBuilder query = getBaseFuzzyQuery(category);
        query.withPosition(Locations.AMSTERDAM_HAARLEM);
        query.withLimit(15);
        return query.build();
    }

    private GeometrySearchQuery getHoofddropQuery(String category) {
        List<Geometry> geometries = new ArrayList<>();
        CircleGeometry circleGeometry = new CircleGeometry(Locations.HOOFDDORP_LOCATION, GEOMETRY_RADIUS);
        Geometry geometry = new Geometry(circleGeometry);
        geometries.add(geometry);
        return new GeometrySearchQueryBuilder(category, geometries).build();
    }

    @SuppressLint("CheckResult")
    public void performSearch(String category) {

        tomtomMap.clear();
        fragment.disableOptionsView();

        //tag::doc_batch_search_request[]
        //Using batch, it is possible to execute different search types:
        //fuzzy, geometry or reverse geocoding. The order of responses
        //is the same as the order in which the queries are added.

        BatchSearchQueryBuilder batchQuery = new BatchSearchQueryBuilder();
        batchQuery.withFuzzySearchQuery(getAmsterdamQuery(category));
        batchQuery.withFuzzySearchQuery(getHaarlemQuery(category));
        batchQuery.withGeometrySearchQuery(getHoofddropQuery(category));

        final SearchApi searchApi = OnlineSearchApi.create(view.getContext());
        searchApi.batchSearch(batchQuery.build(), batchSearchResultListener);
        //end::doc_batch_search_request[]
    }

    private final BatchSearchResultListener batchSearchResultListener = new BatchSearchResultListener() {
        @Override
        public void onSearchResult(BatchSearchResponse response) {
            fragment.enableOptionsView();
            new BatchSearchResponseDisplay(tomtomMap).display(response);
        }

        @Override
        public void onSearchError(SearchError error) {
            ((BatchSearchFragment) view).showErrorMsg(error.getMessage());
            view.enableOptionsView();
            tomtomMap.clear();
        }
    };

    private static class BatchSearchResponseDisplay extends BatchableSearchResponseVisitorAdapter {

        private TomtomMap tomtomMap;

        public BatchSearchResponseDisplay(TomtomMap tomtomMap) {
            this.tomtomMap = tomtomMap;
        }

        public void display(BatchSearchResponse batchSearchResponse){
            ImmutableList<BatchableSearchResponse> responses = batchSearchResponse.getSearchResponses().asList();
            for (BatchableSearchResponse response : responses) {
                response.accept(new BatchSearchResponseDisplay(tomtomMap));
            }
            tomtomMap.zoomToAllMarkers();
        }

        @Override
        public void visit(FuzzySearchResponse fuzzySearchResponse) {
            for (FuzzySearchResult result : fuzzySearchResponse.getResults()) {
                LatLng position = result.getPosition();
                String name = result.getPoi().getName();
                addMarker(position, name);
            }
        }

        @Override
        public void visit(GeometrySearchResponse geometrySearchResponse) {
            for (GeometrySearchResult result : geometrySearchResponse.getResults()) {
                LatLng position = result.getPosition();
                String name = result.getPoi().getName();
                addMarker(position, name);
            }
            addHoofddropGeometry();
        }

        private void addMarker(LatLng position, String name) {
            tomtomMap.addMarker(new MarkerBuilder(position)
                    .markerBalloon(new SimpleMarkerBalloon(name))
                    .shouldCluster(true)
            );
        }

        private void addHoofddropGeometry() {
            tomtomMap.getOverlaySettings().addOverlay(
                    CircleBuilder.create()
                            .position(Locations.HOOFDDORP_LOCATION)
                            .radius(GEOMETRY_RADIUS)
                            .color(Color.argb(128, 255, 0, 0))
                            .fill(true)
                            .build()
            );
        }
    }

}
