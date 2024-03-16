package com.arr.bancamovil.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceFragmentCompat;

import com.arr.bancamovil.R;
import com.arr.bancamovil.databinding.FragmentSettingsBinding;
import com.arr.bancamovil.utils.dialog.DonateDialog;
import com.arr.preferences.M3ListPreference;
import com.arr.preferences.M3Preference;
import com.google.android.material.transition.MaterialSharedAxis;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new SettingsPreference())
                .commit();
        var enterTransition = new MaterialSharedAxis(MaterialSharedAxis.Y, true);
        var exitTransition = new MaterialSharedAxis(MaterialSharedAxis.Y, false);
        setEnterTransition(enterTransition);
        setExitTransition(exitTransition);
        return binding.getRoot();
    }

    public static class SettingsPreference extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preference_root, rootKey);

            // navigate to BackupFragment
            M3Preference backup = findPreference("backup");
            backup.setOnPreferenceClickListener(
                    (preference) -> {
                        navigateTo().navigate(R.id.navigation_backup, null, options());
                        return true;
                    });

            // navigate to BackupFragment
            M3Preference moneda = findPreference("ui");
            moneda.setOnPreferenceClickListener(
                    (preference) -> {
                        navigateTo().navigate(R.id.navigation_money, null, options());
                        return true;
                    });

            // notificaciones
            M3Preference notify = findPreference("notify");
            notify.setOnPreferenceClickListener(
                    (preference) -> {
                        navigateTo().navigate(R.id.navigation_notify, null, options());
                        return true;
                    });

            // soporte
            M3Preference support = findPreference("support");
            support.setOnPreferenceClickListener(
                    (preference) -> {
                        requireActivity()
                                .startActivity(
                                        new Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(
                                                        "https://t.me/soporteapplifybot?start=reporte")));
                        return true;
                    });

            // security
            M3Preference security = findPreference("security");
            assert security != null;
            security.setOnPreferenceClickListener(
                    (preference) -> {
                        navigateTo().navigate(R.id.navigation_security, null, options());
                        return true;
                    });

            // acerca de
            M3Preference about = findPreference("about");
            assert about != null;
            about.setOnPreferenceClickListener(
                    (preference) -> {
                        navigateTo().navigate(R.id.navigation_about, null, options());
                        return true;
                    });

            // help
            M3Preference help = findPreference("help");
            assert help != null;
            help.setOnPreferenceClickListener(
                    (preference) -> {
                        navigateTo().navigate(R.id.navigation_help, null, options());
                        return true;
                    });

            // donate
            M3Preference donar = findPreference("donate");
            assert donar != null;
            donar.setOnPreferenceClickListener(
                    (preference) -> {
                        DialogFragment dialog = new DonateDialog();
                        dialog.show(requireActivity().getSupportFragmentManager(), null);
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
    }

    @Override
    public void onViewCreated(View view, Bundle arg1) {
        super.onViewCreated(view, arg1);
        ListView list = view.findViewById(android.R.id.list);
        if (list != null) {
            list.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        }
    }
}
