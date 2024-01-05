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

package com.razer.chromacc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.razer.chromacc.Constants.CHROMA_KILL_RGB_WHEN_LOCK_SCREEN;
import static com.razer.chromacc.Constants.CHROMA_SWITCH;

public class ScreenStateListener extends BroadcastReceiver {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        SharedPreferenceUtil spfu = SharedPreferenceUtil.getInstance();
        boolean chromaEnabled = (boolean) spfu.get(context, CHROMA_SWITCH,
                false);
        boolean killWhenScreenLock = (boolean) spfu.get(context, CHROMA_KILL_RGB_WHEN_LOCK_SCREEN,
                false);

        if(!chromaEnabled) {
            return;
        }

        if(killWhenScreenLock) {
            ChromaManager tempManager = new ChromaManager();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                tempManager.systemReady();
                tempManager.loadMcuWithParams(context);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                tempManager.setHardwareSuspend(true);
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {

            }
        }
    }
}
