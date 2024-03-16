package com.arr.bancamovil.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arr.bancamovil.R;
import com.arr.bancamovil.databinding.LayoutViewItemsCardsBinding;
import com.arr.bancamovil.databinding.LayoutViewNotCreditCardBinding;
import com.arr.bancamovil.databinding.LayoutViewTicketViajandoBinding;
import com.arr.bancamovil.model.DataCards;
import com.arr.bancamovil.utils.card.DataUtils;

import java.util.List;

public class DataCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private List<DataCards> mList;
    private final DataUtils data;
    private final OnLongClickListener longListener;
    private final OnClickListener rootListener;

    private static final int VIEW_TYPE_EMPTY = 0;

    public DataCardAdapter(
            Context context,
            List<DataCards> list,
            OnLongClickListener longListener,
            OnClickListener onClick) {
        this.mContext = context;
        this.mList = list;
        this.longListener = longListener;
        this.rootListener = onClick;
        this.data = new DataUtils(mContext);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<DataCards> filterList) {
        mList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            LayoutViewNotCreditCardBinding binding =
                    LayoutViewNotCreditCardBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new CardEmpty(binding);

        } else if (viewType == DataCards.VIEW_CARD) {
            LayoutViewItemsCardsBinding binding =
                    LayoutViewItemsCardsBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);

            return new Cards(binding);
        }
        throw new RuntimeException("Exception");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof CardEmpty)) {
            if (holder instanceof Cards view) {
                DataCards items = (DataCards) mList.get(position);

                String number = data.loadInfo(position, "card");
                System.out.println(number);
                // numero de tarjeta
                view.binding.cardNumber.setText(items.getTarjeta());

                // background card
                Drawable bg = background(number);
                if (bg != null) view.binding.rootItem.setBackgroundDrawable(bg);

                // logo de el banco
                int logo = logoBank(number);
                if (logo != 0) {
                    view.binding.logoBank.setImageResource(logo);
                }

                // nombre de el banco
                view.binding.nameBank.setText(getNameBank(number));
                // moneda
                view.binding.moneda.setText(items.getMoneda());
                // titular
                view.binding.titular.setText(items.getUsuario());

                // edit card
                view.binding.getRoot().setOnLongClickListener(v -> {
                    longListener.onLongClick(position);
                    return true;
                });

                // copy card
                view.binding
                        .getRoot()
                        .setOnClickListener(
                                v -> rootListener.onClick(position));
            }
        }  //

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable background(String str) {
        if (str.contains("12")) {
            return mContext.getDrawable(R.drawable.gradient_bpa);
        } else if (str.contains("06")) {
            return mContext.getDrawable(R.drawable.gradient_bandec);
        } else if (str.matches(".*74.*|.*95.*")) {
            return mContext.getDrawable(R.drawable.gradient_banmet);
        } else {
            return mContext.getDrawable(R.drawable.gradient_none_card);
        }
    }

    private int logoBank(String str) {
        if (str.contains("12")) {
            return R.drawable.logo_bpa;
        } else if (str.contains("06")) {
            return R.drawable.logo_bandec;
        } else if (str.matches(".*74.*|.*95.*")) {
            return R.drawable.logo_banmet;
        } else {
            return 0;
        }
    }

    private String getNameBank(String str) {
        if (str.contains("12")) {
            return mContext.getString(R.string.name_bank_bpa);
        } else if (str.contains("06")) {
            return mContext.getString(R.string.name_bank_bandec);
        } else if (str.matches(".*74.*|.*95.*")) {
            return mContext.getString(R.string.name_bank_banmet);
        } else {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        if (mList.isEmpty()) {
            return 1;
        } else {
            return mList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.isEmpty()) {
            return VIEW_TYPE_EMPTY;
        } else {
            return mList.get(position).itemViewType();
        }
    }

    // click listener to copy card
    public interface OnClickListener {
        void onClick(int position);
    }

    // long listener to edit card
    public interface OnLongClickListener {
        void onLongClick(int position);
    }

    static class Cards extends RecyclerView.ViewHolder {

        private final LayoutViewItemsCardsBinding binding;

        public Cards(LayoutViewItemsCardsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class CardEmpty extends RecyclerView.ViewHolder {

        LayoutViewNotCreditCardBinding binding;

        public CardEmpty(LayoutViewNotCreditCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class Ticket extends RecyclerView.ViewHolder {

        LayoutViewTicketViajandoBinding binding;

        public Ticket(LayoutViewTicketViajandoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
