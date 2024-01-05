/*
 * Copyright (c) 2021, Alcatraz323 <alcatraz32323@gmail.com>
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

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;

import static android.provider.Settings.System.MIN_REFRESH_RATE;
import static android.provider.Settings.System.PEAK_REFRESH_RATE;
import static com.razer.parts.Constants.ACTIVE_WAKE;
import static com.razer.parts.Constants.CHROMA;
import static com.razer.parts.Constants.DOLBY_ATMOS;
import static com.razer.parts.Constants.SCREEN_REFRESH_RATE;
import static com.razer.parts.Constants.SCREEN_RESOLUTION;

public class DeviceSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private ListPreference mResolutionPref;
    private ListPreference mRefreshRatePref;

    private Preference mChromaPref;
    private Preference mDolbyAtmosPref;
    private Preference mActiveWakeupPref;

    public String TAG = "RazerParts";

    @Override
    public void onCreatePreferences(Bundle bundle, String key) {
        addPreferencesFromResource(R.xml.device_settings);
        findPreferences();
        bindListeners();
        updateSummary();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        switch (preference.getKey()) {
            case SCREEN_RESOLUTION:
                String resolution = (String) o;
                if (o.equals("1440")) {
                    ShellUtils.execCommand("wm density 560", false);
                    ShellUtils.execCommand("wm size 1440x2560", false);
                } else {
                    ShellUtils.execCommand("wm density 419", false);
                    ShellUtils.execCommand("wm size 1080x1920", false);
                }
                break;
            case SCREEN_REFRESH_RATE:
                int parseInt = Integer.parseInt((String) o);
                Settings.System.putInt(getContext().getContentResolver(), MIN_REFRESH_RATE, parseInt);
                Settings.System.putInt(getContext().getContentResolver(), PEAK_REFRESH_RATE, parseInt);
                break;
        }
        SharedPreferenceUtil spfu = SharedPreferenceUtil.getInstance();
        spfu.put(getContext(), preference.getKey(), (String) o);
        updateSummary();
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case CHROMA:
                preference.getContext().startActivity(new Intent()
                        .setClassName("com.razer.chromacc",
                                "com.razer.chromacc.ChromaActivity"));
                break;
            case DOLBY_ATMOS:
                preference.getContext().startActivity(new Intent()
                        .setClassName("com.dolby.daxappui",
                                "com.dolby.daxappui.MainActivity"));
                break;
            case ACTIVE_WAKE:
                preference.getContext().startActivity(new Intent()
                        .setClassName("org.lineageos.settings",
                                "org.lineageos.settings.doze.DozeSettingsActivity"));
                break;
        }
        return true;
    }

    private void findPreferences() {
        mResolutionPref = findPreference(SCREEN_RESOLUTION);
        mRefreshRatePref = findPreference(SCREEN_REFRESH_RATE);
        mChromaPref = findPreference(CHROMA);
        mDolbyAtmosPref = findPreference(DOLBY_ATMOS);
        mActiveWakeupPref = findPreference(ACTIVE_WAKE);
    }

    private void bindListeners() {
        mResolutionPref.setOnPreferenceChangeListener(this);
        mRefreshRatePref.setOnPreferenceChangeListener(this);
        mChromaPref.setOnPreferenceClickListener(this);
        mDolbyAtmosPref.setOnPreferenceClickListener(this);
        mActiveWakeupPref.setOnPreferenceClickListener(this);
    }

    private void updateSummary() {
        updateResolutionSummary();
        updateRefreshRateSummary();
    }

    private void updateResolutionSummary() {
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        String resolution = (String) sharedPreferenceUtil.get(getContext(), SCREEN_RESOLUTION,
                "1440");
        String[] entryValue = getContext().getResources().getStringArray(R.array.resolution_values);
        String[] entry = getContext().getResources().getStringArray(R.array.resolution_entries);
        for (int i = 0; i < entryValue.length; i++) {
            if (entryValue[i].equals(resolution)) {
                mResolutionPref.setSummary(entry[i]);
            }
        }
    }

    private void updateRefreshRateSummary() {
        String refreshRate = Settings.System.getInt(getContext().getContentResolver(), PEAK_REFRESH_RATE, 120) + "";
        String[] entryvalue = getContext().getResources().getStringArray(R.array.refresh_rate_values);
        String[] entry = getContext().getResources().getStringArray(R.array.refresh_rate_entries);
        mRefreshRatePref.setValue(refreshRate);
        for (int i = 0; i < entryvalue.length; i++) {
            if (entryvalue[i].equals(refreshRate)) {
                mRefreshRatePref.setSummary(entry[i]);
                return;
            }
        }
        mRefreshRatePref.setSummary(null);
    }
}
