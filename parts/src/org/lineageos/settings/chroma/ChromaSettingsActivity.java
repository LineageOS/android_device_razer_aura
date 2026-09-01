// SPDX-FileCopyrightText: Alcatraz323 <alcatraz32323@gmail.com>
// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.chroma;

import android.os.Bundle;

import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;

public class ChromaSettingsActivity extends CollapsingToolbarBaseActivity {

    private static final String TAG_CHROMA = "chroma";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(com.android.settingslib.collapsingtoolbar.R.id.content_frame,
                        new ChromaFragment(), TAG_CHROMA)
                .commit();
    }
}
