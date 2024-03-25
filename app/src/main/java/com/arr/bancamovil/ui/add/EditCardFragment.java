package com.arr.bancamovil.ui.add;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import com.arr.bancamovil.R;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.arr.bancamovil.databinding.FragmentAddBinding;
import com.arr.bancamovil.utils.card.DataUtils;
import com.arr.bancamovil.utils.dialog.NumberPickerDialog;

public class EditCardFragment extends Fragment {

    private FragmentAddBinding binding;

    private DataUtils cards;
    private Menu mMenu;
    private int mId;

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            mId = bundle.getInt("id");
            binding.editUser.setText(bundle.getString("user").toString());
            binding.editCard.setText(bundle.getString("tarjeta").toString());
            int selection = 0;
            if (bundle.getString("moneda").equals("MLC")) {
                selection = 0;
            } else {
                selection = 1;
            }
            binding.editMoneda.setText(
                    binding.editMoneda.getAdapter().getItem(selection).toString(), false);
            binding.editVence.setText(bundle.getString("expire").toString());
            binding.editPhone.setText(bundle.getString("phone").toString());
        }
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
        cards.editData(mId, titular, tarjeta, moneda, vence, phone);
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
        if (validateNumber(tarjeta, "12")) {
            binding.inputLayoutCard.setHelperText("BPA");
            return true;
        } else if (validateNumber(tarjeta, "06")) {
            binding.inputLayoutCard.setHelperText("BANDEC");
            return true;
        } else if (validateNumber(tarjeta, "74") || validateNumber(tarjeta, "95")) {
            binding.inputLayoutCard.setHelperText("BANMET");
            return true;
        } else {
            binding.inputLayoutCard.setHelperText("");
            return false;
        }
    }

    private boolean validateNumber(String tarjeta, String number) {
        String[] grupos = tarjeta.split("(?<=\\G.{4})\\s+");
        if (grupos.length >= 2) {
            String segundoGrupo = grupos[1];
            return segundoGrupo.startsWith(number);
        }
        return false;
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

                                MenuItem saveMenuItem = menu.findItem(R.id.save);
                                View actionView = saveMenuItem.getActionView();
                                TextView save = actionView.findViewById(R.id.save_card);

                                save.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                saveDataCard();
                                                navigateTo()
                                                        .navigate(
                                                                R.id.navigation_home,
                                                                null,
                                                                options());
                                            }
                                        });
                            }

                            @Override
                            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                                int id = menuItem.getItemId();
                                if (id == android.R.id.home) {
                                    navigateTo().navigate(R.id.navigation_home, null, options());
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

    private NavController navigateTo() {
        return Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment_activity_main);
    }

    private NavOptions options() {
        return new NavOptions.Builder().setLaunchSingleTop(true).build();
    }
}
