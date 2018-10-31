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
package com.tomtom.online.sdk.samples.cases;

import android.support.annotation.IdRes;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.driving.tracking.ChevronTrackingFragment;
import com.tomtom.online.sdk.samples.cases.map.geopoliticalview.MapGeopoliticalViewFragment;
import com.tomtom.online.sdk.samples.cases.map.langparam.MapLanguageParamFragment;
import com.tomtom.online.sdk.samples.cases.map.layers.shapes.ShapesCustomFragment;
import com.tomtom.online.sdk.samples.cases.map.layers.styles.CustomMapStyleFragment;
import com.tomtom.online.sdk.samples.cases.map.layers.tilestypes.MapTilesTypesFragment;
import com.tomtom.online.sdk.samples.cases.map.layers.traffic.TrafficLayersFragment;
import com.tomtom.online.sdk.samples.cases.map.layers.traffic.VectorTrafficLayersFragment;
import com.tomtom.online.sdk.samples.cases.map.manipulation.centering.MapCenteringFragment;
import com.tomtom.online.sdk.samples.cases.map.manipulation.events.MapManipulationEventsFragment;
import com.tomtom.online.sdk.samples.cases.map.manipulation.perspective.MapViewPerspectiveFragment;
import com.tomtom.online.sdk.samples.cases.map.manipulation.uiextensions.MapUiExtensionsFragment;
import com.tomtom.online.sdk.samples.cases.map.markers.MarkerCustomFragment;
import com.tomtom.online.sdk.samples.cases.map.markers.advanced.AdvancedMarkersFragment;
import com.tomtom.online.sdk.samples.cases.map.markers.balloons.BalloonCustomFragment;
import com.tomtom.online.sdk.samples.cases.map.markers.clustering.MarkersClusteringFragment;
import com.tomtom.online.sdk.samples.cases.map.route.RouteCustomizationFragment;
import com.tomtom.online.sdk.samples.cases.map.staticimage.StaticImageFragment;
import com.tomtom.online.sdk.samples.cases.map.traffic.incident.TrafficIncidentListFragment;
import com.tomtom.online.sdk.samples.cases.route.alternatives.RouteAlternativesFragment;
import com.tomtom.online.sdk.samples.cases.route.avoid.RouteAvoidsFragment;
import com.tomtom.online.sdk.samples.cases.route.batch.BatchRouteFragment;
import com.tomtom.online.sdk.samples.cases.route.consumption.RouteConsumptionModelFragment;
import com.tomtom.online.sdk.samples.cases.route.departureandarrivaltime.DepartureAndArrivalTimeFragment;
import com.tomtom.online.sdk.samples.cases.route.maneuvers.ManeuversFragment;
import com.tomtom.online.sdk.samples.cases.route.matrix.MatrixRouteFragment;
import com.tomtom.online.sdk.samples.cases.route.modes.RouteTravelModesFragment;
import com.tomtom.online.sdk.samples.cases.route.reachablerange.ReachableRangeFragment;
import com.tomtom.online.sdk.samples.cases.route.supportingpoints.RouteSupportingPointsFragment;
import com.tomtom.online.sdk.samples.cases.route.types.RouteTypesFragment;
import com.tomtom.online.sdk.samples.cases.route.waypoints.RouteWaypointsFragment;
import com.tomtom.online.sdk.samples.cases.search.CategoriesSearchFragment;
import com.tomtom.online.sdk.samples.cases.search.FuzzySearchFragment;
import com.tomtom.online.sdk.samples.cases.search.LanguageSelectorSearchFragment;
import com.tomtom.online.sdk.samples.cases.search.ReverseGeocodingFragment;
import com.tomtom.online.sdk.samples.cases.search.SearchAlongRouteFragment;
import com.tomtom.online.sdk.samples.cases.search.SearchFragment;
import com.tomtom.online.sdk.samples.cases.search.TypeAheadSearchFragment;
import com.tomtom.online.sdk.samples.cases.search.adp.AdditionalDataSearchFragment;
import com.tomtom.online.sdk.samples.cases.search.batch.BatchSearchFragment;
import com.tomtom.online.sdk.samples.cases.search.entrypoints.EntryPointsSearchFragment;
import com.tomtom.online.sdk.samples.cases.search.geometry.GeometrySearchFragment;
import com.tomtom.online.sdk.samples.fragments.CurrentLocationFragment;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.license.AboutFragment;

