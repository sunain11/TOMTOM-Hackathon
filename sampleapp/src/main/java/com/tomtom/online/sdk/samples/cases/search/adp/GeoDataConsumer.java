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

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.tomtom.online.sdk.common.func.Block;
import com.tomtom.online.sdk.common.func.FuncUtils;
import com.tomtom.online.sdk.common.geojson.Feature;
import com.tomtom.online.sdk.common.geojson.GeoJsonObjectVisitorAdapter;
import com.tomtom.online.sdk.common.geojson.geometry.Geometry;
import com.tomtom.online.sdk.common.geojson.geometry.MultiPolygon;
import com.tomtom.online.sdk.common.geojson.geometry.Polygon;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.Overlay;
import com.tomtom.online.sdk.map.PolygonBuilder;
import com.tomtom.online.sdk.map.TomtomMap;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

class GeoDataConsumer implements Consumer<Feature> {

    static final int COLOR_RED = Color.rgb(255, 0, 0);
    static final float COLOR_OPACITY = 0.7f;

    private TomtomMap tomtomMap;

    public GeoDataConsumer(TomtomMap tomtomMap) {
        this.tomtomMap = tomtomMap;
    }

    private void displayPolygon(Polygon polygon) {
        List<LatLng> coordinates = polygon.getExteriorRing().getCoordinates().asList();
        Overlay overlay = PolygonBuilder.create()
                .coordinates(coordinates)
                .color(COLOR_RED)
                .opacity(COLOR_OPACITY)
                .build();
        tomtomMap.getOverlaySettings().addOverlay(overlay);
    }

    @NonNull
    private List<LatLng> parsePolygonLatLngs(Polygon polygon){
        return polygon.getExteriorRing().getCoordinates().asList();
    }

    @Override
    public void accept(Feature feature) throws Exception {

        FuncUtils.apply(feature.getGeometry(), new Block<Geometry>() {
            @Override
            public void apply(Geometry input) {
                input.accept(new GeoJsonObjectVisitorAdapter() {
                    @Override
                    public void visit(Polygon polygon) {
                        displayPolygon(polygon);
                        tomtomMap.setCurrentBounds(parsePolygonLatLngs(polygon));
                    }

                    @Override
                    public void visit(MultiPolygon multiPolygon) {
                        List<LatLng> coordinates = new ArrayList<>();
                        for (Polygon polygon : multiPolygon.getPolygons()) {
                            coordinates.addAll(parsePolygonLatLngs(polygon));
                            displayPolygon(polygon);
                        }
                        tomtomMap.setCurrentBounds(coordinates);
                    }
                });
            }
        });
    }
}
