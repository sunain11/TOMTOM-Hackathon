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
package com.tomtom.online.sdk.samples.cases.map.traffic.incident;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.tomtom.online.sdk.common.location.BoundingBox;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.map.traffic.incident.model.TrafficIncidentItem;
import com.tomtom.online.sdk.traffic.OnlineTrafficApi;
import com.tomtom.online.sdk.traffic.TrafficApi;
import com.tomtom.online.sdk.traffic.api.incident.details.IncidentDetailsResultListener;
import com.tomtom.online.sdk.traffic.api.incident.icons.TrafficIncidentIconProvider;
import com.tomtom.online.sdk.traffic.incidents.query.IncidentDetailsQuery;
import com.tomtom.online.sdk.traffic.incidents.query.IncidentDetailsQueryBuilder;
import com.tomtom.online.sdk.traffic.incidents.query.IncidentStyle;
import com.tomtom.online.sdk.traffic.incidents.response.BaseTrafficIncident;
import com.tomtom.online.sdk.traffic.incidents.response.IncidentDetailsResponse;
import com.tomtom.online.sdk.traffic.incidents.response.TrafficIncident;
import com.tomtom.online.sdk.traffic.incidents.response.TrafficIncidentCluster;
import com.tomtom.online.sdk.traffic.incidents.response.TrafficIncidentVisitor;

import java.util.ArrayList;
import java.util.List;

public class TrafficIncidentListPresenter implements LifecycleObserver {

    private static final BoundingBox LONDON_BOUNDING_BOX = new BoundingBox(new LatLng(51.544300, -0.176267), new LatLng(51.465582, -0.071777));
    private static final int DEFAULT_ZOOM_LEVEL_FOR_EXAMPLE = 12;
    private static final String INCIDENT_DESCRIPTION_FORMAT = "%s / %s";

    private final TrafficApi trafficApi;
    private final TrafficIncidentListFunctionalExample model;
    private final TrafficIncidentListFragment view;

    public TrafficIncidentListPresenter(TrafficIncidentListFragment view) {
        this.view = view;
        model = new TrafficIncidentListFunctionalExample();
        //tag::doc_traffic_init_api[]
        trafficApi = OnlineTrafficApi.create(view.getContext());
        //end::doc_traffic_init_api[]
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        //tag::doc_traffic_query[]
        IncidentDetailsQuery query = IncidentDetailsQueryBuilder.create(IncidentStyle.S1, LONDON_BOUNDING_BOX, DEFAULT_ZOOM_LEVEL_FOR_EXAMPLE, "-1")
                .withExpandCluster(true).build();
        trafficApi.findIncidentDetails(query, incidentDetailsResultListener);
        //end::doc_traffic_query[]

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        view.updateTrafficIncidentsList(new ArrayList<TrafficIncidentItem>());
    }

    TrafficIncidentListFunctionalExample getModel() {
        return model;
    }

    //tag::doc_traffic_result_listener[]
    private IncidentDetailsResultListener incidentDetailsResultListener = new IncidentDetailsResultListener() {
        @Override
        public void onTrafficIncidentDetailsResult(IncidentDetailsResponse result) {

            final List<TrafficIncidentItem> items = new ArrayList<>();

            TrafficIncidentVisitor visitor = new TrafficIncidentVisitor() {
                @Override
                public void visit(TrafficIncidentCluster cluster) {
                    proceedWithCluster(cluster, items);
                }

                @Override
                public void visit(TrafficIncident incident) {
                    proceedWithIncident(incident, items);
                }
            };

            for (BaseTrafficIncident incident : result.getIncidents()) {
                incident.accept(visitor);
            }

            view.updateTrafficIncidentsList(items);
        }

        @Override
        public void onTrafficIncidentDetailsError(Throwable error) {
            Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    //end::doc_traffic_result_listener[]
    private void proceedWithIncident(TrafficIncident incident, List<TrafficIncidentItem> items) {
        Context context = view.getContext();

        //tag::doc_traffic_incident_icon_provider[]
        TrafficIncidentIconProvider provider = new TrafficIncidentIconProvider(incident);
        Drawable incidentDrawable = provider.getIcon(context, TrafficIncidentIconProvider.IconSize.LARGE);
        //end::doc_traffic_incident_icon_provider[]

        items.add(new TrafficIncidentItem(incidentDrawable,
                String.format(INCIDENT_DESCRIPTION_FORMAT, incident.getFrom(), incident.getTo()),
                incident.getDelay().or(0),
                incident.getLengthMeters()));
    }

    private void proceedWithCluster(TrafficIncidentCluster cluster, List<TrafficIncidentItem> items) {
        TrafficIncidentIconProvider provider = new TrafficIncidentIconProvider(cluster);
        items.add(new TrafficIncidentItem(provider.getIcon(view.getContext(), TrafficIncidentIconProvider.IconSize.LARGE),
                view.getString(R.string.traffic_incident_cluster_description),
                0,
                cluster.getLengthMeters(), cluster.getIncidents().size(), true));
    }

}
