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
package com.tomtom.online.sdk.samples.cases.map.markers.balloons;

import android.view.View;
import android.widget.TextView;

import com.tomtom.online.sdk.map.BaseBalloonViewAdapter;
import com.tomtom.online.sdk.map.BaseMarkerBalloon;
import com.tomtom.online.sdk.map.Marker;
import com.tomtom.online.sdk.samples.R;

/**
 * Adapter for SampleApp which support two balloons types.
 * One is {@link com.tomtom.online.sdk.map.SimpleMarkerBalloon} second is a custom marker balloon.
 */
public class TypedBalloonViewAdapter extends BaseBalloonViewAdapter<BaseMarkerBalloon> {
    /**
     * Taking layout from balloon view model.
     * @param marker to be inflated.
     * @param markerBalloon
     * @return
     */
    @Override
    public int getLayout(Marker marker, BaseMarkerBalloon markerBalloon) {
        if (isTextBalloon(markerBalloon)){
            return R.layout.balloon_layout;
        }else{
            return R.layout.custom_balloon_layout;
        }
    }

    private boolean isTextBalloon(BaseMarkerBalloon markerBalloon) {
        return markerBalloon.getProperty(BaseMarkerBalloon.KEY_TEXT) != null;
    }

    /**
     * Bind text view for single line balloon view.
     * @param view   the root view of inflating layout.
     * @param marker value which is used to fill layout.
     * @param markerBalloon balloon model.
     */
    @Override
    public void onBindView(View view, Marker marker, BaseMarkerBalloon markerBalloon) {
        if (isTextBalloon(markerBalloon)){
            TextView textView = view.findViewById(R.id.text);
            textView.setText((CharSequence) markerBalloon.getProperty(BaseMarkerBalloon.KEY_TEXT));
        }
    }

}
