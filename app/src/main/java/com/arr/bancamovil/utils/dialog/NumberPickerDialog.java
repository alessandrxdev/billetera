package com.arr.bancamovil.utils.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import com.arr.bancamovil.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import java.util.Calendar;
import java.util.Objects;

public class NumberPickerDialog extends BottomSheetDialog {

    private Context mContext;
    private DatePickerDialog.OnDateSetListener mListener;
    private Button mPositive;

    public NumberPickerDialog(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View content =
                LayoutInflater.from(mContext).inflate(R.layout.layout_view_number_picker, null);
        setContentView(content);
        Objects.requireNonNull(getWindow()).setNavigationBarColor(getContext().getColor(R.color.colorSurface));
        // calendar
        Calendar calendar = Calendar.getInstance();
        final NumberPicker month = content.findViewById(R.id.month);
        final NumberPicker year = content.findViewById(R.id.year);

        month.setMinValue(1);
        month.setMaxValue(12);
        int mes = calendar.get(Calendar.MONTH) + 1;
        month.setValue(mes);

        int anno = calendar.get(Calendar.YEAR);
        year.setMinValue(anno);
        year.setMaxValue(2099);
        year.setValue(anno);

        // positive button
        mPositive = content.findViewById(R.id.button_ok);
        mPositive.setOnClickListener(
                v -> {
                    mListener.onDateSet(null, year.getValue(), month.getValue(), 0);
                    dismiss();
                });
    }

    public NumberPickerDialog setPositiveButtom(DatePickerDialog.OnDateSetListener listener) {
        this.mListener = listener;

        return this;
    }
}
