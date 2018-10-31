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
package com.tomtom.online.sdk.samples.cases.map.traffic.incident.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.map.traffic.incident.model.TrafficIncidentItem;
import com.tomtom.online.sdk.samples.utils.formatter.DistanceFormatter;
import com.tomtom.online.sdk.samples.utils.formatter.TimeLengthFormatter;

import static com.tomtom.online.sdk.samples.utils.formatter.TimeLengthFormatter.NO_TIME_AVAILABLE_DEFAULT_SYMBOL;

class TrafficIncidentViewHolder extends RecyclerView.ViewHolder {

    private final TrafficIncidentView trafficIncidentView;
    private final TextView trafficDescription;
    private final TextView trafficDelay;
    private final TextView trafficLength;

    TrafficIncidentViewHolder(View itemView) {
        super(itemView);
        trafficIncidentView = itemView.findViewById(R.id.trafficIncidentView);
        trafficDescription = itemView.findViewById(R.id.trafficIncidentDescription);
        trafficDelay = itemView.findViewById(R.id.trafficIncidentDelay);
        trafficLength = itemView.findViewById(R.id.trafficIncidentLength);
    }

    void fillViewWithData(TrafficIncidentItem item) {
        trafficIncidentView.setTrafficIncidentIcon(item.getDrawable());
        if (item.isCluster()) {
            trafficIncidentView.setNumberInsideTrafficIcon(item.getNumberOfIncidentsInCluster());
        }
        trafficDescription.setText(item.getDescription());
        trafficDelay.setText(item.getDelay() != 0 ? TimeLengthFormatter.formatFromSecondsToMinutes(item.getDelay()) : NO_TIME_AVAILABLE_DEFAULT_SYMBOL);
        trafficLength.setText(DistanceFormatter.format(item.getLength()));
    }
}
