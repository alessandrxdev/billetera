package com.arr.bancamovil.utils.dialog.bills;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.DialogFragment;

import com.arr.bancamovil.R;
import com.arr.bancamovil.databinding.LayoutAddIngresosBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IngresosDialog extends DialogFragment {

    private LayoutAddIngresosBinding binding;
    private String chipSelect, selectDate;
    private View.OnClickListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arg2) {
        binding = LayoutAddIngresosBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // date picker
        binding.editFecha.setOnClickListener(this::openDatePicker);

        String fecha = binding.editFecha.getText().toString();
        if (fecha.isEmpty()) {
            binding.editFecha.setText(getFecha());
            selectDate = getFecha();
        }

        binding.close.setOnClickListener(
                v -> {
                    dismiss();
                });

        // chips select
        binding.chipCash.setOnCheckedChangeListener(
                (chip, isSelected) -> {
                    if (isSelected) {
                        chipSelect = "efectivo";
                    }
                });

        binding.chipBank.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "banco";
                    }
                });

        binding.chipTransfer.setOnCheckedChangeListener(
                (chip, isChecked) -> {
                    if (isChecked) {
                        chipSelect = "transferencia";
                    }
                });

        // posituve buttom
        binding.guardar.setOnClickListener(
                v -> {
                    mListener.onClick(v);
                    dismiss();
                });

        return view;
    }

    public IngresosDialog setPositiveButtom(final View.OnClickListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle arg0) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(root);
        dialog.getWindow()
                .setBackgroundDrawable(
                        new ColorDrawable(getActivity().getColor(R.color.colorBackground)));
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    private void openDatePicker(View view) {
        try {
            Calendar calendar = Calendar.getInstance();
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setSelection(calendar.getTimeInMillis());
            MaterialDatePicker<Long> datePicker = builder.build();
            datePicker.addOnPositiveButtonClickListener(
                    selection -> {
                        // Formatear la fecha seleccionada en el formato "dia mes año"
                        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
                        String formattedDate = sdf.format(new Date(selection));
                        selectDate = formattedDate;
                        // Establecer la fecha formateada en el EditText
                        binding.editFecha.setText(formattedDate);
                    });
            datePicker.show(getActivity().getSupportFragmentManager(), "SHOW_DATE_PICKER");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFecha() {
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
            String formattedDate = sdf.format(calendar.getTime());
            return formattedDate;
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    public String getTextEditText() {
        return binding.cantidad.getText().toString();
    }

    public String getChipsSelect() {
        return chipSelect;
    }

    public String getDate() {
        return selectDate;
    }

    public String getDescription() {
        return binding.editDescription.getText().toString();
    }
}
