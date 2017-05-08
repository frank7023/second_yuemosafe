package com.example.yuemosafe.activity.appmanager.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.yuemosafe.activity.appmanager.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取程序的应用信息
 * Created by yueyue on 2017/4/29.
 */

public class AppInfoParser {

    /**
     * 获取手机里面的所有的应用程序
     * @param context 上下文
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context) {
        //获得一个包管理器
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<>();
        for (PackageInfo packInfo:packInfos) {
            AppInfo appInfo = new AppInfo();
            //获得应用的包名
            appInfo.packageName=packInfo.packageName;
            //获得程序的应用图标
            appInfo.icon=packInfo.applicationInfo.loadIcon(pm);
            //获得app应用的名称
            appInfo.appName=packInfo.applicationInfo.loadLabel(pm).toString();
            //获得app应用的安装路径
            appInfo.apkPath=packInfo.applicationInfo.sourceDir;
            File file = new File(appInfo.apkPath);
            appInfo.appSize = file.length();

            //应用程序安装的位置
            int flags = packInfo.applicationInfo.flags;//二进制映射
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags)!=0) {
                //外部存储
                appInfo.isInRoom=false;
            }else {
                //手机内存
                appInfo.isInRoom=true;
            }

            if ((ApplicationInfo.FLAG_SYSTEM & flags)!=0) {
                //系统应用
                appInfo.isUserApp=false;
            }else {
                //用户应用
                appInfo.isUserApp=true;
            }

            appInfos.add(appInfo);
            appInfo=null;//置空让其成为垃圾


        }

        return appInfos;

    }



}
