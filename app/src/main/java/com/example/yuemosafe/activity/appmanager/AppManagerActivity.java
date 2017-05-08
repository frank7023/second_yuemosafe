package com.example.yuemosafe.activity.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yuemosafe.R;
import com.example.yuemosafe.activity.appmanager.adapter.AppManagerAdapter;
import com.example.yuemosafe.activity.appmanager.bean.AppInfo;
import com.example.yuemosafe.activity.appmanager.utils.AppInfoParser;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="AppManagerActivity";
    /**手机剩余内存TextView*/
    private TextView mPhoneMemoryTV;
    /**展示SD卡剩余内存TextView*/
    private TextView mSDMemoryTV;
    private ListView mListView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
    private  List<AppInfo> systemAppInfos = new ArrayList<AppInfo>();
    private AppManagerAdapter adapter;
    /**接收应用程序卸载成功的广播*/
    private UninstallRececiver receciver;
    private Handler mHandler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    if(adapter == null){
                        adapter = new AppManagerAdapter(userAppInfos, systemAppInfos, AppManagerActivity.this);
                        // TODO: 2017/4/29 个人感觉移入这里更为合适
                        mListView.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 15:
                    adapter.notifyDataSetChanged();
                    break;
            }
        };
    };
    private TextView mAppNumTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除顶部
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_app_manager);

        //Intent.ACTION_PACKAGE_REMOVED
        //注册监听卸载的广播
        receciver=new UninstallRececiver();

        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(receciver, intentFilter);
        iniView();

    }

    /**初始化控件*/
    private void iniView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.bright_yellow));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("软件管家");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mPhoneMemoryTV = (TextView) findViewById(R.id.tv_phonememory_appmanager);
        mSDMemoryTV = (TextView) findViewById(R.id.tv_sdmemory_appmanager);
        mAppNumTV = (TextView) findViewById(R.id.tv_appnumber);
        mListView = (ListView) findViewById(R.id.lv_appmanager);
        //拿到手机剩余内存和SD卡剩余内存
        getMemoryFromPhone();
        initData();
        initListener();
    }

    /**初始化监听*/
    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                if (adapter!=null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AppInfo mappInfo = (AppInfo) adapter.getItem(position);
                            //记住当前条目的状态
                            boolean flag = mappInfo.isSelected;
                            //先将所有的集合设为没有选中的先
                            for (AppInfo appInfo:userAppInfos) {
                                appInfo.isSelected=false;
                            }

                            for (AppInfo appInfo:systemAppInfos) {
                                appInfo.isSelected=false;
                            }

                            if (mappInfo!=null) {
                                //如果先前是被选中的,那么改为未选中的
                                if (flag) {
                                    mappInfo.isSelected=false;
                                }else {
                                    mappInfo.isSelected=true;
                                }

                                mHandler.sendEmptyMessage(15);
                            }

                        }
                    }).start();
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i(TAG,"AppManagerActivity中的firstVisibleItem:"+firstVisibleItem);
                if (firstVisibleItem >=userAppInfos.size()+1) {
                    mAppNumTV.setText("系统程序："+systemAppInfos.size()+"个");
                }else{
                    mAppNumTV.setText("用户程序："+userAppInfos.size()+"个");
                }
            }
        });

    }

    /**获得手机内存跟sd卡剩余内存*/
    private void getMemoryFromPhone() {
        long avail_sd = Environment.getExternalStorageDirectory().getFreeSpace();
        long avail_rom = Environment.getDataDirectory().getFreeSpace();
        Log.i(TAG,"AppManagerActivity中的avail_sd:"+avail_sd+"====="+"avail_rom:"+avail_rom);
        String str_avail_sd = Formatter.formatFileSize(this, avail_sd);
        String str_avail_rom = Formatter.formatFileSize(this, avail_rom);

        mPhoneMemoryTV.setText("剩余手机内存：" + str_avail_rom);
        mSDMemoryTV.setText("剩余SD卡内存：" + str_avail_sd);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }

    }

    private class UninstallRececiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播,更新appInfo
            initData();
        }
    }

    private void initData() {
        appInfos=new ArrayList<>();
        //耗时操作,另开线程执行
        new Thread(new Runnable() {
            @Override
            public void run() {
                //使用前最好移除,因为之前不知道它是不是有其他数据的
                appInfos.clear();
                userAppInfos.clear();
                systemAppInfos.clear();
                appInfos.addAll(AppInfoParser.getAppInfos(AppManagerActivity.this));

                for (AppInfo appInfo:appInfos) {
                    if (appInfo.isUserApp) {
                        //用户应用
                        userAppInfos.add(appInfo);
                    }else {
                        systemAppInfos.add(appInfo);
                    }

                    mHandler.sendEmptyMessage(10);
                }
            }
        }).start();
    }

    //退出前最好移除监听器
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receciver!=null) {
            unregisterReceiver(receciver);
            receciver=null;
        }
    }
}
