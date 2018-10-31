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


import android.support.annotation.VisibleForTesting;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.fuzzy.IFuzzySearchQuery;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQuery;

public class TypeAheadSearchFragmentPresenter extends SearchFragmentPresenter {

    public TypeAheadSearchFragmentPresenter(final SearchView presenterListener) {
        super(presenterListener);
    }

    @Override
    public void performSearch(final String text) {
        LatLng position = getLastKnownPosition();
        final FuzzySearchQuery searchQuery = getSearchQuery(text, position);
        performSearchWithoutBlockingUI(searchQuery);
    }

    @VisibleForTesting
    FuzzySearchQuery getSearchQuery(String text, LatLng position) {
        return
                //tag::doc_create_typeahead_search_query[]
                FuzzySearchQueryBuilder.create(text)
                        .withPosition(position)
                        .withTypeAhead(true).build();
                //end::doc_create_typeahead_search_query[]
    }

}
