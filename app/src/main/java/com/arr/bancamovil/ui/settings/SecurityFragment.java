package com.arr.bancamovil.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.arr.bancamovil.MainActivity;
import com.arr.bancamovil.R;
import com.arr.bancamovil.databinding.FragmentSettingsBinding;
import com.arr.preferences.DefSwitchPreference;
import com.arr.preferences.M3Preference;
import com.arr.preferences.M3SwitchPreference;
import com.beautycoder.pflockscreen.security.PFSecurityManager;


public class SecurityFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle arg2) {
        com.arr.bancamovil.databinding.FragmentSettingsBinding binding = FragmentSettingsBinding.inflate(inflater, parent, false);
        addOptionMenu();
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new SecurityPreference())
                .commit();
        return binding.getRoot();
    }

    public static class SecurityPreference extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

        private Handler handler;
        private Runnable runnable;
        private SharedPreferences sharedPreferences;

        @Override
        public void onCreatePreferences(Bundle arg0, String rootKey) {
            setPreferencesFromResource(R.xml.preference_security, rootKey);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

            M3SwitchPreference lock = findPreference("create_pin");
            M3Preference restart_pin = findPreference("restart");
            assert lock != null;
            lock.setOnPreferenceChangeListener(
                    this::onPreferenceChange);

            PFSecurityManager.getInstance()
                    .getPinCodeHelper()
                    .isPinCodeEncryptionKeyExist(
                            result -> {
                                boolean isPinCreated = result.getResult();
                                lock.setChecked(isPinCreated);

                                if (!isPinCreated) {
                                    assert restart_pin != null;
                                    restart_pin.setEnabled(false);
                                }
                            });


            assert restart_pin != null;
            restart_pin.setOnPreferenceClickListener(this::onRestarPin);

            // bloquear captura de pantalla, excepto si el usuario lo activa para que se permita
            // asi poder darle permiso por 5 minutos, despues de eso se vuelve activar la seguridad.
            DefSwitchPreference screenshot = findPreference("screenshot");
            assert screenshot != null;
            screenshot.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (Boolean) newValue;
                if (isChecked) {
                    // Desactivar la bandera FLAG_SECURE de la ventana
                    if (isAdded()) {
                        try {
                            ((MainActivity) requireActivity())
                                    .getMainWindow()
                                    .clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                            // Iniciar el Handler y el Runnable para desactivar el
                            // SwitchPreference después de 5 minutos
                            handler = new Handler(Looper.getMainLooper());
                            runnable =
                                    () -> {
                                        DefSwitchPreference switchPreference =
                                                findPreference("screenshot");
                                        if (switchPreference != null) {
                                            switchPreference.setChecked(false);
                                        }
                                    };
                            handler.postDelayed(
                                    runnable,
                                    5 * 60 * 1000); // 5 minutos en milisegundos
                        } catch (NullPointerException e) {
                            // La actividad actual es nula
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Activar la bandera FLAG_SECURE de la ventana
                    if (isAdded()) {
                        try {
                            ((MainActivity) requireActivity())
                                    .getMainWindow()
                                    .setFlags(
                                            WindowManager.LayoutParams.FLAG_SECURE,
                                            WindowManager.LayoutParams.FLAG_SECURE);
                            // Detener cualquier tarea programada anteriormente para
                            // desactivar el SwitchPreference
                            if (handler != null) {
                                handler.removeCallbacks(runnable);
                            }
                        } catch (NullPointerException e) {
                            // La actividad actual es nula
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            });

        }

        private NavController navigateTo() {
            return Navigation.findNavController(
                    requireActivity(), R.id.nav_host_fragment_activity_main);
        }

        private NavOptions options() {
            return new NavOptions.Builder().setLaunchSingleTop(true).build();
        }

        private boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean switched = (Boolean) newValue;
            if (switched) {
                navigateTo().navigate(R.id.navigation_create_pin, null, options());
            } else {
                PFSecurityManager.getInstance().getPinCodeHelper().delete(result -> {
                });
            }
            return true;
        }

        private boolean onRestarPin(Preference preference) {
            PFSecurityManager.getInstance().getPinCodeHelper().delete(result -> navigateTo().navigate(R.id.navigation_create_pin, null, options()));
            return true;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
            assert key != null;
            if (key.equals("screenshot")) {
                boolean isChecked = sharedPreferences.getBoolean(key, false);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
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
