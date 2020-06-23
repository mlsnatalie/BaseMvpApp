package com.libs.core.business.events;

public class DownloadEvent {

    // 4种下载状态
    public static final int DOWNLOAD_START = -1;// 正在下载
    public static final int DOWNLOAD_STOP = -2;// 正在下载
    public static final int DOWNLOADING = 1;// 正在下载
    public static final int DOWNLOAD_OVERFLOW = 2;// 下载空间不足
    public static final int DOWNLOAD_EXCEPTION = 3;// 下载出现异常
    public static final int DOWNLOAD_SUCCESS = 4;// 下载成功

    int status;

    int progress;

    public DownloadEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public DownloadEvent setStatus(int status) {
        this.status = status;
        return this;
    }

    public int getProgress() {
        return progress;
    }

    public DownloadEvent setProgress(int progress) {
        this.progress = progress;
        return this;
    }
}
