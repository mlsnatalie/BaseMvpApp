package com.libs.core.common.music;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadMusicService extends Service {


    private MusicPlayer musicPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    private void initData() {
        musicPlayer = new MusicPlayer(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            int Action = intent.getIntExtra("Action", 0);
            switch (Action) {
                case 1:
                    startMusic(intent);
                    break;
                case 2:
                    StopMusic();
                    break;

                case 3:
                    if (musicPlayer != null) {
                        if (musicPlayer.getCurrentPosition() > 0 && musicPlayer.getCurrentPosition() == musicPlayer.getDuration()) {
                            startMusic(intent);
                        } else {
                            musicPlayer.play();
                        }
                    }
                    break;

                case 4:
                    destroyMusic();
                    stopSelf();
                    break;

                default:
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void startMusic(Intent intent) {
        String pathString = intent.getStringExtra("Path");
        musicPlayer.playMusic(pathString);
    }

    private void StopMusic() {
        musicPlayer.pause();
    }

    private void destroyMusic() {
        musicPlayer.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        musicPlayer.onDestroy();
        super.onDestroy();
    }

}
