package com.arr.bancamovil.ui.tool.bill;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arr.bancamovil.R;
import com.arr.bancamovil.adapter.MarketAdapter;
import com.arr.bancamovil.databinding.FragmentBillBinding;
import com.arr.bancamovil.model.Market;
import com.arr.bancamovil.utils.dialog.GastosDialog;
import com.arr.bancamovil.utils.dialog.IngresoDialog;
import com.arr.bancamovil.utils.gastos.GastosUtils;
import com.arr.bancamovil.utils.gastos.listeners.SwipeCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class BillFragment extends Fragment {

    private FragmentBillBinding binding;

    private GastosUtils utils;
    private MarketAdapter adapter;
    private List<Market> mlist;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentBillBinding.inflate(inflater, container, false);
        addOptionMenu();

        /**
         * @GastosUtils maneja todo lo relacionado con alamacenar y obtener la la información en el
         * json con todo lo relacionado con Gastos
         */
        utils = new GastosUtils(requireContext());

        binding.recyclerMarket.setHasFixedSize(true);
        adapter = new MarketAdapter(requireContext(), utils.getList());
        binding.recyclerMarket.setAdapter(adapter);
        binding.recyclerMarket.setLayoutManager(new LinearLayoutManager(requireContext()));

        // swipe
        SwipeCallback callback =
                new SwipeCallback(
                        requireContext(), adapter, position -> utils.deleteItem(position));
        ItemTouchHelper touch = new ItemTouchHelper(callback);
        touch.attachToRecyclerView(binding.recyclerMarket);

        // ingesos and gastos
        binding.ingresos.setOnClickListener(this::onSaveIngreso);
        binding.cardGastos.setOnClickListener(this::onSaveGastos);

        // view total, ingresos and gastos
        if (getGasto() != null) {
            binding.textGastos.setText(getGasto() + " CUP");
        } else {
            binding.textGastos.setText("0.00 CUP");
        }

        if (getIngreso() != null) {
            binding.textIngresos.setText(getIngreso() + " CUP");
        } else {
            binding.textIngresos.setText("0.00 CUP");
        }

        if (getTotal() != null) {
            binding.saldoTotal.setText(getTotal());

            double total = Double.parseDouble(getTotal());
            double result = total / getDays();
            // Redondear el resultado a dos decimales
            double roundedResult = Math.round(result * 100.0) / 100.0;

            String formattedResult = String.format("%.2f", roundedResult);
            StringBuilder builder = new StringBuilder();
            builder.append(getString(R.string.recom_gastos)).append(" ");
            builder.append(formattedResult).append(" CUP");
            String cantidad = builder.toString();
            binding.textRecomendado.setText(cantidad);

        } else {
            binding.saldoTotal.setText("0.00");
        }

        return binding.getRoot();
    }

    private void onSaveIngreso(View view) {
        IngresoDialog dialog = new IngresoDialog(requireContext());
        dialog.setTitle("Ingreso");
        dialog.setPositiveButtom(
                (v -> {
                    String textEditText = dialog.getTextEditText();
                    String chipSelected = dialog.getChipSelected();

                    if (!TextUtils.isEmpty(textEditText) && !TextUtils.isEmpty(chipSelected)) {
                        insertIngreso(textEditText, chipSelected);
                    } else {
                        showToast("No deje la categoría o monto en blanco");
                    }
                }));
        dialog.show();
    }

    private void onSaveGastos(View view) {
        GastosDialog dialog = new GastosDialog(requireContext());
        dialog.setTitle("Gastos");
        dialog.setPsitiveButtom(
                (v -> {
                    String textEditText = dialog.getTextEditText();
                    String chipSelected = dialog.getChipSelected();

                    if (!TextUtils.isEmpty(textEditText) && !TextUtils.isEmpty(chipSelected)) {
                        insertGastos(textEditText, chipSelected);
                    } else {
                        showToast("No deje la categoría o monto en blanco");
                    }
                }));
        dialog.show();
    }

    /*
    Debo pasar los int a double para poder insertar los puntos

    */

    private void insertIngreso(String monto, String category) {
        double newMonto = Double.parseDouble(monto);
        String mTotal = getTotal();
        String mGastos = getGasto();
        String mIngresos = getIngreso();

        // Verificar si mTotal es nulo o está vacío
        if (mTotal == null || mTotal.trim().isEmpty()) {
            mTotal = "0";
        }
        if (mGastos == null || mGastos.trim().isEmpty()) {
            mGastos = "0";
        }
        if (mIngresos == null || mIngresos.trim().isEmpty()) {
            mIngresos = "0";
        }

        double doubleTotal = Double.parseDouble(mTotal);
        double doubleGasto = Double.parseDouble(mGastos);
        double doubleIngreso = Double.parseDouble(mIngresos);

        double suma = doubleTotal + newMonto;
        binding.saldoTotal.setText(String.valueOf(suma));

        double ingreso = doubleIngreso + newMonto;
        binding.textIngresos.setText(String.valueOf(ingreso));

        utils.saveData(
                String.valueOf(suma),
                mGastos,
                String.valueOf(ingreso),
                String.valueOf(monto),
                "income",
                category);

        // uodate
        adapter.updateList(utils.getList());
    }

    private void insertGastos(String monto, String category) {
        adapter.notifyDataSetChanged();
        double newMonto = Double.parseDouble(monto);
        String mTotal = getTotal();
        String mGastos = getGasto();
        String mIngresos = getIngreso();

        // Verificar si mTotal es nulo o está vacío
        if (mTotal == null || mTotal.trim().isEmpty()) {
            mTotal = "0";
        }
        if (mGastos == null || mGastos.trim().isEmpty()) {
            mGastos = "0";
        }
        if (mIngresos == null || mIngresos.trim().isEmpty()) {
            mIngresos = "0";
        }

        double doubleTotal = Double.parseDouble(mTotal);
        double doubleGasto = Double.parseDouble(mGastos);
        double doubleIngreso = Double.parseDouble(mIngresos);

        double resta = doubleTotal - newMonto;
        binding.saldoTotal.setText(String.valueOf(resta));

        double gasto = doubleGasto + newMonto;
        binding.textGastos.setText(String.valueOf(gasto));

        utils.saveData(
                String.valueOf(resta),
                String.valueOf(gasto),
                mIngresos,
                String.valueOf(newMonto),
                "bills",
                category);

        // uodate
        adapter.updateList(utils.getList());
    }

    private void deleteData() {
        utils.delete();
        adapter.updateList(utils.getList());
        binding.textGastos.setText("0.00 CUP");
        binding.textIngresos.setText("0.00 CUP");
        binding.saldoTotal.setText("0.00");
        binding.textRecomendado.setText("");
    }

    private void addOptionMenu() {
        requireActivity()
                .addMenuProvider(
                        new MenuProvider() {
                            @Override
                            public void onCreateMenu(
                                    @NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                                menuInflater.inflate(R.menu.delete_menu, menu);

                                ImageView delete =
                                        (ImageView)
                                                menu.findItem(R.id.menu_delete)
                                                        .getActionView()
                                                        .findViewById(R.id.delete);
                                delete.setOnClickListener(
                                        view -> {
                                            deleteData();
                                        });
                            }

                            @Override
                            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                                int id = menuItem.getItemId();
                                if (id == android.R.id.home) {
                                    NavController nav = Navigation.findNavController(requireView());
                                    nav.navigateUp();
                                }
                                return true;
                            }
                        },
                        getViewLifecycleOwner(),
                        Lifecycle.State.RESUMED);
    }

    private NavController navigateTo() {
        return Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment_activity_main);
    }

    private NavOptions options() {
        return new NavOptions.Builder().setLaunchSingleTop(true).build();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    private String formatNumber(String string) {
        if (string != null) {
            try {
                double number = Double.parseDouble(string);
                DecimalFormat formatoGeneral = new DecimalFormat("###,###");
                DecimalFormat formatoMillones = new DecimalFormat("###.###M");

                DecimalFormat formatoFinal = (number >= 1000000) ? formatoMillones : formatoGeneral;
                String numeroFormateado = formatoFinal.format(number);
                return numeroFormateado;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // total
    private String getTotal() {
        return utils.getString("total");
    }

    // gastos
    private String getGasto() {
        return utils.getString("gasto");
    }

    // ingreso
    private String getIngreso() {
        return utils.getString("ingreso");
    }

    // obtener la cantidad de dias del mes
    private double getDays() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return (double) cal.get(Calendar.DAY_OF_MONTH);
    }
}
