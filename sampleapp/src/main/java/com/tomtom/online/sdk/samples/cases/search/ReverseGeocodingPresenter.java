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
import android.support.annotation.NonNull;

import com.google.common.base.Strings;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.Marker;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.api.SearchError;
import com.tomtom.online.sdk.search.api.revgeo.RevGeoSearchResultListener;
import com.tomtom.online.sdk.search.data.common.Address;
import com.tomtom.online.sdk.search.data.reversegeocoder.IReverseGeocoderSearchQuery;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQuery;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQueryBuilder;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse;

public class ReverseGeocodingPresenter extends BaseFunctionalExamplePresenter {

    protected Context context;
    private Marker marker;
    private SimpleMarkerBalloon balloon;

    private TomtomMapCallback.OnMapLongClickListener onMapLongClickListener =
            new TomtomMapCallback.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull LatLng latLng) {
                    tomtomMap.removeMarkers();
                    createMarker(latLng.getLatitude(), latLng.getLongitude());
                    reverseGeocode(latLng.getLatitude(), latLng.getLongitude());
                }
            };

    private TomtomMapCallback.OnMarkerClickListener onMarkerClickListener = new TomtomMapCallback.OnMarkerClickListener() {
        @Override
        public void onMarkerClick(@NonNull Marker marker) {
            tomtomMap.centerOn(marker.getPosition());
        }
    };

    public ReverseGeocodingPresenter() {
        balloon = new SimpleMarkerBalloon("Welcome to TomTom");
    }

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        context = view.getContext();
        setupTomtomMap();
        centerOnAmsterdam();
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new ReverseGeocodingFunctionalExample();
    }

    @Override
    public int getCurrentLocationBottomMarginDelta(FunctionalExampleFragment view) {
        return DEFAULT_CURRENT_LOCATION_BUTTON_BOTTOM_MARGIN_DELTA;
    }

    @Override
    public void cleanup() {
        tomtomMap.getMarkerSettings().removeMarkers();
        tomtomMap.removeOnMarkerClickListener(onMarkerClickListener);
        tomtomMap.removeOnMapLongClickListener(onMapLongClickListener);
    }

    public void centerOnAmsterdam() {
        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH
        );
    }

    protected void setupTomtomMap() {
        tomtomMap.addOnMarkerClickListener(onMarkerClickListener);
        tomtomMap.addOnMapLongClickListener(onMapLongClickListener);
    }

    private void createMarker(double latitude, double longitude) {
        MarkerBuilder markerBuilder = new MarkerBuilder(new LatLng(latitude, longitude)).markerBalloon(balloon);
        balloon.setText(context.getString(R.string.reverse_geocoding_fetching));
        marker = tomtomMap.addMarker(markerBuilder);
    }

    protected SearchApi createSearchAPI() {
        //tag::doc_create_search_object[]
        SearchApi searchApi = OnlineSearchApi.create(context);
        //end::doc_create_search_object[]
        return searchApi;
    }

    protected ReverseGeocoderSearchQuery createReverseGeocoderQuery(double latitude, double longitude) {
        return ReverseGeocoderSearchQueryBuilder.create(latitude, longitude).build();
    }

    protected String getNoReverseGeocodingResultsMessage() {
        return context.getString(R.string.reverse_geocoding_no_results);
    }

    protected String getAddressFromResponse(ReverseGeocoderSearchResponse response) {

        String result = getNoReverseGeocodingResultsMessage();

        if (!response.hasResults()) {
            return result;
        }

        Address address = response.getAddresses().get(0).getAddress();
        String freeformAddress = address.getFreeformAddress();
        if (!Strings.isNullOrEmpty(freeformAddress)) {
            result = freeformAddress;
        }

        return result;
    }

    protected void reverseGeocode(final double latitude, final double longitude) {

        //tag::doc_reverse_geocoding_request[]
        SearchApi searchAPI = createSearchAPI();
        ReverseGeocoderSearchQuery reverseGeocoderQuery =
                createReverseGeocoderQuery(latitude, longitude);

        searchAPI.reverseGeocoding(reverseGeocoderQuery, new RevGeoSearchResultListener() {
            @Override
            public void onSearchResult(ReverseGeocoderSearchResponse response) {
                String address = getAddressFromResponse(response);
                balloon.setText(address);
                marker.select();
            }

            @Override
            public void onSearchError(SearchError error) {
                balloon.setText(context.getString(R.string.reverse_geocoding_error));
                marker.select();
            }
        });
        //end::doc_reverse_geocoding_request[]
    }

}
