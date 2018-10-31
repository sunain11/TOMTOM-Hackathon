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

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tomtom.online.sdk.samples.R;

public class TrafficIncidentsListHeader extends LinearLayout {

    private TextView trafficIconDesc;
    private TextView trafficDescription;
    private TextView trafficDelay;
    private TextView trafficLength;

    public TrafficIncidentsListHeader(Context context) {
        super(context);
        init();
    }

    public TrafficIncidentsListHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrafficIncidentsListHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.header_traffic_incident_list, this);
        trafficIconDesc = findViewById(R.id.trafficIncidentIconDesc);
        trafficDescription = findViewById(R.id.trafficIncidentDescription);
        trafficDelay = findViewById(R.id.trafficIncidentDelay);
        trafficLength = findViewById(R.id.trafficIncidentLength);
        setupHeaderLabels();
    }

    private void setupHeaderLabels() {
        trafficIconDesc.setText(R.string.traffic_incident_list_header_category);
        trafficDescription.setText(R.string.traffic_incident_list_header_desc);
        trafficDelay.setText(R.string.traffic_incident_list_header_delay);
        trafficLength.setText(R.string.traffic_incident_list_header_length);
    }

}
