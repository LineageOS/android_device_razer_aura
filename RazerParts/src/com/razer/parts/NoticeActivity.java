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

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class NoticeActivity extends Activity {
    Boolean unlock = false;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_notice);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        new Thread(() -> {
            try {
                Thread.sleep(10000);
                synchronized(unlock) {
                    unlock = true;
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
            }  
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            synchronized(unlock) {
                if(unlock) {
                    SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
                    sharedPreferenceUtil.put(this, "first_ref_shown", true);
                    finish();
                } else {
                    Toast.makeText(NoticeActivity.this, R.string.pref_first_not_yet, Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}