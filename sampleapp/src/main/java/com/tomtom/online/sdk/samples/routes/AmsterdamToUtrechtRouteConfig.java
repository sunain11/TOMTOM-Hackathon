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
package com.tomtom.online.sdk.samples.routes;

import android.support.annotation.NonNull;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.utils.Locations;

public class AmsterdamToUtrechtRouteConfig implements RouteConfigExample {

    @NonNull
    @Override
    public LatLng getOrigin() {
        return Locations.AMSTERDAM_LOCATION;
    }

    @NonNull
    @Override
    public LatLng getDestination() {
        return Locations.UTRECHT_LOCATION;
    }

    @Override
    public int getDestinationAddress() {
        return R.string.utrecht;
    }

    @Override
    public int getRouteDescription() {
        return R.string.amsterdam_to_utrecht_desc;
    }
}
