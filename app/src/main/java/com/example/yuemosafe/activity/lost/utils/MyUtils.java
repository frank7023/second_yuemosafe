package com.example.yuemosafe.activity.lost.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by yueyue on 2017/4/21.
 */

/**
 * 获取版本号跟安装APK
 */
public class MyUtils {

    /**
     * 获得当前应用的版本号
     *
     * @param context 上下文环境
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        // 获取packagemanager的实例
        PackageManager pm = context.getPackageManager();
        try {
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }


    /**
     * 跳转到安装apk的界面
     *
     * @param activity
     */
    public static void installApk(Activity activity) {
        File targetFile = new File(GlobalValue.UPDATE_APK_PATH);
        if (targetFile.exists()) {
            //创建跳转到安装apk界面的意图
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //添加默认分类
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(Uri.fromFile(targetFile),"application/vnd.android.package-archive");
            activity.startActivityForResult(intent, 10);
        }


    }

    /**
     * 判断sdcard是否挂载上来了
     *
     * @return
     */
    public static boolean isSdcardMounted() {
        String SDstate = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(SDstate)) {
            return true;
        }
        return false;
    }

    /**
     * 设置文件下载的路径,如果sd挂载了,那么就下载到sd里面
     *
     * @param fileUrl
     */
    public static String setFilePath(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        String filePath = null;
        //设置下载的路径
        if (isSdcardMounted()) {
            filePath = Environment.getExternalStorageDirectory() +File.separator + fileName;
        } else {
            filePath = Environment.getDataDirectory() +File.separator + fileName;
        }
        return filePath;
    }
}
