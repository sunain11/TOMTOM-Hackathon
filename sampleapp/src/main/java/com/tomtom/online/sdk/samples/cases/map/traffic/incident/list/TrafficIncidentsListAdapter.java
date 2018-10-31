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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.map.traffic.incident.model.TrafficIncidentItem;

import java.util.ArrayList;
import java.util.List;

public class TrafficIncidentsListAdapter extends RecyclerView.Adapter<TrafficIncidentViewHolder> {

    private List<TrafficIncidentItem> trafficIncidentItems = new ArrayList<>();

    @NonNull
    @Override
    public TrafficIncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_traffic_incident_detail, parent, false);
        return new TrafficIncidentViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TrafficIncidentViewHolder holder, int position) {
        TrafficIncidentItem item = trafficIncidentItems.get(position);
        holder.fillViewWithData(item);
    }

    public void updateIncidentsList(List<TrafficIncidentItem> items) {
        trafficIncidentItems.addAll(items);
        notifyDataSetChanged();
    }

    public void clearIncidentsList() {
        trafficIncidentItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return trafficIncidentItems.size();
    }
}
