package com.arr.bancamovil.ui.settings.security;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.arr.bancamovil.R;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.arr.bancamovil.databinding.FragmentSettingsBinding;
import com.arr.bancamovil.utils.lockscreen.preference.PreferencesSettings;
import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;


import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;

public class CreatePinFragment extends Fragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle arg2) {
        com.arr.bancamovil.databinding.FragmentSettingsBinding binding = FragmentSettingsBinding.inflate(inflater, parent, false);
        showLockScreenFragment();
        addOptionMenu();
        return binding.getRoot();
    }

    private final PFLockScreenFragment.OnPFLockScreenCodeCreateListener mCodeCreateListener =
            new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {
                @Override
                public void onCodeCreated(String encodedCode) {
                    NavController nav = Navigation.findNavController(requireView());
                    nav.navigateUp();
                    PreferencesSettings.saveToPref(requireActivity(), encodedCode);
                }

                @Override
                public void onNewCodeValidationFailed() {
                    Toast.makeText(requireActivity(), "Code validation error", Toast.LENGTH_SHORT)
                            .show();
                }
            };

    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {

                @Override
                public void onCodeInputSuccessful() {
                    NavController nav = Navigation.findNavController(requireView());
                    nav.navigateUp();
                }

                @Override
                public void onFingerprintSuccessful() {
                    Toast.makeText(requireActivity(), "Fingerprint successfull", Toast.LENGTH_SHORT)
                            .show();
                    NavController nav = Navigation.findNavController(requireView());
                    nav.navigateUp();
                }

                @Override
                public void onPinLoginFailed() {
                    Toast.makeText(requireActivity(), "Pin failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFingerprintLoginFailed() {
                    Toast.makeText(requireActivity(), "Fingerprint failed", Toast.LENGTH_SHORT)
                            .show();
                }
            };

    private void showLockScreenFragment() {
        new PFPinCodeViewModel()
                .isPinCodeEncryptionKeyExist()
                .observe(
                        getViewLifecycleOwner(),
                        result -> {
                            if (result == null) {
                                return;
                            }
                            if (result.getError() != null) {
                                Toast.makeText(
                                                requireActivity(),
                                                "Can not get pin code info",
                                                Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            showLockScreenFragment(result.getResult());
                        });
    }

    private void showLockScreenFragment(boolean isPinExist) {
        final PFFLockScreenConfiguration.Builder builder =
                new PFFLockScreenConfiguration.Builder(requireContext())
                        .setTitle("Ingrese su nuevo PIN")
                        .setCodeLength(4)
                        .setMode(PFFLockScreenConfiguration.MODE_CREATE)
                        .setNewCodeValidation(true)
                        .setErrorAnimation(true)
                        .setErrorVibration(true)
                        .setNextButton("Confirmar")
                        .setNewCodeValidationTitle("Repita su PIN")
                        .setUseFingerprint(true);

        final PFLockScreenFragment fragment = new PFLockScreenFragment();
        builder.setMode(
                isPinExist
                        ? PFFLockScreenConfiguration.MODE_AUTH
                        : PFFLockScreenConfiguration.MODE_CREATE);
        if (isPinExist) {
            fragment.setEncodedPinCode(PreferencesSettings.getCode(requireContext()));
            fragment.setLoginListener(mLoginListener);
        }

        fragment.setConfiguration(builder.build());
        fragment.setCodeCreateListener(mCodeCreateListener);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
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
                        }, getViewLifecycleOwner(),
                        Lifecycle.State.RESUMED);
    }
}
