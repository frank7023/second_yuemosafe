package com.example.yuemosafe.activity.lost.utils;

/**
 * Created by yueyue on 2017/4/21.
 */

import android.os.Environment;

import java.io.File;

/**
 * 存放应用的全局变量
 */
public class GlobalValue {

    /**
     * 安全手机卫士更新安装包存储的位置
     */
    //Environment.getExternalStorageDirectory() + File.separator + "mobilesafe2.apk"
    public static final String UPDATE_APK_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "mobilesafe2.apk";

    /**
     * 安全手机卫士版本获得服务器版本的链接
     */
    public static final String UPDATE_APK_URL = "http://10.0.2.2:8080/yuesafeupdate.json";


    /**
     * 之前是否进行设置过手机防盗界面
     */
    public static final String IS_LOST_SETUP_PASSWORD ="is_setup_password";

    /**
     * 保存进入手机防盗功能对话框的密码
     */
    public static final String DIALOG_LOST_INTER_PWD = "dialog_lost_inter_pwd";

}
