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
package com.tomtom.online.sdk.samples.cases.map.traffic.incident.model;

import android.graphics.drawable.Drawable;

public final class TrafficIncidentItem {

    private static final int DEFAULT_NUMBER_OF_INCIDENTS = -1;

    private final Drawable drawable;

    private final String description;

    private final int delay;

    private final int length;

    private final boolean isCluster;

    private final int numberOfIncidentsInCluster;

    public TrafficIncidentItem(Drawable drawable, String description, int delay, int length) {
        this(drawable, description, delay, length, DEFAULT_NUMBER_OF_INCIDENTS, false);
    }

    public TrafficIncidentItem(Drawable drawable, String description, int delay, int length, int numberOfIncidentsInCluster, boolean isCluster) {
        this.drawable = drawable;
        this.description = description;
        this.delay = delay;
        this.length = length;
        this.isCluster = isCluster;
        this.numberOfIncidentsInCluster = numberOfIncidentsInCluster;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getDescription() {
        return description;
    }

    public int getDelay() {
        return delay;
    }

    public int getLength() {
        return length;
    }

    public boolean isCluster() {
        return isCluster;
    }

    public int getNumberOfIncidentsInCluster() {
        return numberOfIncidentsInCluster;
    }
}
