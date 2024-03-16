package com.arr.bancamovil.adapter;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arr.bancamovil.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.arr.bancamovil.databinding.LayoutItemsViewTipsBinding;
import com.arr.bancamovil.model.Tips;
import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tips> mList;

    public TipsAdapter(List<Tips> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemsViewTipsBinding binding =
                LayoutItemsViewTipsBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
        return new TipsView(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TipsView view) {
            Tips model = mList.get(position);

            view.binding.title.setText(model.getTitle());
            view.binding.message.setText(model.getMessage());
            view.binding.message.setMovementMethod(LinkMovementMethod.getInstance());

            boolean isExpanded = model.isExpanded();
            view.binding.message.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            view.binding.expand.setImageResource(
                    isExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TipsView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LayoutItemsViewTipsBinding binding;

        public TipsView(LayoutItemsViewTipsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(this);
            binding.expand.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Tips item = mList.get(position);
                item.setExpanded(!item.isExpanded());
                notifyItemChanged(position);
            }
        }
    }
}
