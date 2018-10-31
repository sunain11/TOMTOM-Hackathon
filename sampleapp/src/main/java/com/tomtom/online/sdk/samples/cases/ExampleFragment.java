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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.ActionBarModel;
import com.tomtom.online.sdk.samples.activities.FunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.InfoBarView;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

import timber.log.Timber;

public abstract class ExampleFragment<T extends FunctionalExamplePresenter> extends Fragment
        implements FunctionalExampleFragment, OptionsButtonsView.OptionsChangeValue {

    private static final String MAP_RESTORE_KEY = "MAP_RESTORED_ARG";

    protected T presenter;
    protected ActionBarModel actionBar;
    protected OptionsButtonsView optionsView;

    private InfoBarView infoBarView;
    protected TextView infoTextView;
    private Runnable infoViewRunnable;

    private boolean isRestored;

    public ExampleFragment() {
        presenter = createPresenter();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isRestored = savedInstanceState.getBoolean(MAP_RESTORE_KEY, false);
        }
        Timber.d("isRestored= " + isRestored);
        return inflater.inflate(R.layout.example_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actionBar = (ActionBarModel) getActivity();

        infoTextView = view.findViewById(R.id.infoTextView);
        optionsView = view.findViewById(R.id.optionsButtons);
        infoBarView = view.findViewById(R.id.info_bar_view);

        if (optionsView != null) {
            optionsView.setListener(this);
            onOptionsButtonsView(optionsView);
        }
    }

    protected InfoBarView getInfoBarView() {
        return infoBarView;
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        optionsView.setEnabled(false);
        presenter.onResume(getContext());
        actionBar = (ActionBarModel) getActivity();

        MapFragment mapFragment = (MapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getAsyncMap(new OnMapReadyCallback() {
            @Override
            public void onMapReady(TomtomMap tomtomMap) {
                optionsView.setEnabled(true);
                presenter.bind(ExampleFragment.this, tomtomMap);
            }
        });
    }

    @Override
    public boolean isMapRestored() {
        return isRestored;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MAP_RESTORE_KEY, true);
        Timber.d("onSaveInstanceState isRestored true");
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onBackPressed() {
        if (optionsView.isEnabled()) {
            presenter.cleanup();
        }
        return true;
    }

    @Override
    public void setActionBarTitle(@StringRes int titleId) {
        actionBar.setActionBarTitle(titleId);
    }

    @Override
    public void setActionBarSubtitle(@StringRes int subtitleId) {
        actionBar.setActionBarSubtitle(subtitleId);
    }

    @Override
    public void onMenuItemSelected() {
        //Default implementation is empty
        //Override in the specific class if required
    }

    @Override
    public String getFragmentTag() {
        return getClass().getName();
    }

    @Override
    public void showInfoText(String text, long duration) {

        infoTextView.setText(text);
        infoTextView.setVisibility(View.VISIBLE);

        if (infoViewRunnable != null) {
            infoTextView.getHandler().removeCallbacks(infoViewRunnable);
        }

        infoViewRunnable = new Runnable() {
            @Override
            public void run() {
                infoTextView.setVisibility(View.GONE);
            }
        };

        infoTextView.getHandler().postDelayed(infoViewRunnable, duration);
    }

    @Override
    public void showInfoText(@StringRes int text, long duration) {
        showInfoText(getString(text), duration);
    }

    protected abstract T createPresenter();

    protected abstract void onOptionsButtonsView(OptionsButtonsView view);

    @Override
    public void enableOptionsView() {
        optionsView.setEnabled(true);
    }

    @Override
    public void disableOptionsView() {
        optionsView.setEnabled(false);
    }

}