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
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.tomtom.online.sdk.routing.data.Instruction;
import com.tomtom.online.sdk.samples.R;

//Dummy implementation, just to get the example
public class ManeuversIconProvider {

    public static final String MAN_TYPE_TURN = "TURN";
    public static final String MAN_TYPE_SHARP = "SHARP";
    public static final String MAN_TYPE_ROUNDABOUT = "ROUNDABOUT";
    public static final String MAN_TYPE_ENTER = "ENTER";
    public static final String MAN_TYPE_EXIT = "EXIT";
    public static final String MAN_TYPE_ARRIVE = "ARRIVE";
    public static final String MAN_SPLITTER = "_";

    public static final String MANEUVER_PREFIX = "maneuver_";
    //file names
    public static final String MAN_FILE_ROUNDABOUT_RIGHT = "roundabout_right_";
    public static final String MAN_FILE_LEFT_45 = "left_45";
    public static final String MAN_FILE_RIGHT_45 = "right_45";
    public static final String MAN_FILE_ARRIVAL_FLAG = "arrival_flag";
    public static final String MAN_FILE_STRAIGHT = "straight";

    public static final String ANDROID_DRAWABLE_DIR_NAME = "drawable";

    public Drawable getIcon(Context context, Instruction instruction) {

        String maneuver = instruction.getManeuver();
        int angleInDecimalDegree = Math.abs(instruction.getTurnAngleInDecimalDegrees());
        String iconName;
        iconName = getIconName(maneuver, angleInDecimalDegree);

        int resId = context.getResources().getIdentifier(iconName, ANDROID_DRAWABLE_DIR_NAME, context.getPackageName());
        if (resId == 0) {
            resId = R.drawable.maneuver_straight;
        }
        return ContextCompat.getDrawable(context, resId);
    }

    @NonNull
    private String getIconName(String maneuver, int angleInDecimalDegree) {
        String iconName;
        if (maneuver.contains(MAN_TYPE_TURN) || maneuver.contains(MAN_TYPE_SHARP)) {
            String[] maneuverParts = maneuver.split(MAN_SPLITTER);
            iconName = MANEUVER_PREFIX + maneuverParts[maneuverParts.length - 1].toLowerCase() + MAN_SPLITTER + angleInDecimalDegree;
        } else if (maneuver.contains(MAN_TYPE_ROUNDABOUT)) {
            iconName = MANEUVER_PREFIX + MAN_FILE_ROUNDABOUT_RIGHT + angleInDecimalDegree;
        } else if (maneuver.contains(MAN_TYPE_ENTER)) {
            iconName = MANEUVER_PREFIX + MAN_FILE_LEFT_45;
        } else if (maneuver.contains(MAN_TYPE_EXIT)) {
            iconName = MANEUVER_PREFIX + MAN_FILE_RIGHT_45;
        } else if (maneuver.contains(MAN_TYPE_ARRIVE)) {
            iconName = MANEUVER_PREFIX + MAN_FILE_ARRIVAL_FLAG;
        } else {
            iconName = MANEUVER_PREFIX + MAN_FILE_STRAIGHT;
        }
        return iconName;
    }

}
