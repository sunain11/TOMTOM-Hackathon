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
package com.tomtom.online.sdk.samples.utils.formatter;

import android.support.annotation.NonNull;

import com.tomtom.online.sdk.common.location.LatLng;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class LatLngFormatter {

    public static final int SCALE = 6;

    @NonNull
    public static String toSimpleString(LatLng latLng) {
        StringBuilder str = new StringBuilder();

        BigDecimal lat = BigDecimal.valueOf(latLng.getLatitude()).setScale(SCALE, RoundingMode.CEILING).stripTrailingZeros();
        BigDecimal lon = BigDecimal.valueOf(latLng.getLongitude()).setScale(SCALE,RoundingMode.CEILING).stripTrailingZeros();
        str.append(lat.toPlainString()).append(',').append(lon.toPlainString());

        return str.toString();
    }
}
