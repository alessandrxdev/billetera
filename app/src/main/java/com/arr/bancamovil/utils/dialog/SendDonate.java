package com.arr.bancamovil.utils.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.arr.bancamovil.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.Objects;

public class SendDonate extends BottomSheetDialog {

    private Context mContext;
    private Button mPositive;
    private TextView message;
    private EditText edit;

    public SendDonate(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View content = LayoutInflater.from(mContext).inflate(R.layout.layout_bs_donate_view, null);
        setContentView(content);
        Objects.requireNonNull(getWindow())
                .setNavigationBarColor(getContext().getColor(R.color.colorSurface));

        mPositive = content.findViewById(R.id.aceptar);
        message = content.findViewById(R.id.textMessage);
        edit = content.findViewById(R.id.clave);
    }

    public SendDonate setPositiveButtom(final View.OnClickListener listener) {
        mPositive.setOnClickListener(
                view -> {
                    if (listener != null) {
                        listener.onClick(view);
                    }
                    dismiss();
                });
        return this;
    }

    public SendDonate setMessage(String string) {
        if (string != null) {
            message.setText(string);
        }
        return this;
    }

    public String getTextEditText() {
        return edit.getText().toString();
    }
}
