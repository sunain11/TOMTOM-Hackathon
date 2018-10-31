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
package com.tomtom.online.sdk.samples.cases.route.matrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingResponse;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.ActionBarModel;
import com.tomtom.online.sdk.samples.cases.map.traffic.incident.helpers.FunctionalExampleFragmentAdapter;

public class MatrixRouteFragment extends FunctionalExampleFragmentAdapter implements View.OnClickListener, MatrixRoutePresenter.MatrixRouteView {

    private MatrixRoutePresenter presenter;
    private RecyclerView matrixRoutesList;
    private RadioButton planMatrixBtn;
    private RadioButton planMatrixTaxiBtn;
    private MatrixRoutesTableListAdapter matrixRoutesListAdapter;
    private MatrixRouteTableHeader matrixRouteTableHeader;
    private MapFragment mapFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_matrix_routing_example, container, false);
        setupMatrixRouteFragment(view);
        return view;
    }

    private void setupMatrixRouteFragment(View view) {
        initViews(view);
        setupMatrixRoutesTable();
        setupActionButtons();
        setupPresenter();
    }

    private void setupPresenter() {
        presenter = new MatrixRoutePresenter(this);
        getLifecycle().addObserver(presenter);
    }

    private void setupActionButtons() {
        planMatrixBtn.setOnClickListener(this);
        planMatrixTaxiBtn.setOnClickListener(this);
    }

    private void setupMatrixRoutesTable() {
        matrixRoutesList.setLayoutManager(new LinearLayoutManager(getContext()));
        matrixRoutesListAdapter = new MatrixRoutesTableListAdapter();
        matrixRoutesList.setAdapter(matrixRoutesListAdapter);
    }

    private void initViews(View view) {
        mapFragment = (MapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        planMatrixBtn = view.findViewById(R.id.matrixRestaurantsBtn);
        planMatrixTaxiBtn = view.findViewById(R.id.matrixTaxiBtn);
        matrixRoutesList = view.findViewById(R.id.matrixRoutesList);
        matrixRouteTableHeader = view.findViewById(R.id.matrixRoutesListHeader);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBarModel actionBar = (ActionBarModel) getActivity();
        actionBar.setActionBarTitle(presenter.getModel().getPlayableTitle());
        actionBar.setActionBarSubtitle(presenter.getModel().getPlayableSubtitle());
    }

    @Override
    public boolean onBackPressed() {
        presenter.cleanup();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.matrixTaxiBtn) {
            presenter.planMatrixOfRoutesFromPassengersToTaxi();
        } else if (v.getId() == R.id.matrixRestaurantsBtn) {
            presenter.planMatrixOfRoutesFromAmsterdamCenterToRestaurants();
        }
    }

    public void updateMatrixRoutesList(MatrixRoutingResponse matrixRoutingResponse) {
        matrixRoutesListAdapter.updateListWithMatrixResponse(matrixRoutingResponse);
        matrixRoutesList.setVisibility(View.VISIBLE);
        matrixRouteTableHeader.setVisibility(View.VISIBLE);
    }

    public void changeTableHeadersToOneToMany() {
        matrixRouteTableHeader.setEtaLabel(R.string.label_matrix_table_header_eta);
        matrixRouteTableHeader.setOriginLabel(R.string.label_matrix_table_header_origin);
        matrixRouteTableHeader.setDestinationLabel(R.string.label_matrix_table_header_destination);
    }

    public void changeTableHeadersToManyToMany() {
        matrixRouteTableHeader.setEtaLabel(R.string.label_matrix_header_pick_up);
        matrixRouteTableHeader.setOriginLabel(R.string.label_matrix_header_passenger);
        matrixRouteTableHeader.setDestinationLabel(R.string.label_matrix_header_taxi);
    }

    public void hideMatrixRoutesTable() {
        matrixRouteTableHeader.setVisibility(View.GONE);
        matrixRoutesList.setVisibility(View.GONE);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void runOnTomtomMap(final OnMapReadyCallback callback) {
        mapFragment.getAsyncMap(callback);
    }
}
