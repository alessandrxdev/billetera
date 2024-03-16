package com.arr.bancamovil.utils.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.arr.bancamovil.R;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import com.arr.bancamovil.adapter.PremiumAdapter;
import com.arr.bancamovil.adapter.ViewPagerAdapter;
import com.arr.bancamovil.databinding.LayoutViewPayPremiumBinding;
import com.arr.bancamovil.model.Premium;
import java.util.ArrayList;
import java.util.List;

public class PremiumDialog extends DialogFragment {

    private LayoutViewPayPremiumBinding binding;
    private PremiumAdapter adapter;
    private List<Premium> list = new ArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LayoutViewPayPremiumBinding.inflate(inflater, container, false);

        
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PremiumAdapter(list);
        binding.recycler.setAdapter(adapter);
        
        list.add(new Premium(R.drawable.ic_transfer_24px, "Tasas de Cambio", "Actualice las tasas de cambio automáticamente"));
        list.add(new Premium(R.drawable.ic_gastos, "Gastos", "Manege sus gastos de forma sencilla"));
        list.add(new Premium(R.drawable.ic_card_fill_24px, "Tarjetas", "Almacenamiento de tarjetas ilimitado"));
        list.add(new Premium(R.drawable.ic_gastos, "Funciones", "Podrá disfrutar de las próximas funciones que sean de pago"));
        
        binding.checkPremium.setChecked(true);
        
        binding.close.setOnClickListener(
                view -> {
                    dismiss();
                });
        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
