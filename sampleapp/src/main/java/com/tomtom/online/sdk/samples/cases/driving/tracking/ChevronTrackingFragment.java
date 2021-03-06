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

package com.tomtom.online.sdk.samples.cases.driving.tracking;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class ChevronTrackingFragment extends ExampleFragment<ChevronTrackingPresenter> {

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {
        if (newValues[0]) {
            presenter.startTracking();
        } else if (newValues[1]) {
            presenter.stopTracking();
        }
        else if(newValues[2])
        {
            presenter.accident_prone();
        }
    }

    @Override
    protected ChevronTrackingPresenter createPresenter() {
        return new ChevronTrackingPresenter();
    }

    @Override
    protected void onOptionsButtonsView(final OptionsButtonsView view) {

        view.addOption(R.string.chevron_tracking_start);
        view.addOption(R.string.chevron_tracking_stop);
        view.addOption("Met an accident");

        optionsView.selectItem(1, true);
    }

}
