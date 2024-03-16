package com.arr.bancamovil.utils.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.arr.bancamovil.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.textfield.TextInputEditText;

public class ImportDialog extends BottomSheetDialog {

    private Context mContext;
    private TextView message, title;
    private TextInputEditText editText;
    private Button mCancel, mAcept;
    private BottomSheetBehavior<View> behavior;
    private View content, bsheet;

    public ImportDialog(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        content = LayoutInflater.from(mContext).inflate(R.layout.layout_export_alert_dialog, null);
        setContentView(content);
        getWindow().setNavigationBarColor(getContext().getColor(R.color.colorSurface));
        bsheet = getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        behavior = BottomSheetBehavior.from(bsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // Title
        title = content.findViewById(R.id.title);

        // Message
        message = content.findViewById(R.id.message);

        // edittext
        editText = content.findViewById(R.id.edit_text);

        // positive buttom
        mAcept = content.findViewById(R.id.aceptar);

        // negative buttom
        mCancel = content.findViewById(R.id.cancelar);
    }

    public ImportDialog setTitle(String str) {
        if (str != null) {
            title.setText(str);
        } else {
            title.setVisibility(View.GONE);
        }
        return this;
    }

    public ImportDialog setMessage(String string) {
        if (string != null) {
            message.setText(string);
        } else {
            message.setVisibility(View.GONE);
        }
        return this;
    }

    public String getEditTextValue() {
        if (editText != null) {
            return editText.getText().toString();
        }
        return "";
    }

    public ImportDialog setPositiveButtom(final View.OnClickListener listener) {
        mAcept.setOnClickListener(
                v -> {
                    if (listener != null) {
                        listener.onClick(v);
                    }
                    dismiss();
                });
        return this;
    }

    public ImportDialog setNegativeButtom(View.OnClickListener listener) {
        mCancel.setOnClickListener(
                v -> {
                    if (listener != null) {
                        listener.onClick(v);
                    }
                    dismiss();
                });
        return this;
    }
}
