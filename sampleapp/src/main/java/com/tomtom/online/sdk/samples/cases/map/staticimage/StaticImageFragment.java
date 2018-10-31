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
package com.tomtom.online.sdk.samples.cases.map.staticimage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tomtom.online.sdk.common.config.loader.ConfigLoader;
import com.tomtom.online.sdk.common.config.loader.ManifestConfigLoader;
import com.tomtom.online.sdk.common.config.provider.ConfigProvider;
import com.tomtom.online.sdk.common.config.provider.PriorityConfigProvider;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.ActionBarModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.Locations;
import com.tomtom.online.sdk.staticimage.DownloadListener;
import com.tomtom.online.sdk.staticimage.StaticImage;

public class StaticImageFragment extends Fragment implements FunctionalExampleFragment {

    private ActionBarModel actionBar;
    private ImageView staticMap1;
    private ImageView staticMap2;
    private ImageView staticMap3;
    private ImageView staticMap4;
    private ImageView staticMap5;
    private ImageView staticMap6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.static_image_fragment, container, false);
        staticMap1 = view.findViewById(R.id.map_1);
        staticMap2 = view.findViewById(R.id.map_2);
        staticMap3 = view.findViewById(R.id.map_3);
        staticMap4 = view.findViewById(R.id.map_4);
        staticMap5 = view.findViewById(R.id.map_5);
        staticMap6 = view.findViewById(R.id.map_6);
        staticMap1.setOnClickListener(new DisplayInfoClickListener(getString(R.string.staticMap1_msg)));
        staticMap2.setOnClickListener(new DisplayInfoClickListener(getString(R.string.staticMap2_msg)));
        staticMap3.setOnClickListener(new DisplayInfoClickListener(getString(R.string.staticMap3_msg)));
        staticMap4.setOnClickListener(new DisplayInfoClickListener(getString(R.string.staticMap4_msg)));
        staticMap5.setOnClickListener(new DisplayInfoClickListener(getString(R.string.staticMap5_msg)));
        staticMap6.setOnClickListener(new DisplayInfoClickListener(getString(R.string.staticMap6_msg)));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar = (ActionBarModel) getActivity();

        setActionBarTitle(R.string.static_image_title);
        setActionBarSubtitle(R.string.static_image_help);

        loadData();
    }

    /**
     * Default key from manifest.
     */
    private static String getMapApiKey(@NonNull Context context) {
        ConfigLoader configLoader = new ManifestConfigLoader(context);
        ConfigProvider configProvider = new PriorityConfigProvider(configLoader);
        return configProvider.provideConfiguration().get(ConfigProvider.ONLINE_MAPS_KEY).getValue();
    }

    private void loadData() {
        //tag::doc_static_image[]
        StaticImage.builder(getMapApiKey(getContext()))
                .center(Locations.AMSTERDAM_LOCATION) //use center or boundingBox
                .layerBasic() // basic layer is default
                .styleMain() // main style is default
                .mapSize(512, 512) // default size in px
                .png() //png is default
                .build()
                .downloadInto(staticMap1);
        //end::doc_static_image[]
        StaticImage.builder(getMapApiKey(getContext())).center(Locations.AMSTERDAM_LOCATION).styleNight().build().downloadInto(staticMap2);
        StaticImage.builder(getMapApiKey(getContext())).center(Locations.AMSTERDAM_LOCATION).zoom(15).build().downloadInto(staticMap3);
        StaticImage.builder(getMapApiKey(getContext())).center(Locations.AMSTERDAM_LOCATION).layerHybrid().build().downloadInto(staticMap4);
        StaticImage.builder(getMapApiKey(getContext())).center(Locations.AMSTERDAM_LOCATION).style("main-azure").build().downloadInto(staticMap5);
        StaticImage.builder(getMapApiKey(getContext())).center(Locations.AMSTERDAM_LOCATION).styleNight().zoom(8).build().download(getContext(), new ExampleDownloadListener());
    }

    @Override
    public void onMenuItemSelected() {

    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public boolean isMapRestored() {
        return false;
    }

    @Override
    public String getFragmentTag() {
        return getClass().getName();
    }

    @Override
    public void setActionBarTitle(int titleId) {
        actionBar.setActionBarTitle(titleId);
    }

    @Override
    public void setActionBarSubtitle(int subtitleId) {
        actionBar.setActionBarSubtitle(subtitleId);
    }

    @Override
    public void showInfoText(String text, long duration) {

    }

    @Override
    public void showInfoText(int text, long duration) {

    }

    @Override
    public void enableOptionsView() {

    }

    @Override
    public void disableOptionsView() {

    }

    private class DisplayInfoClickListener implements View.OnClickListener {
        private final String message;

        public DisplayInfoClickListener(String message) {
            this.message = message;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private class ExampleDownloadListener implements DownloadListener {
        @Override
        public void onReady(Drawable resource, String url) {
            staticMap6.setImageDrawable(resource);
        }

        @Override
        public void onFailed(String url) {
            Toast.makeText(getContext(), "resource not loaded " + url, Toast.LENGTH_SHORT).show();
        }
    }
}
