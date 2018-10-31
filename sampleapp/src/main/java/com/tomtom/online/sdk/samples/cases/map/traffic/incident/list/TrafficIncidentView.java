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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomtom.online.sdk.samples.R;

public class TrafficIncidentView extends FrameLayout {

    private TextView trafficIconNumber;
    private ImageView trafficIcon;

    public TrafficIncidentView(Context context) {
        super(context);
        init();
    }

    public TrafficIncidentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrafficIncidentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_traffic_incident, this);
        trafficIcon = findViewById(R.id.trafficIncidentIcon);
        trafficIconNumber = findViewById(R.id.trafficIncidentIconNumber);
    }

    @SuppressLint("SetTextI18n")
    public void setNumberInsideTrafficIcon(int numberOfIncidentsInCluster) {
        trafficIconNumber.setText(Integer.toString(numberOfIncidentsInCluster));
    }

    public void setTrafficIncidentIcon(Drawable icon) {
        trafficIcon.setImageDrawable(icon);
    }


}
