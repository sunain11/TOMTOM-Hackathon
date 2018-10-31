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
package com.tomtom.online.sdk.samples.cases.map.manipulation.uiextensions;

import android.widget.ImageView;

import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.ui.arrowbuttons.ArrowButtonsGroup;
import com.tomtom.online.sdk.map.ui.zoom.ZoomButtonsGroup;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

public class MapUiExtensionsPresenter extends BaseFunctionalExamplePresenter {


    @Override
    public void bind(final FunctionalExampleFragment view, final TomtomMap map) {
        super.bind(view, map);
        if (!view.isMapRestored()) {
            centerOnAmsterdam();
            defaultMapComponentIcons();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new MapUiExtensionsFunctionalExample();
    }

    @Override
    public void cleanup() {
        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH);
        defaultMapComponentIcons();
        defaultMapComponentsVisibility();
    }

    private void defaultMapComponentsVisibility() {
        tomtomMap.getUiSettings().getCompassView().show();
        tomtomMap.getUiSettings().getCurrentLocationView().show();
        tomtomMap.getUiSettings().getPanningControlsView().hide();
        tomtomMap.getUiSettings().getZoomingControlsView().hide();
    }

    private void centerOnAmsterdam() {
        tomtomMap.centerOn(
                Locations.AMSTERDAM_LOCATION.getLatitude(),
                Locations.AMSTERDAM_LOCATION.getLongitude(),
                DEFAULT_ZOOM_LEVEL,
                MapConstants.ORIENTATION_NORTH_WEST);
    }

    public void defaultMapComponentIcons() {
        ImageView compassView = tomtomMap.getUiSettings().getCompassView().getView();
        compassView.setImageResource(R.drawable.btn_compass);
        ImageView currentLocationView = tomtomMap.getUiSettings().getCurrentLocationView().getView();
        currentLocationView.setImageResource(R.drawable.btn_current_location);

        //tag::set_default_panning_controls[]
        ArrowButtonsGroup arrowButton = tomtomMap.getUiSettings().getPanningControlsView().getView();
        arrowButton.getArrowDownButton().setImageResource(R.drawable.btn_down);
        arrowButton.getArrowUpButton().setImageResource(R.drawable.btn_up);
        arrowButton.getArrowLeftButton().setImageResource(R.drawable.btn_left);
        arrowButton.getArrowRightButton().setImageResource(R.drawable.btn_right);
        //end::set_default_panning_controls[]

        //tag::set_default_zooming_controls[]
        ZoomButtonsGroup zoomButtons = tomtomMap.getUiSettings().getZoomingControlsView().getView();
        zoomButtons.getZoomInButton().setImageResource(R.drawable.btn_zoom_in);
        zoomButtons.getZoomOutButton().setImageResource(R.drawable.btn_zoom_out);
        //end::set_default_zooming_controls[]
    }

    public void show() {
        centerOnAmsterdam();
        //tag::show_compass[]
        tomtomMap.getUiSettings().getCompassView().show();
        //end::show_compass[]

        //tag::show_current_location[]
        tomtomMap.getUiSettings().getCurrentLocationView().show();
        //end::show_current_location[]

        //tag::show_panning_controls[]
        tomtomMap.getUiSettings().getPanningControlsView().show();
        //end::show_panning_controls[]

        //tag::show_zooming_controls[]
        tomtomMap.getUiSettings().getZoomingControlsView().show();
        //end::show_zooming_controls[]
    }

    public void hide() {
        centerOnAmsterdam();
        //tag::hide_compass[]
        tomtomMap.getUiSettings().getCompassView().hide();
        //end::hide_compass[]

        //tag::hide_current_location[]
        tomtomMap.getUiSettings().getCurrentLocationView().hide();
        //end::hide_current_location[]

        //tag::hide_panning_controls[]
        tomtomMap.getUiSettings().getPanningControlsView().hide();
        //end::hide_panning_controls[]

        //tag::hide_zooming_controls[]
        tomtomMap.getUiSettings().getZoomingControlsView().hide();
        //end::hide_zooming_controls[]
    }

    public void customMapComponentIcons() {
        centerOnAmsterdam();
        ImageView compasView = tomtomMap.getUiSettings().getCompassView().getView();
        compasView
                .setImageResource(R.drawable.ic_custom_compass);

        ImageView currentLocationButton = tomtomMap.getUiSettings().getCurrentLocationView().getView();
        currentLocationButton
                .setImageResource(R.drawable.ic_map_position);

        //tag::set_custom_panning_controls[]
        ArrowButtonsGroup arrowButtons = tomtomMap.getUiSettings().getPanningControlsView().getView();
        arrowButtons.getArrowDownButton().setImageResource(R.drawable.btn_down_custom);
        arrowButtons.getArrowUpButton().setImageResource(R.drawable.btn_up_custom);
        arrowButtons.getArrowLeftButton().setImageResource(R.drawable.btn_left_custom);
        arrowButtons.getArrowRightButton().setImageResource(R.drawable.btn_right_custom);
        //end::set_custom_panning_controls[]

        //tag::set_custom_zooming_controls[]
        ZoomButtonsGroup zoomButtons = tomtomMap.getUiSettings().getZoomingControlsView().getView();
        zoomButtons.getZoomInButton().setImageResource(R.drawable.btn_zoom_in_custom);
        zoomButtons.getZoomOutButton().setImageResource(R.drawable.btn_zoom_out_custom);
        //end::set_custom_zooming_controls[]
    }


}
