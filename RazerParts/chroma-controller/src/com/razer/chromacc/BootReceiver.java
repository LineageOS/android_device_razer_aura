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

package com.razer.chromacc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.razer.chromacc.Constants.CHROMA_SWITCH;

public class BootReceiver extends BroadcastReceiver {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            return;
        }

        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        context.startService(new Intent(context, HolderService.class));

        boolean chromaEnabled = (boolean) sharedPreferenceUtil.get(context, CHROMA_SWITCH, false);
        if (chromaEnabled) {
            ChromaManager tempManager = new ChromaManager();
            tempManager.systemReady();
            tempManager.loadMcuWithParams(context);
        }
    }
}
