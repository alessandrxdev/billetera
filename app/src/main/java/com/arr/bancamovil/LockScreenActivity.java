package com.arr.bancamovil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.arr.bancamovil.databinding.ActivityLockBinding;
import com.arr.bancamovil.utils.lockscreen.preference.PreferencesSettings;
import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;

import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;

public class LockScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        com.arr.bancamovil.databinding.ActivityLockBinding binding =
                ActivityLockBinding.inflate(getLayoutInflater());
        showLockScreenFragment();
        setContentView(binding.getRoot());
    }

    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {

                @Override
                public void onCodeInputSuccessful() {
                    startActivity(new Intent(LockScreenActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onFingerprintSuccessful() {
                    startActivity(new Intent(LockScreenActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onPinLoginFailed() {
                    Toast.makeText(LockScreenActivity.this, "PIN Incorrecto", Toast.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onFingerprintLoginFailed() {
                    Toast.makeText(LockScreenActivity.this, "Se ha cancelado", Toast.LENGTH_SHORT)
                            .show();
                }
            };

    private void showLockScreenFragment() {
        new PFPinCodeViewModel()
                .isPinCodeEncryptionKeyExist()
                .observe(
                        this,
                        result -> {
                            if (result == null) {
                                return;
                            }
                            if (result.getError() != null) {
                                return;
                            }
                            showLockScreenFragment(result.getResult());
                        });
    }

    private void showLockScreenFragment(boolean isPinExist) {
        final PFFLockScreenConfiguration.Builder builder =
                new PFFLockScreenConfiguration.Builder(this)
                        .setTitle(getString(R.string.inserte_su_pin))
                        .setCodeLength(4)
                        .setClearCodeOnError(true)
                        .setMode(PFFLockScreenConfiguration.MODE_AUTH)
                        .setNextButton(getString(R.string.aceptar_button))
                        .setUseFingerprint(true);

        final PFLockScreenFragment fragment = new PFLockScreenFragment();
        if (isPinExist) {
            fragment.setEncodedPinCode(PreferencesSettings.getCode(this));
            fragment.setLoginListener(mLoginListener);
        }

        fragment.setConfiguration(builder.build());
        showFragment(fragment);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }
}
