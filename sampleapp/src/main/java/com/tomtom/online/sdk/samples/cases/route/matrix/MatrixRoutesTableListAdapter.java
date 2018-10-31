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

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingResponse;
import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingResult;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.route.matrix.data.AmsterdamPoi;
import com.tomtom.online.sdk.samples.utils.formatter.DistanceFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class MatrixRoutesTableListAdapter extends RecyclerView.Adapter<MatrixRoutesTableListAdapter.MatrixRouteItemViewHolder> {

    private List<MatrixRoutingResult> itemList = new ArrayList<>();

    @NonNull
    @Override
    public MatrixRouteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View matrixListItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.matrix_list_item, parent, false);
        return new MatrixRouteItemViewHolder(matrixListItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull MatrixRouteItemViewHolder holder, int position) {
        final MatrixRoutingResult item = itemList.get(position);

        DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        holder.no.setText(String.format("%s%d", AmsterdamPoi.EMPTY_STRING, position + 1));
        holder.eta.setText(simpleDateFormat.format(item.getSummary().getArrivalTime()));
        holder.destination.setText(AmsterdamPoi.getName(holder.destination.getContext(), item.getDestination()));
        holder.origin.setText(AmsterdamPoi.getName(holder.origin.getContext(), item.getOrigin()));
        holder.distance.setText(DistanceFormatter.format(item.getSummary().getLengthInMeters()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateListWithMatrixResponse(MatrixRoutingResponse response) {
        itemList.clear();
        itemList.addAll(response.getResults().values());
        notifyDataSetChanged();
    }

    static class MatrixRouteItemViewHolder extends RecyclerView.ViewHolder {

        TextView no;
        TextView destination;
        TextView distance;
        TextView eta;
        TextView origin;

        MatrixRouteItemViewHolder(View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.matrixItemNo);
            destination = itemView.findViewById(R.id.matrixItemDestination);
            distance = itemView.findViewById(R.id.matrixItemDistance);
            eta = itemView.findViewById(R.id.matrixItemEta);
            origin = itemView.findViewById(R.id.matrixItemOrigin);
        }
    }
}
