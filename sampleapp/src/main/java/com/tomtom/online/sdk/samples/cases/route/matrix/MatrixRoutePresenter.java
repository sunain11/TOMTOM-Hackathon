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
package com.tomtom.online.sdk.samples.cases.route.matrix;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.common.rx.RxContext;
import com.tomtom.online.sdk.common.util.Contextable;
import com.tomtom.online.sdk.map.BaseMarkerBalloon;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.Polyline;
import com.tomtom.online.sdk.map.PolylineBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.routing.OnlineRoutingApi;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.routing.data.Summary;
import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingQuery;
import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingQueryBuilder;
import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingResponse;
import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingResult;
import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingResultKey;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.route.matrix.data.AmsterdamPoi;
import com.tomtom.online.sdk.samples.utils.DimensionUtils;
import com.tomtom.online.sdk.samples.utils.Locations;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.tomtom.online.sdk.map.MapConstants.ORIENTATION_SOUTH;

public class MatrixRoutePresenter implements LifecycleObserver, RxContext {

    private final static int DEFAULT_CURRENT_LOCATION_BUTTON_BOTTOM_MARGIN = 40;
    private final static double DEFAULT_ZOOM_LEVEL_FOR_EXAMPLE = 12.0;
    private final static int NUMBER_OF_NETWORKING_THREADS = 4;
    private final static int DEFAULT_MAP_PADDING = 0;

    private final MatrixRouteFunctionalExample model;
    private final MatrixRoutesTableViewModel routesTableViewModel;
    private final Context context;
    private final MatrixRouteView view;

    private RoutingApi routePlannerAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Scheduler networkScheduler = Schedulers.from(Executors.newFixedThreadPool(NUMBER_OF_NETWORKING_THREADS));


