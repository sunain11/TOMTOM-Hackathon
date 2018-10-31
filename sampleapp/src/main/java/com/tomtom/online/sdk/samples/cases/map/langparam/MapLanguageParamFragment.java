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

package com.tomtom.online.sdk.samples.cases.map.langparam;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.cases.ExampleFragment;
import com.tomtom.online.sdk.samples.utils.views.OptionsButtonsView;

public class MapLanguageParamFragment extends ExampleFragment<MapLanguageParamPresenter> {

    @Override
    public void onChange(boolean[] oldValues, boolean[] newValues) {
        if (newValues[0]) {
            presenter.english();
        } else if (newValues[1]) {
            presenter.russian();
        } else if (newValues[2]) {
            presenter.dutch();
        }
    }

    @Override
    protected MapLanguageParamPresenter createPresenter() {
        return new MapLanguageParamPresenter();
    }

    @Override
    protected void onOptionsButtonsView(final OptionsButtonsView view) {
        view.addOption(R.string.english);
        view.addOption(R.string.russian);
        view.addOption(R.string.dutch);
        optionsView.selectItem(0, true);
    }

}
