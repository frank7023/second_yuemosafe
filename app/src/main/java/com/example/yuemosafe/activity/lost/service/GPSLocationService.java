package com.example.yuemosafe.activity.lost.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;


public class GPSLocationService extends Service {
    private LocationManager lm;


    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//获取准确的位置
        criteria.setCostAllowed(true);//容许产生费用
        String provider = lm.getBestProvider(criteria, true);//从打开的定位设备中选取

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(provider, 0, 0, listener);


    }

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            StringBuffer sb = new StringBuffer();
            float accuracy = location.getAccuracy();//获得精确度
            float speed = location.getSpeed();//获得移动的速度
            double longitude = location.getLongitude();//经度
            double latitude = location.getLatitude();//维度
            sb.append("accuracy:" + accuracy + "\n" + "speed:" + speed + "\n" +
                    "longitude:" + longitude + "\n" + "latitude:" + latitude + "\n");

            String result = sb.toString();
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            String safephone = sp.getString("safephone", null);
            //发送相关信息给安全号码
            SmsManager.getDefault().sendTextMessage(safephone, null, result, null, null);

            stopSelf();//停止服务

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (listener!=null) {
            lm.removeUpdates(listener);
            listener=null;
        }
    }
}
