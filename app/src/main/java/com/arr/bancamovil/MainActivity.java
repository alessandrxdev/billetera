package com.arr.bancamovil;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;


import com.arr.bancamovil.broadcast.SmsReceiver;
import com.arr.bancamovil.databinding.ActivityMainBinding;
import com.arr.bugsend.BugSend;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.mToolbar);

        // crash app
        new BugSend(this).setLaunchActivity(LogActivity.class).show();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                                R.id.navigation_home,
                                R.id.navigation_tool,
                                R.id.navigation_settings)
                        .build();
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // navigation destination
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {
                    int id = destination.getId();
                    if (id == R.id.navigation_help
                            || id == R.id.navigation_bill
                            || id == R.id.navigation_add_card
                            || id == R.id.navigation_info
                            || id == R.id.navigation_edit_card
                            || id == R.id.navigation_backup
                            || id == R.id.navigation_export
                            || id == R.id.navigation_money
                            || id == R.id.navigation_notify
                            || id == R.id.navigation_calculate
                            || id == R.id.navigation_security
                            || id == R.id.navigation_create_pin
                            || id == R.id.navigation_market
                            || id == R.id.navigation_about) {
                        binding.navView.setVisibility(View.GONE);
                        getWindow().setNavigationBarColor(getColor(R.color.colorBackground));
                    } else {
                        binding.navView.setVisibility(View.VISIBLE);
                        getWindow().setNavigationBarColor(getColor(R.color.colorSurface));
                    }
                });

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean screenshot = sp.getBoolean("screenshot", true);
        Log.e("Screenshot", "hjjnj" + screenshot);
        if (screenshot) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            getWindow()
                    .setFlags(
                            WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean screenshot = sp.getBoolean("screenshot", true);
        if (screenshot) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            getWindow()
                    .setFlags(
                            WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    public Window getMainWindow() {
        return getWindow();
    }
}
