package com.example.yuemosafe.activity.progressmanager.bean;

/**
 * Created by yueyue on 2017/5/2.
 */

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.example.yuemosafe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务信息 & 进程信息的解析器
 *
 * @author Administrator
 *
 */
public class TaskInfoParser {

    /**
     * 获取正在运行的所有的进程的信息。
     * @param context 上下文
     * @return 进程信息的集合
     */
    public static List<TaskInfo> getRunningTaskInfos(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        for (RunningAppProcessInfo processInfo : processInfos) {
            String packname = processInfo.processName;
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.packageName = packname;
            MemoryInfo[]  memroyinfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            long memsize = memroyinfos[0].getTotalPrivateDirty()*1024;
            taskInfo.appMemory = memsize;
            try {
                PackageInfo packInfo = pm.getPackageInfo(packname, 0);
                Drawable icon = packInfo.applicationInfo.loadIcon(pm);
                taskInfo.appIcon = icon;
                String appname = packInfo.applicationInfo.loadLabel(pm).toString();
                taskInfo.appName = appname;
                if((ApplicationInfo.FLAG_SYSTEM&packInfo.applicationInfo.flags)!=0){
                    //系统进程
                    taskInfo.isUserApp = false;
                }else{
                    //用户进程
                    taskInfo.isUserApp = true;
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.appName = packname;
                taskInfo.appIcon = context.getResources().getDrawable(R.drawable.ic_default);
            }


            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}