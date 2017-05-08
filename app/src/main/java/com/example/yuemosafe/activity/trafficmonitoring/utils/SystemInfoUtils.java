package com.example.yuemosafe.activity.trafficmonitoring.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by yueyue on 2017/5/3.
 */

public class SystemInfoUtils {
    /**
     * 判断一个服务是否处于运行状态
     * @param context 上下文
     * @return
     */
    public static boolean isServiceRunning(Context context, String className){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(200);
        for(ActivityManager.RunningServiceInfo info:infos){
            String serviceClassName = info.service.getClassName();
            if(className.equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }

}