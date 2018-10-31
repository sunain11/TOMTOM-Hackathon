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
package com.tomtom.online.sdk.samples.cases.map.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.Icon.Factory;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.Marker;
import com.tomtom.online.sdk.map.MarkerAnchor;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.map.rx.RxTomtomMap;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.map.markers.balloons.TypedBalloonViewAdapter;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;
import com.tomtom.online.sdk.samples.utils.formatter.LatLngFormatter;

import java.util.List;

import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.tomtom.online.sdk.map.MapConstants.ORIENTATION_SOUTH;

public class MarkerCustomPresenter extends BaseFunctionalExamplePresenter implements TomtomMapCallback.OnMarkerClickListener {

    @SuppressLint("CheckResult")
    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        tomtomMap.getMarkerSettings().setMarkerBalloonViewAdapter(new TypedBalloonViewAdapter());

        //tag::doc_register_marker_listener[]
        tomtomMap.addOnMarkerClickListener(this);
        //end::doc_register_marker_listener[]

        //tag::doc_register_marker_observable[]
        RxTomtomMap rxTomtomMap = new RxTomtomMap(tomtomMap);
        rxTomtomMap.getOnMarkerClickObservable().subscribe(new Consumer<Marker>() {
            @Override
            public void accept(Marker marker) {
                //Your code goes here
            }
        });
        //end::doc_register_marker_observable[]

        if (!view.isMapRestored()) {
            centerMapOnLocation();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new MarkerCustomFunctionalExample();
    }

    @Override
    public void cleanup() {
        if (!isMapReady()) {
            return;
        }
        tomtomMap.removeMarkers();
    }

    @Override
    public void onResume(Context context) {
    }

    @Override
    public void onPause() {
        if (!isMapReady()) {
            return;
        }
        tomtomMap.removeOnMarkerClickListeners();
    }

    private boolean isMapReady() {
        return tomtomMap != null;
    }

    public void createSimpleMarker() {
        centerMapOnLocation();

        //tag::doc_remove_all_markers[]
        tomtomMap.removeMarkers();
        //end::doc_remove_all_markers[]

        //tag::doc_remove_tag_markers[]
        tomtomMap.removeMarkerByTag("tag");
        //end::doc_remove_tag_markers[]

        //tag::doc_remove_id_markers[]
        tomtomMap.removeMarkerByID(1);
        //end::doc_remove_id_markers[]
        List<LatLng> list = Locations.randomLocation(Locations.AMSTERDAM_LOCATION, 5, 0.2f);
        for (LatLng position : list) {
            //tag::doc_create_simple_marker[]
            MarkerBuilder markerBuilder = new MarkerBuilder(position)
                    .markerBalloon(new SimpleMarkerBalloon(positionToText(position)));
            tomtomMap.addMarker(markerBuilder);
            //end::doc_create_simple_marker[]
        }
    }

    public void createDecalMarker() {
        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL, ORIENTATION_SOUTH);

        tomtomMap.removeMarkers();
        List<LatLng> list = Locations.randomLocation(Locations.AMSTERDAM_LOCATION, 5, 0.2f);
        for (LatLng position : list) {
            //tag::doc_create_decal_marker[]
            MarkerBuilder markerBuilder = new MarkerBuilder(position)
                    .icon(Factory.fromResources(getContext(), R.drawable.ic_favourites))
                    .markerBalloon(new SimpleMarkerBalloon(positionToText(position)))
                    .tag("more information in tag").iconAnchor(MarkerAnchor.Bottom)
                    .decal(true); //By default is false
            tomtomMap.addMarker(markerBuilder);
            //end::doc_create_decal_marker[]
        }

    }

    public void centerMapOnLocation() {
        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH
        );

    }

    @Override
    public void onMarkerClick(@NonNull Marker m) {
        Timber.d("marker selected " + m.getTag());
    }

    @NonNull
    private String positionToText(LatLng position) {
        return LatLngFormatter.toSimpleString(position);
    }

}
