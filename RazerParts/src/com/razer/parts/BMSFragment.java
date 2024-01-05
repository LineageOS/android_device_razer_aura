/*
 * Copyright (c) 2023, Alcatraz323 <alcatraz32323@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package com.razer.parts;

import android.app.ActivityManager;
import android.content.Context;
import android.content.om.IOverlayManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;

import static com.razer.parts.Constants.BMS_STEP_CHG_SWITCH;
import static com.razer.parts.Constants.BMS_ALWAYS_CONNECTED_MODE;
import static com.razer.parts.Constants.BMS_LIMIT_TO_EIGHTY;
import static com.razer.parts.Constants.BMS_DISABLE_CHARGING_RIPPLE_EFFECT_SWITCH;
import static com.razer.parts.Constants.BMS_OVERLAY_DISABLE_CHARGING_RIPPLE;

public class BMSFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "BMSFragment";

    private SwitchPreference mStepChargingSwitch, mDisableChargingRippleEffectSwitch;

    @Override
    public void onCreatePreferences(Bundle bundle, String key) {
        addPreferencesFromResource(R.xml.bms_settings);
        findPreferences();
        bindListeners();
        updateSwitches();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        switch (preference.getKey()) {
            case BMS_STEP_CHG_SWITCH:
                boolean enabled = (boolean) o;
                ShellUtils.execCommand("echo " + (enabled ? "1" : "0") + " > /sys/class/power_supply/battery/step_charging_enabled", false);
                ShellUtils.execCommand("echo " + (enabled ? "1" : "0") + " > /sys/class/power_supply/battery/sw_jeita_enabled", false);
                break;
            case BMS_DISABLE_CHARGING_RIPPLE_EFFECT_SWITCH:
                boolean disabled = (boolean) o;
                toggleRippleDisableOverlay(disabled);
                break;
        }
        return true;
    }

    private void findPreferences() {
        mStepChargingSwitch = findPreference(BMS_STEP_CHG_SWITCH);
        mDisableChargingRippleEffectSwitch = findPreference(BMS_DISABLE_CHARGING_RIPPLE_EFFECT_SWITCH);
    }

    private void bindListeners() {
        mStepChargingSwitch.setOnPreferenceChangeListener(this);
        mDisableChargingRippleEffectSwitch.setOnPreferenceChangeListener(this);
    }

    private void updateSwitches() {
        ShellUtils.CommandResult result = ShellUtils.execCommand("cat /sys/class/power_supply/battery/step_charging_enabled", false);
        mStepChargingSwitch.setChecked(result.responseMsg.contains("1"));
    }

    private void toggleRippleDisableOverlay(boolean disabled) {
        final IOverlayManager iom = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
        final int userId = ActivityManager.getCurrentUser();
        try {
            iom.setEnabled(BMS_OVERLAY_DISABLE_CHARGING_RIPPLE, disabled, userId);
            if (disabled) {
                iom.setHighestPriority(BMS_OVERLAY_DISABLE_CHARGING_RIPPLE, userId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to " + (disabled ? "enable" : "disable")
                    + " overlay " + BMS_OVERLAY_DISABLE_CHARGING_RIPPLE + " for user " + userId);
        }
    }
}
