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
package com.tomtom.online.sdk.samples.cases.map.layers.tilestypes;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class MapTilesTypesFragment extends ExampleFragment<MapTilesTypesPresenter> {


    public static final int FIRST_INDEX = 0;

    @Override
    protected MapTilesTypesPresenter createPresenter() {
        return new MapTilesTypesPresenter();
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.map_types_vector);
        view.addOption(R.string.map_types_raster);
        view.selectItem(FIRST_INDEX, true);
    }

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {
        optionsView.setEnabled(false);
        if (newValues[0]) {
            presenter.showOnlyVectorTiles();
        } else if (newValues[1]) {
            presenter.showOnlyRasterTiles();
        }
        optionsView.setEnabled(true);
    }

}
