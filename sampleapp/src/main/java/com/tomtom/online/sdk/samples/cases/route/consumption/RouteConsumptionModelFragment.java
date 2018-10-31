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
package com.tomtom.online.sdk.samples.cases.route.consumption;

import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.RoutePlannerFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class RouteConsumptionModelFragment extends RoutePlannerFragment<RouteConsumptionModelPresenter> {

    @Override
    protected RouteConsumptionModelPresenter createPresenter() {
        return new RouteConsumptionModelPresenter(this);
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.btn_text_combustion);
        view.addOption(R.string.btn_text_electric);
    }

    @Override
    public void onChange(final boolean[] oldValues, final boolean[] newValues) {
        if (newValues[0]) {
            presenter.startRoutingCombustion();
        } else if (newValues[1]) {
            presenter.startRoutingElectric();
        }
    }

    @Override
    public void routeUpdated(FullRoute route) {
        super.routeUpdated(route);
        getInfoBarView().hideLeftIcon();
        float consumption = presenter.getConsumption(route);
        if (optionsView.isSelected(0)) {
            getInfoBarView().setLeftText(getString(R.string.combustion_consumption, consumption));
        } else if (optionsView.isSelected(1)) {
            getInfoBarView().setLeftText(getString(R.string.electric_consumption, consumption));
        }
    }
}
