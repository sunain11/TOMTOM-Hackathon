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
package com.tomtom.online.sdk.samples.cases.route.maneuvers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tomtom.online.sdk.routing.data.Instruction;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.RoutePlannerFragment;
import com.tomtom.online.sdk.samples.utils.LanguageSelector;
import com.tomtom.online.sdk.samples.utils.views.RadioModifierView;

import java.util.ArrayList;
import java.util.List;

public class ManeuversFragment extends RoutePlannerFragment<ManeuversPresenter> {

    private RadioModifierView languageSelectorView;
    private ManeuversListAdapter maneuversListAdapter;
    private ListView maneuversListView;
    private static final String KEY_LANGUAGE_SELECTED_CODE = "SELECTED_LANG";
    private LanguageSelector languageSelected = null;

    @Override
    public void onResume() {
        super.onResume();
        setActionBarTitle(presenter.getModel().getPlayableTitle());
        setActionBarSubtitle(presenter.getModel().getPlayableSubtitle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_maneuvers_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initManeuversListView();
        initLanguageSelectorView();
    }

    @Override
    protected ManeuversPresenter createPresenter() {
        return new ManeuversPresenter(this);
    }

    public void onMapReady() {
        if (languageSelected == null) {
            languageSelectorView.clickItem(RadioModifierView.ModifierButton.LEFT);
        } else {
            languageSelectorView.selectButtonByCaption(languageSelected.getName());
            presenter.startRouting(languageSelected.getCode());
        }
    }

    @Override
    public void repeatRequestWhenNotFinished() {
    }

    public void updateInstructions(List<Instruction> instructions) {
        maneuversListAdapter.setInstructions(instructions);
    }

    private void initManeuversListView() {
        maneuversListView = getActivity().findViewById(R.id.maneuvers_list);
        maneuversListAdapter = new ManeuversListAdapter(getContext());
        maneuversListView.setAdapter(maneuversListAdapter);
    }

    @Override
    public void onRestoreSavedInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreSavedInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            languageSelected = (LanguageSelector) savedInstanceState.getSerializable(KEY_LANGUAGE_SELECTED_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_LANGUAGE_SELECTED_CODE, languageSelected);
    }

    private void initLanguageSelectorView() {
        languageSelectorView = getActivity().findViewById(R.id.language_selector);
        languageSelectorView.setRadioNames(
                LanguageSelector.EN.getDecoratedName(),
                LanguageSelector.FR.getDecoratedName(),
                LanguageSelector.DE.getDecoratedName(),
                LanguageSelector.ES.getDecoratedName());
        languageSelectorView.setSearchModifiersLViewListener(languageSelectorListener);
        languageSelectorView.setSecondMiddleRadioVisible(true);
        languageSelectorView.setFirstMiddleRadioVisible(true);
        languageSelectorView.setEnabled(true);
    }

    private RadioModifierView.RadioModifierListener languageSelectorListener = new RadioModifierView.RadioModifierListener() {
        @Override
        public void onLeftButtonClicked() {
            languageSelected = LanguageSelector.EN;
            presenter.startRouting(languageSelected.getCode());
        }

        @Override
        public void onRightButtonClicked() {
            languageSelected = LanguageSelector.FR;
            presenter.startRouting(languageSelected.getCode());
        }

        @Override
        public void onFirstMiddleButtonClicked() {
            languageSelected = LanguageSelector.DE;
            presenter.startRouting(languageSelected.getCode());

        }

        @Override
        public void onSecondMiddleButtonClicked() {
            languageSelected = LanguageSelector.ES;
            presenter.startRouting(languageSelected.getCode());
        }
    };

    private static class ManeuversListAdapter extends BaseAdapter {

        private static final int METERS_IN_KM = 1000;

        private Context context;
        private List<Instruction> instructions = new ArrayList<>();

        public ManeuversListAdapter(Context context) {
            this.context = context;
        }

        public void setInstructions(List<Instruction> instructions) {
            this.instructions = instructions;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return instructions.size();
        }

        @Override
        public Instruction getItem(int idx) {
            return instructions.get(idx);
        }

        @Override
        public long getItemId(int idx) {
            return idx;
        }

        @Override
        public View getView(int idx, View view, ViewGroup viewGroup) {

            final Instruction instruction = getItem(idx);
            final ManeuversIconProvider iconManeuverProvider = new ManeuversIconProvider();

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.view_item_maneuver_instruction, viewGroup, false);
            }

            final TextView description = view.findViewById(R.id.maneuver_description);
            description.setText(instruction.getMessage());

            final ImageView icon = view.findViewById(R.id.maneuver_icon);
            icon.setImageDrawable(iconManeuverProvider.getIcon(context, instruction));

            final TextView distance = view.findViewById(R.id.maneuver_distance);
            int metersToManeuver = instruction.getRouteOffsetInMeters();
            int kilometersToManeuver = metersToManeuver / METERS_IN_KM;
            if (metersToManeuver > METERS_IN_KM) {
                distance.setText(context.getString(R.string.distance_km, kilometersToManeuver));
            } else {
                distance.setText(context.getString(R.string.distance_m, metersToManeuver));
            }

            return view;
        }

    }

}
