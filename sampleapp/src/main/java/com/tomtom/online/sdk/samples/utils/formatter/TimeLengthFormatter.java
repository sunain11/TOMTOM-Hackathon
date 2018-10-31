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

import android.annotation.SuppressLint;

public final class TimeLengthFormatter {

    public static final String NO_TIME_AVAILABLE_DEFAULT_SYMBOL = "---";

    static final int SECONDS_IN_MIN = 60;

    static final int SECONDS_IN_HOUR = SECONDS_IN_MIN * 60;

    static final int SECONDS_IN_DAY = SECONDS_IN_HOUR * 24;

    static final int SECONDS_IN_MONTH = SECONDS_IN_DAY * 30;

    @SuppressLint("DefaultLocale")
    public static String formatFromSecondsToMinutes(int seconds) {
        if (seconds < SECONDS_IN_MIN) {
            return String.format("%d s", seconds);
        } else if (seconds < SECONDS_IN_HOUR) {
            return String.format("%d min %d s", seconds / SECONDS_IN_MIN, (seconds % SECONDS_IN_MIN));
        } else if (seconds < SECONDS_IN_DAY) {
            return String.format("%d h %d min", seconds / SECONDS_IN_HOUR, (seconds % SECONDS_IN_HOUR) / SECONDS_IN_MIN);
        } else if (seconds < SECONDS_IN_MONTH) {
            return String.format("%d days %d h", seconds / SECONDS_IN_DAY, (seconds % SECONDS_IN_DAY) / SECONDS_IN_HOUR);
        }
        return NO_TIME_AVAILABLE_DEFAULT_SYMBOL;
    }

}
