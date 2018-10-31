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
package com.tomtom.online.sdk.samples.license;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.ActionBarModel;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AboutFragment extends Fragment implements FunctionalExampleFragment {


    public static final String HTML_TITLE = "<html><body><h2>About</h2>";

    public static final String DIR_LICENSES = "licenses";

    public static final String MODULE_LICENSE_TITLE = "Open source";
    public static final String HTML_END = "</body></html>";
    public static final String BACKUP_URL = "https://developer.tomtom.com/maps-android-sdk/";
    public static final String MIME_TYPE = "text/html; charset=utf-8";
    public static final String ENCODING = "UTF-8";
    public static final String HTML_EXT = ".html";
    public static final String FILE_RES = "file";
    public static final String FILE_ASSET_URL = FILE_RES + ":///android_asset/";
    public static final String LINK_FORMAT = "<a href=\"" + FILE_ASSET_URL + "%s/%s" + HTML_EXT + "\">%s</a><br/><br/>";
    public static final String MODULE_TITLE_FORMAT = "<h3>%s</h3>";
    public static final String HTML_NAME_SEPARATOR = "_";
    private ActionBarModel actionBar;

    List<String> modules;
    private WebView webView;

    @Override
    public void onResume() {
        super.onResume();
        actionBar = (ActionBarModel) getActivity();

        setActionBarTitle(R.string.about_menu);
        setActionBarSubtitle(R.string.empty);

        loadData();
    }

    private void loadData() {
        StringBuilder sb = new StringBuilder(HTML_TITLE);
        sb.append(loadHeader(""));
        sb.append(loadModules(DIR_LICENSES, MODULE_LICENSE_TITLE));
        sb.append(HTML_END);
        webView.loadDataWithBaseURL(BACKUP_URL, sb.toString(), MIME_TYPE, ENCODING, null);
    }


    private String loadHeader(String title) {
        return getContext().getResources().getString(R.string.license_header_text);
    }

    private String loadModules(String dir, String title) {
        modules = getLicencesModules(dir);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(MODULE_TITLE_FORMAT, title));
        for (String module : modules) {
            sb.append(String.format(LINK_FORMAT, dir, module, module.replace(HTML_NAME_SEPARATOR, " ")));
        }
        return sb.toString();
    }

    private List<String> getLicencesModules(String dir) {
        List<String> modules = new ArrayList<>();
        try {
            String[] list = getContext().getAssets().list(dir);
            for (String file : list) {
                String module = file.replace(HTML_EXT, "");
                Timber.d("module name loaded " + module);
                modules.add(module);
            }
            return ImmutableList.<String>copyOf(modules);
        } catch (IOException e) {
            Timber.e(e);
            return ImmutableList.<String>of();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.license_fragment, container, false);
        webView = view.findViewById(R.id.modules_list);
        webView.getSettings().setJavaScriptEnabled(true);
        //https://stackoverflow.com/a/40753538
        webView.setWebViewClient(new WebViewClient(){

            @Override
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        return view;
    }

    @Override
    public void showInfoText(String text, long duration) {
    }

    @Override
    public void showInfoText(int text, long duration) {

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
    public void onMenuItemSelected() {

    }

    @Override
    public boolean onBackPressed() {
        boolean back = !webView.getUrl().startsWith(FILE_RES);
        if (webView.canGoBack()) {
            webView.goBack();

            loadData();
            return back;
        }
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
    public void enableOptionsView() {

    }

    @Override
    public void disableOptionsView() {

    }
}
