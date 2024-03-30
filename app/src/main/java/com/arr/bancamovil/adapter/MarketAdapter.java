package com.arr.bancamovil.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.arr.bancamovil.R;
import androidx.recyclerview.widget.RecyclerView;
import com.arr.bancamovil.databinding.LayoutItemsViewMarketBinding;
import com.arr.bancamovil.databinding.LayoutViewMarketEmptyBinding;
import com.arr.bancamovil.model.Market;
import com.arr.bancamovil.utils.bills.BillsData;
import com.arr.bancamovil.utils.gastos.GastosUtils;
import com.tooltip.OnClickListener;
import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Market> mList;

    private static final int VIEW_EMPTY = 0;
    private static final int VIEW_MARKET = 1;

    private BillsData data;

    private OnClickListener mListener;

    public MarketAdapter(Context context, List<Market> newList, OnClickListener listener) {
        this.mContext = context;
        this.mList = newList;
        this.mListener = listener;
        data = new BillsData(mContext);
    }

    public void updateList(List<Market> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_MARKET) {
            LayoutItemsViewMarketBinding view =
                    LayoutItemsViewMarketBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new MarketView(view);
        } else if (viewType == VIEW_EMPTY) {
            LayoutViewMarketEmptyBinding view =
                    LayoutViewMarketEmptyBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new MarketEmpty(view);
        }
        throw new RuntimeException("Market exception");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MarketView view) {
            Market model = mList.get(position);

            view.binding.monto.setText(model.getSaldo());
            view.binding.categoria.setText(model.getTipo());
            view.binding.date.setText(model.getDate());

            // icon categoria
            Drawable iconCategory = getIconCategory(model.getTipo());
            if (iconCategory != null) {
                view.binding.iconCategory.setImageDrawable(iconCategory);
            }

            int colorCat = getColorCategory(model.getTipo());
            if (colorCat != 0) {
                view.binding.card.setCardBackgroundColor(colorCat);
            }

            String type = data.getStringArray("type", position);
            if (type != null) {
                Drawable ic = getIconType(type);
                if (ic != null) {
                    view.binding.icon.setImageDrawable(ic);
                }
                int bg = getBgIcon(type);
                if (bg != 0) {
                    view.binding.icon.setColorFilter(bg);
                }
            }

            view.binding.item.setOnClickListener(v -> mListener.onClick(position));
        }
    }

    private Drawable getIconCategory(String category) {
        switch (category) {
            case "alimento":
                return mContext.getDrawable(R.drawable.ic_food_24px);
            case "transporte":
                return mContext.getDrawable(R.drawable.ic_transporte_24px);
            case "factura":
                return mContext.getDrawable(R.drawable.ic_facturas_24px);
            case "ocio":
                return mContext.getDrawable(R.drawable.ic_ocio_24px);
            case "ropa":
                return mContext.getDrawable(R.drawable.ic_ropa_24px);
            case "belleza":
                return mContext.getDrawable(R.drawable.ic_belleza_24px);
            case "otros":
                return mContext.getDrawable(R.drawable.ic_oters_24px);
            case "efectivo":
                return mContext.getDrawable(R.drawable.ic_cash_24px);
            case "transferencia":
                return mContext.getDrawable(R.drawable.ic_transfer_24px);
            case "banco":
                return mContext.getDrawable(R.drawable.ic_bank_24px);
            default:
                return null;
        }
    }

    private int getColorCategory(String category) {
        switch (category) {
            case "alimento":
                return mContext.getColor(R.color.color_alimentos);
            case "transporte":
                return mContext.getColor(R.color.color_transporte);
            case "factura":
                return mContext.getColor(R.color.color_factura);
            case "ocio":
                return mContext.getColor(R.color.color_ocio);
            case "ropa":
                return mContext.getColor(R.color.color_ropa);
            case "belleza":
                return mContext.getColor(R.color.color_belleza);
            case "otros":
                return mContext.getColor(R.color.color_oters);
            default:
                return mContext.getColor(R.color.color_recive);
        }
    }

    private Drawable getIconType(String type) {
        if (type.equals("income")) {
            return mContext.getDrawable(R.drawable.ic_receive);
        } else if (type.equals("bills")) {
            return mContext.getDrawable(R.drawable.ic_send);
        } else {
            return null;
        }
    }

    private int getBgIcon(String type) {
        switch (type) {
            case "income":
                return mContext.getColor(R.color.recive);
            case "bills":
                return mContext.getColor(R.color.send);
            default:
                return 0;
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null || mList.isEmpty()) {
            return 1;
        } else {
            return mList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList == null || mList.isEmpty()) {
            return VIEW_EMPTY;
        } else {
            return VIEW_MARKET;
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public static class MarketView extends RecyclerView.ViewHolder {

        private LayoutItemsViewMarketBinding binding;

        public MarketView(LayoutItemsViewMarketBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ImageView getDeleteIcon() {
            return binding.delete;
        }
    }

    class MarketEmpty extends RecyclerView.ViewHolder {

        private LayoutViewMarketEmptyBinding binding;

        public MarketEmpty(LayoutViewMarketEmptyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
