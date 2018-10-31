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
package com.tomtom.online.sdk.samples.cases.driving;

import android.location.Location;

import com.tomtom.online.sdk.common.location.LatLng;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

public class RouteSimulator {

    private static final int SIMULATOR_PERIOD_IN_MILLIS = 1000;


    private List<LatLng> routeCoordinates;

    private Timer timer;
    private RouteSimulatorEventListener routeSimulatorEventListener;

    public RouteSimulator(RouteSimulatorEventListener listener) {
        Timber.d("RouteSimulator " + this + " listener " + listener);
        this.routeSimulatorEventListener = listener;
    }

    public void start(List<LatLng> routeCoordinates) {
        this.routeCoordinates = routeCoordinates;
        start(routeCoordinates.get(0));
    }

    public void start(List<LatLng> routeCoordinates, LatLng activePointLocation) {
        this.routeCoordinates = routeCoordinates;
        start(activePointLocation);
    }


    private void start(LatLng activePointLocation) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateChevronTask(routeCoordinates, routeSimulatorEventListener, activePointLocation), 0, SIMULATOR_PERIOD_IN_MILLIS);
    }

    private static class UpdateChevronTask extends TimerTask {
        private final List<LatLng> routeCoordinates;
        private final RouteSimulatorEventListener routeSimulatorEventListener;
        private int routeActivePointIdx = 0;

        public UpdateChevronTask(List<LatLng> routeCoordinates, RouteSimulatorEventListener routeSimulatorEventListener, LatLng activePointLocation) {

            this.routeCoordinates = routeCoordinates;
            this.routeSimulatorEventListener = routeSimulatorEventListener;
            for (LatLng coord : routeCoordinates) {
                if (coord.equals(activePointLocation)) {
                    break;
                }
                routeActivePointIdx++;
            }
        }

        @Override
        public void run() {
            //Never ending loop, start from the beginning when end of route
            if (routeActivePointIdx >= routeCoordinates.size()) {
                routeActivePointIdx = 0;
            }

            //Notify about the current point
            notifyNewRoutePointVisited(routeActivePointIdx);

            //Go to next point with new iteration
            routeActivePointIdx++;
        }

        private void notifyNewRoutePointVisited(int pointIdx) {
            LatLng point = routeCoordinates.get(pointIdx);
            double bearing = 0.0;

            if (pointIdx > 0) {
                LatLng prevPoint = routeCoordinates.get(pointIdx - 1);
                bearing = getBearingInDegrees(prevPoint, point);
                Timber.d("notifyNewRoutePointVisited(): " + this + "  %f", bearing);
            }

            if (routeSimulatorEventListener != null) {
                routeSimulatorEventListener.onNewRoutePointVisited(point, bearing);
            } else {
                Timber.d("RouteSimulator routeSimulatorEventListener == null" + this);
            }
        }

        private double getBearingInDegrees(LatLng from, LatLng to) {
            Location fromLocation = from.toLocation();
            Location toLocation = to.toLocation();
            return fromLocation.bearingTo(toLocation);
        }
    }

    public void stop() {
        if (timer != null) {
            Timber.d("Stop RouteSimulator " + this);
            timer.cancel();
            timer.purge();
        }else{
            Timber.d("Not Stop RouteSimulator " + this);
        }
    }

    public interface RouteSimulatorEventListener {
        void onNewRoutePointVisited(LatLng point, double bearing);
    }
}