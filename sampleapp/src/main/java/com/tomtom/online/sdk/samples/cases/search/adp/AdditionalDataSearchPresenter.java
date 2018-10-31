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
package com.tomtom.online.sdk.samples.cases.search.adp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.data.additionaldata.AdditionalDataSearchQueryBuilder;
import com.tomtom.online.sdk.search.data.additionaldata.AdditionalDataSearchResponse;
import com.tomtom.online.sdk.search.data.common.additionaldata.GeometryDataSource;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQuery;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResponse;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResult;
import com.tomtom.online.sdk.common.rx.RxContext;

import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class AdditionalDataSearchPresenter extends BaseFunctionalExamplePresenter implements RxContext {

    private static final int ZOOM_LEVEL_FOR_EXAMPLE = 10;

    protected Context context;
    protected FunctionalExampleFragment fragment;
    private Disposable observableWork;

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);

        fragment = view;
        context = view.getContext();

        if (!view.isMapRestored()) {
            centerOnDefaultLocation();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new AdditionalDataSearchFunctionalExample();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        tomtomMap.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (observableWork !=null) {
            observableWork.dispose();
        }
    }

    public void centerOnDefaultLocation() {
        tomtomMap.centerOn(CameraPosition.builder(Locations.AMSTERDAM_CENTER_LOCATION)
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(ZOOM_LEVEL_FOR_EXAMPLE)
                .build());
    }

    @SuppressLint("CheckResult")
    public void performSearch(String term) {

        tomtomMap.clear();
        fragment.disableOptionsView();

        //tag::doc_adp_search_request[]
        //ADP Data is not available in all types (e.g. not in POI)
        //We restrict list of results only to Geo (Geographies) types
        //So that we are sure that we receive fields required by ADP
        FuzzySearchQueryBuilder fuzzySearchQuery = FuzzySearchQueryBuilder.create(term)
                .withIdx("Geo");

        final SearchApi searchApi = OnlineSearchApi.create(view.getContext());
        observableWork = searchApi.search(fuzzySearchQuery.build())
                .observeOn(getWorkingScheduler())
                .filter(nonEmptyResponse())
                .map(firstResultWithAdditionalData())
                .filter(nonEmptyResult())
                .map(whenResultPresent())
                .filter(whenGeometrySourcePresent())
                .map(firstAvailableGeometryData())
                .flatMap(new Function<GeometryDataSource, MaybeSource<? extends AdditionalDataSearchResponse>>() {
                    @Override
                    public MaybeSource<? extends AdditionalDataSearchResponse> apply(GeometryDataSource geometryDataSource) throws Exception {
                        AdditionalDataSearchQueryBuilder adpQuery = AdditionalDataSearchQueryBuilder.create();
                        adpQuery.withGeometryDataSource(geometryDataSource);
                        return searchApi.additionalDataSearch(adpQuery.build()).toMaybe();
                    }
                })
                .subscribeOn(getResultScheduler())
                .subscribe(new Consumer<AdditionalDataSearchResponse>() {
                    @Override
                    public void accept(AdditionalDataSearchResponse adpResponse) throws Exception {
                        fragment.enableOptionsView();
                        new AdpResponseParser(new GeoDataConsumer(tomtomMap)).parseAdpResponse(adpResponse);
                    }
                });
        //end::doc_adp_search_request[]
    }

    @NonNull
    private Function<FuzzySearchResult, GeometryDataSource> firstAvailableGeometryData() {
        return new Function<FuzzySearchResult, GeometryDataSource>() {
            @Override
            public GeometryDataSource apply(FuzzySearchResult fuzzySearchResult) throws Exception {
                return fuzzySearchResult.getAdditionalDataSources().getGeometryDataSource().get();
            }
        };
    }

    @NonNull
    private Predicate<FuzzySearchResult> whenGeometrySourcePresent() {
        return new Predicate<FuzzySearchResult>() {
            @Override
            public boolean test(FuzzySearchResult fuzzySearchResult) throws Exception {
                return fuzzySearchResult.getAdditionalDataSources() != null &&
                        fuzzySearchResult.getAdditionalDataSources().getGeometryDataSource().isPresent();
            }
        };
    }

    @NonNull
    private Function<FuzzySearchResponse, Optional<FuzzySearchResult>> firstResultWithAdditionalData() {
        return new Function<FuzzySearchResponse, Optional<FuzzySearchResult>>() {
            @Override
            public Optional<FuzzySearchResult> apply(FuzzySearchResponse fuzzySearchResponse) throws Exception {
                for (FuzzySearchResult result : fuzzySearchResponse.getResults()) {
                    if (result.getAdditionalDataSources() == null) {
                        continue;
                    }
                    return Optional.of(result);
                }
                return Optional.absent();
            }
        };
    }

    @NonNull
    private Predicate<FuzzySearchResponse> nonEmptyResponse() {
        return new Predicate<FuzzySearchResponse>() {
            @Override
            public boolean test(FuzzySearchResponse fuzzySearchResponse) throws Exception {
                return !fuzzySearchResponse.getResults().isEmpty();
            }
        };
    }

    @NonNull
    private Predicate<Optional<FuzzySearchResult>> nonEmptyResult(){
        return new Predicate<Optional<FuzzySearchResult>>() {
            @Override
            public boolean test(Optional<FuzzySearchResult> fuzzySearchResult) throws Exception {
                return fuzzySearchResult.isPresent();
            }
        };
    }

    private Function<Optional<FuzzySearchResult>, FuzzySearchResult> whenResultPresent(){
        return new Function<Optional<FuzzySearchResult>, FuzzySearchResult>() {
            @Override
            public FuzzySearchResult apply(Optional<FuzzySearchResult> fuzzySearchResultOptional) throws Exception {
                return fuzzySearchResultOptional.get();
            }
        };
    }


    @NonNull
    @Override
    public Scheduler getWorkingScheduler() {
        return Schedulers.newThread();
    }

    @NonNull
    @Override
    public Scheduler getResultScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
