package com.arr.bancamovil.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import com.arr.bancamovil.R;
import com.arr.bancamovil.utils.dialog.PremiumDialog;
import com.arr.bancamovil.utils.theme.ThemeApp;
import com.arr.bancamovil.works.UpdateTasasWork;
import com.arr.preferences.M3ListPreference;
import com.arr.preferences.M3MultiSelectListPreference;
import com.arr.preferences.M3Preference;

import java.util.concurrent.TimeUnit;

public class UiFragment extends PreferenceFragmentCompat {

    boolean isLargeLayout;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_ui, rootKey);

        // mostrar tipo de tasa
        M3MultiSelectListPreference tasas = findPreference("monedas");
        assert tasas != null;
        tasas.setOnPreferenceChangeListener((preference, newValue) -> true);

        // tema
        M3ListPreference tema = findPreference("tema");
        assert tema != null;
        if (tema.getValue() == null) {
            tema.setValue(ThemeApp.Mode.system.name());
        }
        tema.setOnPreferenceChangeListener(
                (preference, value) -> {
                    ThemeApp.apply(ThemeApp.Mode.valueOf((String) value));
                    return true;
                });

        M3ListPreference update = findPreference("update_tasas");
        if (update != null) {
            update.setOnPreferenceChangeListener(
                    (preference, newValue) -> {
                        if (newValue.equals("0")) {
                            WorkManager.getInstance(requireContext())
                                    .cancelUniqueWork("Tasas"); // Detener trabajo específico
                        } else {
                            int interval;
                            if (newValue.equals("1")) {
                                interval = 6;
                            } else if (newValue.equals("2")) {
                                interval = 12;
                            } else {
                                return false;
                            }
                            updateBalances(interval);
                        }
                        return true;
                    });
        }

        M3Preference ahorro = findPreference("ahorro");
        if (ahorro != null) {
            ahorro.setOnPreferenceClickListener(this::onPreferenceClick);
        }
    }

    @NonNull
    @Override
    public android.view.View onCreateView(
            @NonNull LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        addOptionMenu();
        isLargeLayout = getResources().getBoolean(R.bool.large_layout);
        return super.onCreateView(arg0, arg1, arg2);
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

    private void updateBalances(int interval) {
        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(UpdateTasasWork.class, interval, TimeUnit.HOURS)
                        .setConstraints(
                                new Constraints.Builder().setRequiresBatteryNotLow(true).build())
                        .build();

        WorkManager.getInstance(requireContext())
                .enqueueUniquePeriodicWork("Tasas", ExistingPeriodicWorkPolicy.KEEP, workRequest);
    }

    private boolean onPreferenceClick(Preference preference) {
        PowerManager powerManager =
                (PowerManager) requireActivity().getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            if (!powerManager.isIgnoringBatteryOptimizations(requireActivity().getPackageName())) {
                @SuppressLint("BatteryLife")
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + requireActivity().getPackageName()));
                startActivity(intent);
            } else {
                Toast.makeText(
                                requireActivity(),
                                "El ahorro de bateria ya está desactivada",
                                Toast.LENGTH_LONG)
                        .show();
            }
        }
        return true;
    }

    private void showDialogPremium() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        DialogFragment dialog = new PremiumDialog();
        if (isLargeLayout) {
            dialog.show(fragmentManager, "dialog");
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, dialog).addToBackStack(null).commit();
        }
    }
}
