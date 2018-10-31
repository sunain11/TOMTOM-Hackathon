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
package com.tomtom.online.sdk.samples.cases.map.layers.shapes;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.Circle;
import com.tomtom.online.sdk.map.CircleBuilder;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.Polygon;
import com.tomtom.online.sdk.map.PolygonBuilder;
import com.tomtom.online.sdk.map.Polyline;
import com.tomtom.online.sdk.map.PolylineBuilder;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShapesCustomPresenter extends BaseFunctionalExamplePresenter {

    private static final int TOAST_DURATION = 2000; //milliseconds
    private final int POLYLINE_POINTS = 24;

    @Override
    public void bind(final FunctionalExampleFragment view, final TomtomMap map) {
        super.bind(view, map);
        if (!view.isMapRestored()) {
            centerMapOnLocation();
        }

        //tag::doc_register_shape_listeners[]
        tomtomMap.addOnCircleClickListener(onCircleClickListener);
        tomtomMap.addOnPolygonClickListener(onPolygonClickListener);
        tomtomMap.addOnPolylineClickListener(onPolylineClickListener);
        //end::doc_register_shape_listeners[]
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new ShapesCustomFunctionalExample();
    }

    @Override
    public void cleanup() {
        tomtomMap.getOverlaySettings().removeOverlays();
    }

    @Override
    public void onResume(final Context context) {

    }

    @Override
    public void onPause() {
        //tag::doc_unregister_shape_listeners[]
        tomtomMap.removeOnCircleClickListeners();
        tomtomMap.removeOnPolygonClickListeners();
        tomtomMap.removeOnPolylineClickListeners();
        //end::doc_unregister_shape_listeners[]
    }

    public void drawShapePolygon() {
        cleanup();
        centerMapOnLocation();
        List<LatLng> coordinates = randomCoordinates(Locations.AMSTERDAM_LOCATION, 360.0f);
        int color = randomColor();

        //tag::doc_shape_polygon[]
        Polygon polygon = PolygonBuilder.create()
                .coordinates(coordinates)
                .color(color)
                .build();
        tomtomMap.getOverlaySettings().addOverlay(polygon);
        //end::doc_shape_polygon[]
    }

    public void drawShapePolyline() {
        cleanup();
        centerMapOnLocation();
        List<LatLng> coordinates = randomCoordinates(Locations.AMSTERDAM_LOCATION, 270.0f);
        int color = randomColor();

        //tag::doc_shape_polyline[]
        Polyline polyline = PolylineBuilder.create()
                .coordinates(coordinates)
                .color(color)
                .build();
        tomtomMap.getOverlaySettings().addOverlay(polyline);
        //end::doc_shape_polyline[]
    }

    public void drawShapeCircle() {
        cleanup();
        centerMapOnLocation();
        int color = randomColor();
        LatLng position = Locations.AMSTERDAM_LOCATION;
        double radius = 5000.0;

        //tag::doc_shape_circle[]
        Circle circle = CircleBuilder.create()
                .fill(true)
                .radius(radius)
                .position(position)
                .color(color)
                .build();
        tomtomMap.getOverlaySettings().addOverlay(circle);
        //end::doc_shape_circle[]
    }

    private void centerMapOnLocation() {
        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH
        );
    }

    private List<LatLng> randomCoordinates(LatLng centroid, float degrees) {
        float radius = suitableRadiusToZoomLevel();
        float degreesPerPoint = degrees / POLYLINE_POINTS;
        float centerSpaceRation = 0.8f;

        List<LatLng> coordinates = new ArrayList<>();
        for (int i = 0; i < POLYLINE_POINTS; i++) {
            float distance = radius * centerSpaceRation + radius * (1.0f - centerSpaceRation) * randomRation();
            float angle = i * degreesPerPoint + randomRation() * degreesPerPoint;
            double y = Math.sin(Math.toRadians(angle)) * distance;
            double x = Math.cos(Math.toRadians(angle)) * distance;
            LatLng latLng = new LatLng(
                    centroid.getLatitude() + y,
                    centroid.getLongitude() + x
            );
            coordinates.add(latLng);
        }
        return coordinates;
    }

    private float suitableRadiusToZoomLevel() {
        return 128.0f / (1 << ((int)tomtomMap.getUiSettings().getCameraPosition().getZoom()));
    }

    private int randomColor() {
        Random random = new Random();
        int alpha = random.nextInt(255), red = random.nextInt(255), green = random.nextInt(255), blue = random.nextInt(255);
        return Color.argb(alpha, red, green, blue);
    }

    private float randomRation() {
        Random random = new Random();
        return random.nextFloat();
    }

    private TomtomMapCallback.OnPolylineClickListener onPolylineClickListener = new TomtomMapCallback.OnPolylineClickListener() {
        @Override
        public void onPolylineClick(@NonNull Polyline polyline) {
            view.showInfoText(R.string.polyline_clicked, TOAST_DURATION);
        }
    };

    private TomtomMapCallback.OnPolygonClickListener onPolygonClickListener = new TomtomMapCallback.OnPolygonClickListener() {
        @Override
        public void onPolygonClick(@NonNull Polygon polygon) {
            view.showInfoText(R.string.polygon_clicked, TOAST_DURATION);
        }
    };

    private TomtomMapCallback.OnCircleClickListener onCircleClickListener = new TomtomMapCallback.OnCircleClickListener() {
        @Override
        public void onCircleClick(@NonNull Circle circle) {
            view.showInfoText(R.string.circle_clicked, TOAST_DURATION);
        }
    };

}
