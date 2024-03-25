package com.arr.bancamovil.utils.gastos.listeners;

import android.content.Context;
import android.os.Vibrator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.arr.bancamovil.adapter.MarketAdapter;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private Context mContext;
    private MarketAdapter mAdapter;
    private SwipeLeftListener mListener;

    public SwipeCallback(Context context, MarketAdapter adapter, SwipeLeftListener listener) {
        super(0, ItemTouchHelper.LEFT);
        mAdapter = adapter;
        mContext = context;
        mListener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recycler, ViewHolder holder, ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(ViewHolder holder, int direction) {
        int position = holder.getAdapterPosition();
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (direction == ItemTouchHelper.LEFT) {
            mAdapter.notifyItemChanged(position);
            vibrator.vibrate(70);
            if (mListener != null) {
                mListener.onSwipeLeft(position);
            }
        }
    }

    public interface SwipeLeftListener {
        void onSwipeLeft(int position);
    }
}
