package com.arr.bancamovil.ui.tool;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.arr.bancamovil.databinding.FragmentCalculateBinding;

import com.arr.bancamovil.utils.preferences.Pref;
import java.text.NumberFormat;
import java.util.Objects;

public class CalculateFragment extends Fragment {

    private FragmentCalculateBinding binding;
    private long total1,
            total3,
            total5,
            total10,
            total20,
            total50,
            total100,
            total200,
            total500,
            totalMil;

    private Pref pref;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle arg2) {
        binding = FragmentCalculateBinding.inflate(inflater, container, false);
        addOptionMenu();

        pref = new Pref(requireContext());

        binding.editOne.addTextChangedListener(one);
        binding.editThree.addTextChangedListener(tres);
        binding.editFive.addTextChangedListener(cinco);
        binding.editTen.addTextChangedListener(dies);
        binding.editTwenty.addTextChangedListener(veinte);
        binding.editFifty.addTextChangedListener(cincuenta);
        binding.editCien.addTextChangedListener(cien);
        binding.editDosciento.addTextChangedListener(dosciento);
        binding.editQuiniento.addTextChangedListener(quinientos);
        binding.editMil.addTextChangedListener(mil);

        // delete
        binding.delete.setOnClickListener(view -> delete());

        // total
        binding.textTotal.setText(pref.loadPrefString("total", "0"));

        return binding.getRoot();
    }

    private void delete() {
        Objects.requireNonNull(binding.editOne.getText()).clear();
        Objects.requireNonNull(binding.editThree.getText()).clear();
        Objects.requireNonNull(binding.editFive.getText()).clear();
        Objects.requireNonNull(binding.editTen.getText()).clear();
        Objects.requireNonNull(binding.editTwenty.getText()).clear();
        Objects.requireNonNull(binding.editFifty.getText()).clear();
        Objects.requireNonNull(binding.editCien.getText()).clear();
        Objects.requireNonNull(binding.editDosciento.getText()).clear();
        Objects.requireNonNull(binding.editQuiniento.getText()).clear();
        Objects.requireNonNull(binding.editMil.getText()).clear();
        pref.prefString("total", "0");
        binding.textTotal.setText("0");
    }

    private void viewTotal() {
        long total =
                total1 + total3 + total5 + total10 + total20 + total50 + total100 + total200
                        + total500 + totalMil;
        NumberFormat format = NumberFormat.getInstance();
        String result = format.format(total);
        binding.textTotal.setText(result);
        pref.prefString("total", result);
    }

    private void addOptionMenu() {
        requireActivity()
                .addMenuProvider(
                        new MenuProvider() {
                            @Override
                            public void onCreateMenu(
                                    @NonNull Menu arg0, @NonNull MenuInflater arg1) {}

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

    private final TextWatcher one =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir

                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String uno = s.toString();
                    if (!uno.isEmpty()) {
                        int cantidad = Integer.parseInt(uno);
                        total1 = 1L * cantidad;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(total1);
                        binding.inputLayoutOne.setHelperText(result);
                    } else {
                        binding.inputLayoutOne.setHelperText(null);
                        total1 = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };

    private final TextWatcher tres =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir

                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String tres = s.toString();
                    if (!tres.isEmpty()) {
                        int billetes = Integer.parseInt(tres);
                        total3 = 3L * billetes;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(total3);
                        binding.inputLayoutThree.setHelperText(result);
                    } else {
                        binding.inputLayoutThree.setHelperText(null);
                        total3 = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };

    private final TextWatcher cinco =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir

                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String cantidad = s.toString();
                    if (!cantidad.isEmpty()) {
                        int billetes = Integer.parseInt(cantidad);
                        total5 = 5L * billetes;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(total5);
                        binding.inputLayoutFive.setHelperText(result);
                    } else {
                        binding.inputLayoutFive.setHelperText(null);
                        total5 = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };

    private final TextWatcher dies =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir

                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String uno = s.toString();
                    if (!uno.isEmpty()) {
                        int billetesUno = Integer.parseInt(uno);
                        total10 = 10L * billetesUno;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(total10);
                        binding.inputLayoutTen.setHelperText(result);
                    } else {
                        binding.inputLayoutTen.setHelperText(null);
                        total10 = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };

    private final TextWatcher veinte =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir

                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String tres = s.toString();
                    if (!tres.isEmpty()) {
                        int billetes = Integer.parseInt(tres);
                        total20 = 20L * billetes;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(total20);
                        binding.inputLayoutTwenty.setHelperText(result);
                    } else {
                        binding.inputLayoutTwenty.setHelperText(null);
                        total20 = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };

    private final TextWatcher cincuenta =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir

                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String cantidad = s.toString();
                    if (!cantidad.isEmpty()) {
                        int billetes = Integer.parseInt(cantidad);
                        total50 = 50L * billetes;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(total50);
                        binding.inputLayoutFifty.setHelperText(result);
                    } else {
                        binding.inputLayoutFifty.setHelperText(null);
                        total50 = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };

    private final TextWatcher cien =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir

                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String uno = s.toString();
                    if (!uno.isEmpty()) {
                        int billetesUno = Integer.parseInt(uno);
                        total100 = 100L * billetesUno;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(total100);
                        binding.inputLayoutCien.setHelperText(result);
                    } else {
                        binding.inputLayoutCien.setHelperText(null);
                        total100 = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };

    private final TextWatcher dosciento =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir

                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String tres = s.toString();
                    if (!tres.isEmpty()) {
                        int billetes = Integer.parseInt(tres);
                        total200 = 200L * billetes;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(total200);
                        binding.inputLayoutDosciento.setHelperText(result);
                    } else {
                        binding.inputLayoutDosciento.setHelperText(null);
                        total200 = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };

    private final TextWatcher quinientos =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir

                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String cantidad = s.toString();
                    if (!cantidad.isEmpty()) {
                        int billetes = Integer.parseInt(cantidad);
                        total500 = 500L * billetes;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(total500);
                        binding.inputLayoutQuiniento.setHelperText(result);
                    } else {
                        binding.inputLayoutQuiniento.setHelperText(null);
                        total500 = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };

    private final TextWatcher mil =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // metodo antes de ecribir
                }

                @Override
                public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                    // metodo mientras escribes
                    String cantidad = s.toString();
                    if (!cantidad.isEmpty()) {
                        int billetes = Integer.parseInt(cantidad);
                        totalMil = 1000L * billetes;
                        NumberFormat format = NumberFormat.getInstance();
                        String result = format.format(totalMil);
                        binding.inputLayoutMil.setHelperText(result);
                    } else {
                        binding.inputLayoutMil.setHelperText(null);
                        totalMil = 0;
                    }
                    viewTotal();
                }

                @Override
                public void afterTextChanged(Editable e) {
                    // metodo despues de que cambie el edittext
                }
            };
}
