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
package com.tomtom.online.sdk.samples.cases.route.consumption;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableMap;
import com.tomtom.online.sdk.data.SpeedToConsumptionMap;
import com.tomtom.online.sdk.map.Route;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.routing.data.VehicleEngineType;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.RoutePlannerPresenter;
import com.tomtom.online.sdk.samples.cases.RoutingUiListener;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.routes.AmsterdamToUtrechtRouteConfig;
import com.tomtom.online.sdk.samples.routes.RouteConfigExample;

public class RouteConsumptionModelPresenter extends RoutePlannerPresenter {

    public RouteConsumptionModelPresenter(RoutingUiListener viewModel) {
        super(viewModel);
    }

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        tomtomMap.addOnRouteClickListener(onRouteClickListener);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        tomtomMap.removeOnRouteClickListener(onRouteClickListener);
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new RouteConsumptionModelFunctionalExample();
    }

    public void startRoutingElectric() {
        tomtomMap.clearRoute();
        viewModel.showRoutingInProgressDialog();
        showRoute(getRouteQueryElectric());
    }


    public void startRoutingCombustion() {
        tomtomMap.clearRoute();
        viewModel.showRoutingInProgressDialog();
        showRoute(getRouteQueryCombustion());
    }

    protected RouteQuery getRouteQueryElectric() {
        //tag::doc_consumption_model_electric[]
        RouteQuery queryBuilder = RouteQueryBuilder.create(getRouteConfig().getOrigin(), getRouteConfig().getDestination())
                .withConsiderTraffic(false)
                .withMaxAlternatives(2)
                .withVehicleWeightInKg(1600) //vehicle weight in kilograms
                .withCurrentChargeInKWh(43.0)
                .withMaxChargeInKWh(85.0)
                .withAuxiliaryPowerInKW(1.7)
                .withAccelerationEfficiency(0.33) //e.g. KineticEnergyGained/ChemicalEnergyConsumed
                .withDecelerationEfficiency(0.33) //e.g. ChemicalEnergySaved/KineticEnergyLost
                .withUphillEfficiency(0.33) //e.g. PotentialEnergyGained/ChemicalEnergyConsumed
                .withDownhillEfficiency(0.33) //e.g. ChemicalEnergySaved/PotentialEnergyLost
                .withVehicleEngineType(VehicleEngineType.ELECTRIC)
                .withConstantSpeedConsumptionInKWhPerHundredKm(SpeedToConsumptionMap.create(ImmutableMap.<Integer, Double>builder()
                        //vehicle specific consumption model <speed, consumption in kWh>
                        .put(10, 5.0)
                        .put(30, 10.0)
                        .put(50, 15.0)
                        .put(70, 20.0)
                        .put(90, 25.0)
                        .put(120, 30.0)
                        .build())
                ).build();
        //end::doc_consumption_model_electric[]
        return queryBuilder;
    }

    protected RouteQuery getRouteQueryCombustion() {
        //tag::doc_consumption_model_combustion[]
        RouteQuery queryBuilder = RouteQueryBuilder.create(getRouteConfig().getOrigin(), getRouteConfig().getDestination())
                .withConsiderTraffic(false)
                .withMaxAlternatives(2)
                .withVehicleWeightInKg(1600) //vehicle weight in kilograms
                .withCurrentFuelInLiters(50.0)
                .withAuxiliaryPowerInLitersPerHour(0.2)
                .withFuelEnergyDensityInMJoulesPerLiter(34.2)
                .withAccelerationEfficiency(0.33) //e.g. KineticEnergyGained/ChemicalEnergyConsumed
                .withDecelerationEfficiency(0.33) //e.g. ChemicalEnergySaved/KineticEnergyLost
                .withUphillEfficiency(0.33) //e.g. PotentialEnergyGained/ChemicalEnergyConsumed
                .withDownhillEfficiency(0.33) //e.g. ChemicalEnergySaved/PotentialEnergyLost
                .withVehicleEngineType(VehicleEngineType.COMBUSTION)
                .withConstantSpeedConsumptionInLitersPerHundredKm(SpeedToConsumptionMap.create(ImmutableMap.<Integer, Double>builder()
                        //vehicle specific consumption model <speed, consumption in liters>
                        .put(10, 6.5)
                        .put(30, 7.0)
                        .put(50, 8.0)
                        .put(70, 8.4)
                        .put(90, 7.7)
                        .put(120, 7.5)
                        .put(150, 9.0)
                        .build())
                ).build();
        //end::doc_consumption_model_combustion[]
        return queryBuilder;
    }

    @Override
    public RouteConfigExample getRouteConfig() {
        return new AmsterdamToUtrechtRouteConfig();
    }

    private TomtomMapCallback.OnRouteClickListener onRouteClickListener = new TomtomMapCallback.OnRouteClickListener() {
        @Override
        public void onRouteClick(@NonNull Route route) {
            long routeId = route.getId();
            tomtomMap.getRouteSettings().setRoutesInactive();
            tomtomMap.getRouteSettings().setRouteActive(routeId);
            FullRoute fullRoute = routesMap.get(routeId);
            displayInfoAboutRoute(fullRoute);
        }
    };

}
