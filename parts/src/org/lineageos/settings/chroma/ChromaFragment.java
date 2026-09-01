// SPDX-FileCopyrightText: Alcatraz323 <alcatraz32323@gmail.com>
// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.chroma;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import org.lineageos.settings.R;

public class ChromaFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    public static final String KEY_ENABLED         = "chroma_enabled";
    public static final String KEY_SUSPEND_ON_SLEEP = "chroma_suspend_on_sleep";
    public static final String KEY_MODE            = "chroma_mode";
    public static final String KEY_COLOR           = "chroma_color";
    public static final String KEY_BRIGHTNESS      = "chroma_brightness";

    private final ChromaManager mManager = new ChromaManager();

    private SwitchPreferenceCompat mEnabledPref;
    private SwitchPreferenceCompat mSuspendPref;
    private ListPreference mModePref;
    private EditTextPreference mColorPref;
    private EditTextPreference mBrightnessPref;

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
            return true;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        prefs.edit().putString(preference.getKey(), (String) newValue).apply();

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

        mColorPref.setSummary(prefs.getString(KEY_COLOR, "#00FF00"));
        mBrightnessPref.setSummary(prefs.getString(KEY_BRIGHTNESS, "200"));
    }
}
