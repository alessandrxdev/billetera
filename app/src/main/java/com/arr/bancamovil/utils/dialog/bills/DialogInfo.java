package com.arr.bancamovil.utils.dialog.bills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.arr.bancamovil.R;
import androidx.annotation.NonNull;
import com.arr.bancamovil.databinding.LayoutViewInfoBillBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.Objects;

public class DialogInfo extends BottomSheetDialog {

    private LayoutViewInfoBillBinding binding;

    public DialogInfo(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        binding = LayoutViewInfoBillBinding.inflate(LayoutInflater.from(getContext()));
        View view = binding.getRoot();
        setContentView(view);
        View bsheet =
                Objects.requireNonNull(getWindow())
                        .findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // navigationBar Color
        Objects.requireNonNull(getWindow())
                .setNavigationBarColor(getContext().getColor(R.color.colorSurface));
    }

    public DialogInfo setMonto(String monto) {
        if (monto != null) {
            binding.monto.setText(monto);
        }
        return this;
    }

    public DialogInfo setNote(String string) {
        if (string != null) {
            binding.description.setText(string);
        }
        return this;
    }
}
