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

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.tomtom.online.sdk.samples.R;

import timber.log.Timber;

public class RoutePlanningInProgressDialog extends DialogFragment {

    public static final String ROUTING_IN_PROGRESS_DIALOG_TAG = "ROUTING_IN_PROGRESS";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.dialog_routing_in_progress_msg)
                .setCancelable(false)
                .create();
    }


    public void show(FragmentManager manager) {
        if (manager == null) {
            return;
        }
        if (!isAdded()) {
            try {
                super.show(manager, ROUTING_IN_PROGRESS_DIALOG_TAG);
            } catch (IllegalStateException e) {
                Timber.d(e, "dialog not displayed problem with support library");
            }
        }
    }

    @Override
    public void dismiss() {
        if (getFragmentManager() == null) {
            return;
        }
        if (isAdded()) {
            super.dismiss();
        }
    }
}
