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
package com.tomtom.online.sdk.samples.cases.search.entrypoints;

import android.content.Context;
import android.widget.Toast;

import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.api.SearchError;
import com.tomtom.online.sdk.search.api.fuzzy.FuzzySearchResultListener;
import com.tomtom.online.sdk.search.data.common.EntryPoint;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQuery;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResponse;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResult;

public class EntryPointsSearchPresenter extends BaseFunctionalExamplePresenter {

    private static final int ZOOM_LEVEL_FOR_EXAMPLE = 10;
    private static final int DEFAULT_MAP_PADDING = 0;

    protected Context context;
    protected FunctionalExampleFragment fragment;

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        context = view.getContext();
        fragment = view;

        if (!view.isMapRestored()) {
            centerOnDefaultLocation();
        }
        confMapPadding();
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new EntryPointsFunctionalExample();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        tomtomMap.clear();
        tomtomMap.setPadding(
                DEFAULT_MAP_PADDING,
                DEFAULT_MAP_PADDING,
                DEFAULT_MAP_PADDING,
                DEFAULT_MAP_PADDING
        );
    }

    @Override
    protected void confMapPadding() {

        int offsetDefault = view.getContext().getResources().getDimensionPixelSize(R.dimen.offset_default);

        int actionBarHeight = view.getContext().getResources().getDimensionPixelSize(
                android.support.v7.appcompat.R.dimen.abc_action_bar_default_height_material);

        int padding = actionBarHeight + offsetDefault;

        tomtomMap.setPadding(padding, padding, padding, padding);
    }

    private void centerOnDefaultLocation() {
        tomtomMap.centerOn(CameraPosition.builder(Locations.AMSTERDAM_LOCATION)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(ZOOM_LEVEL_FOR_EXAMPLE)
                .build());
    }

    public void performSearch(String term) {

        fragment.disableOptionsView();

        SearchApi searchAPI = OnlineSearchApi.create(context);
        searchAPI.search(FuzzySearchQueryBuilder.create(term).build(), new FuzzySearchResultListener() {
            @Override
            public void onSearchResult(FuzzySearchResponse response) {

                fragment.enableOptionsView();
                tomtomMap.clear();

                if (response.getResults().isEmpty()) {
                    view.showInfoText(R.string.entry_points_no_results, Toast.LENGTH_LONG);
                    return;
                }

                Icon icon = Icon.Factory.fromResources(
                        view.getContext(), R.drawable.ic_marker_entry_point);

                FuzzySearchResult fuzzySearchResult = response.getResults().get(0);

                tomtomMap.addMarker(new MarkerBuilder(fuzzySearchResult.getPosition())
                        .markerBalloon(new SimpleMarkerBalloon(fuzzySearchResult.getPoi().getName()))
                );

                //tag::doc_entry_points_search_request[]
                for (EntryPoint entryPoint : fuzzySearchResult.getEntryPoints()) {
                    SimpleMarkerBalloon balloon = new SimpleMarkerBalloon(
                            String.format(view.getString(R.string.entry_points_type),
                                    entryPoint.getType())
                    );
                    tomtomMap.addMarker(new MarkerBuilder(entryPoint.getPosition())
                            .markerBalloon(balloon)
                            .icon(icon));
                }
                //end::doc_entry_points_search_request[]

                tomtomMap.getMarkerSettings().zoomToAllMarkers();
            }

            @Override
            public void onSearchError(SearchError error) {
                view.showInfoText(error.getMessage(), Toast.LENGTH_LONG);
                fragment.enableOptionsView();
                tomtomMap.clear();
            }
        });
    }

}
