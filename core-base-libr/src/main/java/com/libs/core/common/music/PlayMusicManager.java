package com.libs.core.common.music;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.libs.core.business.events.MusicEvent;
import com.libs.core.common.utils.ToastHelper;

public class PlayMusicManager {

    private static PlayMusicManager instance;

    private String url;

    private int status;

    private String id;

    private PlayMusicManager() {
        status = MusicEvent.PAUSE;
    }

    public static PlayMusicManager get() {

        if (instance == null) {
            synchronized (PlayMusicManager.class) {
                if (instance == null) {
                    instance = new PlayMusicManager();
                }
            }
        }

        return instance;
    }


    /**
     * 第一次播放
     *
     * @param context
     * @param path
     */
    /*public void startMusic(Context context, String path) {

        if (TextUtils.isEmpty(path)) {
            ToastHelper.getInstance().showCenter(context, "音频链接错误，无法播放！");
            return;
        }

        this.url = path;

        Intent intent = new Intent(context, DownloadMusicService.class);
        intent.putExtra("Action", 1);
        intent.putExtra("Path", path);
        context.startService(intent);
    }*/
    public void startMusic(Context context, String path,String id) {

        if (TextUtils.isEmpty(path)) {
            ToastHelper.getInstance().showCenter(context, "音频链接错误，无法播放！");
            return;
        }

        this.url = path;
        this.id = id;
        Intent intent = new Intent(context, DownloadMusicService.class);
        intent.putExtra("Action", 1);
        intent.putExtra("Path", path);
        context.startService(intent);
    }
    /**
     * 暂停播放
     *
     * @param context
     */
    public void StopMusic(Context context) {
        Intent intent = new Intent(context, DownloadMusicService.class);
        intent.putExtra("Action", 2);
        context.startService(intent);
    }

    public void resumeMusic(Context context) {
        Intent intent = new Intent(context, DownloadMusicService.class);
        intent.putExtra("Action", 3);
        context.startService(intent);
    }

    public void closeMusic(Context context) {
        Intent intent = new Intent(context, DownloadMusicService.class);
        intent.putExtra("Action", 4);
        context.startService(intent);
    }


    public void reset() {
        this.url = null;
        this.id = null;
        this.status = MusicEvent.PAUSE;
    }

    /*public String getUrl() {
        return url;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public void setUrl(String url) {
//        this.url = url;
//    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
