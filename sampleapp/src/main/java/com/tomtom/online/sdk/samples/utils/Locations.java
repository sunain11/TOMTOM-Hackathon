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
package com.tomtom.online.sdk.samples.utils;

import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.common.location.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Locations {


    public final static LatLng BERLIN_LOCATION = new LatLng(52.520007, 13.404954);
    public final static LatLng ROTTERDAM_LOCATION = new LatLng(51.935966, 4.482865);
    public final static LatLng AMSTERDAM_LOCATION = new LatLng(52.377271, 4.909466);
    public final static LatLng AMSTERDAM_CENTER_LOCATION = new LatLng(52.373154,4.890659);
    public final static LatLng OSLO_LOCATION = new LatLng(59.911491, 10.757933);
    public final static LatLng LONDON_LOCATION = new LatLng(51.507351, -0.127758);
    public final static LatLng AMSTERDAM_HENK_SNEEVLIETWEG = new LatLng(52.345971, 4.844899);
    public final static LatLng AMSTERDAM_JAN_VAN_GALENSTRAAT = new LatLng(52.372281, 4.846595);
    public final static LatLng AMSTERDAM_BERLIN_CENTER_LOCATION = new LatLng(52.575119, 9.149708);
    public static final LatLng LODZ_LOCATION = new LatLng(51.759434, 19.449011);
    public static final LatLng AMSTERDAM_HAARLEM = new LatLng(52.381222, 4.637558);
    public static final LatLng UTRECHT_LOCATION = new LatLng(52.09179,5.11457);
    public static final LatLng HOOFDDORP_LOCATION = new LatLng(52.3058782,4.6483191);

    static Random r = new Random(System.currentTimeMillis());

    public static List<LatLng> randomLocation(LatLng center, int number, float distanceInDegree) {
        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            double lat = center.getLatitude() + (r.nextFloat() - 0.5f) * distanceInDegree;
            double lng = center.getLongitude() + (r.nextFloat() - 0.5f) * distanceInDegree;
            latLngs.add(new LatLng(lat, lng));
        }
        return ImmutableList.copyOf(latLngs);
    }

    public static class CenterLocation {

        private double latitudeNorth;
        private double latitudeSouth;
        private double longitudeWest;
        private double longitudeEast;

        public static CenterLocation create(LatLng origin, LatLng destination) {
            return new CenterLocation(origin, destination);
        }

        private CenterLocation(LatLng origin, LatLng destination) {

            latitudeNorth = origin.getLatitude() < destination.getLatitude() ?
                    destination.getLatitude() : origin.getLatitude();

            latitudeSouth = origin.getLatitude() < destination.getLatitude() ?
                    origin.getLatitude() : destination.getLatitude();

            longitudeEast = origin.getLongitude() < destination.getLongitude() ?
                    destination.getLongitude() : origin.getLongitude();

            longitudeWest = origin.getLongitude() < destination.getLongitude() ?
                    origin.getLongitude() : destination.getLongitude();

        }

        public double getLatitudeNorth() {
            return latitudeNorth;
        }

        public double getLatitudeSouth() {
            return latitudeSouth;
        }

        public double getLongitudeWest() {
            return longitudeWest;
        }

        public double getLongitudeEast() {
            return longitudeEast;
        }

    }
}
