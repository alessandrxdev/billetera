package com.arr.bancamovil.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.arr.bancamovil.databinding.LayoutItemViewPagePremiumBinding;
import com.arr.bancamovil.model.Premium;
import java.util.List;

public class PremiumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Premium> mList;

    public PremiumAdapter(List<Premium> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutItemViewPagePremiumBinding view =
                LayoutItemViewPagePremiumBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
        return new PremiumView(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PremiumView view) {
            Premium model = mList.get(position);

            view.v.icon.setImageResource(model.getIcon());
            view.v.title.setText(model.getTitle());
            view.v.subtitle.setText(model.getSubtitle());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PremiumView extends RecyclerView.ViewHolder {

        private LayoutItemViewPagePremiumBinding v;

        public PremiumView(LayoutItemViewPagePremiumBinding v) {
            super(v.getRoot());
            this.v = v;
        }
    }
}
