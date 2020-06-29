package com.libs.core.common.adapter.recycler.listener;

import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.libs.core.R;

/**
 * RecyclerView点击和长按事件监听支持类
 */
public class ItemClickSupport {

    private final RecyclerView mRecyclerView;
    private final TouchListener mTouchListener;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;


    private ItemClickSupport(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mTouchListener = new TouchListener(recyclerView);
        recyclerView.addOnItemTouchListener(mTouchListener);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        if (!mRecyclerView.isLongClickable()) {
            mRecyclerView.setLongClickable(true);
        }
        mItemLongClickListener = listener;
    }

    public static ItemClickSupport addTo(RecyclerView recyclerView) {
        ItemClickSupport itemClickSupport = (ItemClickSupport) recyclerView.getTag(R.id.item_click_support);
        if (itemClickSupport == null) {
            itemClickSupport = new ItemClickSupport(recyclerView);
            recyclerView.setTag(R.id.item_click_support, itemClickSupport);
        }
        return itemClickSupport;
    }

    public static void removeFrom(RecyclerView recyclerView) {
        final ItemClickSupport itemClickSupport = (ItemClickSupport) recyclerView.getTag(R.id.item_click_support);
        if (itemClickSupport == null) {
            return;
        }
        recyclerView.removeOnItemTouchListener(itemClickSupport.mTouchListener);
        recyclerView.setTag(R.id.item_click_support, null);
    }

    /**
     * 触摸监听
     */
    private class TouchListener extends ClickItemTouchListener {

        TouchListener(RecyclerView recyclerView) {
            super(recyclerView);
        }

        @Override
        public boolean performItemClick(RecyclerView parent, View child, int position, long id) {
            if (mItemClickListener != null) {
                child.playSoundEffect(SoundEffectConstants.CLICK);

                // 此处非常重要，可以避免RecyclerView越界异常bug
                if (position >= 0) {
                    mItemClickListener.onItemClick(parent, child, position, id);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean performItemLongClick(RecyclerView parent, View view, int position, long id) {
            if (mItemLongClickListener != null) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

                // 此处非常重要，可以避免RecyclerView越界异常bug
                if (position >= 0) {
                    return mItemLongClickListener.onItemLongClick(parent, view, position, id);
                }
            }
            return false;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    /**
     * 单击监听
     */
    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View view, int position, long id);
    }

    /**
     * 长按监听
     */
    public interface OnItemLongClickListener {
        boolean onItemLongClick(RecyclerView parent, View view, int position, long id);
    }
}
