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

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

import timber.log.Timber;

public class MapUiExtensionsFragment extends ExampleFragment<MapUiExtensionsPresenter> {

    private static final int NUMBER_OF_ACTIONS = 3;

    @Override
    protected MapUiExtensionsPresenter createPresenter() {
        return new MapUiExtensionsPresenter();
    }

    @Override
    protected void onOptionsButtonsView(final OptionsButtonsView view) {
        view.addOption(R.string.map_ui_extensions_default);
        view.addOption(R.string.map_ui_extensions_custom);
        view.addOption(R.string.map_ui_extensions_hide);
        optionsView.selectItem(0, true);
    }

    @Override
    public boolean isMapRestored() {

        Timber.d("isMapRestored()");

        final boolean[] previousState = new boolean[] {
                optionsView.isSelected(0),
                optionsView.isSelected(1),
                optionsView.isSelected(2)
        };

        return executeActions(previousState);
    }

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {
        Timber.d("onChange()");
        executeActions(newValues);
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
        Timber.d("executeActionForSelection() %d", selectionId);

        switch (selectionId) {
            case 0:
                presenter.show();
                presenter.defaultMapComponentIcons();
                break;
            case 1:
                presenter.show();
                presenter.customMapComponentIcons();
                break;
            case 2:
                presenter.hide();
                presenter.defaultMapComponentIcons();
                break;
        }
    }

}
