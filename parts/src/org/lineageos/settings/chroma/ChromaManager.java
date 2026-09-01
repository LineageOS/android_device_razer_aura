// SPDX-FileCopyrightText: Alcatraz323 <alcatraz32323@gmail.com>
// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.chroma;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChromaManager {

    private static final String TAG = "ChromaManager";

    private static final int NUM_LEDS = 9;

    private static final String NODE_CHANNEL_BASE = "/sys/class/leds/lp5523:channel";
    private static final String NODE_DEVICE = "/sys/class/leds/lp5523:channel0/device";
    private static final String NODE_ENGINE_1_MODE = NODE_DEVICE + "/engine1_mode";
    private static final String NODE_ENGINE_2_MODE = NODE_DEVICE + "/engine2_mode";
    private static final String NODE_ENGINE_3_MODE = NODE_DEVICE + "/engine3_mode";
    private static final String NODE_MASTER_FADER_1 = NODE_DEVICE + "/master_fader1";
    private static final String NODE_MASTER_FADER_LEDS = NODE_DEVICE + "/master_fader_leds";
    private static final String NODE_MEMORY = NODE_DEVICE + "/memory";
    private static final String NODE_PROG_1_START = NODE_DEVICE + "/prog_1_start";
    private static final String NODE_PROG_2_START = NODE_DEVICE + "/prog_2_start";
    private static final String NODE_PROG_3_START = NODE_DEVICE + "/prog_3_start";
    private static final String NODE_RUN_ENGINE = NODE_DEVICE + "/run_engine";
    private static final String NODE_SUSPEND = NODE_DEVICE + "/force_suspend";

    private static final String[] NODE_BRIGHTNESS = new String[NUM_LEDS];
    private static final String[] NODE_CURRENT = new String[NUM_LEDS];

    private static final String PROGRAM_BREATHING =
            "01C00015002A9C000DFF7E002E009000910484600400BF87BF87BF0502005A00E004E008920484600400BF91BF91BF0F02007E007E007E006000E004E008A0059C010DFF7E002E009000910484600400BF87BF87BF05E080920484600400BF8EBF8EBF0CE080A0059C020DFF7E002E009000910484600400BF87BF87BF05E080920484600400BF8EBF8EBF0CE080A005000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final String PROGRAM_SPECTRUM =
            "01C00015002A9C0026FF4A00A0074C537E006C00E00855103C00A18963103400A30C57164800590D5000E00437BC4A00E1007400A19648006C0E5A00429EA00400009C0123FF34124C004018A00863205A00610A480046614468E080690848007400A18FE0087400A1124A104400E00245874E0055206000A00600009C0226FF40FFE0802BF14C00670E5800E10016DF4A0050206C00A00300000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final String PROGRAM_WAVE_LTR =
            "01FF01C00001004000020004008000080010010000209C000DFF9C010CFFE004E0089C029C84442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A008E0809C059C8709FF442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A004E0809C089C8A11FF442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A004000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final String PROGRAM_WAVE_RTL =
            "01FF01C00001004000020004008000080010010000209C000DFF9C010CFFE004E0089C089C8A442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A008E0809C059C8709FF442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A004E0809C029C8411FF442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A004000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

    private static final String BREATHING_START_2 = "20";
    private static final String BREATHING_START_3 = "34";
    private static final String SPECTRUM_START_2 = "21";
    private static final String SPECTRUM_START_3 = "3E";
    private static final String WAVE_START_1 = "0B";
    private static final String WAVE_START_2 = "22";
    private static final String WAVE_START_3 = "35";

    private final Object mLock = new Object();
    private boolean mSuspended = false;

    static {
        for (int i = 0; i < NUM_LEDS; i++) {
            NODE_BRIGHTNESS[i] = NODE_CHANNEL_BASE + i + "/brightness";
            NODE_CURRENT[i] = NODE_CHANNEL_BASE + i + "/led_current";
        }
    }

    public void systemReady() {
        synchronized (mLock) {
            try {
                write(NODE_SUSPEND, "0");
                initLeds();
            } catch (IOException e) {
                Log.wtf(TAG, "Failed to initialize LED hardware", e);
            }
        }
    }

    public void applySettings(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = prefs.getString(ChromaFragment.KEY_MODE, "color");
        String color = prefs.getString(ChromaFragment.KEY_COLOR, "#00FF00");
        int brightness;
        try {
            brightness = prefs.getInt(ChromaFragment.KEY_BRIGHTNESS, 200);
        } catch (ClassCastException e) {
            // Migrate from String (old EditTextPreference) to Int (SeekBarPreference)
            String s = prefs.getString(ChromaFragment.KEY_BRIGHTNESS, "200");
            try { brightness = Integer.parseInt(s); } catch (NumberFormatException ignored) { brightness = 200; }
            prefs.edit().remove(ChromaFragment.KEY_BRIGHTNESS).apply();
        }

        switch (mode) {
            case "color":    applyColor(Color.parseColor(color)); break;
            case "breath":   applyProgram(PROGRAM_BREATHING, "03", BREATHING_START_2, BREATHING_START_3); break;
            case "spectrum": applyProgram(PROGRAM_SPECTRUM, "03", SPECTRUM_START_2, SPECTRUM_START_3); break;
            case "wave_ltr": applyProgram(PROGRAM_WAVE_LTR, WAVE_START_1, WAVE_START_2, WAVE_START_3); break;
            case "wave_rtl": applyProgram(PROGRAM_WAVE_RTL, WAVE_START_1, WAVE_START_2, WAVE_START_3); break;
        }

        setBrightness(brightness);
    }

    public void setBrightness(int value) {
        value = Math.max(5, Math.min(255, value));
        synchronized (mLock) {
            try {
                write(NODE_MASTER_FADER_1, String.valueOf(value));
            } catch (IOException e) {
                Log.e(TAG, "Failed to set brightness", e);
            }
        }
    }

    public void setHardwareSuspend(boolean suspend) {
        synchronized (mLock) {
            try {
                write(NODE_SUSPEND, suspend ? "1" : "0");
                if (!suspend) initLeds();
                mSuspended = suspend;
            } catch (IOException e) {
                Log.e(TAG, "Failed to " + (suspend ? "suspend" : "resume") + " LED hardware", e);
            }
        }
    }

    public void suspend() {
        synchronized (mLock) {
            try {
                write(NODE_MASTER_FADER_1, "0");
            } catch (IOException e) {
                Log.e(TAG, "Failed to suspend LEDs", e);
            }
        }
    }

    private void initLeds() throws IOException {
        for (int i = 0; i < NUM_LEDS; i++) {
            write(NODE_CURRENT[i], "255");
        }
        write(NODE_MASTER_FADER_LEDS, "111111111");
        write(NODE_MASTER_FADER_1, "255");
    }

    private void applyColor(int color) {
        stopEngines();
        try {
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            // Channel layout: [g, b, g, b, g, b, r, r, r]
            int[] values = {g, b, g, b, g, b, r, r, r};
            for (int i = 0; i < NUM_LEDS; i++) {
                write(NODE_BRIGHTNESS[i], String.valueOf(values[i]));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to apply color", e);
        }
    }

    private void applyProgram(String memory, String start1, String start2, String start3) {
        synchronized (mLock) {
            try {
                write(NODE_MEMORY, memory.getBytes());
                write(NODE_PROG_1_START, start1);
                write(NODE_PROG_2_START, start2);
                write(NODE_PROG_3_START, start3);
                write(NODE_RUN_ENGINE, "1");
            } catch (IOException e) {
                Log.e(TAG, "Failed to apply program", e);
            }
        }
    }

    private void stopEngines() {
        synchronized (mLock) {
            try {
                write(NODE_ENGINE_1_MODE, "disabled");
                write(NODE_ENGINE_2_MODE, "disabled");
                write(NODE_ENGINE_3_MODE, "disabled");
            } catch (IOException e) {
                Log.e(TAG, "Failed to stop LED engines", e);
            }
        }
    }

    private void write(String path, String value) throws IOException {
        write(path, value.getBytes());
    }

    private void write(String path, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File(path))) {
            fos.write(data);
        }
    }
}
