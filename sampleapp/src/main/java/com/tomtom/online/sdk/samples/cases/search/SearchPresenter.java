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
package com.tomtom.online.sdk.samples.cases.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tomtom.online.sdk.common.location.LatLng;

public interface SearchPresenter {

    void onViewCreated(View view, @Nullable Bundle savedInstanceState);

    void onCreate(Context context);

    void onResume();

    void onPause();

    void performSearch(String text);

    void performSearch(String query, String lang);

    void performSearchWithPosition(String text);

    LatLng getLastKnownPosition();

    void enableSearchUI();

    void disableSearchUI();

    void onSaveInstanceState(Bundle outState);
}
