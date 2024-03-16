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

import java.util.Objects;

public class GastosDialog extends BottomSheetDialog {

    private EditText cantidad;
    private Button bPositive, bNegative;
    private TextView title;
    private Chip alimento, transporte, factura, ocio, ropa, belleza, otros;

    private String chipSelect;

    public GastosDialog(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        @SuppressLint("InflateParams")
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_add_gasto, null);
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
        alimento = view.findViewById(R.id.chip_alimentos);
        alimento.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "alimento";
                    }
                });

        transporte = view.findViewById(R.id.chip_transporte);
        transporte.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "transporte";
                    }
                });

        factura = view.findViewById(R.id.chip_facturas);
        factura.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "factura";
                    }
                });

        ocio = view.findViewById(R.id.chip_ocio);
        ocio.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "ocio";
                    }
                });

        ropa = view.findViewById(R.id.chip_ropa);
        ropa.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "ropa";
                    }
                });

        belleza = view.findViewById(R.id.chip_belleza);
        belleza.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "belleza";
                    }
                });

        otros = view.findViewById(R.id.chip_otros);
        otros.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "otros";
                    }
                });
    }

    // obtener el string del edittext
    public String getTextEditText() {
        String str = cantidad.getText().toString();
        if (str != null) {
            return str;
        } else {
            return null;
        }
    }

    public String getChipSelected() {
        if (chipSelect != null) {
            return chipSelect.toString();
        }
        return null;
    }

    // buttom positivo
    public GastosDialog setPsitiveButtom(final View.OnClickListener listener) {
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
    public GastosDialog setTitle(String string) {
        if (string != null) {
            title.setText(string);
        }
        return this;
    }
}
