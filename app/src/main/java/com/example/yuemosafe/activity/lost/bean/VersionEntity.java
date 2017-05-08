package com.example.yuemosafe.activity.lost.bean;

/**
 * Created by yueyue on 2017/4/21.
 */

public class VersionEntity {
    /**服务器版的版本号*/
    public String versionCode;

    /**新版本的描述*/
    public String des;

    /**apk下载地址*/
    public String apkUrl;

    @Override
    public String toString() {
        return "VersionEntity{" +
                "versionCode='" + versionCode + '\'' +
                ", des='" + des + '\'' +
                ", apkurl='" + apkUrl + '\'' +
                '}';
    }
}
