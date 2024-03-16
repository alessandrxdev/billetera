package com.arr.bancamovil.utils.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.arr.bancamovil.R;
import com.arr.bancamovil.databinding.LayoutItemsTransactionBinding;
import com.arr.bancamovil.utils.exchange.Transactions;
import com.arr.bancamovil.utils.exchange.interfaces.TransferListener;

import com.arr.bancamovil.utils.exchange.model.Items;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import java.util.ArrayList;
import java.util.List;

public class TransactionDialog extends BottomSheetDialog implements TransferListener {

    private Context mContext;
    private Transactions transfer;
    private List<Items> mList = new ArrayList<>();
    private Data adapter;
    private String tarjeta;

    public TransactionDialog(Context context) {
        super(context);
        this.mContext = context;
        this.transfer = new Transactions(mContext, this);
        init();
    }

    private void init() {
        View content =
                LayoutInflater.from(mContext).inflate(R.layout.layout_view_transacciones, null);
        setContentView(content);
        getWindow().setNavigationBarColor(getContext().getColor(R.color.colorSurface));
        adapter = new Data(mContext, mList);
        RecyclerView recycler = content.findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(adapter);
    }

    public TransactionDialog setTarjeta(String card) {
        if (card != null) {
            this.tarjeta = card;
            transfer.processSms(card);
        }
        return this;
    }

    private static class Data extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Items> list;
        private Context mContext;
        private OnClickListener mListener;

        public Data(Context context, List<Items> list) {
            this.mContext = context;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutItemsTransactionBinding binding =
                    LayoutItemsTransactionBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new DataViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof DataViewHolder) {
                Items item = list.get(position);
                DataViewHolder view = (DataViewHolder) holder;

                view.binding.icon.setImageResource(item.getIcon());
                view.binding.textBeneficiario.setText(item.getBeneficiario());
                view.binding.textMonto.setText(item.getCantidad());
                view.binding.textId.setText(item.getId());

                // copy id transaccion to clipboard
                view.binding
                        .getRoot()
                        .setOnClickListener(
                                v -> {
                                    copyCreditCard(item.getId());
                                });
            }
        }

        private void copyCreditCard(String id) {
            ClipboardManager clipboard =
                    (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("No. Transacción ", id);
            clipboard.setPrimaryClip(clip);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public interface OnClickListener {
            void onClickCopyId(int position);
        }

        class DataViewHolder extends RecyclerView.ViewHolder {

            private LayoutItemsTransactionBinding binding;

            public DataViewHolder(LayoutItemsTransactionBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    @Override
    public void onDataExtracted(
            String beneficiario, String transaccion, String monto, String tiempo) {
        mList.add(new Items(R.drawable.ic_send_transfer_24px, "- " + monto, transaccion, tiempo));
        adapter.notifyDataSetChanged();
    }
}
