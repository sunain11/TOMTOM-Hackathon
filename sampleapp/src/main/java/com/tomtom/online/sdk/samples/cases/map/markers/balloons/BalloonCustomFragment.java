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
package com.tomtom.online.sdk.samples.cases.map.markers.balloons;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class BalloonCustomFragment extends ExampleFragment<BalloonCustomPresenter> {

    @Override
    protected BalloonCustomPresenter createPresenter() {
        return new BalloonCustomPresenter();
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.menu_markers_balloon_simple);
        view.addOption(R.string.menu_markers_balloon_custom);
    }

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {
        if(newValues[0]) {
            presenter.createSimpleBalloon();
        } else if(newValues[1]) {
            presenter.createCustomBalloon();
        }
    }

    @Override
    public boolean isMapRestored() {

        final boolean[] previousState = new boolean[] {
                optionsView.isSelected(0),
                optionsView.isSelected(1),
        };
        onChange(previousState, previousState);
        return super.isMapRestored();
    }

}
