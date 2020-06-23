package com.libs.core.common.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.libs.core.R;
import com.libs.core.business.sensor.SensorsUtils;
import com.libs.core.common.dialog.SimpleDialog;
import com.libs.core.common.utils.DensityUtils;
import com.libs.core.common.utils.LogUtils;
import com.libs.core.common.utils.ScreenUtils;
import com.libs.core.common.view.PlayStatusView;
import com.libs.core.common.view.XGMarqueeView;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.PermissionUtil;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;


public class FloatWindowManager {

    private static FloatWindowManager instance;

    private static final String TAG = "FloatWindowManager";

    private Handler handler = new Handler();

    private boolean show = false;

    private Class[] activities;

    private Context context;

    private boolean isOpenSetting; // 是否有打开悬浮权限设置页面

    private FloatWindowManager() {
    }

    public static FloatWindowManager get() {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager();
                }
            }
        }

        return instance;
    }

    public void init(Context context, Class... activities) {
        this.context = context;
        this.activities = activities;
    }

    /**
     * @param context
     * @param path
     * @param title
     * @param id       当前播放的项 唯一的标识
     * @param listener
     */
    public void create(final Context context, final String path, String title, String id, View.OnClickListener listener) {
        isOpenSetting = false;
        IFloatWindow aNew = FloatWindow.get(TAG);
        if (aNew == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.float_window_music, null);
            PlayStatusView playStatusView = view.findViewById(R.id.playStatusView);
            FloatWindow
                    .with(context.getApplicationContext())
                    .setWidth(DensityUtils.dp2px(context.getApplicationContext(), 200f))
                    .setHeight(DensityUtils.dp2px(context.getApplicationContext(), 50f))
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
                        }

                        @Override
                        public void onShow() {
                            LogUtils.d("==", "setViewStateListener onShow isShow = " + show);
                            if(isOpenSetting){
                                isOpenSetting = false;
                                if (playStatusView.isPlaying()) {
                                    SensorsUtils.trackCommon("app_article", "音频浮层-暂停");
                                    PlayMusicManager.get().StopMusic(context.getApplicationContext());
                                } else {
                                    SensorsUtils.trackCommon("app_article", "音频浮层-播放");
                                    if (TextUtils.equals(PlayMusicManager.get().getId(), id)) {
                                        PlayMusicManager.get().resumeMusic(context.getApplicationContext());
                                    } else {
                                        PlayMusicManager.get().startMusic(context.getApplicationContext(), path, id);
                                    }
                                }
                                playStatusView.setStatus(playStatusView.getOppositeStatus());
                            }
                        }

                        @Override
                        public void onHide() {
                            LogUtils.d("==", "setViewStateListener onHide show = " + show);
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
                            int y = FloatWindow.get(TAG).getY();
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
                    .setFilter(false, activities)
                    .setTag(TAG)
                    .setDesktopShow(false)
                    .setY(Screen.height, 0.6f)
                    .build();
        }
        //设置显示的数据
        final PlayStatusView playStatusView = FloatWindow.get(TAG).getView().findViewById(R.id.playStatusView);
        playStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playStatusView.isPlaying()) {
                    SensorsUtils.trackCommon("app_article", "音频浮层-暂停");
                    PlayMusicManager.get().StopMusic(context.getApplicationContext());
                } else {
                    SensorsUtils.trackCommon("app_article", "音频浮层-播放");
                    if (TextUtils.equals(PlayMusicManager.get().getId(), id)) {
                        PlayMusicManager.get().resumeMusic(context.getApplicationContext());
                    } else {
                        PlayMusicManager.get().startMusic(context.getApplicationContext(), path, id);
                    }
                }
                playStatusView.setStatus(playStatusView.getOppositeStatus());
            }
        });

        XGMarqueeView tvTitle = FloatWindow.get(TAG).getView().findViewById(R.id.text_title);
        tvTitle.startWithText(title);
        FloatWindow.get(TAG).getView().findViewById(R.id.image_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsUtils.trackCommon("app_article", "音频浮层-关闭");
                PlayMusicManager.get().closeMusic(context.getApplicationContext());
                show = false;
                PlayMusicManager.get().reset();
                destroy();
            }
        });
        FloatWindow.get(TAG).getView().setOnClickListener(listener);
        //设置播放
        if (PermissionUtil.hasPermission(context)) {
            if (TextUtils.isEmpty(path)) {
                hide();
            } else {
                FloatWindow.get(TAG).show();
                /*final PlayStatusView playStatusView = FloatWindow.get(TAG).getView().findViewById(R.id.playStatusView);
                playStatusView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (playStatusView.isPlaying()) {
                            SensorsUtils.trackCommon("app_article", "音频浮层-暂停");
                            PlayMusicManager.get().StopMusic(context.getApplicationContext());
                        } else {
                            SensorsUtils.trackCommon("app_article", "音频浮层-播放");
                            if (TextUtils.equals(PlayMusicManager.get().getId(), id)) {
                                PlayMusicManager.get().resumeMusic(context.getApplicationContext());
                            } else {
                                PlayMusicManager.get().startMusic(context.getApplicationContext(), path, id);
                            }
                        }
                        playStatusView.setStatus(playStatusView.getOppositeStatus());
                    }
                });

                XGMarqueeView tvTitle = FloatWindow.get(TAG).getView().findViewById(R.id.text_title);
                tvTitle.startWithText(title);
                FloatWindow.get(TAG).getView().findViewById(R.id.image_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SensorsUtils.trackCommon("app_article", "音频浮层-关闭");
                        PlayMusicManager.get().closeMusic(context.getApplicationContext());
                        show = false;
                        PlayMusicManager.get().reset();
                        destroy();
                    }
                });*/
                if (TextUtils.equals(PlayMusicManager.get().getId(), id)) {
                    PlayMusicManager.get().resumeMusic(context.getApplicationContext());
                } else {
                    PlayMusicManager.get().startMusic(context.getApplicationContext(), path, id);
                }
                playStatusView.setStatus(PlayStatusView.Status.PLAYING);
                //FloatWindow.get(TAG).getView().setOnClickListener(listener);
                show = true;
            }
        } else {
            showFloatViewDialog(context);
        }
    }

    public void show() {
        if (PermissionUtil.hasPermission(context)) {
            IFloatWindow aNew = FloatWindow.get(TAG);
            if (aNew != null) {
                FloatWindow.get(TAG).show();
            }
        }
    }

    public void hide() {
        if (PermissionUtil.hasPermission(context)) {
            IFloatWindow aNew = FloatWindow.get(TAG);
            if (aNew != null) {
                FloatWindow.get(TAG).hide();
            }
        }
    }

    public void destroy() {
        if (PermissionUtil.hasPermission(context)) {
            show = false;
            PlayMusicManager.get().reset();
            FloatWindow.destroy(TAG);
        }
    }

    public void update(final int second, final float progress) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                IFloatWindow aNew = FloatWindow.get(TAG);

                if (aNew != null) {
                    String mm = second / 60 < 10 ? "0" + second / 60 : second / 60 + "";
                    String ss = second % 60 < 10 ? "0" + second % 60 : second % 60 + "";
                    LogUtils.d("==", "mm =" + mm + ", ss = " + ss);
                    ((TextView) aNew.getView().findViewById(R.id.text_time)).setText(mm + ":" + ss);
                    ((PlayStatusView) aNew.getView().findViewById(R.id.playStatusView)).updateProgress(progress);
                }
            }
        });
    }

    public void updateComplete() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                IFloatWindow aNew = FloatWindow.get(TAG);
                if (aNew != null) {
                    ((PlayStatusView) aNew.getView().findViewById(R.id.playStatusView)).updateComplete();
                }
            }
        });
    }

    public boolean isShow() {
        return FloatWindow.get(TAG) != null && FloatWindow.get(TAG).isShowing();
    }

    private void showFloatViewDialog(Context context) {
        if (!(context instanceof Activity))
            return;
        final SimpleDialog dialog = new SimpleDialog(context);
        dialog.setContentView(R.layout.dialog_float_window).show();
        dialog.getDialog().findViewById(R.id.text_refuse)
                .setOnClickListener(v -> dialog.dismiss());

        dialog.getDialog().findViewById(R.id.imageView_open)
                .setOnClickListener(v -> {
                    dialog.dismiss();
                    isOpenSetting = true;
                    openSetting(context);
                });
    }

    /**
     * android.content.ActivityNotFoundException: No Activity found to handle Intent
     * { act=android.settings.action.MANAGE_OVERLAY_PERMISSION dat=package:com.jindashi.yingstock flg=0x10000000 }
     *
     * @param context
     */
    private void openSetting(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
    }
}
