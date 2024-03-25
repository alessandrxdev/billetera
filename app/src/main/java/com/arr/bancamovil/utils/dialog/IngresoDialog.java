package com.arr.bancamovil.utils.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.arr.bancamovil.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class IngresoDialog extends BottomSheetDialog {

    private EditText cantidad;
    private Button bPositive, bNegative;
    private TextView title;

    private Chip efectivo, banco, transferencia;

    private String chipSelect;

    public IngresoDialog(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        @SuppressLint("InflateParams")
        View view =
                LayoutInflater.from(getContext())
                        .inflate(R.layout.layout_view_bs_add_ingresos, null);
        setContentView(view);
        View bsheet =
                Objects.requireNonNull(getWindow())
                        .findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // navigationBar Color
        Objects.requireNonNull(getWindow())
                .setNavigationBarColor(getContext().getColor(R.color.colorSurface));

        // textinputEditText
        cantidad = view.findViewById(R.id.cantidad);

        // positive buttom
        bPositive = view.findViewById(R.id.b_positive);
        bNegative = view.findViewById(R.id.b_negative);
        bNegative.setOnClickListener(
                v -> {
                    dismiss();
                });

        // title
        title = view.findViewById(R.id.title_dialog);

        // chips
        efectivo = view.findViewById(R.id.chip_cash);
        efectivo.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "efectivo";
                    }
                });

        banco = view.findViewById(R.id.chip_bank);
        banco.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "banco";
                    }
                });

        transferencia = view.findViewById(R.id.chip_transfer);
        transferencia.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "transferencia";
                    }
                });
    }

    // obtener el string del edittext
    public String getTextEditText() {
        return cantidad.getText().toString();
    }

    // buttom positivo
    public IngresoDialog setPositiveButtom(final View.OnClickListener listener) {
        bPositive.setOnClickListener(
                view -> {
                    if (listener != null) {
                        listener.onClick(view);
                    }
                    dismiss();
                });
        return this;
    }

    // insertar title al buttom prefeference
    public IngresoDialog setTitle(String string) {
        if (string != null) {
            title.setText(string);
        }
        return this;
    }

    // chips
    public String getChipSelected() {
        if (chipSelect != null) {
            return chipSelect.toString();
        }
        return null;
    }
}
