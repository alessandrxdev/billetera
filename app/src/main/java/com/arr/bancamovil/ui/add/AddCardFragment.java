package com.arr.bancamovil.ui.add;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;

import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.arr.bancamovil.R;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import com.arr.bancamovil.databinding.FragmentAddBinding;
import com.arr.bancamovil.utils.card.DataUtils;
import com.arr.bancamovil.utils.dialog.NumberPickerDialog;


public class AddCardFragment extends Fragment {

    private FragmentAddBinding binding;
    private DataUtils cards;
    private Menu mMenu;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        addOptionMenu(); // options menu

        // Data cards utils
        cards = new DataUtils(requireContext());

        // validation
        binding.editUser.addTextChangedListener(new ValidationTextWatcher(binding.editUser));
        binding.editCard.addTextChangedListener(new ValidationTextWatcher(binding.editCard));
        binding.editMoneda.addTextChangedListener(new ValidationTextWatcher(binding.editMoneda));
        binding.editCard.addTextChangedListener(new CardFormatting()); // format number card

        // number picker
        binding.editVence.setOnClickListener(
                v -> {
                    new NumberPickerDialog(requireContext())
                            .setPositiveButtom(
                                    (view, year, month, day) -> {
                                        String yearFormat = String.valueOf(year).substring(2);
                                        String monthFormat = String.format("%02d", (month));
                                        binding.editVence.setText(monthFormat + "/" + yearFormat);
                                    })
                            .show();
                });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void saveDataCard() {
        String titular = binding.editUser.getText().toString().trim();
        String tarjeta = binding.editCard.getText().toString().replaceAll("\\s", "").trim();
        String moneda = binding.editMoneda.getText().toString().trim();
        String vence = binding.editVence.getText().toString().trim();
        String phone = binding.editPhone.getText().toString().trim();
        cards.saveData(titular, tarjeta, moneda, vence, phone);
    }

    private boolean performValidations() {
        String titular = binding.editUser.getText().toString().trim();
        String tarjeta = binding.editCard.getText().toString().trim();
        String moneda = binding.editMoneda.getText().toString().trim();

        boolean titularValid = !TextUtils.isEmpty(titular) && titular.length() > 3;
        boolean tarjetaValid = !TextUtils.isEmpty(tarjeta) && tarjeta.length() > 15;
        boolean monedaValid = !TextUtils.isEmpty(moneda) && moneda.length() > 1;
        boolean validateCardNumber = validateCardNumber(tarjeta);

        return titularValid && tarjetaValid && monedaValid && validateCardNumber;
    }

    private void updateValidation() {
        if (mMenu != null) {
            TextView save = mMenu.findItem(R.id.save).getActionView().findViewById(R.id.save_card);
            boolean allValidationPass = performValidations();
            save.setEnabled(allValidationPass);
        }
    }

    private boolean validateCardNumber(String tarjeta) {
        boolean bpa = tarjeta.contains("12");
        boolean bandec = tarjeta.contains("06");
        boolean banmet = tarjeta.matches(".*74.*|.*95.*");

        if (bpa) {
            binding.inputLayoutCard.setHelperText("BPA");
            return true;
        } else if (bandec) {
            binding.inputLayoutCard.setHelperText("BANDEC");
            return true;
        } else if (banmet) {
            binding.inputLayoutCard.setHelperText("BANMET");
            return true;
        } else {
            binding.inputLayoutCard.setHelperText("");
            return false;
        }
    }

    private void addOptionMenu() {
        requireActivity()
                .addMenuProvider(
                        new MenuProvider() {
                            @Override
                            public void onCreateMenu(
                                    @NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                                menuInflater.inflate(R.menu.save_menu, menu);
                                mMenu = menu;
                                updateValidation();
                                TextView text =
                                        (TextView)
                                                menu.findItem(R.id.save)
                                                        .getActionView()
                                                        .findViewById(R.id.save_card);
                                text.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                saveDataCard();
                                                NavController nav =
                                                        Navigation.findNavController(requireView());
                                                nav.navigateUp();
                                            }
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

    private class ValidationTextWatcher implements TextWatcher {

        private View view;
        private String previewsText;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            int id = view.getId();
            if (id == R.id.edit_user || id == R.id.edit_card || id == R.id.edit_moneda) {
                updateValidation();
                return;
            }
        }
    }

    // formatear el codigo de la tarjeta en grupos de 4 dígitos
    public static class CardFormatting implements TextWatcher {

        private boolean lock;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (lock || s.toString().replaceAll("\\s", "").length() > 16) {
                return;
            }
            lock = true;
            int len = s.length();
            for (int i = len - 1; i >= 0; i--) {
                if (s.toString().charAt(i) == ' ') {
                    s.delete(i, i + 1);
                }
            }
            for (int i = 4; i < s.length(); i += 5) {
                s.insert(i, " ");
            }
            lock = false;
        }
    }
}
