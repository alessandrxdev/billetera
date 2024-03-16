package com.arr.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.arr.preferences.R;

public class M3Preference extends Preference {

    public M3Preference(Context context, AttributeSet attr) {
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
}
