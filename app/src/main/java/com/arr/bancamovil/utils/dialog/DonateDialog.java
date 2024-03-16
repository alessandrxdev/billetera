package com.arr.bancamovil.utils.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.arr.bancamovil.R;
import android.widget.RelativeLayout;
import androidx.fragment.app.DialogFragment;
import com.arr.bancamovil.databinding.LayoutDonateViewBinding;
import com.google.android.material.card.MaterialCardView;

public class DonateDialog extends DialogFragment {

    private LayoutDonateViewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arg2) {
        View view = inflater.inflate(R.layout.layout_donate_view, container, false);

        MaterialCardView coffe = view.findViewById(R.id.caffe);
        coffe.setOnClickListener(this::callCoffe);

        MaterialCardView pizza = view.findViewById(R.id.pizza);
        pizza.setOnClickListener(this::callPizza);

        ImageButton close = view.findViewById(R.id.close);
        close.setOnClickListener(
                v -> {
                    dismiss();
                });

        return view;
    }

    private void callCoffe(View view) {
        SendDonate dialog = new SendDonate(requireContext());
        dialog.setMessage(
                "Al presionar Continuar usted enviará 50.00 CUP como donación para el desarrollo de Billetera. Este dinero no es reembolsable, por favor hágalo con responsabilidad.");
        dialog.setPositiveButtom(
                (listener) -> {
                    //
                    String clave = dialog.getTextEditText();
                    if (!clave.isEmpty()) {
                        if (ContextCompat.checkSelfPermission(
                                        requireActivity(), Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    new String[] {Manifest.permission.CALL_PHONE},
                                    20);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(
                                    Uri.parse(
                                            "tel:*234*1*54250705*"
                                                    + clave
                                                    + "*50*1"
                                                    + Uri.encode("#")));

                            if (intent.resolveActivity(requireActivity().getPackageManager())
                                    != null) {
                                requireActivity().startActivity(intent);
                            }
                        }
                    }
                });

        dialog.show();
    }

    private void callPizza(View view) {
        SendDonate dialog = new SendDonate(requireContext());
        dialog.setMessage(
                "Al presionar Continuar usted enviará 120.00 CUP como donación para el desarrollo de Billetera. Este dinero no es reembolsable, por favor hágalo con responsabilidad.");
        dialog.setPositiveButtom(
                (listener) -> {
                    //
                    String clave = dialog.getTextEditText();
                    if (!clave.isEmpty()) {
                        if (ContextCompat.checkSelfPermission(
                                        requireActivity(), Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    new String[] {Manifest.permission.CALL_PHONE},
                                    20);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(
                                    Uri.parse(
                                            "tel:*234*1*54250705*"
                                                    + clave
                                                    + "*120"
                                                    + Uri.encode("#")));

                            if (intent.resolveActivity(requireActivity().getPackageManager())
                                    != null) {
                                requireActivity().startActivity(intent);
                            }
                        }
                    }
                });

        dialog.show();
    }

    @Override
    public Dialog onCreateDialog(Bundle arg0) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(root);
        dialog.getWindow()
                .setBackgroundDrawable(
                        new ColorDrawable(getActivity().getColor(R.color.colorBackground)));
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}
