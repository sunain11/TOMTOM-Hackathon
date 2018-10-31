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
package com.tomtom.online.sdk.samples.utils;

import android.content.Context;

import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.common.util.Contextable;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContextAndMapReadyContract implements OnMapReadyCallback, Contextable {

    private TomtomMap tomtomMap;
    private Context context;
    private AllReadyListener listener;

    @Override
    public void onMapReady(TomtomMap tomtomMap) {
        this.tomtomMap = tomtomMap;
        checkAndExecute();
    }

    public void setContext(Context context) {
        this.context = context;
        checkAndExecute();
    }

    public void clear() {
        context = null;
        tomtomMap = null;
    }

    private void checkAndExecute() {
        if (getContext() != null && tomtomMap != null) {
            checkNotNull(listener, "All is ready but listener is not set");
            listener.execute();
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void registerAllIsReady(AllReadyListener listener) {
        this.listener = listener;
    }

    public interface AllReadyListener {
        void execute();
    }
}
