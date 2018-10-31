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
package com.tomtom.online.sdk.samples.cases.map.layers.styles;

import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class CustomMapStyleFragment extends ExampleFragment<CustomMapStylePresenter> {


    public static final int FIRST_SELECTED_INDEX = 0;

    @Override
    protected CustomMapStylePresenter createPresenter() {
        return new CustomMapStylePresenter();
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.map_style_base);
        view.addOption(R.string.map_style_night);
        view.selectItem(FIRST_SELECTED_INDEX, true);
    }

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {
        optionsView.setEnabled(false);
        if (newValues[0]) {
            presenter.showBaseStyle();
        } else if (newValues[1]) {
            presenter.showNightStyle();
        }
        optionsView.setEnabled(true);
    }

}
