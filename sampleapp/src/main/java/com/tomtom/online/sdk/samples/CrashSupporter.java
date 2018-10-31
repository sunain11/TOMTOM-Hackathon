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
package com.tomtom.online.sdk.samples;

import android.content.Context;

/**
 * The type Crash supporter.
 */
public final class CrashSupporter {

    private CrashSupporter(final Context context) {
        // Intentionally empty constructor
    }

    /**
     * Create crash supporter.
     *
     * @param context the context
     * @return the crash supporter
     */
    public static CrashSupporter create(final Context context) {
        return new CrashSupporter(context);
    }

}