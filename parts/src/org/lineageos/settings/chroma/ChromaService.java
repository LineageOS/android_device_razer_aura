// SPDX-FileCopyrightText: Alcatraz323 <alcatraz32323@gmail.com>
// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.chroma;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.preference.PreferenceManager;

public class ChromaService extends Service {

    private final ChromaManager mManager = new ChromaManager();
    private BroadcastReceiver mScreenReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mScreenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                if (!prefs.getBoolean(ChromaFragment.KEY_ENABLED, false)) return;
                if (!prefs.getBoolean(ChromaFragment.KEY_SUSPEND_ON_SLEEP, false)) return;

                if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                    mManager.systemReady();
                    mManager.applySettings(context);
                } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                    mManager.setHardwareSuspend(true);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenReceiver, filter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mScreenReceiver != null) {
            unregisterReceiver(mScreenReceiver);
        }
        super.onDestroy();
    }
}
