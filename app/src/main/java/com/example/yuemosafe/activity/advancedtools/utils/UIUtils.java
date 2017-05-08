package com.example.yuemosafe.activity.advancedtools.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by yueyue on 2017/5/4.
 */


public class UIUtils {
    public static void showToast(final Activity activity, final String msg){
        if("main".equals(Thread.currentThread().getName())){
            //toast是运行在主线程中的
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }else{
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}