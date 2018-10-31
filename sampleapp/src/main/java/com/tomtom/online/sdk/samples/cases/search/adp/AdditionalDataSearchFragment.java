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
package com.tomtom.online.sdk.samples.cases.search.adp;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class AdditionalDataSearchFragment extends ExampleFragment<AdditionalDataSearchPresenter> {

    @Override
    protected AdditionalDataSearchPresenter createPresenter() {
        return new AdditionalDataSearchPresenter();
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.btn_text_amsterdam);
        view.addOption(R.string.btn_text_berlin);
        view.addOption(R.string.btn_text_poland);
    }

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {
        if (newValues[0]) {
            presenter.performSearch("Amsterdam");
        } else if (newValues[1]) {
            presenter.performSearch("Berlin");
        } else if (newValues[2]) {
            presenter.performSearch("Poland");
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
