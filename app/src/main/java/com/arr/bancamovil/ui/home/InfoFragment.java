package com.arr.bancamovil.ui.home;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.arr.bancamovil.R;
import com.arr.bancamovil.databinding.FragmentInfoBinding;
import com.arr.bancamovil.utils.card.DataUtils;
import com.arr.bancamovil.utils.dialog.TransactionDialog;
import com.arr.bancamovil.utils.exchange.Transactions;
import com.arr.bancamovil.utils.exchange.interfaces.TransferListener;
import com.arr.bancamovil.utils.permissions.PermissionCheck;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class InfoFragment extends Fragment implements TransferListener {

    private FragmentInfoBinding binding;
    private Transactions transfer;
    private Bundle mBundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle arg2) {
        binding = FragmentInfoBinding.inflate(inflater, parent, false);
        addOptionMenu();
        //  transfer = new Transactions(requireContext(), this);

        // load data
        mBundle = getArguments();
        if (mBundle != null) {
            String tarjeta = mBundle.getString("tarjeta");
            String moneda = mBundle.getString("moneda");
            String titular = mBundle.getString("user");
            String phone = mBundle.getString("phone");
            String vence = mBundle.getString("expire");
            int id = mBundle.getInt("id");

            assert tarjeta != null;
            String card = agrupar(tarjeta);

            binding.textTarjeta.setText(card);
            binding.textMoneda.setText(moneda);
            binding.textTitular.setText("T: " + titular);
            binding.textBank.setText("B: " + getNameBank(tarjeta));
            binding.textVence.setText("V: " + vence);

            String LINK_TM = "TRANSFERMOVIL_ETECSA,TRANSFERENCIA,";
            String generateLinkTM = LINK_TM + tarjeta + "," + phone + ",";
            QRGEncoder qr = new QRGEncoder(generateLinkTM, null, QRGContents.Type.TEXT, 200);
            qr.setColorBlack(requireContext().getColor(android.R.color.black));
            qr.setColorWhite(requireContext().getColor(android.R.color.white));
            try {
                Bitmap bitmap = qr.getBitmap(0);
                binding.codeQr.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // share numero tarjeta y numero movil
            binding.share.setOnClickListener(
                    view -> {
                        StringBuilder builder = new StringBuilder();
                        builder.append(getString(R.string.share_card)).append(" ");
                        builder.append(moneda).append("\n");
                        builder.append(card);
                        if (!phone.isEmpty() && phone != null) {
                            builder.append("\n\n");
                            builder.append(getString(R.string.share_phone)).append(" ");
                            builder.append(phone);
                        }
                        builder.append("\n\nGracias.");
                        new ShareCompat.IntentBuilder(requireContext())
                                .setText(builder.toString())
                                .setType("text/plain")
                                .setChooserTitle("Compartir:")
                                .startChooser();
                    });

            // delete tarjeta de creditio
            binding.delete.setOnClickListener(
                    view -> {
                        new DataUtils(requireContext()).deleteCard(id);
                        NavController nav = Navigation.findNavController(requireView());
                        nav.navigateUp();
                    });

            // edit data
            binding.edit.setOnClickListener(
                    view -> navigateTo().navigate(R.id.navigation_edit_card, mBundle));
        }

        /* Mostrar las ultimas transacciones a la tarjeta seleccionada
         * Update: esta funcion fue necesario eliminarla debido a que era necesario
         * el permiso READ_SMS y PlayStore no acepta ese permiso exceptuando casos especiales
         *

        binding.transactions.setOnClickListener(
                v ->
                        new PermissionCheck(requireActivity())
                                .setTitle(R.string.tite_permission_sms)
                                .setMessage(R.string.message_permission_sms)
                                .setPermission(Manifest.permission.READ_SMS)
                                .setPositiveButton("Conceder")
                                .launchPermission(
                                        () ->
                                                requestPermissionLauncher.launch(
                                                        Manifest.permission.READ_SMS))
                                .executeCode(this::viewTransacciones));

        */

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*
        private void viewTransacciones() {
            String tarjeta = mBundle.getString("tarjeta");
            String digit = null;
            if (tarjeta != null) {
                digit = tarjeta.substring(tarjeta.length() - 4);
            }
            System.out.println("TARJETA: " + digit);
            if (transfer.hasCreditCardNumber(digit)) {
                new TransactionDialog(requireContext()).setTarjeta(digit).show();
            } else {
                Toast.makeText(requireContext(), "Sin transacciones", Toast.LENGTH_LONG).show();
            }
        }
    */
    private String agrupar(String numero) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < numero.length(); i += 4) {
            if (i + 4 < numero.length()) {
                resultado.append(numero, i, i + 4).append(" ");
            } else {
                resultado.append(numero, i, numero.length());
            }
        }

        return resultado.toString();
    }

    private NavController navigateTo() {
        return Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment_activity_main);
    }

    private NavOptions options() {
        return new NavOptions.Builder().setLaunchSingleTop(true).build();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    result -> {
                        if (result) {
                            // PERMISSION GRANTED
                            // viewTransacciones();
                        } // PERMISSION NOT GRANTED
                    });

    @Override
    public void onDataExtracted(
            String beneficiario, String transaccion, String monto, String tiempo) {}

    private String getNameBank(String str) {
        if (str.contains("12")) {
            return "BPA";
        } else if (str.contains("06")) {
            return "BANDEC";
        } else if (str.matches(".*74.*|.*95.*")) {
            return "BANMET";
        } else {
            return "";
        }
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