public class FunctionalExamplesFactory {

    public FunctionalExampleFragment create(@IdRes int itemId) {

        switch (itemId) {

            case R.id.maneuvers_list:
                return new ManeuversFragment();

            case R.id.route_avoids:
                return new RouteAvoidsFragment();

            case R.id.departure_and_arrival_time:
                return new DepartureAndArrivalTimeFragment();

            case R.id.route_travel_modes:
                return new RouteTravelModesFragment();

            case R.id.route_types:
                return new RouteTypesFragment();

            case R.id.traffic_vector_layer:
                return new VectorTrafficLayersFragment();

            case R.id.traffic_raster_layer:
                return new TrafficLayersFragment();

            case R.id.map_types:
                return new MapTilesTypesFragment();

            case R.id.mapcentering:
                return new MapCenteringFragment();

            case R.id.maplanguageparam:
                return new MapLanguageParamFragment();

            case R.id.mapgeopoliticalview:
                return new MapGeopoliticalViewFragment();

            case R.id.map_custom_style:
                return new CustomMapStyleFragment();

            case R.id.mapmode:
                return new MapViewPerspectiveFragment();

            case R.id.map_events:
                return new MapManipulationEventsFragment();

            case R.id.address_search:
                return new SearchFragment();

            case R.id.category_search:
                return new CategoriesSearchFragment();

            case R.id.language_selector_search:
                return new LanguageSelectorSearchFragment();

            case R.id.markers_custom:
                return new MarkerCustomFragment();

            case R.id.markers_balloons:
                return new BalloonCustomFragment();

            case R.id.custom_shapes:
                return new ShapesCustomFragment();

            case R.id.fuzzy_search:
                return new FuzzySearchFragment();

            case R.id.typeahead_search:
                return new TypeAheadSearchFragment();

            case R.id.reverse_geocoding:
                return new ReverseGeocodingFragment();

            case R.id.map_ui_extensions:
                return new MapUiExtensionsFragment();

            case R.id.license:
                return new AboutFragment();

            case R.id.map_static_image:
                return new StaticImageFragment();

            case R.id.route_waypoints:
                return new RouteWaypointsFragment();

            case R.id.route_alternatives:
                return new RouteAlternativesFragment();

            case R.id.search_along_route:
                return new SearchAlongRouteFragment();

            case R.id.geometry_search:
                return new GeometrySearchFragment();

            case R.id.entry_points:
                return new EntryPointsSearchFragment();

            case R.id.consumption_model:
                return new RouteConsumptionModelFragment();

            case R.id.supporting_points:
                return new RouteSupportingPointsFragment();

            case R.id.route_reachable_range:
                return new ReachableRangeFragment();

            case R.id.markers_clustering:
                return new MarkersClusteringFragment();

            case R.id.additional_data:
                return new AdditionalDataSearchFragment();

            case R.id.traffic_incident:
                return new TrafficIncidentListFragment();

            case R.id.batch_search:
                return new BatchSearchFragment();

            case R.id.batch_routing:
                return new BatchRouteFragment();

            case R.id.multiple_chevrons:
                return new ChevronTrackingFragment();

            case R.id.matrix_routing:
                return new MatrixRouteFragment();

            case R.id.advanced_markers:
                return new AdvancedMarkersFragment();

            case R.id.route_customization:
                return new RouteCustomizationFragment();

            default:
                return new CurrentLocationFragment();
        }
    }

}