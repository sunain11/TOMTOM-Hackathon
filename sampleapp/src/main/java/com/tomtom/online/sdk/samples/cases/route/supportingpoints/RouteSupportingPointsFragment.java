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
package com.tomtom.online.sdk.samples.cases.route.supportingpoints;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.RoutePlannerFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class RouteSupportingPointsFragment extends RoutePlannerFragment<RouteSupportingPointsPresenter> {

    private static final int ZERO_METERS = 0;
    private static final int TEN_KM_IN_METERS = 10000;

    @Override
    protected RouteSupportingPointsPresenter createPresenter() {
        return new RouteSupportingPointsPresenter(this);
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.btn_txt_0_m);
        view.addOption(R.string.btn_txt_10_km);
    }

    @Override
    public void onChange(final boolean[] oldValues, final boolean[] newValues) {
        //Min deviation distance in meters
        if (newValues[0]) {
            presenter.startRouting(ZERO_METERS);
        } else if (newValues[1]) {
            presenter.startRouting(TEN_KM_IN_METERS);
        }
    }

}
