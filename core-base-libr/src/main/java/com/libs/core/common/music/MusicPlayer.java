package com.libs.core.common.music;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.media.AudioAttributesCompat;
import android.text.TextUtils;
import android.view.TextureView;
import android.widget.Toast;

import com.libs.core.business.events.MusicEvent;
import com.libs.core.common.rxbus.RxBus;
import com.libs.core.common.utils.LogUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer {

    public static final String TAG = "MusicPlayer";

    private int currentStatus = -1;

    private Context context;

    private MediaPlayer mediaPlayer;

    private Timer mTimer;

    private TimerTask mTask;

    private boolean isPrepared = false;

    private String url;

    public MusicPlayer(Context context) {
        this.context = context;
        mTimer = new Timer();
    }

    private void initMediaPlayer() {
        LogUtils.d(TAG, "initMediaPlayer");
        mediaPlayer = new MediaPlayer();
        // 设置是否循环播放
        mediaPlayer.setLooping(false);
        //播放完成监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.d(TAG, "setOnCompletionListener onCompletion");
                if (getDuration() > 0 && getCurrentPosition() * 1.0f / getDuration() > 0.99f) {
                    FloatWindowManager.get().updateComplete();
                    if (currentStatus != MusicEvent.PAUSE) {
                        currentStatus = MusicEvent.PAUSE;
                        notifyPlayChange();
                    }
                }
            }
        });
        //网络流媒体缓冲监听
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                // percent 0~100
                LogUtils.d(TAG, "缓存进度" + percent + "%");
                if (isPlaying() && currentStatus != MusicEvent.PLAYING) {
                    currentStatus = MusicEvent.PLAYING;
                    notifyPlayChange();
                }
            }
        });
        //准备Prepared完成监听
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtils.d(TAG, "onPrepared");
                isPrepared = true;
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        });

        //进度调整完成SeekComplete监听，主要是配合seekTo(int)方法
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                LogUtils.d(TAG, "setOnSeekCompleteListener onSeekComplete");
            }
        });


        if (currentStatus != MusicEvent.BUFFERING) {
            currentStatus = MusicEvent.BUFFERING;
            notifyPlayChange();
        }
    }

    public int getDuration() {
        LogUtils.d(TAG, "getDuration");
        if (isPrepared && mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        LogUtils.d(TAG, "getCurrentPosition");
        if (isPrepared && mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void play() {
        LogUtils.d(TAG, "Play");
        if (isPrepared && mediaPlayer != null && !mediaPlayer.isPlaying()) {
            geTimerTask();
            mTimer.schedule(mTask, 0, 20);
            mediaPlayer.start();
        }

        if (currentStatus != MusicEvent.PLAYING) {
            currentStatus = MusicEvent.PLAYING;
            notifyPlayChange();
        }
    }


    public void playMusic(String pathString) {
        LogUtils.d(TAG, "PlayMusic");
        if (TextUtils.isEmpty(pathString)) {
            return;
        }
        if (!onRequestAudioFocus()) {
            Toast.makeText(context, "当前音频通道已被暂用!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {

            if (mediaPlayer != null) {

                url = pathString;

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                mediaPlayer.release();

                mediaPlayer = null;
                isPrepared = false;

                initMediaPlayer();
                mediaPlayer.setDataSource(pathString);
                mediaPlayer.prepareAsync();
                LogUtils.d(TAG, "initMediaPlayer prepareAsync");

            } else {
                initMediaPlayer();
                mediaPlayer.setDataSource(pathString);
                mediaPlayer.prepareAsync();
                LogUtils.d(TAG, "initMediaPlayer prepareAsync");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FloatWindowManager.get().update(0, 0f);
        geTimerTask();

        try {
            mTimer.schedule(mTask, 0, 1000);
        } catch (Exception e) {
            e.printStackTrace();
            mTimer = null;
            mTimer = new Timer();
            mTimer.schedule(mTask, 0, 1000);
        }


    }

    private void geTimerTask() {
        cancelTask();
        mTask = new TimerTask() {

            @Override
            public void run() {
                //getDuration 获取的参数可能是非法的
                int tempDuration = getDuration();
                tempDuration = tempDuration <= 0 ? 0 : tempDuration;
                FloatWindowManager.get().update(tempDuration / 1000,
                        tempDuration == 0 ? 0f : getCurrentPosition() * 1.0f / tempDuration);
                /*FloatWindowManager.get().update(getDuration() / 1000,
                        getDuration() == 0 ? 0f : getCurrentPosition() * 1.0f / getDuration());*/
            }
        };
    }

    private void cancelTask() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    public boolean isPlaying() {
        return isPrepared && mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void pause() {
        LogUtils.d(TAG, "Pause");
        cancelTask();
        if (isPrepared && mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (currentStatus != MusicEvent.PAUSE) {
                currentStatus = MusicEvent.PAUSE;
                notifyPlayChange();
            }
        }
    }

    /**
     * 状态改变更新状态
     */
    private void notifyPlayChange() {
        PlayMusicManager.get().setStatus(currentStatus);
        MusicEvent musicEvent = new MusicEvent(MusicEvent.MUSIC_STATUS_CHANGE);
        RxBus.getInstance().post(musicEvent);
    }


    public void onDestroy() {
        LogUtils.d(TAG, "onDestroy");
        cancelTask();
        if (currentStatus != MusicEvent.PAUSE) {
            currentStatus = MusicEvent.PAUSE;
            notifyPlayChange();
        }
        if (isPrepared && mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        mediaPlayer = null;
        url = null;
        isPrepared = false;

        onAbandonAudioFocus();
    }


    //请求音频焦点
    AudioManager audioManager;
    AudioAttributes playbackAttributes;
    AudioFocusRequest focusRequest;
    final Object focusLock = new Object();

    private boolean onRequestAudioFocus() {

        onAbandonAudioFocus();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int res = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            playbackAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(playbackAttributes)
                    .setAcceptsDelayedFocusGain(false)
                    //.setWillPauseWhenDucked() //	当其他应用使用 AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK 请求焦点时，持有焦点的应用通常不会收到 onAudioFocusChange() 回调，因为系统可以自行降低音量。如果您需要暂停播放而不是降低音量，请调用 setWillPauseWhenDucked(true)，然后创建并设置 OnAudioFocusChangeListener，具体如自动降低音量中所述。
                    //.setOnAudioFocusChangeListener(focusChangeListener, new Handler())
                    .setOnAudioFocusChangeListener(focusChangeListener)
                    //.setWillPauseWhenDucked()  //告诉系统使用您的回调，而不是执行自动降低音量。
                    .build();
            res = audioManager.requestAudioFocus(focusRequest);
        } else {
            res = audioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
        LogUtils.e(TAG, "audio focus = " + res);
        synchronized (focusLock) {
            if (res == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                return false;
            } else if (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                return true;
            } else if (res == AudioManager.AUDIOFOCUS_REQUEST_DELAYED) {
                return false;
            }
        }
        return false;
    }

    private void onAbandonAudioFocus() {
        if (audioManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (focusRequest != null) {
                    audioManager.abandonAudioFocusRequest(focusRequest);
                }
            } else {
                audioManager.abandonAudioFocus(focusChangeListener);
            }
        }
    }

      AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            String strFocus = "";
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    strFocus = "AUDIOFOCUS_GAIN";
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    strFocus = "AUDIOFOCUS_GAIN_TRANSIENT";
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE:
                    strFocus = "AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE";
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    strFocus = "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK";

                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    strFocus = "AUDIOFOCUS_LOSS";
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    strFocus = "AUDIOFOCUS_LOSS_TRANSIENT";
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    strFocus = "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK";
                    break;
            }
            LogUtils.e(TAG, "audio focusChange = " + strFocus);
        }
    };
}