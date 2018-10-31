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

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomtom.online.sdk.samples.R;

public class MatrixRouteTableHeader extends LinearLayout {

    private TextView originLabel;
    private TextView destinationLabel;
    private TextView etaLabel;

    public MatrixRouteTableHeader(Context context) {
        super(context);
        init();
    }

    public MatrixRouteTableHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MatrixRouteTableHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.matrix_routes_table_header, this);
        originLabel = findViewById(R.id.matrixTableHeaderOrigin);
        destinationLabel = findViewById(R.id.matrixTableHeaderDestination);
        etaLabel = findViewById(R.id.matrixTableHeaderEta);
    }

    public void setOriginLabel(@StringRes int textResId) {
        originLabel.setText(textResId);
    }

    public void setDestinationLabel(@StringRes int textResId) {
        destinationLabel.setText(textResId);
    }

    public void setEtaLabel(@StringRes int textResId) {
        etaLabel.setText(textResId);
    }

}
