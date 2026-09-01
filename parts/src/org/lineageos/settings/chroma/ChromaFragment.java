// SPDX-FileCopyrightText: Alcatraz323 <alcatraz32323@gmail.com>
// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.chroma;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import org.lineageos.settings.R;

public class ChromaFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String KEY_ENABLED         = "chroma_enabled";
    public static final String KEY_SUSPEND_ON_SLEEP = "chroma_suspend_on_sleep";
    public static final String KEY_MODE            = "chroma_mode";
    public static final String KEY_COLOR           = "chroma_color";
    public static final String KEY_BRIGHTNESS      = "chroma_brightness";

    private final ChromaManager mManager = new ChromaManager();

    private SwitchPreferenceCompat mEnabledPref;
    private SwitchPreferenceCompat mSuspendPref;
    private ListPreference mModePref;
    private ColorPickerPreference mColorPref;
    private SeekBarPreference mBrightnessPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.chroma_settings);

        mEnabledPref    = findPreference(KEY_ENABLED);
        mSuspendPref    = findPreference(KEY_SUSPEND_ON_SLEEP);
        mModePref       = findPreference(KEY_MODE);
        mColorPref      = findPreference(KEY_COLOR);
        mBrightnessPref = findPreference(KEY_BRIGHTNESS);

        mEnabledPref.setOnPreferenceChangeListener(this);
        mSuspendPref.setOnPreferenceChangeListener(this);
        mModePref.setOnPreferenceChangeListener(this);
        mColorPref.setOnPreferenceChangeListener(this);
        mBrightnessPref.setOnPreferenceChangeListener(this);

        updateSummaries();
        updateColorEnabled();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Activity activity = getActivity();

        if (KEY_ENABLED.equals(preference.getKey())) {
            boolean enabled = (Boolean) newValue;
            if (enabled) {
                mManager.systemReady();
                mManager.applySettings(activity);
            } else {
                mManager.suspend();
            }
            SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(activity);
            mColorPref.setEnabled(enabled && "color".equals(p.getString(KEY_MODE, "color")));
            return true;
        }

        if (newValue instanceof Boolean) {
            return true; // SwitchPreferenceCompat auto-persists
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        if (newValue instanceof Integer) {
            // SeekBarPreference auto-persists; apply live brightness if enabled
            if (KEY_BRIGHTNESS.equals(preference.getKey())) {
                mBrightnessPref.setSummary(newValue + "%");
                if (prefs.getBoolean(KEY_ENABLED, false)) {
                    mManager.setBrightness((Integer) newValue);
                }
            }
            return true;
        }

        // String values: mode (ListPreference) and color (ColorPickerPreference)
        prefs.edit().putString(preference.getKey(), (String) newValue).apply();

        if (KEY_MODE.equals(preference.getKey())) {
            updateColorEnabled();
        }

        updateSummaries();

        if (prefs.getBoolean(KEY_ENABLED, false)) {
            mManager.applySettings(activity);
        }

        return true;
    }

    private void updateSummaries() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String[] modeValues  = getResources().getStringArray(R.array.chroma_mode_values);
        String[] modeEntries = getResources().getStringArray(R.array.chroma_mode_entries);
        String currentMode   = prefs.getString(KEY_MODE, "color");
        for (int i = 0; i < modeValues.length; i++) {
            if (modeValues[i].equals(currentMode)) {
                mModePref.setSummary(modeEntries[i]);
                break;
            }
        }

        // ColorPickerPreference manages its own summary; brightness shown as percentage
        mBrightnessPref.setSummary(prefs.getInt(KEY_BRIGHTNESS, 78) + "%");
    }

    private void updateColorEnabled() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean chromaOn = prefs.getBoolean(KEY_ENABLED, false);
        boolean isColor  = "color".equals(prefs.getString(KEY_MODE, "color"));
        mColorPref.setEnabled(chromaOn && isColor);
    }
}
