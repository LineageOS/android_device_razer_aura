/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2018 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import org.lineageos.settings.chroma.ChromaFragment;
import org.lineageos.settings.chroma.ChromaManager;
import org.lineageos.settings.chroma.ChromaService;
import org.lineageos.settings.doze.DozeUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final boolean DEBUG = false;
    private static final String TAG = "AuraParts";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (DozeUtils.isDozeEnabled(context) && DozeUtils.sensorsEnabled(context)) {
            if (DEBUG) Log.d(TAG, "Starting Doze service");
            DozeUtils.startService(context);
        }

        context.startService(new Intent(context, ChromaService.class));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean(ChromaFragment.KEY_ENABLED, false)) {
            ChromaManager manager = new ChromaManager();
            manager.systemReady();
            manager.applySettings(context);
        }
    }

}
