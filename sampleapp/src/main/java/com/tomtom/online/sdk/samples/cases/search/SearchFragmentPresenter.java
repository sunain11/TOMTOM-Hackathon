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
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.common.location.LatLngAcc;
import com.tomtom.online.sdk.common.util.Contextable;
import com.tomtom.online.sdk.location.LocationRequestsFactory;
import com.tomtom.online.sdk.location.LocationSource;
import com.tomtom.online.sdk.location.LocationSourceFactory;
import com.tomtom.online.sdk.location.LocationUpdateListener;
import com.tomtom.online.sdk.location.Locations;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.fuzzy.IFuzzySearchQuery;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQuery;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResponse;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResult;
import com.tomtom.online.sdk.search.extensions.SearchService;
import com.tomtom.online.sdk.search.extensions.SearchServiceConnectionCallback;
import com.tomtom.online.sdk.common.rx.RxContext;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SearchFragmentPresenter implements SearchPresenter, LocationUpdateListener, SearchServiceConnectionCallback, Contextable, RxContext {

    protected final static String LAST_SEARCH_QUERY_BUNDLE_KEY = "LAST_SEARCH_QUERY_BUNDLE_KEY";
    public static final int STANDARD_RADIUS = 30 * 1000; //30 km
    private static final int NETWORK_THREADS_NUMBER = 4;

    protected SearchView searchView;
    protected SearchService searchService;
    protected ImmutableList<FuzzySearchResult> lastSearchResult;

    protected LocationSource locationSource;

    public SearchFragmentPresenter(SearchView searchView) {
        Timber.d("SearchFragmentPresenter()");
        this.searchView = searchView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            lastSearchResult =  (ImmutableList<FuzzySearchResult>) savedInstanceState.getSerializable(LAST_SEARCH_QUERY_BUNDLE_KEY);
            if (lastSearchResult != null) {
                searchView.updateSearchResults(lastSearchResult);
            }
        }
    }

    @Override
    public void onCreate(Context context) {
        Timber.d("onCreate()");
        locationSource = getLocationSource(context);
    }

    @NonNull
    public LocationSource getLocationSource(Context context) {
        return new LocationSourceFactory().createDefaultLocationSource(
                context,
                this,
                LocationRequestsFactory.create().createSearchLocationRequest()
        );
    }


    @Override
    public void onResume() {
        Timber.d("onResume()");
        //tag::doc_location_source_activation[]
        locationSource.activate();
        //end::doc_location_source_activation[]
    }

    @Override
    public void onPause() {
        Timber.d("onPause()");
        //tag::doc_location_source_deactivation[]
        locationSource.deactivate();
        //end::doc_location_source_deactivation[]
    }

    @Override
    public void performSearch(String text) {
        Timber.d("performSearch(): %s", text);

        if (TextUtils.isEmpty(text)) {
            return;
        }

        performSearch(createSimpleQuery(text));
    }

    @Override
    public void performSearch(String query, String lang) {
        Timber.d("performSearch(): %s", query);

        if (TextUtils.isEmpty(query)) {
            return;
        }

        performSearch(createSimpleQuery(query, lang));
    }

    @Override
    public void performSearchWithPosition(String text) {
        Timber.d(";performSearchWithPosition(): %s", text);

        if (TextUtils.isEmpty(text)) {
            return;
        }

        performSearch(createQueryWithPosition(text, getLastKnownPosition()));
    }


    protected void searchFinished() {
        Timber.d("searchFinished()");
        enableSearchUI();
        searchView.getSearchProgressBar().setVisibility(View.GONE);
    }

    protected void performSearch(FuzzySearchQuery query) {

        disableSearchUI();
        cancelPreviousSearch();

        performSearchWithoutBlockingUI(query);
    }

    protected void performSearchWithoutBlockingUI(FuzzySearchQuery query) {
        searchView.getSearchProgressBar().setVisibility(View.VISIBLE);

        lastSearchResult = null;

        //tag::doc_perform_search[]
        getSearchProvider()
                .search(query)
                .subscribeOn(getWorkingScheduler())
                .observeOn(getResultScheduler())
                .subscribe(new DisposableSingleObserver<FuzzySearchResponse>() {
                    @Override
                    public void onSuccess(FuzzySearchResponse fuzzySearchResponse) {
                        lastSearchResult = fuzzySearchResponse.getResults();
                        searchView.updateSearchResults(fuzzySearchResponse.getResults());
                        searchFinished();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        searchView.showSearchFailedMessage(throwable.getMessage());
                        searchView.updateSearchResults(ImmutableList.<FuzzySearchResult>of());
                        searchFinished();
                    }
                });
        //end::doc_perform_search[]
    }

    protected SearchService getSearchProvider() {
        return searchService;
    }

    protected FuzzySearchQuery createSimpleQuery(String text) {
        Timber.d("createSimpleQuery(): %s", text);
        return FuzzySearchQueryBuilder.create(text).build();
    }

    protected FuzzySearchQuery createSimpleQuery(String text, String lang) {
        Timber.d("createSimpleQuery(): %s, %s", text, lang);
        //tag::doc_create_simple_query_with_lang[]
        return FuzzySearchQueryBuilder.create(text).withLanguage(lang).build();
        //end::doc_create_simple_query_with_lang[]
    }

    @SuppressWarnings("unused")
    private FuzzySearchQuery getSimpleQueryBuilderWithTerm(String text) {
        //tag::doc_create_basic_query[]
        return FuzzySearchQueryBuilder.create(text).build();
        //end::doc_create_basic_query[]
    }

    protected FuzzySearchQuery createQueryWithPosition(String text, LatLng position) {

        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        //tag::doc_create_query_with_position[]
        return FuzzySearchQueryBuilder.create(text).withPreciseness(new LatLngAcc(position, STANDARD_RADIUS)).build();
        //end::doc_create_query_with_position[]
    }

    @Override
    public LatLng getLastKnownPosition() {
        Location location = locationSource.getLastKnownLocation();
        if (location == null) {
            location = Locations.AMSTERDAM;
        }
        return new LatLng(location);
    }

    @Override
    public void enableSearchUI() {
        searchView.enableToggleButtons();
        searchView.enableInputField();
    }

    @Override
    public void disableSearchUI() {
        searchView.disableToggleButtons();
        searchView.disableInputField();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (lastSearchResult != null) {
            outState.putSerializable(LAST_SEARCH_QUERY_BUNDLE_KEY, lastSearchResult);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Timber.d("onLocationChanged()");
        searchView.refreshSearchResults();
    }

    //tag::doc_search_service_connection_callback[]
    @Override
    public void onBindSearchService(SearchService service) {
        searchService = service;
    }
    //end::doc_search_service_connection_callback[]

    public void cancelPreviousSearch() {
        //tag::doc_cancel_search[]
        getSearchProvider().cancelSearchIfRunning();
        //end::doc_cancel_search[]
    }

    @Override
    public Context getContext() {
        return searchView.getContext();
    }

    @NonNull
    @Override
    public Scheduler getWorkingScheduler() {
        return Schedulers.from(Executors.newFixedThreadPool(NETWORK_THREADS_NUMBER));
    }

    @NonNull
    @Override
    public Scheduler getResultScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
