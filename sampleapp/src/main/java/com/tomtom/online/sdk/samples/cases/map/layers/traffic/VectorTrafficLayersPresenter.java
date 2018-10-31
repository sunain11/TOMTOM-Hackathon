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
package com.tomtom.online.sdk.samples.cases.map.layers.traffic;

import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TrafficFlowType;
import com.tomtom.online.sdk.map.TrafficFlowType.VectorTrafficFlowType;
import com.tomtom.online.sdk.map.model.MapTilesType;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

public class VectorTrafficLayersPresenter extends BaseFunctionalExamplePresenter {

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        tomtomMap.getUiSettings().setMapTilesType(MapTilesType.VECTOR);
        if (!view.isMapRestored()) {
            centerOnLondon();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new VectorTrafficLayersFunctionalExample();
    }

    @Override
    public void cleanup() {
        hideTrafficInformation();
        tomtomMap.zoomTo(DEFAULT_ZOOM_LEVEL);
    }


    public void showTrafficFlowTiles() {
        tomtomMap.getUiSettings().turnOffTraffic();
        //tag::doc_traffic_vector_flow_on[]
        tomtomMap.getUiSettings().turnOnVectorTrafficFlowTiles();
        //end::doc_traffic_vector_flow_on[]
    }

    @SuppressWarnings("unused")
    public void exampleOfUsingTrafficStyle(){
        //tag::doc_traffic_flow_styles[]
        tomtomMap.getUiSettings().turnOnVectorTrafficFlowTiles(new TrafficFlowType.RelativeTrafficFlowStyle()); //default
        tomtomMap.getUiSettings().turnOnVectorTrafficFlowTiles(new TrafficFlowType.AbsoluteTrafficFlowStyle());
        tomtomMap.getUiSettings().turnOnVectorTrafficFlowTiles(new TrafficFlowType.RelativeDelayTrafficFlowStyle());
        //end::doc_traffic_flow_styles[]
        //tag::doc_get_style_info[]
        VectorTrafficFlowType style = tomtomMap.getUiSettings().getTrafficVectorFlowStyle();
        //end::doc_get_style_info[]
    }

    public void hideTrafficInformation() {
        //tag::doc_traffic_off[]
        tomtomMap.getUiSettings().turnOffTraffic();
        tomtomMap.getUiSettings().turnOffTrafficFlowTiles();
        //end::doc_traffic_off[]
    }

    public void centerOnLondon() {
        tomtomMap.centerOn(
                Locations.LONDON_LOCATION.getLatitude(),
                Locations.LONDON_LOCATION.getLongitude(),
                DEFAULT_ZOOM_TRAFFIC_LEVEL,
                MapConstants.ORIENTATION_NORTH
        );
    }

}
