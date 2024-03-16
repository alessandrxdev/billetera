package com.arr.bancamovil.utils.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.arr.bancamovil.R;
import com.arr.bancamovil.databinding.LayoutItemViewAddBinding;
import com.arr.bancamovil.databinding.LayoutViewItemsCardsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import java.util.ArrayList;
import java.util.List;

public class AddCardsDialog extends BottomSheetDialog {

    private Context mContext;
    private OnItemClickListener itemClickListener;

    public AddCardsDialog(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View content = LayoutInflater.from(mContext).inflate(R.layout.layout_view_add_data, null);
        setContentView(content);
        getWindow().setNavigationBarColor(mContext.getColor(R.color.colorSurface));
        content.setElevation(0f);

        // recyclerview
        List<Items> list = new ArrayList<>();
        RecyclerView recycler = content.findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(
                new Content(
                        getContext(),
                        list,
                        position -> {
                            handleItemClick(position); // click item position
                        }));

        list.add(
                new Items(
                        R.drawable.ic_credit_card_unfilled_24px,
                        getContext().getString(R.string.title_item_add_card)));
        list.add(
                new Items(
                        R.drawable.ic_bus_unfilled_24px,
                        getContext().getString(R.string.title_item_add_pasaje)));
    }

    public AddCardsDialog setItemClick(OnItemClickListener listener) {
        if (listener != null) {
            itemClickListener = listener;
        }
        return this;
    }

    private void handleItemClick(int position) {
        if (itemClickListener != null) {
            itemClickListener.onClick(position);
            dismiss();
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private static class Items {

        private String title;
        private int icon;

        public Items(int icon, String title) {
            this.title = title;
            this.icon = icon;
        }

        public int getIcon() {
            return icon;
        }

        public String getTitle() {
            return title;
        }
    }

    private static class Content extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private List<Items> mList;
        private final OnItemClickListener itemClick;

        public Content(Context context, List<Items> list, OnItemClickListener itemClick) {
            this.mContext = context;
            this.mList = list;
            this.itemClick = itemClick;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutItemViewAddBinding binding =
                    LayoutItemViewAddBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder) {
                Items item = mList.get(position);
                ViewHolder view = (ViewHolder) holder;

                view.binding.icon.setImageResource(item.getIcon());
                view.binding.title.setText(item.getTitle());

                // onclick item
                view.binding
                        .getRoot()
                        .setOnClickListener(
                                v -> {
                                    itemClick.onItemClick(position);
                                });
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private LayoutItemViewAddBinding binding;

            public ViewHolder(LayoutItemViewAddBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
