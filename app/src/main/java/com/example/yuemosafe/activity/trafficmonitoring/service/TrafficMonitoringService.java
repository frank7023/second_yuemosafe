package com.example.yuemosafe.activity.trafficmonitoring.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.IBinder;

import com.example.yuemosafe.activity.trafficmonitoring.dao.TrafficDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrafficMonitoringService extends Service {
    private long mOldRxBytes;
    private long mOldTxBytes;
    private TrafficDao dao;
    private SharedPreferences mSp;
    private long usedFlow;
    boolean flag = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mOldRxBytes = TrafficStats.getMobileRxBytes();
        mOldTxBytes = TrafficStats.getMobileTxBytes();
        dao = new TrafficDao(this);
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        mThread.start();
    }

    private Thread mThread = new Thread() {
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(2000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateTodayGPRS();
            }
        }
        private void updateTodayGPRS() {
            // 获取已经使用了的流量
            usedFlow = mSp.getLong("usedflow", 0);
            Date date = new Date();
            Calendar calendar = Calendar.getInstance(); // 得到日历
            calendar.setTime(date);// 把当前时间赋给日历
            if (calendar.DAY_OF_MONTH == 1 & calendar.HOUR_OF_DAY == 0
                    & calendar.MINUTE < 1 & calendar.SECOND < 30) {
                usedFlow = 0;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataString = sdf.format(date);
            long moblieGPRS = dao.getMoblieGPRS(dataString);
            long mobileRxBytes = TrafficStats.getMobileRxBytes();
            long mobileTxBytes = TrafficStats.getMobileTxBytes();
            // 新产生的流量
            long newGprs = (mobileRxBytes + mobileTxBytes) - mOldRxBytes
                    - mOldTxBytes;
            mOldRxBytes = mobileRxBytes;
            mOldTxBytes = mobileTxBytes;
            if (newGprs < 0) {
                // 网络切换过
                newGprs = mobileRxBytes + mobileTxBytes;
            }
            if (moblieGPRS == -1) {
                dao.insertTodayGPRS(newGprs);
            } else {
                if (moblieGPRS < 0) {
                    moblieGPRS = 0;
                }
                dao.UpdateTodayGPRS(moblieGPRS + newGprs);
            }
            usedFlow = usedFlow + newGprs;
            SharedPreferences.Editor edit = mSp.edit();
            edit.putLong("usedflow", usedFlow);
            edit.commit();
        };
    };

    @Override
    public void onDestroy() {
        if (mThread != null & !mThread.interrupted()) {
            flag = false;
            mThread.interrupt();
            mThread = null;
        }
        super.onDestroy();
    }
}
