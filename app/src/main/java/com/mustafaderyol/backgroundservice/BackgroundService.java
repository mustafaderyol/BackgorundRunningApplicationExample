package com.mustafaderyol.backgroundservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MustafaDeryol on 08.12.2017.
 */


public class BackgroundService extends Service {
    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;

    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);

        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new BackgroundService.TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            stopSelf();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getDateTime(),
                            Toast.LENGTH_SHORT).show();
                }

            });
        }

        private String getDateTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

    }
}
