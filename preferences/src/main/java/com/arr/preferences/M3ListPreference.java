package com.arr.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import com.arr.preferences.R;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceViewHolder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class M3ListPreference extends ListPreference {

    private String textPremium;
    private boolean showDialog;

    public M3ListPreference(Context context, AttributeSet attr) {
        super(context, attr);
        setLayoutResource(R.layout.layout_view_preference_premium);

        // Typed Array
        TypedArray a =
                context.getTheme().obtainStyledAttributes(attr, R.styleable.AddPremium, 0, 0);
        try {
            textPremium = a.getString(R.styleable.AddPremium_addText);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onClick() {
        if (showDialog) {
            final CharSequence[] entries = getEntries();
            final CharSequence[] entryValues = getEntryValues();
            int selectedIndex = findIndexOfValue(getValue());
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle(getDialogTitle())
                    .setSingleChoiceItems(
                            entries,
                            selectedIndex,
                            (dialog, which) -> {
                                String value = (String) entryValues[which];
                                if (callChangeListener(value)) {
                                    setValue(value);
                                }
                                dialog.dismiss();
                            })
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
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

        // adds text
        TextView add = (TextView) holder.findViewById(R.id.add);
        if (textPremium != null) {
            add.setText(getAddText());
        } else {
            add.setVisibility(View.GONE);
        }
    }

    private String getAddText() {
        return textPremium.toString();
    }

    public M3ListPreference setAddText(String text) {
        this.textPremium = text;
        return this;
    }

    public M3ListPreference showDialog(boolean isShow) {
        this.showDialog = isShow;
        return this;
    }
}
