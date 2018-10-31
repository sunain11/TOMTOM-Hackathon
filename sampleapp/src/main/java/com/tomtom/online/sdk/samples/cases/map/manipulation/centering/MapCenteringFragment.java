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
package com.tomtom.online.sdk.samples.cases.map.manipulation.centering;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class MapCenteringFragment extends ExampleFragment<MapCenteringPresenter> {

    private final int NUMBER_OF_ACTIONS = 3;

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {
        executeActions(newValues);
    }

    @Override
    protected MapCenteringPresenter createPresenter() {
        return new MapCenteringPresenter();
    }

    @Override
    protected void onOptionsButtonsView(final OptionsButtonsView view) {
        view.addOption(R.string.map_center_amsterdam);
        view.addOption(R.string.map_center_berlin);
        view.addOption(R.string.map_center_london);

        optionsView.selectItem(0, true);
    }

    @Override
    public boolean isMapRestored() {

        final boolean[] previousState = new boolean[] {
                optionsView.isSelected(0),
                optionsView.isSelected(1),
                optionsView.isSelected(2)
        };

       return executeActions(previousState);
    }

    private boolean executeActions(boolean[] newValues) {

        for(int i = 0; i < NUMBER_OF_ACTIONS; i++) {
            if (newValues[i]) {
                executeActionForSelection(i);
                return true;
            }
        }
        return false;
    }

    private void executeActionForSelection(int selectionId) {
        switch (selectionId) {
            case 0:
                presenter.centerOnAmsterdam();
                break;
            case 1:
                presenter.centerOnBerlin();
                break;
            case 2:
                presenter.centerOnLondon();
                break;
        }
    }

}
