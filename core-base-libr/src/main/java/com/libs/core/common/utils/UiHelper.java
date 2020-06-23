package com.libs.core.common.utils;


import android.graphics.Outline;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewOutlineProvider;


public class UiHelper {


    private static SparseArray<ViewOutlineProvider> mProvider = new SparseArray<>();

    public static void radiosView(View view, final int radio) {
        ViewOutlineProvider viewOutlineProvider = mProvider.get(radio);
        if (viewOutlineProvider == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewOutlineProvider = new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), AppUtils.dp2px(radio));
                    }
                };
                view.setOutlineProvider(viewOutlineProvider);
                view.setClipToOutline(true);
            }
            mProvider.put(radio, viewOutlineProvider);
        }
    }
}
