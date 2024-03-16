package com.arr.bancamovil.ui.settings;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.arr.bancamovil.R;

import androidx.preference.PreferenceFragmentCompat;

import com.arr.bancamovil.utils.backups.ExportFile;
import com.arr.bancamovil.utils.backups.FileExport;
import com.arr.preferences.M3Preference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.transition.MaterialSharedAxis;

public class BackupFragment extends PreferenceFragmentCompat {

    private ExportFile util;
    private FileExport file;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_backup, rootKey);

        util = new ExportFile(requireContext());
        file = new FileExport(getContext());

        // export
        M3Preference export = findPreference("export");
        assert export != null;
        export.setOnPreferenceClickListener(
                (preference) -> {
                    navigateTo().navigate(R.id.navigation_export, null, options());
                    return true;
                });

        // import
        M3Preference importFile = findPreference("import");
        assert importFile != null;
        importFile.setOnPreferenceClickListener(
                (preference) -> {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/octet-stream");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    /*
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/octet-stream", "application/x-data"});
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"data"});
                    */
                    importLauncher.launch(intent);
                    return true;
                });

        // gen key
        M3Preference key = findPreference("token");
        assert key != null;
        key.setOnPreferenceClickListener(
                (preference) -> {
                    viewDialogKey();
                    return true;
                });
    }

    private void viewDialogKey() {
        AlertDialog.Builder dialog =
                new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme);
        // --> dialog.setTitle("Token");
        View view =
                LayoutInflater.from(requireContext())
                        .inflate(R.layout.layout_view_dialog_token, null);
        dialog.setView(view);

        MaterialCardView copy = view.findViewById(R.id.copy_token);
        TextView textToken = view.findViewById(R.id.text_token);
        textToken.setText(util.generateKey());
        copy.setOnClickListener(
                v -> {
                    ClipboardManager clipboard =
                            (ClipboardManager)
                                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Token", textToken.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    dialog.create().dismiss();
                });

        // update token
        MaterialButton update = view.findViewById(R.id.update_token);
        update.setOnClickListener(v -> textToken.setText(util.generateKey()));

        dialog.show();
    }

    private NavController navigateTo() {
        return Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment_activity_main);
    }

    private NavOptions options() {
        return new NavOptions.Builder().setLaunchSingleTop(true).build();
    }

    ActivityResultLauncher<Intent> importLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                // util.importFileWithEncryptionCheck(uri);
                                file.importFile(uri);
                            }
                        }
                    });

    @NonNull
    @Override
    public android.view.View onCreateView(
            @NonNull LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        addOptionMenu();
        var enterTransition = new MaterialSharedAxis(MaterialSharedAxis.Y, true);
        var exitTransition = new MaterialSharedAxis(MaterialSharedAxis.Y, false);
        setEnterTransition(enterTransition);
        setExitTransition(exitTransition);
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
}
