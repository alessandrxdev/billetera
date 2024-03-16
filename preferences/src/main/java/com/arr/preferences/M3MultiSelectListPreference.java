package com.arr.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceViewHolder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.HashSet;

public class M3MultiSelectListPreference extends MultiSelectListPreference {

    public M3MultiSelectListPreference(Context context, AttributeSet attr) {
        super(context, attr);
        setLayoutResource(R.layout.layout_view_preference);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        TextView title = (TextView) holder.findViewById(R.id.title);
        if (getTitle() != null) {
            title.setText(getTitle());
        } else {
            title.setVisibility(View.GONE);
        }

        TextView summary = (TextView) holder.findViewById(R.id.summary);
        if (getSummary() != null) {
            summary.setText(getSummary());
        } else {
            summary.setVisibility(View.GONE);
        }

        ImageView icon = (ImageView) holder.findViewById(R.id.icon);
        if (getIcon() != null) {
            icon.setImageDrawable(getIcon());
        } else {
            icon.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onClick() {
        final CharSequence[] entries = getEntries();
        final CharSequence[] entriesValue = getEntryValues();
        final boolean[] selectedItem = new boolean[entriesValue.length];
        final HashSet<String> savedValue = new HashSet<>(getValues());
        for (int i = 0; i < entriesValue.length; ++i) {
            if (savedValue.contains(entriesValue[i].toString())) {
                selectedItem[i] = true;
            }
        }
        new MaterialAlertDialogBuilder(getContext())
                .setTitle(getDialogTitle())
                .setMultiChoiceItems(
                        entries,
                        selectedItem,
                        (dialog, which, isChecked) -> selectedItem[which] = isChecked)
                .setPositiveButton(
                        android.R.string.ok,
                        (dialog, which) -> {
                            HashSet<String> newValue = new HashSet<>();
                            for (int i = 0; i < selectedItem.length; ++i) {
                                if (selectedItem[i]) {
                                    newValue.add(entriesValue[i].toString());
                                }
                            }
                            setValues(newValue);
                        })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
