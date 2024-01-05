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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.os.BatteryManager;
import android.os.Bundle;

import static com.razer.parts.Constants.BMS_ALWAYS_CONNECTED_MODE;
import static com.razer.parts.Constants.BMS_LIMIT_TO_EIGHTY;

public class BMSReceiver extends BroadcastReceiver {
    boolean alwaysConnected, limitToEighty;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            return;
        }

        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        alwaysConnected = (boolean) sharedPreferenceUtil.get(context, BMS_ALWAYS_CONNECTED_MODE, false);
        limitToEighty = (boolean) sharedPreferenceUtil.get(context, BMS_LIMIT_TO_EIGHTY, false);

        processBatteryChange(intent);
    }

    private void resetSuspendState() {
        ShellUtils.CommandResult result = ShellUtils.execCommand("cat /sys/class/power_supply/battery/input_suspend", false);
        if(result.responseMsg.contains("1"))
            ShellUtils.execCommand("echo 0 > /sys/class/power_supply/battery/input_suspend", false);
    }

    private void suspendCharger() {
        ShellUtils.CommandResult result = ShellUtils.execCommand("cat /sys/class/power_supply/battery/input_suspend", false);
        if(result.responseMsg.contains("0"))
            ShellUtils.execCommand("echo 1 > /sys/class/power_supply/battery/input_suspend", false);
    }

    private synchronized void processBatteryChange(Intent intent) {
        Bundle bundle = intent.getExtras();
        int current = bundle.getInt("level");

        if(alwaysConnected) { // Always connected overrides limit to 80
             if(current < 40) 
                resetSuspendState();
             else if(current > 60)
                suspendCharger();
        } else if(limitToEighty) {
            if(current < 78) 
                resetSuspendState();
            else if(current > 80)
                suspendCharger();
        } else {
            resetSuspendState();
        }
    }
}
