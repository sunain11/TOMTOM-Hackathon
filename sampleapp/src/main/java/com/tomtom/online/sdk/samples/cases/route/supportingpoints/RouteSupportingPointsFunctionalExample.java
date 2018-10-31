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
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;

public class RouteSupportingPointsFunctionalExample implements FunctionalExampleModel {

    @Override
    public int getPlayableTitle() {
        return R.string.route_supporting_points;
    }

    @Override
    public int getPlayableSubtitle() {
        return R.string.supporting_points_subtitle;
    }
}
