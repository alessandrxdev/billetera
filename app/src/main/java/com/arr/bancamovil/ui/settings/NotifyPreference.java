package com.arr.bancamovil.ui.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.arr.bancamovil.R;

import androidx.preference.PreferenceFragmentCompat;

import com.arr.preferences.DefSwitchPreference;


public class NotifyPreference extends PreferenceFragmentCompat {

    private DefSwitchPreference notify;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_notification, rootKey);

        // activar notificaciones
        notify = findPreference("notify");
        assert notify != null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            notify.setEnabled(false);
        }
        notify.setOnPreferenceClickListener(
                (preference) -> {
                    // Verificar si el permiso ya se ha concedido
                    boolean isPermissionGranted =
                            false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        isPermissionGranted = ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.POST_NOTIFICATIONS)
                                == PackageManager.PERMISSION_GRANTED;
                    }

                    // Actualizar el estado del interruptor según el permiso concedido
                    notify.setChecked(isPermissionGranted);

                    // Si el permiso no se ha concedido, solicitar permisos
                    if (!isPermissionGranted) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissions(
                                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 22);
                        }
                    }

                    return true;
                });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 22) {
            // Verificar si el permiso ha sido concedido
            boolean isPermissionGranted =
                    grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            // Actualizar el estado del interruptor según el permiso concedido
            notify.setChecked(isPermissionGranted);

            // Aquí puedes realizar cualquier acción adicional que desees cuando el permiso está
            // concedido
        }
    }

    @NonNull
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        addOptionMenu();
        return super.onCreateView(arg0, arg1, arg2);
    }

    private void addOptionMenu() {
        requireActivity()
                .addMenuProvider(
                        new MenuProvider() {
                            @Override
                            public void onCreateMenu(@NonNull Menu arg0, @NonNull MenuInflater arg1) {
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
}
