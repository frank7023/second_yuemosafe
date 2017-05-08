package com.example.yuemosafe.activity.appmanager.utils;

/**
 * Created by yueyue on 2017/4/29.
 */
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.yuemosafe.activity.appmanager.bean.AppInfo;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**业务工具类*/
public class EngineUtils {
    private static final String TAG="EngineUtils";

    /**
     * 分享应用
     * @param context
     * @param appInfo
     */
    public static void shareApplication(Context  context, AppInfo appInfo) {
        //隐式意图开启短信应用
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.parse("tel:"+110),"text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"我推荐你使用一款很好的应用.名称叫:"
                +appInfo.appName+"官网下载地址:http://dl.360safe.com/360browser_phone.apk");
        context.startActivity(intent);

    }

    /**
     * 启动应用
     * @param context
     * @param appInfo
     */
    public static void startApplication(Context context,AppInfo appInfo){
        PackageManager pm = context.getPackageManager();
        Log.i(TAG,appInfo.packageName);
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
        if (intent!=null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "该app没有启动界面,启动失败", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 开启应用的设置界面
     * @param context
     * @param appInfo
     */
    public static void SettingAppDetail(Context context,AppInfo appInfo) {

        //APPLICATION_DETAILS_SETTINGS
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:"+appInfo.packageName));
        context.startActivity(intent);

    }

    public static void uninstallApplication(Context context,AppInfo appInfo) {
        if (appInfo.isUserApp) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:"+appInfo.packageName));
        } else {
            //系统应用需要root权限,而且需要利用lunix命令去删除文件
            if (!RootTools.isRootAvailable()) {
                Toast.makeText(context, "卸载系统应用必须获取root权限", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!RootTools.isAccessGiven()) {
                Toast.makeText(context, "小悦安全卫士暂时还没有这个权限,请授权", Toast.LENGTH_SHORT).show();
                return;
            }

            //RootTools.sendShell("mount -o -remount,rw /system",3000);
            try {
                RootTools.sendShell("mount -o remount rw /system",8000);
                RootTools.sendShell("rm -r"+appInfo.apkPath, 3000);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (RootToolsException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }

    }
}
