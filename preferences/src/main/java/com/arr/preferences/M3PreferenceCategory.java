package com.arr.preferences;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

public class M3PreferenceCategory extends PreferenceCategory {

    // Constructores
    public M3PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public M3PreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.layout_view_preference_category);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        TextView textCategory = (TextView) holder.findViewById(R.id.text_view_category);
        textCategory.setText(getTitle());
        
        holder.itemView.setElevation(5f);
    }
}
