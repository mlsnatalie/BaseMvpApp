package com.libs.core.common.floatwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.libs.core.R;
import com.libs.core.common.utils.DensityUtils;
import com.libs.core.common.utils.LogUtils;
import com.libs.core.common.utils.ScreenUtils;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.PermissionUtil;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;


public class XiguaGroupFloatWindowManager {
    public static final String TAG = "XiguaGroupFloatWindowManager";

    private static XiguaGroupFloatWindowManager instance;
    private Class[] activities;

    public View view_point;
    public View.OnClickListener mListener;

    public static XiguaGroupFloatWindowManager get() {
        if (instance == null) {
            synchronized (XiguaGroupFloatWindowManager.class) {
                if (instance == null) {
                    instance = new XiguaGroupFloatWindowManager();
                }
            }
        }

        return instance;
    }

    public void init(Class... activities) {
        this.activities = activities;
    }

    public void create(final Context context, View.OnClickListener listener) {
        mListener = listener;
        IFloatWindow aNew = FloatWindow.get(TAG);
        if (aNew == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.float_window_xigua_group, null);
            view_point = view.findViewById(R.id.view_point);
            view_point.setVisibility(View.VISIBLE);
            FloatWindow.with(context.getApplicationContext())
                    .setWidth(DensityUtils.dp2px(context.getApplicationContext(), 63f))
                    .setHeight(DensityUtils.dp2px(context.getApplicationContext(), 66f))
                    .setView(view)
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onSuccess() {
                            LogUtils.d("==", "setPermissionListener onSuccess");
                        }

                        @Override
                        public void onFail() {
                            LogUtils.d("==", "setPermissionListener onFail");
                        }
                    })
                    .setViewStateListener(new ViewStateListener() {
                        @Override
                        public void onPositionUpdate(int i, int i1) {
                            LogUtils.d("==", "setViewStateListener onPositionUpdate");
                            FloatWindow.get(TAG).getView().setBackground(null);
                        }

                        @Override
                        public void onShow() {
                            LogUtils.d("==", "setViewStateListener onShow isShow = " + true);
                        }

                        @Override
                        public void onHide() {
                            LogUtils.d("==", "setViewStateListener onHide isShow = " + false);
                        }

                        @Override
                        public void onDismiss() {
                            LogUtils.d("==", "setViewStateListener onDismiss");
                        }

                        @Override
                        public void onMoveAnimStart() {
                            LogUtils.d("==", "setViewStateListener onMoveAnimStart");
                        }

                        @Override
                        public void onMoveAnimEnd() {
                            LogUtils.d("==", "setViewStateListener onMoveAnimEnd");
                            int x = FloatWindow.get(TAG).getX();
                            int y = FloatWindow.get(TAG).getY();
                            LogUtils.d(TAG, "x=" + x + ",y=" + y);
                            if (x > ScreenUtils.getScreenWidth(context) / 2) {
                                // 右侧
                                FloatWindow.get(TAG).getView().setBackground(context.getResources().getDrawable(R.drawable.bg_circle_r));
                            } else {
                                // 左侧
                                FloatWindow.get(TAG).getView().setBackground(context.getResources().getDrawable(R.drawable.bg_circle_l));
                            }
                            if (y < 100) {
                                FloatWindow.get(TAG).updateY(100);
                            }
                            if (y > ScreenUtils.getScreenHeight(context) * 0.8f) {
                                FloatWindow.get(TAG).updateY(Screen.height, 0.8f);
                            }
                        }

                        @Override
                        public void onBackToDesktop() {
                            LogUtils.d("==", "setViewStateListener onBackToDesktop");
                        }
                    })
                    .setFilter(true, activities)
                    .setTag(TAG)
                    .setDesktopShow(false)
                    .setX(ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 63))
                    .setY(Screen.height, 0.6f)
                    .build();
        }
        if (PermissionUtil.hasPermission(context)) {
            FloatWindow.get(TAG).show();
        }
        if (mListener != null) {
            FloatWindow.get(TAG).getView().setOnClickListener(mListener);
        }

    }

    public void show(Context context) {
        if (PermissionUtil.hasPermission(context)) {
            IFloatWindow aNew = FloatWindow.get(TAG);
            if (aNew == null) {
                create(context, mListener);
                return;
            }
            if (aNew != null) {
                FloatWindow.get(TAG).show();
            }
        }
    }

    public void hide(Context context) {
        IFloatWindow aNew = FloatWindow.get(TAG);
        if (aNew != null) {
            FloatWindow.get(TAG).hide();
        }
    }
}
