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
package com.tomtom.online.sdk.samples.cases.route.matrix.data;

import android.content.Context;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.utils.Locations;

public enum AmsterdamPoi {

    CITY_AMSTERDAM(R.string.empty, R.string.amsterdam_city_name, Locations.AMSTERDAM_CENTER_LOCATION, R.drawable.ic_car),

    RESTAURANT_WAGAMAMA(R.string.empty, R.string.restaurant_wagamama, new LatLng(52.362620, 4.883390),R.drawable.ic_favourites),
    RESTAURANT_BRIDGES(R.string.empty, R.string.restaurant_bridges, new LatLng(52.370813, 4.895107),R.drawable.ic_favourites),
    RESTAURANT_GREETJE(R.string.empty, R.string.restaurant_greetje, new LatLng(52.371536, 4.907739),R.drawable.ic_favourites),
    RESTAURANT_LA_RIVE(R.string.empty, R.string.restaurant_la_rive, new LatLng(52.360276, 4.905146),R.drawable.ic_favourites),
    RESTAURANT_ENVY(R.string.empty, R.string.restaurant_envy, new LatLng(52.371450, 4.883201),R.drawable.ic_favourites),

    PASSENGER_ONE(R.string.passenger_ballon_prefix, R.string.pasenger_one, new LatLng(52.379958, 4.856268), R.drawable.ic_walk),
    PASSENGER_TWO(R.string.passenger_ballon_prefix, R.string.passenger_two, new LatLng(52.330002, 4.914814), R.drawable.ic_walk),
    TAXI_ONE(R.string.taxi_ballon_prefix, R.string.taxi_one, new LatLng(52.348068, 4.832925), R.drawable.ic_car),
    TAXI_TWO(R.string.taxi_ballon_prefix, R.string.taxi_two, new LatLng(52.347154, 4.901458), R.drawable.ic_car);

    private int prefixResId;
    public static final String EMPTY_STRING = "";
    private LatLng location;
    private int nameResId;
    private int iconResId;

    AmsterdamPoi(int prefix, int name, LatLng latLng, int icon) {
        location = latLng;
        this.nameResId = name;
        this.prefixResId = prefix;
        this.iconResId = icon;
    }

    public static String getName(Context context, LatLng atLocation) {
        for (AmsterdamPoi amsterdamPoi : values()) {
            if (amsterdamPoi.location.equals(atLocation)) {
                return context.getResources().getString(amsterdamPoi.nameResId);
            }
        }
        return EMPTY_STRING;
    }

    public static String getNameWithPrefix(Context context, LatLng atLocation) {
        for (AmsterdamPoi amsterdamPoi : values()) {
            if (amsterdamPoi.location.equals(atLocation)) {
                return String.format("%s %s",
                        context.getResources().getString(amsterdamPoi.prefixResId),
                        context.getResources().getString(amsterdamPoi.nameResId)).trim();
            }
        }
        return EMPTY_STRING;
    }

    public LatLng getLocation() {
        return location;
    }

    public static AmsterdamPoi parsePoiByLocation(LatLng destination) {
        for (AmsterdamPoi amsterdamPoi : values()) {
            if (amsterdamPoi.location.equals(destination)) {
                return amsterdamPoi;
            }
        }
        return null;
    }

    public int getIconResId() {
        return iconResId;
    }
}
