package com.arr.bancamovil.ui.settings;

import android.app.Activity;
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
import android.widget.Toast;

import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.arr.bancamovil.R;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.arr.bancamovil.databinding.FragmentExportBinding;
import com.arr.bancamovil.utils.backups.ExportFile;
import com.arr.bancamovil.utils.backups.FileExport;
import com.arr.bancamovil.utils.card.DataUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExportFragment extends Fragment {

    private FragmentExportBinding binding;
    private ExportFile export;
    private TextInputEditText clave;
    private String code;
    private final boolean isSwitchActive = false;
    private FileExport util;
    private DataUtils data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle arg2) {
        binding = FragmentExportBinding.inflate(getLayoutInflater(), parent, false);
        addOptionMenu();

        // export = new ExportFile(requireContext());
        util = new FileExport(requireContext());
        data = new DataUtils(requireContext());

        File file = data.directory();
        if (file.exists()) {
            binding.buttonExport.setEnabled(true);
        } else {
            binding.buttonExport.setEnabled(false);
        }

        // switch export encrypt
        binding.switchEncrypt.setOnCheckedChangeListener(
                (cb, isChecked) -> {
                    updateExportButtonState(isChecked);
                });

        updateExportButtonState(binding.switchEncrypt.isChecked());

        return binding.getRoot();
    }

    private void updateExportButtonState(boolean isActive) {
        if (isActive) {
            binding.buttonExport.setOnClickListener(
                    v -> {
                        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("application/octet-stream");
                        intent.putExtra(Intent.EXTRA_TITLE, "bm-wallet-backup-" + getFecha() + ".data");
                        exportDesencryp.launch(intent);
                    });
        } else {
            binding.buttonExport.setOnClickListener(
                    v -> {
                        exportDialog();
                    });
        }
    }

    private void exportDialog() {
        AlertDialog.Builder dialog =
                new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme);
        dialog.setTitle("Exportar");
        dialog.setCancelable(false);
        View view =
                LayoutInflater.from(getActivity())
                        .inflate(R.layout.layout_view_dialog_export, null);
        dialog.setView(view);
        clave = view.findViewById(R.id.edit_text);
        ((TextView) view.findViewById(R.id.message)).setText(getString(R.string.message_export));
        dialog.setPositiveButton(
                "Aceptar",
                (dialogInterface, i) -> {
                    code = clave.getText().toString().trim();
                    if (!code.isEmpty() && code != null) {
                        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("application/octet-stream");
                        intent.putExtra(Intent.EXTRA_TITLE, "bm-wallet-backup-" + getFecha() + ".data");
                        exportEncrypt.launch(intent);
                    } else {
                        showToast("No puede dejar la clave en blanco");
                    }
                });
        dialog.setNegativeButton(
                "Cancelar",
                (d, i) -> {
                    d.dismiss();
                });
        dialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    private String getFecha() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }

    private void addOptionMenu() {
        requireActivity()
                .addMenuProvider(
                        new MenuProvider() {
                            @Override
                            public void onCreateMenu(Menu arg0, MenuInflater arg1) {}

                            @Override
                            public boolean onMenuItemSelected(MenuItem menuItem) {
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

    private ActivityResultLauncher<Intent> shareFileLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // El archivo cifrado fue compartido exitosamente
                        }
                    });

    private ActivityResultLauncher<String> exportDesencrypt =
            registerForActivityResult(
                    new ActivityResultContracts.CreateDocument(),
                    result -> {
                        if (result != null) {
                            Uri uri = result;
                            export.exportFile(uri);

                            // Compartir el archivo cifrado
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("*/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            shareFileLauncher.launch(
                                    Intent.createChooser(shareIntent, "Compartir archivo cifrado"));
                        }
                    });

    private void guardarArchivo() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, "backup-ambia.data");
        exportDesencrypt.launch("backup-ambia.data");
    }

    // export startActivity laucher
    private ActivityResultLauncher<Intent> exportEncrypt =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                // export.exportFileEncrypt(uri, code);
                                util.exportFileEncrypt(uri, code);
                            }
                        }
                    });

    // export desencrip
    private ActivityResultLauncher<Intent> exportDesencryp =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                util.exportFileDescrypt(uri);
                            }
                        }
                    });
}
