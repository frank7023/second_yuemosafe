package com.example.yuemosafe.activity.main;

import android.app.Application;

import org.xutils.x;

/**
 * Created by yueyue on 2017/4/21.
 */

/**
 * application,配置应用色设置
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.
        
    }
}
