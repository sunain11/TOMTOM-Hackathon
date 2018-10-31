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
package com.tomtom.online.sdk.samples.cases.route.departureandarrivaltime;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.RoutePlannerFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

import org.joda.time.DateTime;

import java.util.Date;

public class DepartureAndArrivalTimeFragment extends RoutePlannerFragment<DepartureAndArrivalTimePresenter> {

    enum ExampleType {
        NONE,
        ARRIVAL,
        DEPARTURE
    }

    private ExampleType exampleType = ExampleType.NONE;

    @Override
    protected DepartureAndArrivalTimePresenter createPresenter() {
        return new DepartureAndArrivalTimePresenter(this);
    }

    @Override
    protected void onOptionsButtonsView(OptionsButtonsView view) {
        view.addOption(R.string.btn_text_departure_at);
        view.addOption(R.string.btn_text_arrival_at);
    }

    @Override
    public void onChange(final boolean[] oldValues, final boolean[] newValues) {
        presenter.centerOnDefaultLocation();
        getInfoBarView().hide();
        presenter.clearRoute();
        if (newValues[0]) {
            startDepartureAt();
        } else if (newValues[1]) {
            startArrivalAt();
        }
    }

    public void startDepartureAt() {
        showDateAndTimeDialog(R.string.label_departure_time, new SingleDateAndTimePickerDialog.Listener() {
            @Override
            public void onDateSelected(Date date) {
                DateTime departureDate = new DateTime(date);
                if (!isValidDate(departureDate)) {
                    return;
                }
                presenter.displayDepartureAtRoute(departureDate);
                exampleType = ExampleType.DEPARTURE;
            }
        });
    }


    public void startArrivalAt() {
        showDateAndTimeDialog(R.string.label_arrival_time, new SingleDateAndTimePickerDialog.Listener() {
            @Override
            public void onDateSelected(Date date) {
                DateTime arrivalDateTime = new DateTime(date);
                if (!isValidDate(arrivalDateTime)) {
                    return;
                }
                presenter.displayArrivalAtRoute(arrivalDateTime);
                exampleType = ExampleType.ARRIVAL;
            }
        });

    }

    private void showDateAndTimeDialog(int title, SingleDateAndTimePickerDialog.Listener listener) {
        DateTime initialDateTime = DateTime.now().plusHours(5);
        new SingleDateAndTimePickerDialog.Builder(getContext())
                .bottomSheet()
                .defaultDate(initialDateTime.toDate())
                .curved()
                .title(getResources().getString(title))
                .listener(listener)
                .display();
    }

    private boolean isValidDate(DateTime departureDate) {
        if (departureDate.isBeforeNow()) {
            showError(R.string.msg_error_date_in_past);
            return false;
        }
        return true;
    }

    @Override
    public void routeUpdated(FullRoute route) {
        super.routeUpdated(route);
        if(exampleType == ExampleType.DEPARTURE){
            String arrivalTime = timeFormat.format(route.getSummary().getArrivalTime());
            getInfoBarView().setLeftIcon(R.drawable.maneuver_arrival_flag);
            getInfoBarView().setLeftText(arrivalTime);
        }
        else if(exampleType == ExampleType.ARRIVAL){
            String departureTime = timeFormat.format(route.getSummary().getDepartureTime());
            getInfoBarView().setLeftIcon(R.drawable.ic_arrival_time);
            getInfoBarView().setLeftText(departureTime);
        }
    }
}
