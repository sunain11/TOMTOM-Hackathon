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

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class TrafficLayersFragment extends ExampleFragment<TrafficLayersPresenter> {

    @Override
    protected TrafficLayersPresenter createPresenter() {
        return new TrafficLayersPresenter();
    }

    @Override
    protected void onOptionsButtonsView(final OptionsButtonsView view) {
        view.setSelectMode(OptionsButtonsView.SelectMode.MULTI);
        view.addOption(getString(R.string.traffic_incidents));
        view.addOption(getString(R.string.traffic_flow));
        view.addOption(getString(R.string.traffic_off_btn));
        optionsView.selectItem(2, true);
    }

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {
        if(!oldValues[2] && newValues[2]) {
                optionsView.selectItem(0, false);
                optionsView.selectItem(1, false);
                optionsView.selectItem(2, true);
                presenter.hideTrafficInformation();
            } else if(newValues[0] && newValues[1]) {
                optionsView.selectItem(2, false);
                presenter.showTrafficFlowAndIncidentsTiles();
            } else if(newValues[0]) {
                optionsView.selectItem(2, false);
                presenter.showTrafficIncidents();
            } else if(newValues[1]) {
                optionsView.selectItem(2, false);
                presenter.showTrafficFlowTiles();
            } else {
                presenter.hideTrafficInformation();
                optionsView.selectItem(2, true);
        }
    }

    @Override
    public boolean isMapRestored() {

        final boolean[] previousState = new boolean[] {
                optionsView.isSelected(0),
                optionsView.isSelected(1),
                optionsView.isSelected(2)
        };
        onChange(previousState, previousState);
        return super.isMapRestored();
    }

}
