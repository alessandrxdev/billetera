package com.arr.bancamovil.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.arr.bancamovil.databinding.LayoutViewItemToolsBinding;
import com.arr.bancamovil.model.Tools;
import java.util.List;

public class ToolsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tools> list;

    public ToolsAdapter(List<Tools> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutViewItemToolsBinding binding =
                LayoutViewItemToolsBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
        return new ToolsView(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ToolsView) {
            Tools model = list.get(position);
            ToolsView view = (ToolsView) holder;

            view.binding.icon.setImageResource(model.icon());
            view.binding.title.setText(model.getTitle());
            
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ToolsView extends RecyclerView.ViewHolder {

        private LayoutViewItemToolsBinding binding;

        public ToolsView(LayoutViewItemToolsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
