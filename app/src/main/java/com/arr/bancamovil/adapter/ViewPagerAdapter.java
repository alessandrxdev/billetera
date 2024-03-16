package com.arr.bancamovil.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.arr.bancamovil.R;
import androidx.recyclerview.widget.RecyclerView;

public class ViewPagerAdapter extends RecyclerView.Adapter<PagerVH> {

    @Override
    public PagerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_item_view_page_premium, parent, false);
        return new PagerVH(itemView);
    }

    // Get the size of color array
    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    // Binding the screen with view
    @Override
    public void onBindViewHolder(PagerVH holder, int position) {
        View itemView = holder.itemView;
        if (position == 0) {
            TextView title = itemView.findViewById(R.id.title);
            TextView subtitle = itemView.findViewById(R.id.subtitle);
            ImageView icon = itemView.findViewById(R.id.icon);
            ViewGroup container = itemView.findViewById(R.id.container);

            //
            title.setText("Tasas");
            subtitle.setText("Obtenga las tasas de cambio automáticamente.");
            icon.setImageResource(R.drawable.vector_tasas);
        }
        if (position == 1) {
            TextView title = itemView.findViewById(R.id.title);
            TextView subtitle = itemView.findViewById(R.id.subtitle);
            ImageView icon = itemView.findViewById(R.id.icon);
            ViewGroup container = itemView.findViewById(R.id.container);

            //
            title.setText("Actualizar");
            subtitle.setText("Obtenga las tasas de cambio automáticamente.");
            icon.setImageResource(R.drawable.vector_update);
        }

        /*
        || position == 1 || position == 2 || position == 3) {

            ViewGroup container = itemView.findViewById(R.id.container);

            tvTitle.setText("ViewPager2");
            tvAbout.setText("In this application we will learn about ViewPager2");
            ivImage.setImageResource(R.drawable.face_image);
            container.setBackgroundResource(colors[position % colors.length]);

            */
    }
}

class PagerVH extends RecyclerView.ViewHolder {

    public PagerVH(View itemView) {
        super(itemView);
    }
}
