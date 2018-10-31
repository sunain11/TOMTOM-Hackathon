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

public enum LanguageSelector {
    EN("\uD83C\uDDEC\uD83C\uDDE7", "EN", "en-GB"),
    FR("\uD83C\uDDEB\uD83C\uDDF7", "FR", "fr-FR"),
    DE("\uD83C\uDDE9\uD83C\uDDEA", "DE", "de-DE"),
    ES("\uD83C\uDDEA\uD83C\uDDF8", "ES", "es-ES");

    private String asciiIcon;
    private String name;
    private String code;

    LanguageSelector(String asciiIcon, String name, String code) {
        this.asciiIcon = asciiIcon;
        this.name = name;
        this.code = code;
    }

    public String getDecoratedName() {
        return String.format("%s %s", asciiIcon, name);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}