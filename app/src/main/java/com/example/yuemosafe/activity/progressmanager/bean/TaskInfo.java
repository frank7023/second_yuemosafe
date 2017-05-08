package com.example.yuemosafe.activity.progressmanager.bean;

/**
 * Created by yueyue on 2017/5/2.
 */

import android.graphics.drawable.Drawable;

/**正在运行的App的信息**/
public class TaskInfo {

    public String appName;
    public long appMemory;
    /**用来标记app是否被选中*/
    public boolean isChecked;
    public Drawable appIcon;
    public boolean isUserApp;
    public String  packageName;
}
