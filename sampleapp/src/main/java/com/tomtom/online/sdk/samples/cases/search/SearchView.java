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

import android.widget.ProgressBar;

import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.common.util.Contextable;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResult;

import java.util.List;

public interface SearchView extends Contextable {

    void refreshSearchResults();

    void enableToggleButtons();

    void disableToggleButtons();

    void enableInputField();

    void disableInputField();

    void showSearchFailedMessage(String message);

    void updateSearchResults(ImmutableList<FuzzySearchResult> results);

    String getSearchFieldText();

    ProgressBar getSearchProgressBar();

}
