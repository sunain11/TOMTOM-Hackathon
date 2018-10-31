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
package com.tomtom.online.sdk.samples.cases.route.reachablerange;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class ReachableRangeFragment extends ExampleFragment<ReachableRangePresenter> {

    @Override
    protected ReachableRangePresenter createPresenter() {
        return new ReachableRangePresenter();
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.btn_text_combustion);
        view.addOption(R.string.btn_text_electric);
        view.addOption(R.string.btn_two_hours);
    }

    @Override
    public void onChange(final boolean[] oldValues, final boolean[] newValues) {
        presenter.cleanup();
        presenter.confMapCenter();
        presenter.confMapPadding();
        if (newValues[0]) {
            presenter.startReachableRangeCalculationForFuel();
        } else if (newValues[1]) {
            presenter.startReachableRangeCalculationForElectric();
        } else if (newValues[2]) {
            presenter.startReachableRangeCalculationFor2HLimit();
        }
    }
}
