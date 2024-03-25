package com.arr.bancamovil.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.arr.bancamovil.R;
import androidx.recyclerview.widget.RecyclerView;
import com.arr.bancamovil.databinding.LayoutViewHeaderBinding;
import com.arr.bancamovil.databinding.LayoutViewTasasBinding;
import com.arr.bancamovil.databinding.LayoutViewTasasEmptyBinding;
import com.arr.bancamovil.model.Header;
import com.arr.bancamovil.model.Items;
import com.arr.bancamovil.model.Tasas;
import java.util.List;

public class TasasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Items> mList;
    private Context mContext;

    private static final int VIEW_EMPTY = 2;
    private OnClickListener mListener;

    public TasasAdapter(Context context, List<Items> list, OnClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Tasas.VIEW_TASAS) {
            LayoutViewTasasBinding binding =
                    LayoutViewTasasBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new TasasView(binding);
        } else if (viewType == Header.VIEW_HEADER) {
            LayoutViewHeaderBinding binding =
                    LayoutViewHeaderBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new HeaderView(binding);

            /*
            } else if (viewType == VIEW_EMPTY) {
                LayoutViewTasasEmptyBinding view =
                        LayoutViewTasasEmptyBinding.inflate(
                                LayoutInflater.from(parent.getContext()), parent, false);
                return new TasasEmpty(view);

                */
        }
        throw new RuntimeException("Exception TasasAdapter");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TasasView) {
            Tasas tasas = (Tasas) mList.get(position);
            TasasView view = (TasasView) holder;

            view.binding.moneda.setText(tasas.getMoneda());
            view.binding.buy.setText(tasas.getCompra());
            view.binding.sell.setText(tasas.getVenta());

            // icon
            Drawable icon = getIcon(tasas.getMoneda());
            if (icon != null) {
                view.binding.icon.setImageDrawable(icon);
            }
            String money = getMoney(tasas.getMoneda());
            if (money != null) {
                view.binding.oneMoney.setText(money);
            }

            view.binding.getRoot().setOnClickListener(v -> mListener.onClick(position));
            
            
        } else if (holder instanceof HeaderView) {
            Header header = (Header) mList.get(position);
            HeaderView view = (HeaderView) holder;

            view.binding.textHeader.setText(header.getHeader());
            /*
                    } else if (holder instanceof TasasEmpty) {
            */
        }
    }

    private Drawable getIcon(String string) {
        if (string.equals("USD")) {
            return mContext.getDrawable(R.drawable.ic_flag_usa_56dp);
        } else if (string.equals("EUR")) {
            return mContext.getDrawable(R.drawable.ic_flag_europe_56dp);
        } else if (string.equals("MLC")) {
            return mContext.getDrawable(R.drawable.ic_credit_card_mlc_56dp);
        } else if (string.equals("CHF")) {
            return mContext.getDrawable(R.drawable.ic_flag_switzland_56dp);
        } else if (string.equals("JPY")) {
            return mContext.getDrawable(R.drawable.ic_flag_japan_56dp);
        } else if (string.equals("MXN")) {
            return mContext.getDrawable(R.drawable.ic_flag_mexico_56dp);
        } else if (string.equals("GBP")) {
            return mContext.getDrawable(R.drawable.ic_flag_united_kingdom_56dp);
        } else if (string.equals("CAD")) {
            return mContext.getDrawable(R.drawable.ic_flag_canada_56dp);
        }
        return null;
    }

    private String getMoney(String string) {
        if (string.equals("USD")) {
            return "1 USD";
        } else if (string.equals("EUR")) {
            return "1 EUR";
        } else if (string.equals("MLC")) {
            return "1 MLC";
        } else if (string.equals("CHF")) {
            return "1 CHF";
        } else if (string.equals("JPY")) {
            return "1 JPY";
        } else if (string.equals("MXN")) {
            return "1 MXN";
        } else if (string.equals("GBP")) {
            return "1 GBP";
        } else if (string.equals("CAD")) {
            return "1 CAD";
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).itemViewType();
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    class TasasView extends RecyclerView.ViewHolder {

        private LayoutViewTasasBinding binding;

        public TasasView(LayoutViewTasasBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class HeaderView extends RecyclerView.ViewHolder {

        private LayoutViewHeaderBinding binding;

        public HeaderView(LayoutViewHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class TasasEmpty extends RecyclerView.ViewHolder {

        private LayoutViewTasasEmptyBinding binding;

        public TasasEmpty(LayoutViewTasasEmptyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
