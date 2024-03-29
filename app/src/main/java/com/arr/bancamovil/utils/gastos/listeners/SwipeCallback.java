package com.arr.bancamovil.utils.gastos.listeners;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.view.View;
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
        if (direction == ItemTouchHelper.LEFT) {
            mAdapter.notifyItemChanged(position);
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(70);
            if (mListener != null) {
                mListener.onSwipeLeft(position);
            }
        }
    }

    public interface SwipeLeftListener {
        void onSwipeLeft(int position);
    }

    @Override
    public void onChildDraw(
            Canvas canvas,
            RecyclerView recycler,
            RecyclerView.ViewHolder holder,
            float dx,
            float dy,
            int actionState,
            boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recycler, holder, dx, dy, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = holder.itemView;
            if (dx < 0) {
                float alpha = 0.75f - Math.abs(dx) / (float) itemView.getWidth();
                holder.itemView.setAlpha(alpha);
                holder.itemView.setTranslationX(dx * 0.75f);
                holder.itemView.setTranslationY(dy * 0.75f);
                ((MarketAdapter.MarketView) holder).getDeleteIcon().setVisibility(View.VISIBLE);
            } else if (dx > 0) {
                float alpha = 1.0f - Math.abs(dx) / (float) itemView.getWidth();
                holder.itemView.setAlpha(alpha);
                holder.itemView.setTranslationX(dx);
                holder.itemView.setTranslationY(dy);
                ((MarketAdapter.MarketView) holder).getDeleteIcon().setVisibility(View.GONE);
            }
        } else if (!isCurrentlyActive) {
            holder.itemView.setAlpha(1.0f);
            holder.itemView.setTranslationX(0);
            holder.itemView.setTranslationY(0);
            ((MarketAdapter.MarketView) holder).getDeleteIcon().setVisibility(View.GONE);
        }
    }

    @Override
    public void clearView(RecyclerView recycler, RecyclerView.ViewHolder holder) {
        super.clearView(recycler, holder);
        holder.itemView.setAlpha(1.0f);
        holder.itemView.setTranslationX(0);
        holder.itemView.setTranslationY(0);
        ((MarketAdapter.MarketView) holder).getDeleteIcon().setVisibility(View.GONE);
    }
}
