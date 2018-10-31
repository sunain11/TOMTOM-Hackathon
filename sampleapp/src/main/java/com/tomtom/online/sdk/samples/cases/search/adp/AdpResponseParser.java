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
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.tomtom.online.sdk.common.func.Block;
import com.tomtom.online.sdk.common.func.FuncUtils;
import com.tomtom.online.sdk.common.geojson.Feature;
import com.tomtom.online.sdk.common.geojson.FeatureCollection;
import com.tomtom.online.sdk.common.geojson.GeoJsonObject;
import com.tomtom.online.sdk.common.geojson.GeoJsonObjectVisitorAdapter;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.search.data.additionaldata.AdditionalDataSearchResponse;
import com.tomtom.online.sdk.search.data.additionaldata.AdditionalDataSearchResultVisitor;
import com.tomtom.online.sdk.search.data.additionaldata.result.AdditionalDataSearchResult;
import com.tomtom.online.sdk.search.data.additionaldata.result.GeometryResult;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import timber.log.Timber;

class AdpResponseParser {

    private GeoDataConsumer consumer;

    public AdpResponseParser(GeoDataConsumer consumer) {
        this.consumer = consumer;
    }

    @SuppressLint("CheckResult")
    public void parseAdpResponse(AdditionalDataSearchResponse additionalDataResponse) {
        Observable.just(additionalDataResponse)
                .filter(nonEmptyResults())
                .map(firstAdpResult())
                .subscribe(new Consumer<AdditionalDataSearchResult>() {
                    @Override
                    public void accept(AdditionalDataSearchResult adpResult) throws Exception {
                        parseAdpResult(adpResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable, "Error when adp processed");
                    }
                });
    }

    public void parseAdpResult(AdditionalDataSearchResult adpResult) {
        adpResult.accept(new AdditionalDataSearchResultVisitor() {
            @Override
            public void visit(GeometryResult result) {
                FuncUtils.apply(result.getGeometryData(), new Block<GeoJsonObject>() {
                    @Override
                    public void apply(GeoJsonObject input) {
                        input.accept(new GeoJsonObjectVisitorAdapter() {
                            @Override
                            public void visit(FeatureCollection featureCollection) {
                                parseFeatureCollection(featureCollection);
                            }
                        });
                    }
                });
            }
        });
    }

    @SuppressLint("CheckResult")
    public void parseFeatureCollection(FeatureCollection featureCollection) {
        Observable.just(featureCollection)
                .filter(nonEmptyFeatures())
                .map(firstAdpFeature())
                .subscribe(consumer);
    }

    @NonNull
    public Predicate<AdditionalDataSearchResponse> nonEmptyResults() {
        return new Predicate<AdditionalDataSearchResponse>() {
            @Override
            public boolean test(AdditionalDataSearchResponse adpResults) throws Exception {
                return !adpResults.hasError() && !adpResults.getResults().isEmpty();
            }
        };
    }

    @NonNull
    private Function<AdditionalDataSearchResponse, AdditionalDataSearchResult> firstAdpResult() {
        return new Function<AdditionalDataSearchResponse, AdditionalDataSearchResult>() {
            @Override
            public AdditionalDataSearchResult apply(AdditionalDataSearchResponse adpResults) throws Exception {
                return adpResults.getResults().asList().get(0);
            }
        };
    }

    @NonNull
    public Predicate<FeatureCollection> nonEmptyFeatures() {
        return new Predicate<FeatureCollection>() {
            @Override
            public boolean test(FeatureCollection featureSet) throws Exception {
                return !featureSet.getFeatures().isEmpty();
            }
        };
    }

    @NonNull
    public Function<FeatureCollection, Feature> firstAdpFeature() {
        return new Function<FeatureCollection, Feature>() {
            @Override
            public Feature apply(FeatureCollection featureSet) throws Exception {
                return featureSet.getFeatures().get(0);
            }
        };
    }

}
