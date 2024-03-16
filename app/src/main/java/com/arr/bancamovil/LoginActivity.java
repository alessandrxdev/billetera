package com.arr.bancamovil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.arr.bancamovil.databinding.ActivityLoginBinding;

import com.beautycoder.pflockscreen.security.PFSecurityManager;

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.arr.bancamovil.databinding.ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(android.R.color.transparent);
        Window w = getWindow();
        w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        /* Comprobar si es primera vez que el usuario accede a la aplicación para
         * mostrarle la pantalla de Bienvenida y que acepte las politicas de la aplicación.
         * Se comprueba mediante un preference.
         */
        SharedPreferences sp = getSharedPreferences("welcome", Context.MODE_PRIVATE);
        boolean isFirstTime = sp.getBoolean("firstTime", false);
        if (isFirstTime) {
            PFSecurityManager.getInstance()
                    .getPinCodeHelper()
                    .isPinCodeEncryptionKeyExist(
                            result -> {
                                boolean isPinCreated = result.getResult();
                                if (isPinCreated) {
                                    startActivity(
                                            new Intent(
                                                    LoginActivity.this,
                                                    LockScreenActivity.class));
                                    finish();
                                } else {
                                    startActivity(
                                            new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
        }

        /* Al hacer onClick se lleva al usuario a la pantalla de inicio y el boolean
         * se vuelve "true" para no mostrar mas la pantalla de Bienvenida.
         */
        binding.buttonAcept.setOnClickListener(
                view -> {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("firstTime", true);
                    editor.apply();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });

        /* permitir hacer onclick en el texto que contenga un link */
        binding.policy.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
