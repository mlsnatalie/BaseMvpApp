package com.libs.core.common.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 *
 * @author zhang.zheng
 * @version 2018-01-10
 */
public class KeyboardUtils {

    /**
     * 判断软键盘是否可见
     */
    public static boolean isSoftInputVisible(final Activity activity) {
        // 默认软键盘最小高度为200
        return getVisibleViewHeight(activity) >= 200;
    }

    /**
     * 判断软键盘是否可见
     */
    public static boolean isSoftInputVisible(final Activity activity, final int minHeightOfSoftInput) {
        return getVisibleViewHeight(activity) >= minHeightOfSoftInput;
    }

    /**
     * 获取屏幕可见视图高度
     */
    private static int getVisibleViewHeight(final Activity activity) {
        final View contentView = activity.findViewById(android.R.id.content);
        Rect r = new Rect();
        contentView.getWindowVisibleDisplayFrame(r);
        return contentView.getBottom() - r.bottom;
    }

    /**
     * 关闭软键盘
     */
    public static void hideSoftInput(final Activity activity) {
        if (activity == null)
            return;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (imm != null && imm.isActive() && view != null) {
            if (view.getWindowToken() != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * 复制内容到剪切板
     */
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", text);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
        }
    }
}
