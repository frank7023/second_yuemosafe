package com.example.yuemosafe.activity.lost.utils;

/**
 * Created by yueyue on 2017/4/22.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 主要用于保存应用的一些参数设置
 */
public class SpUtils {

    private static SharedPreferences sp=null;
    /**
     * 在sp中存储boolean类型数据
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context,String key,boolean value){
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 在sp中取出boolean类型数据
     * @param context
     * @param key
     * @return  key对应的value,默认返回false
     */
    public static boolean getBoolean(Context context,String key){
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,false);
    }

    /**
     * 在sp中存储String类型数据
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context,String key, String value) {
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }

    /**
     * 在sp中取出String类型数据
     * @param context
     * @param key
     * @return key对应的value,默认返回空字符串""
     */
    public static String getString(Context context, String key){
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key,"");
    }
}
