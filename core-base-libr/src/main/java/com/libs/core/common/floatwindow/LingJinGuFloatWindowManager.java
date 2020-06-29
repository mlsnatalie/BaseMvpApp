//package com.libs.core.common.floatwindow;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.libs.core.R;
//import com.libs.core.common.utils.LogUtils;
//import com.libs.core.common.utils.ScreenUtils;
//import com.yhao.floatwindow.FloatWindow;
//import com.yhao.floatwindow.IFloatWindow;
//import com.yhao.floatwindow.PermissionListener;
//import com.yhao.floatwindow.PermissionUtil;
//import com.yhao.floatwindow.Screen;
//import com.yhao.floatwindow.ViewStateListener;
//
//import me.jessyan.autosize.utils.AutoSizeUtils;
//
//
//public class LingJinGuFloatWindowManager {
//    public static final String TAG = "LingJinGuFloatWindowManager";
//
//    private static LingJinGuFloatWindowManager instance;
//    private Class[] activities;
//
//    public View view_point;
//    public View.OnClickListener mListener;
//    public ImageView group_chat;
//    public Context mContext;
//
//    public static LingJinGuFloatWindowManager get() {
//        if (instance == null) {
//            synchronized (LingJinGuFloatWindowManager.class) {
//                if (instance == null) {
//                    instance = new LingJinGuFloatWindowManager();
//                }
//            }
//        }
//
//        return instance;
//    }
//
//    public void init(Class... activities) {
//        this.activities = activities;
//    }
//
//    public void create(final Context context, View.OnClickListener listener) {
//        mListener = listener;
//        mContext = context;
//        IFloatWindow aNew = FloatWindow.get(TAG);
//        if (aNew == null) {
//            View view = LayoutInflater.from(context).inflate(R.layout.float_window_ling_jin_gu_group, null);
//            group_chat = view.findViewById(R.id.group_chat);
//            FloatWindow.with(context.getApplicationContext())
//                    .setWidth(AutoSizeUtils.pt2px(context.getApplicationContext(), 112f))
//                    .setHeight(AutoSizeUtils.pt2px(context.getApplicationContext(), 112f))
//                    .setView(view)
//                    .setPermissionListener(new PermissionListener() {
//                        @Override
//                        public void onSuccess() {
//                            LogUtils.d("==", "setPermissionListener onSuccess");
//                        }
//
//                        @Override
//                        public void onFail() {
//                            LogUtils.d("==", "setPermissionListener onFail");
//                        }
//                    })
//                    .setViewStateListener(new ViewStateListener() {
//                        @Override
//                        public void onPositionUpdate(int i, int i1) {
//                            LogUtils.d("==", "setViewStateListener onPositionUpdate");
//                            FloatWindow.get(TAG).getView().setBackground(null);
//                        }
//
//                        @Override
//                        public void onShow() {
//                            LogUtils.d("==", "setViewStateListener onShow isShow = " + true);
//                        }
//
//                        @Override
//                        public void onHide() {
//                            LogUtils.d("==", "setViewStateListener onHide isShow = " + false);
//                        }
//
//                        @Override
//                        public void onDismiss() {
//                            LogUtils.d("==", "setViewStateListener onDismiss");
//                        }
//
//                        @Override
//                        public void onMoveAnimStart() {
//                            LogUtils.d("==", "setViewStateListener onMoveAnimStart");
//                        }
//
//                        @Override
//                        public void onMoveAnimEnd() {
//                            LogUtils.d("==", "setViewStateListener onMoveAnimEnd");
//                            int x = FloatWindow.get(TAG).getX();
//                            int y = FloatWindow.get(TAG).getY();
//                            LogUtils.d(TAG, "x=" + x + ",y=" + y);
//                            if (x > ScreenUtils.getScreenWidth(context) / 2) {
//                                // 右侧
//                                FloatWindow.get(TAG).getView().setBackground(context.getResources().getDrawable(R.drawable.bg_ling_jin_gu));
//                            } else {
//                                // 左侧
//                                FloatWindow.get(TAG).getView().setBackground(context.getResources().getDrawable(R.drawable.bg_ling_jin_gu));
//                            }
//                            if (y < 100) {
//                                FloatWindow.get(TAG).updateY(100);
//                            }
//                            if (y > ScreenUtils.getScreenHeight(context) * 0.8f) {
//                                FloatWindow.get(TAG).updateY(Screen.height, 0.8f);
//                            }
//                        }
//
//                        @Override
//                        public void onBackToDesktop() {
//                            LogUtils.d("==", "setViewStateListener onBackToDesktop");
//                        }
//                    })
//                    .setFilter(true, activities)
//                    .setTag(TAG)
//                    .setDesktopShow(false)
//                    .setX(ScreenUtils.getScreenWidth(context) - AutoSizeUtils.pt2px(context.getApplicationContext(), 112f))
////                    .setX(AutoSizeUtils.pt2px(context.getApplicationContext(), 112f))
////                    .setY(Screen.height, 0.5f)
//                    .setY(Screen.height, 0.6f)
//                    .build();
//        }
//        if (PermissionUtil.hasPermission(context)) {
//            FloatWindow.get(TAG).show();
//        }
//        if (mListener != null) {
//            FloatWindow.get(TAG).getView().setOnClickListener(mListener);
//        }
//
//    }
//
//    public void show(Context context) {
//        if (PermissionUtil.hasPermission(context)) {
//            IFloatWindow aNew = FloatWindow.get(TAG);
//            if (aNew == null) {
//                create(context, mListener);
//                return;
//            }
//            if (aNew != null) {
//                FloatWindow.get(TAG).show();
//            }
//        }
//    }
//
//    public void hide(Context context) {
//        IFloatWindow aNew = FloatWindow.get(TAG);
//        if (aNew != null) {
//            FloatWindow.get(TAG).hide();
//        }
//    }
//
//    public void loadPicture(String url) {
//        //加载图片
////        RequestOptions options = new RequestOptions();
////        options.placeholder(R.drawable.bg_ling_jin_gu);
////        options.error(R.drawable.bg_ling_jin_gu);
////        options.fallback(R.drawable.bg_ling_jin_gu);
//        Glide.with(mContext)
//                .load(url)
////                .apply(options)
//                .into(group_chat);
//    }
//
//    public void loadDefaultPicture() {
//        group_chat.setImageResource(R.drawable.bg_ling_jin_gu);
//    }
//
//    public void updateX() {
//        int x = FloatWindow.get(TAG).getX();
//        if (x > ScreenUtils.getScreenWidth(mContext) / 2) {
//            // 右侧
//            FloatWindow.get(TAG).updateX(ScreenUtils.getScreenWidth(mContext) - AutoSizeUtils.dp2px(mContext, 44f));
//        } else {
//            // 左侧
//            FloatWindow.get(TAG).updateX(AutoSizeUtils.pt2px(mContext, -44f));
//        }
//    }
//
//    public void updateXNormal() {
//        int x = FloatWindow.get(TAG).getX();
//        if (x > ScreenUtils.getScreenWidth(mContext) / 2) {
//            // 右侧
//            FloatWindow.get(TAG).updateX(ScreenUtils.getScreenWidth(mContext) - AutoSizeUtils.dp2px(mContext, 112f));
//        } else {
//            // 左侧
//            FloatWindow.get(TAG).updateX(AutoSizeUtils.pt2px(mContext, 112f));
//        }
//    }
//}
