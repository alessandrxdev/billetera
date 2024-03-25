package com.arr.bancamovil.utils.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import com.arr.bancamovil.R;
import com.arr.bancamovil.adapter.convert.ConvertAdapter;
import com.arr.bancamovil.databinding.LayoutViewConvertBinding;
import com.arr.bancamovil.model.convert.Moneda;
import com.arr.bancamovil.utils.tasas.TasasUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

public class ConvertDialog extends DialogFragment {

    private ConvertAdapter adapter;

    private String[] moneda = {"USD", "EUR", "MLC"};
    private int[] icon = {
        R.drawable.ic_flag_usa_56dp, R.drawable.ic_flag_europe_56dp, R.drawable.ic_credit_cards
    };

    private String[] moneda2 = {"CUP"};
    private int[] icon2 = {R.drawable.ic_flag_cuba_56dp};
    private TasasUtils tasas;

    private EditText edittext, edittext2;

    private String selectedCurrency;

    private TextView message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arg2) {
        View view = inflater.inflate(R.layout.layout_view_convert, container, false);

        tasas = new TasasUtils(requireContext());
        edittext = view.findViewById(R.id.editext);
        edittext2 = view.findViewById(R.id.editext2);

        AppCompatSpinner spinner = view.findViewById(R.id.spinner);
        adapter = new ConvertAdapter(requireContext(), icon, moneda);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        selectedCurrency = moneda[position];
                        try {
                            JSONObject informal =
                                    new JSONObject(tasas.getFileJson().getString("informal"));
                            if (informal.has(selectedCurrency)) {
                                JSONObject object = informal.getJSONObject(selectedCurrency);
                                double buy = object.getDouble("buy");
                                edittext2.setText(String.valueOf(buy));
                                edittext.setText("1");
                                updateMessage();
                            }
                        } catch (Exception err) {
                            // Manejar la excepción adecuadamente
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

        AppCompatSpinner cup = view.findViewById(R.id.spinner2);
        adapter = new ConvertAdapter(requireContext(), icon2, moneda2);
        cup.setAdapter(adapter);

        edittext.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // No se necesita implementar este método
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // No se necesita implementar este método
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String cantidadString = s.toString();
                        if (cantidadString.isEmpty()) {
                            cantidadString = "0";
                        }
                        try {
                            double cantidad = Double.parseDouble(cantidadString);

                            JSONObject informal =
                                    new JSONObject(tasas.getFileJson().getString("informal"));
                            if (informal.has(selectedCurrency)) {
                                JSONObject object = informal.getJSONObject(selectedCurrency);
                                double buy = object.getDouble("buy");
                                double total = cantidad * buy;
                                edittext2.setText(String.valueOf(total));
                            }
                        } catch (Exception err) {
                            // Manejar la excepción adecuadamente
                        }
                    }
                });

        message = view.findViewById(R.id.text_message);
        updateMessage();

        return view;
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

    private void updateMessage() {
        if (selectedCurrency != null) {
            message.setText("1 CUP = " + getBuyMoneda(selectedCurrency) + " " + selectedCurrency +" al tipo de cambio del mercado informal.");
        } else {
            message.setText("Error: Moneda no seleccionada");
        }
    }

    private double getBuyMoneda(String currency) {
        try {
            JSONObject informal = new JSONObject(tasas.getFileJson().getString("informal"));
            if (informal.has(currency)) {
                JSONObject object = informal.getJSONObject(currency);
                return object.getDouble("buy");
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return 0;
    }
}
