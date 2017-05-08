package com.example.yuemosafe.activity.lost.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.yuemosafe.R;

public class AlarmService extends Service {

    //创建一个播放音乐的实例
    private MediaPlayer mPlayer;

    public AlarmService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer=MediaPlayer.create(this, R.raw.shimian);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.setVolume(0,1.0f);
        mPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
