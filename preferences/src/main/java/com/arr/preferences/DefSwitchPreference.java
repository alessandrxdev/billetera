package com.arr.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreferenceCompat;
import com.google.android.material.materialswitch.MaterialSwitch;

public class DefSwitchPreference extends SwitchPreferenceCompat {

    public DefSwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.layout_def_switch_preference_m3);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        MaterialSwitch materialSwitch = (MaterialSwitch) holder.findViewById(R.id.switchWidget);
        if (getTitle() != null && materialSwitch != null) {
            materialSwitch.setChecked(isChecked());
        }

        TextView title = (TextView) holder.findViewById(R.id.title);
        if (getTitle() != null) {
            title.setText(getTitle());
        } else {
            title.setVisibility(View.GONE);
        }

        // summary preference
        TextView summary = (TextView) holder.findViewById(R.id.summary);
        if (getSummary() != null) {
            summary.setText(getSummary());
        } else {
            summary.setVisibility(View.GONE);
        }

        // icon
        ImageView icon = (ImageView) holder.findViewById(R.id.icon);
        if (getIcon() != null) {
            icon.setImageDrawable(getIcon());
        } else {
            icon.setVisibility(View.GONE);
        }
    }
}
