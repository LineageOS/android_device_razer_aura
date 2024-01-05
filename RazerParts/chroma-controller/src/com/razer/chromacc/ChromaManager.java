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

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChromaManager {
    private static final String BREATHING_START_1 = "03";
    private static final String BREATHING_START_2 = "20";
    private static final String BREATHING_START_3 = "34";
    private static final boolean DEBUG = false;
    private static final int DEFAULT_BRIGHTNESS = 255;
    private static final int NUM_LEDS = 9;
    private static final String PROGRAM_BREATHING = "01C00015002A9C000DFF7E002E009000910484600400BF87BF87BF0502005A00E004E008920484600400BF91BF91BF0F02007E007E007E006000E004E008A0059C010DFF7E002E009000910484600400BF87BF87BF05E080920484600400BF8EBF8EBF0CE080A0059C020DFF7E002E009000910484600400BF87BF87BF05E080920484600400BF8EBF8EBF0CE080A005000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final String PROGRAM_SPECTRUM = "01C00015002A9C0026FF4A00A0074C537E006C00E00855103C00A18963103400A30C57164800590D5000E00437BC4A00E1007400A19648006C0E5A00429EA00400009C0123FF34124C004018A00863205A00610A480046614468E080690848007400A18FE0087400A1124A104400E00245874E0055206000A00600009C0226FF40FFE0802BF14C00670E5800E10016DF4A0050206C00A00300000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final int RAZER_GREEN = Color.GREEN;
    private static final String SPECTRUM_START_1 = "03";
    private static final String SPECTRUM_START_2 = "21";
    private static final String SPECTRUM_START_3 = "3E";
    private static final String PROGRAM_WAVE_LTR = "01FF01C00001004000020004008000080010010000209C000DFF9C010CFFE004E0089C029C84442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A008E0809C059C8709FF442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A004E0809C089C8A11FF442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A004000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final String PROGRAM_WAVE_RTL = "01FF01C00001004000020004008000080010010000209C000DFF9C010CFFE004E0089C089C8A442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A008E0809C059C8709FF442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A004E0809C029C8411FF442312DC9D8011FF9D8020FF9D8011FF9D80442312DC9D8011FF9D80A004000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final String WAVE_START_1 = "0B";
    private static final String WAVE_START_2 = "22";
    private static final String WAVE_START_3 = "35";
    static final String TAG = "Ths";
    private static final String THS_CHANNEL_SPEC = "lp5523:channel";
    private static final String THS_DEVICE_DIR = "/sys/class/leds/lp5523:channel0/device";
    private static final String THS_LEDS_DIR = "/sys/class/leds";
    private static final String[] THS_NODE_CHANNELS = new String[9];
    private static final String[] THS_NODE_CHANNELS_BRIGHTNESS = new String[9];
    private static final String[] THS_NODE_CHANNELS_CURRENTS = new String[9];
    private static final String THS_NODE_ENGINE_1_MODE = "/sys/class/leds/lp5523:channel0/device/engine1_mode";
    private static final String THS_NODE_ENGINE_2_MODE = "/sys/class/leds/lp5523:channel0/device/engine2_mode";
    private static final String THS_NODE_ENGINE_3_MODE = "/sys/class/leds/lp5523:channel0/device/engine3_mode";
    private static final String THS_NODE_MASTER_FADER_1 = "/sys/class/leds/lp5523:channel0/device/master_fader1";
    private static final String THS_NODE_MASTER_FADER_2 = "/sys/class/leds/lp5523:channel0/device/master_fader2";
    private static final String THS_NODE_MASTER_FADER_3 = "/sys/class/leds/lp5523:channel0/device/master_fader3";
    private static final String THS_NODE_MASTER_FADER_LEDS = "/sys/class/leds/lp5523:channel0/device/master_fader_leds";
    private static final String THS_NODE_MEMORY = "/sys/class/leds/lp5523:channel0/device/memory";
    private static final String THS_NODE_PROG_1_START = "/sys/class/leds/lp5523:channel0/device/prog_1_start";
    private static final String THS_NODE_PROG_2_START = "/sys/class/leds/lp5523:channel0/device/prog_2_start";
    private static final String THS_NODE_PROG_3_START = "/sys/class/leds/lp5523:channel0/device/prog_3_start";
    private static final String THS_NODE_RUN_ENGINE = "/sys/class/leds/lp5523:channel0/device/run_engine";
    private static final String THS_NODE_SUSPEND = "/sys/class/leds/lp5523:channel0/device/force_suspend";
    private static boolean mHardwareSuspended = false;
    private final Object mLock = new Object();

    static {
        for (int i = 0; i < 9; i++) {
            String[] strArr = THS_NODE_CHANNELS;
            strArr[i] = "/sys/class/leds/lp5523:channel" + i;
            String[] strArr2 = THS_NODE_CHANNELS_CURRENTS;
            strArr2[i] = THS_NODE_CHANNELS[i] + "/led_current";
            String[] strArr3 = THS_NODE_CHANNELS_BRIGHTNESS;
            strArr3[i] = THS_NODE_CHANNELS[i] + "/brightness";
        }
    }

    public void loadMcuWithParams(Context context) {
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        String mode = (String) sharedPreferenceUtil.get(context, Constants.CHROMA_MODE,
                "color");
        String color = (String) sharedPreferenceUtil.get(context, Constants.CHROMA_COLOR,
                "#00FF00");
        String brightness = (String) sharedPreferenceUtil.get(context, Constants.CHROMA_BRIGHTNESS,
                "200");

        switch (mode) {
            case "color":
                runThsEffect(1, Color.parseColor(color));
                break;
            case "breath":
                runThsEffect(2, 0);
                break;
            case "spectrum":
                runThsEffect(3, 0);
                break;
            case "wave_ltr":
                runThsEffect(4, 0);
                break;
            case "wave_rtl":
                runThsEffect(5, 0);
                break;
        }

        setBrightness(Integer.parseInt(brightness));
    }

    public void systemReady() {
        synchronized (this) {
            try {
                writeToNode(THS_NODE_SUSPEND, "0");
                initLED();
            } catch (IOException e) {
                Log.wtf(TAG, "Error initialising THS nodes", e);
            }
        }
    }

    private void initLED() throws IOException {
        for (int i = 0; i < 9; i++) {
            writeToNode(THS_NODE_CHANNELS_CURRENTS[i], "255");
        }
        writeToNode(THS_NODE_MASTER_FADER_LEDS, "111111111");
        setBrightness(255);
    }

    public void runThsEffect(int i, int i2) {
        runChromaProgram(i, i2);
    }

    public void runThsProgram(String str, String str2, String str3, String str4) {
        if (str.isEmpty() || str2.isEmpty() || str3.isEmpty() || str4.isEmpty()) {
            throw new IllegalArgumentException();
        }
        runUnknownProgram(str, str2, str3, str4);
    }

    public void previewColor(int i) {
        runProgramForColor(i);
    }

    public void setBrightness(int i) {
        if (i < 5) {
            i = 5;
        } else if (i > 255) {
            i = 255;
        }
        applyBrightness(i);
    }

    public void suspendThs() {
        runStopProgram();
    }

    public void setMasterFader1(int i) {
        applyBrightnessForFader(THS_NODE_MASTER_FADER_1, i);
    }

    public void setMasterFader2(int i) {
        applyBrightnessForFader(THS_NODE_MASTER_FADER_2, i);
    }

    public void setMasterFader3(int i) {
        applyBrightnessForFader(THS_NODE_MASTER_FADER_3, i);
    }

    public void setMasterFaderLEDs(String str) {
        setMasterFader(str);
    }

    public boolean getHardwareSuspend() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mHardwareSuspended;
        }
        return z;
    }

    public void setHardwareSuspend(boolean z) {
        synchronized (this.mLock) {
            try {
                writeToNode(THS_NODE_SUSPEND, z ? "1" : "0");
                if (!z) {
                    initLED();
                }
                this.mHardwareSuspended = z;
            } catch (IOException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to ");
                sb.append(z ? "suspend" : "resume");
                sb.append(" THS hardware!");
                Log.e(TAG, sb.toString(), e);
            }
        }
    }

    private void runChromaProgram(int i, int i2) {
        synchronized (this.mLock) {
            switch (i) {
                case 1:
                    runProgramForColor(i2);
                    break;
                case 2:
                    try {
                        runProgram(PROGRAM_BREATHING.getBytes(), "03", BREATHING_START_2, BREATHING_START_3);
                    } catch (IOException e) {
                        Log.e(TAG, "Error applying chroma effect", e);
                        break;
                    }
                    break;
                case 3:
                    try {
                        runProgram(PROGRAM_SPECTRUM.getBytes(), "03", SPECTRUM_START_2, SPECTRUM_START_3);
                        break;
                    } catch (IOException e) {
                        Log.e(TAG, "Error applying chroma effect", e);
                        break;
                    }
                case 4:
                    try {
                        runProgram(PROGRAM_WAVE_LTR.getBytes(), WAVE_START_1, WAVE_START_2, WAVE_START_3);
                        break;
                    } catch (IOException e) {
                        Log.e(TAG, "Error applying chroma effect", e);
                        break;
                    }
                case 5:
                    try {
                        runProgram(PROGRAM_WAVE_RTL.getBytes(), WAVE_START_1, WAVE_START_2, WAVE_START_3);
                        break;
                    } catch (IOException e) {
                        Log.e(TAG, "Error applying chroma effect", e);
                        break;
                    }
            }
        }
    }

    private void runUnknownProgram(String str, String str2, String str3, String str4) {
        synchronized (this.mLock) {
            try {
                writeToNode(THS_NODE_MEMORY, str.getBytes());
                writeToNode(THS_NODE_PROG_1_START, str2);
                writeToNode(THS_NODE_PROG_2_START, str3);
                writeToNode(THS_NODE_PROG_3_START, str4);
                writeToNode(THS_NODE_RUN_ENGINE, "1");
            } catch (IOException e) {
                Log.e(TAG, "Error attempting to apply program: " + str, e);
            }
        }
    }

    private void runProgramForColor(int i) {
        runSuspendProgram();
        try {
            int red = Color.red(i);
            int green = Color.green(i);
            int blue = Color.blue(i);
            int[] iArr = {green, blue, green, blue, green, blue, red, red, red};
            for (int i2 = 0; i2 < 9; i2++) {
                String str = THS_NODE_CHANNELS_BRIGHTNESS[i2];
                writeToNode(str, iArr[i2] + "");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error applying color: " + i, e);
        }
    }

    private void runSuspendProgram() {
        synchronized (this.mLock) {
            try {
                writeToNode(THS_NODE_ENGINE_1_MODE, "disabled");
                writeToNode(THS_NODE_ENGINE_2_MODE, "disabled");
                writeToNode(THS_NODE_ENGINE_3_MODE, "disabled");
            } catch (IOException e) {
                Log.e(TAG, "Error suspending LEDs", e);
            }
        }
    }

    private void runStopProgram() {
        synchronized (this.mLock) {
            try {
                readMemory();
                writeToNode(THS_NODE_MASTER_FADER_1, "0");
            } catch (IOException e) {
                Log.e(TAG, "Error stopping program", e);
            }
        }
    }

    private void applyBrightness(int i) {
        synchronized (this.mLock) {
            try {
                writeToNode(THS_NODE_MASTER_FADER_1, i + "");
            } catch (IOException e) {
                Log.e(TAG, "Error applying brightness", e);
            }
        }
    }

    private void applyBrightnessForFader(String str, int i) {
        synchronized (this.mLock) {
            try {
                writeToNode(str, i + "");
            } catch (IOException e) {
                Log.e(TAG, "error applying brightness " + i + "to fader: " + str, e);
            }
        }
    }

    private void setMasterFader(String str) {
        synchronized (this.mLock) {
            try {
                writeToNode(THS_NODE_MASTER_FADER_LEDS, str);
            } catch (IOException e) {
                Log.e(TAG, "Error mapping fader: " + str, e);
            }
        }
    }

    private void runProgram(byte[] bArr, String str, String str2, String str3) throws IOException {
        writeToNode(THS_NODE_MEMORY, bArr);
        writeToNode(THS_NODE_PROG_1_START, str);
        writeToNode(THS_NODE_PROG_2_START, str2);
        writeToNode(THS_NODE_PROG_3_START, str3);
        writeToNode(THS_NODE_RUN_ENGINE, "1");
    }

    private void writeToNode(String str, String str2) throws IOException {
        writeToNode(str, str2.getBytes());
    }

    private void writeToNode(String str, byte[] bArr) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
        fileOutputStream.write(bArr);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private String readMemory() {
        try {
            byte[] bArr = new byte[512];
            int read = new FileInputStream(new File(THS_NODE_MEMORY)).read(bArr);
            if (read == bArr.length) {
                Log.e(TAG, "Too much data");
            }
            return new String(bArr, 0, read);
        } catch (IOException e) {
            Log.wtf(TAG, "Unexpected IOException reading memory", e);
            return null;
        }
    }
}
