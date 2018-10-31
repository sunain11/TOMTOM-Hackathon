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
package com.tomtom.online.sdk.samples.cases.map.langparam;

import android.content.Context;

import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;

import java.util.Locale;

public class MapLanguageParamPresenter extends BaseFunctionalExamplePresenter {

    final static int DEFAULT_MAP_ZOOM_LEVEL_FOR_EXAMPLE = 3;
    final static String BRITISH_ENGLISH_CODE = "en-GB";
    final static String RUSSIAN_CODE = "ru-RU";
    final static String DUTCH_CODE = "nl-NL";
    final static String NGT_CODE = "NGT";


    @Override
    public void bind(final FunctionalExampleFragment view, final TomtomMap map) {
        super.bind(view, map);
        if (!view.isMapRestored()) {
            centerMapOnLocation();
            english();
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new MapLanguageParamFunctionalExample();
    }

    private void centerMapOnLocation() {
        tomtomMap.centerOn(
                Locations.LODZ_LOCATION.getLatitude(),
                Locations.LODZ_LOCATION.getLongitude(),
                DEFAULT_MAP_ZOOM_LEVEL_FOR_EXAMPLE,
                MapConstants.ORIENTATION_NORTH
        );
    }

    @Override
    public void cleanup() {
        tomtomMap.setLanguage(Locale.getDefault().getLanguage());
    }

    public void english() {
        //tag::doc_map_language[]
        //BRITISH_ENGLISH_CODE = "en-GB"
        tomtomMap.setLanguage(BRITISH_ENGLISH_CODE);
        //end::doc_map_language[]
    }

    public void russian() {
        tomtomMap.setLanguage(RUSSIAN_CODE);
    }

    public void dutch() {
        tomtomMap.setLanguage(DUTCH_CODE);
    }

}