    MatrixRoutePresenter(final MatrixRouteView view) {

        if (view == null) {
            throw new IllegalArgumentException("Fragment can't be null.");
        }

        this.view = view;
        model = new MatrixRouteFunctionalExample();
        routePlannerAPI = OnlineRoutingApi.create(view.getContext());
        context = view.getContext();

        routesTableViewModel = ViewModelProviders.of(view.getFragment()).get(MatrixRoutesTableViewModel.class);
        routesTableViewModel.getLastMatrixRoutingResponse().observe(view.getFragment(), new Observer<MatrixRoutingResponse>() {
            @Override
            public void onChanged(final MatrixRoutingResponse matrixRoutingResponse) {

                view.runOnTomtomMap(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull TomtomMap tomtomMap) {
                        proceedWithResponse(matrixRoutingResponse);
                    }
                });
            }
        });
    }

    public FunctionalExampleModel getModel() {
        return model;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        view.runOnTomtomMap(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull TomtomMap tomtomMap) {
                confCurrentLocationButton(tomtomMap);
                tomtomMap.centerOn(
                        Locations.AMSTERDAM_CENTER_LOCATION.getLatitude(),
                        Locations.AMSTERDAM_CENTER_LOCATION.getLongitude(),
                        DEFAULT_ZOOM_LEVEL_FOR_EXAMPLE, ORIENTATION_SOUTH);
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        compositeDisposable.clear();
    }

    @Override
    public Scheduler getWorkingScheduler() {
        return networkScheduler;
    }

    @Override
    public Scheduler getResultScheduler() {
        return AndroidSchedulers.mainThread();
    }

    void planMatrixOfRoutesFromAmsterdamCenterToRestaurants() {
        cleanup();

        List<LatLng> origins = provideLocationsListForPois(AmsterdamPoi.CITY_AMSTERDAM);
        List<LatLng> destinations = provideLocationsListForPois(
                AmsterdamPoi.RESTAURANT_BRIDGES,
                AmsterdamPoi.RESTAURANT_GREETJE,
                AmsterdamPoi.RESTAURANT_LA_RIVE,
                AmsterdamPoi.RESTAURANT_WAGAMAMA,
                AmsterdamPoi.RESTAURANT_ENVY);

        MatrixRoutingQuery query = MatrixRoutingQueryBuilder.create(origins, destinations).build();
        changeTableHeadersToOneToMany();
        proceedWithQuery(query);
    }

    private void changeTableHeadersToOneToMany() {
        view.changeTableHeadersToOneToMany();
    }

    void planMatrixOfRoutesFromPassengersToTaxi() {
        cleanup();

        List<LatLng> origins = provideLocationsListForPois(AmsterdamPoi.PASSENGER_ONE, AmsterdamPoi.PASSENGER_TWO);
        List<LatLng> destinations = provideLocationsListForPois(AmsterdamPoi.TAXI_ONE, AmsterdamPoi.TAXI_TWO);

        //tag::doc_matrix_query[]
        MatrixRoutingQuery query = MatrixRoutingQueryBuilder.create(origins, destinations).build();
        //end::doc_matrix_query[]

        changeTableHeadersToManyToMany();
        proceedWithQuery(query);
    }

    private void changeTableHeadersToManyToMany() {
        view.changeTableHeadersToManyToMany();
    }

    private void proceedWithQuery(MatrixRoutingQuery query) {
        //tag::doc_execute_matrix_routing[]
        Disposable subscribe = routePlannerAPI.planMatrixRoutes(query)
                //end::doc_execute_matrix_routing[]
                .subscribeOn(getWorkingScheduler())
                .observeOn(getResultScheduler())
                .subscribe(new Consumer<MatrixRoutingResponse>() {
                    @Override
                    public void accept(MatrixRoutingResponse matrixRoutingResponse) {
                        routesTableViewModel.saveMatrixRoutingResponse(matrixRoutingResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Timber.e(throwable, throwable.getMessage());
                        Toast.makeText(context, R.string.matrix_routing_error_message, Toast.LENGTH_LONG).show();
                    }
                });

        compositeDisposable.add(subscribe);
    }

    private void proceedWithResponse(MatrixRoutingResponse matrixRoutingResponse) {
        view.updateMatrixRoutesList(matrixRoutingResponse);
        markPoiOnMap(matrixRoutingResponse);
        setupMapAfterResponse();
    }

    private void setupMapAfterResponse() {
        view.runOnTomtomMap(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull TomtomMap tomtomMap) {
                confMapPadding(tomtomMap);
                tomtomMap.getMarkerSettings().zoomToAllMarkers();
            }
        });
    }


    @NonNull
    private List<LatLng> provideLocationsListForPois(AmsterdamPoi... pois) {
        final List<LatLng> locations = new ArrayList<>();

        for (AmsterdamPoi poi : pois) {
            locations.add(poi.getLocation());
        }

        return locations;
    }

    private void markPoiOnMap(MatrixRoutingResponse matrixRoutingResponse) {

        final Set<MatrixRoutingResultKey> matrixRoutingKeys = matrixRoutingResponse.getResults().keySet();

        for (MatrixRoutingResultKey matrixRoutingResultKey : matrixRoutingKeys) {
            if (matrixRoutingResponse.getResults().get(matrixRoutingResultKey) == null) {
                //Route to originPoi was not found.
                continue;
            }

            final AmsterdamPoi originPoi = AmsterdamPoi.parsePoiByLocation(matrixRoutingResultKey.getOrigin());
            final AmsterdamPoi destinationPoi = AmsterdamPoi.parsePoiByLocation(matrixRoutingResultKey.getDestination());

            displayPoi(matrixRoutingResponse, matrixRoutingResultKey, originPoi, destinationPoi);
        }

    }

    private void displayPoi(MatrixRoutingResponse matrixRoutingResponse, MatrixRoutingResultKey matrixRoutingResultKey, AmsterdamPoi originPoi, AmsterdamPoi destinationPoi) {
        if (originPoi == null || destinationPoi == null) {
            //Route to poi was not found.
            return;
        }

        addMarkerForPoi(originPoi, Icon.Factory.fromResources(context, originPoi.getIconResId()));
        addMarkerForPoi(destinationPoi, Icon.Factory.fromResources(context, destinationPoi.getIconResId()));
        addPolylineFromOriginToPoi(matrixRoutingResponse, originPoi, destinationPoi, matrixRoutingResultKey);
    }

    private void addPolylineFromOriginToPoi(MatrixRoutingResponse matrixRoutingResponse, AmsterdamPoi originPoi, AmsterdamPoi destinationPoi, MatrixRoutingResultKey matrixRoutingResultKey) {
        final List<LatLng> originDestinationLine = new ArrayList<>();
        originDestinationLine.add(originPoi.getLocation());
        originDestinationLine.add(destinationPoi.getLocation());

        final Polyline polyline = PolylineBuilder.create()
                .coordinates(originDestinationLine)
                .color(determineColor(matrixRoutingResultKey, matrixRoutingResponse))
                .build();

        view.runOnTomtomMap(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull TomtomMap tomtomMap) {
                tomtomMap.getOverlaySettings().addOverlay(polyline);
            }
        });
    }

    private void addMarkerForPoi(AmsterdamPoi poi, Icon icon) {
        final String text = AmsterdamPoi.getNameWithPrefix(context, poi.getLocation());
        final BaseMarkerBalloon balloon = new SimpleMarkerBalloon(text);
        final MarkerBuilder markerBuilder = new MarkerBuilder(poi.getLocation());

        markerBuilder.markerBalloon(balloon);

        if (icon != null) {
            markerBuilder.icon(icon);
        }

        view.runOnTomtomMap(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull TomtomMap tomtomMap) {
                tomtomMap.getMarkerSettings().addMarker(markerBuilder);
            }
        });
    }

    void cleanup() {
        view.runOnTomtomMap(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull TomtomMap tomtomMap) {
                tomtomMap.getOverlaySettings().removeOverlays();
                tomtomMap.removeMarkers();
                tomtomMap.setPadding(DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING);
            }
        });
        view.hideMatrixRoutesTable();
    }

    private int determineColor(MatrixRoutingResultKey matrixRoutingResultKey, MatrixRoutingResponse matrixRoutingResponse) {

        Summary summary = matrixRoutingResponse.getResults().get(matrixRoutingResultKey).getSummary();
        if (summary == null){
            return Color.GRAY;
        }
        DateTime currentRouteEta = new DateTime(summary.getArrivalTime());
        DateTime minEta = currentRouteEta;

        for (MatrixRoutingResultKey key : matrixRoutingResponse.getResults().keySet()) {
            if (key.getOrigin().equals(matrixRoutingResultKey.getOrigin())) {
                final MatrixRoutingResult result = matrixRoutingResponse.getResults().get(key);
                if (result.getSummary() != null) {
                    final DateTime arrivalTime = new DateTime(result.getSummary().getArrivalTime());
                    if (arrivalTime.isBefore(minEta)) {
                        minEta = arrivalTime;
                    }
                }
            }
        }

        return currentRouteEta == minEta ? Color.GREEN : Color.GRAY;
    }

    private void confMapPadding(TomtomMap tomtomMap) {
        int offsetTop = context.getResources().getDimensionPixelSize(R.dimen.matrix_routing_box_height);
        int offsetBottom = context.getResources().getDimensionPixelSize(R.dimen.control_top_panel_height);
        int offsetDefault = context.getResources().getDimensionPixelSize(R.dimen.offset_big);

        tomtomMap.setPadding(offsetTop, offsetDefault, offsetBottom, offsetDefault);
    }

    private void confCurrentLocationButton(TomtomMap tomtomMap) {
        int currentLocationButtonBottomMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.current_location_default_margin_bottom);
        int currentLocationButtonLeftMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.compass_default_margin_start);

        tomtomMap.getUiSettings().getCurrentLocationView()
                .setMargins(currentLocationButtonLeftMargin, 0, 0,
                        currentLocationButtonBottomMargin + getCurrentLocationBottomMarginDelta());
    }

    private int getCurrentLocationBottomMarginDelta() {
        return DimensionUtils.fromDpToPx(DEFAULT_CURRENT_LOCATION_BUTTON_BOTTOM_MARGIN,
                context.getResources().getDisplayMetrics());
    }


    interface MatrixRouteView extends Contextable {

        void updateMatrixRoutesList(MatrixRoutingResponse matrixRoutingResponse);

        void changeTableHeadersToOneToMany();

        void changeTableHeadersToManyToMany();

        void hideMatrixRoutesTable();

        Fragment getFragment();

        void runOnTomtomMap(final OnMapReadyCallback callback);
    }
}
