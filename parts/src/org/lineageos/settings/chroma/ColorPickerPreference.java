// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.chroma;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import org.lineageos.settings.R;

public class ColorPickerPreference extends Preference implements SeekBar.OnSeekBarChangeListener {

    private int mColor = Color.GREEN;

    private View mPreviewView;
    private TextView mHexText;
    private SeekBar mSeekR, mSeekG, mSeekB;
    private TextView mValR, mValG, mValB;

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.preference_color_widget);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        String hex = getPersistedString(defaultValue != null ? (String) defaultValue : "#00FF00");
        mColor = parseColor(hex);
        setSummary(String.format("#%06X", 0xFFFFFF & mColor));
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View widget = holder.findViewById(R.id.color_widget);
        if (widget != null) {
            GradientDrawable d = new GradientDrawable();
            d.setShape(GradientDrawable.OVAL);
            d.setColor(mColor);
            widget.setBackground(d);
        }
    }

    @Override
    protected void onClick() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_color_picker, null);
        mPreviewView = v.findViewById(R.id.color_preview);
        mHexText     = v.findViewById(R.id.hex_value);
        mSeekR       = v.findViewById(R.id.seekbar_r);
        mSeekG       = v.findViewById(R.id.seekbar_g);
        mSeekB       = v.findViewById(R.id.seekbar_b);
        mValR        = v.findViewById(R.id.value_r);
        mValG        = v.findViewById(R.id.value_g);
        mValB        = v.findViewById(R.id.value_b);

        mSeekR.setMax(255); mSeekR.setProgress(Color.red(mColor));
        mSeekG.setMax(255); mSeekG.setProgress(Color.green(mColor));
        mSeekB.setMax(255); mSeekB.setProgress(Color.blue(mColor));

        mSeekR.setOnSeekBarChangeListener(this);
        mSeekG.setOnSeekBarChangeListener(this);
        mSeekB.setOnSeekBarChangeListener(this);
        updatePreview();

        new AlertDialog.Builder(getContext())
                .setTitle(getTitle())
                .setView(v)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    int color = Color.rgb(mSeekR.getProgress(), mSeekG.getProgress(), mSeekB.getProgress());
                    String hex = String.format("#%06X", 0xFFFFFF & color);
                    if (callChangeListener(hex)) {
                        mColor = color;
                        persistString(hex);
                        setSummary(hex);
                        notifyChanged();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updatePreview();
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override public void onStopTrackingTouch(SeekBar seekBar) {}

    private void updatePreview() {
        int r = mSeekR.getProgress(), g = mSeekG.getProgress(), b = mSeekB.getProgress();
        mPreviewView.setBackgroundColor(Color.rgb(r, g, b));
        mHexText.setText(String.format("#%02X%02X%02X", r, g, b));
        mValR.setText(String.valueOf(r));
        mValG.setText(String.valueOf(g));
        mValB.setText(String.valueOf(b));
    }

    private static int parseColor(String hex) {
        try { return Color.parseColor(hex); }
        catch (IllegalArgumentException e) { return Color.GREEN; }
    }
}
