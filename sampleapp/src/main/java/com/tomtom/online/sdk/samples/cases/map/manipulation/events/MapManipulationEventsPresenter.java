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
package com.tomtom.online.sdk.samples.cases.map.manipulation.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.tomtom.core.maps.gestures.GesturesDetectionSettings;
import com.tomtom.core.maps.gestures.GesturesDetectionSettingsBuilder;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.map.rx.RxTomtomMap;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class MapManipulationEventsPresenter extends BaseFunctionalExamplePresenter {

    private static final String TOAST_MESSAGE = "%s : %f, %f";
    private static final int TOAST_DURATION = 2000; //milliseconds

    private CompositeDisposable rxDisposables = new CompositeDisposable();

    //tag::doc_map_define_map_manipulation_listeners[]
    private TomtomMapCallback.OnMapClickListener onMapClickListener =
            new TomtomMapCallback.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    displayMessage(
                            R.string.menu_events_on_map_click,
                            latLng.getLatitude(),
                            latLng.getLongitude()
                    );
                }
            };
    private TomtomMapCallback.OnMapLongClickListener onMapLongClickListener =
            new TomtomMapCallback.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull LatLng latLng) {
                    displayMessage(
                            R.string.menu_events_on_map_long_click,
                            latLng.getLatitude(),
                            latLng.getLongitude()
                    );
                }
            };
    private TomtomMapCallback.OnMapViewPortChanged onMapViewPortChangedListener =
            new TomtomMapCallback.OnMapViewPortChanged() {
                @Override
                public void onMapViewPortChanged(float focalLatitude,
                                                 float focalLongitude,
                                                 double zoomLevel,
                                                 float perspectiveRatio,
                                                 float yawDegrees) {
                    displayMessage(
                            R.string.menu_events_on_map_panning,
                            focalLatitude,
                            focalLongitude
                    );
                }
            };

    //end::doc_map_define_map_manipulation_listeners[]


    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);

        setupTomtomMap();
        setupRxTomtomMap();

        if (!view.isMapRestored()) {
            centerOnAmsterdam();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new MapManipulationEventsFunctionalExample();
    }


    @Override
    public int getCurrentLocationBottomMarginDelta(FunctionalExampleFragment view) {
        return DEFAULT_CURRENT_LOCATION_BUTTON_BOTTOM_MARGIN_DELTA;
    }

    @Override
    public void cleanup() {
        Timber.d("RxUtils: clean()");
        rxDisposables.dispose();
    }

    public void centerOnAmsterdam() {
        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH
        );
    }

    private void setupTomtomMap() {
        //tag::doc_map_set_map_manipulation_listeners[]
        tomtomMap.addOnMapClickListener(onMapClickListener);
        tomtomMap.addOnMapLongClickListener(onMapLongClickListener);
        tomtomMap.addOnMapViewPortChangedListener(onMapViewPortChangedListener);
        //end::doc_map_set_map_manipulation_listeners[]
    }

    private void setupRxTomtomMap() {

        //tag::doc_map_set_rx_wrapper[]
        RxTomtomMap rxTomtomMap = new RxTomtomMap(tomtomMap);
        //end::doc_map_set_rx_wrapper[]

        //tag::doc_map_set_map_manipulation_observables[]
        Disposable mapClickDisposable = rxTomtomMap
                .getOnMapClickObservable()
                .observeOn(mainThread())
                .subscribe(new Consumer<LatLng>() {
                    @Override
                    public void accept(LatLng latLng) throws Exception {
                        displayMessage(
                                R.string.menu_events_on_map_click,
                                latLng.getLatitude(),
                                latLng.getLongitude()
                        );
                    }
                });
        //end::doc_map_set_map_manipulation_observables[]
        rxDisposables.add(mapClickDisposable);
    }


    private void displayMessage(@StringRes int titleId, double lat, double lon) {

        Context context = view.getContext();
        if (context == null) {
            return;
        }

        String title = view.getContext().getString(titleId);
        Timber.d("Functional Example on %s", title);
        String message = String.format(java.util.Locale.getDefault(),
                TOAST_MESSAGE,
                title,
                lat,
                lon);

        view.showInfoText(message, TOAST_DURATION);
    }

    // This method is only here to provide dynamic code snippet for documentation.
    @SuppressWarnings("unused")
    private void exampleForGesturesDetectionEnabling() {

        //tag::doc_gesture_disable_zoom[]
        tomtomMap.updateGesturesDetectionSettings(
                GesturesDetectionSettingsBuilder.create()
                        .zoomEnabled(false)
                        .build()
        );
        //end::doc_gesture_disable_zoom[]

        //tag::doc_gesture_disable_tilt[]
        tomtomMap.updateGesturesDetectionSettings(
                GesturesDetectionSettingsBuilder.create()
                        .tiltEnabled(false)
                        .build()
        );
        //end::doc_gesture_disable_tilt[]

        //tag::doc_gesture_disable_rotation[]
        tomtomMap.updateGesturesDetectionSettings(
                GesturesDetectionSettingsBuilder.create()
                        .rotationEnabled(false)
                        .build()
        );
        //end::doc_gesture_disable_rotation[]


        //tag::doc_gesture_disable_panning[]
        tomtomMap.updateGesturesDetectionSettings(
                GesturesDetectionSettingsBuilder.create()
                        .panningEnabled(false)
                        .build()
        );
        //end::doc_gesture_disable_panning[]


        //tag::doc_gesture_disable_rotation_panning[]
        tomtomMap.updateGesturesDetectionSettings(
                GesturesDetectionSettingsBuilder.create()
                        .rotationEnabled(false)
                        .panningEnabled(false)
                        .build()
        );
        //end::doc_gesture_disable_rotation_panning[]


        //tag::doc_gesture_enable_all[]
        tomtomMap.updateGesturesDetectionSettings(
                GesturesDetectionSettingsBuilder.create()
                        .rotationEnabled(true)
                        .panningEnabled(true)
                        .zoomEnabled(true)
                        .tiltEnabled(true)
                        .build()
        );
        //end::doc_gesture_enable_all[]


        //tag::doc_gesture_enable_all_default[]
        tomtomMap.updateGesturesDetectionSettings(
                GesturesDetectionSettings.createDefault()
        );
        //end::doc_gesture_enable_all_default[]

    }


}