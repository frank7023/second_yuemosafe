package com.example.yuemosafe.activity.cacheclear;

import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuemosafe.R;
import com.example.yuemosafe.activity.cacheclear.adapter.CacheCleanAdapter;
import com.example.yuemosafe.activity.cacheclear.bean.CacheInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CacheClearListActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final int SCANNING = 100;
    protected static final int FINISH = 101;
    private AnimationDrawable animation;
    /** 建议清理 */
    private TextView mRecomandTV;
    /** 可清理 */
    private TextView mCanCleanTV;
    private long cacheMemory;
    private List<CacheInfo> cacheInfos = new ArrayList<CacheInfo>();
    private List<CacheInfo> mCacheInfos = new ArrayList<CacheInfo>();
    private PackageManager pm;
    private CacheCleanAdapter adapter;
    private ListView mCacheLV;
    private Button mCacheBtn;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCANNING:
                    PackageInfo info = (PackageInfo) msg.obj;
                    mRecomandTV.setText("正在扫描： "+info.packageName);
                    mCanCleanTV.setText("已扫描缓存 ："+ Formatter.formatFileSize(CacheClearListActivity.this, cacheMemory));
                    //在主线程添加变化后集合
                    mCacheInfos.clear();
                    mCacheInfos.addAll(cacheInfos);
                    //ListView  刷新
                    adapter.notifyDataSetChanged();
                    mCacheLV.setSelection(mCacheInfos.size());
                    break;
                case FINISH:
                    //扫描完了，动画停止
                    animation.stop();
                    if(cacheMemory >0){
                        mCacheBtn.setEnabled(true);
                    }else{
                        mCacheBtn.setEnabled(false);
                        Toast.makeText(CacheClearListActivity.this, "您的手机洁净如新", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        };
    };
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_cache_clear_list);

        //PackageManager
        pm = getPackageManager();
        initView();
    }

    /***
     * 初始化控件
     */
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.rose_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        ((TextView) findViewById(R.id.tv_title)).setText("缓存扫描");
        mRecomandTV = (TextView) findViewById(R.id.tv_recommend_clean);
        mCanCleanTV = (TextView) findViewById(R.id.tv_can_clean);
        mCacheLV = (ListView) findViewById(R.id.lv_scancache);
        mCacheBtn = (Button) findViewById(R.id.btn_cleanall);
        mCacheBtn.setOnClickListener(this);
        animation = (AnimationDrawable) findViewById(R.id.imgv_broom)
                .getBackground();
        animation.setOneShot(false);
        animation.start();
        adapter = new CacheCleanAdapter(this, mCacheInfos);
        mCacheLV.setAdapter(adapter);
        fillData();
    }

    /***
     * 填充数据
     */
    private void fillData() {
        thread = new Thread() {

            public void run() {
                // 遍历手机里面的所有的应用程序。
                cacheInfos.clear();
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                for (PackageInfo info : infos) {
                    getCacheSize(info);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    msg.obj = info;
                    msg.what = SCANNING;
                    handler.sendMessage(msg);
                }
                Message msg = Message.obtain();
                msg.what = FINISH;
                handler.sendMessage(msg);
            };
        };
        thread.start();
    }

    /**
     * 获取某个包名对应的应用程序的缓存大小
     *
     * @param info
     *            应用程序的包信息
     */
    public void getCacheSize(PackageInfo info) {
        try {
            Method method = PackageManager.class.getDeclaredMethod(
                    "getPackageSizeInfo", String.class,
                    IPackageStatsObserver.class);
            method.invoke(pm, info.packageName, new MyPackObserver(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_cleanall:
                if(cacheMemory >0){
                    //跳转至清理缓存的页面的Activity
                    Intent intent = new Intent(this,CleanCacheActivity.class);
                    //将要清理的垃圾大小传递至另一个页面
                    intent.putExtra("cacheMemory", cacheMemory);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animation.stop();
        if(thread != null){
            thread.interrupt();
        }
    }

    private class MyPackObserver extends android.content.pm.
            IPackageStatsObserver.Stub {
        private PackageInfo info;

        public MyPackObserver(PackageInfo info) {
            this.info = info;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                throws RemoteException {
            long cachesize = pStats.cacheSize;
            if (cachesize >= 0) {
                CacheInfo cacheInfo = new CacheInfo();
                cacheInfo.cacheSize = cachesize;
                cacheInfo.packagename = info.packageName;
                cacheInfo.appName = info.applicationInfo.loadLabel(pm)
                        .toString();
                cacheInfo.appIcon = info.applicationInfo.loadIcon(pm);
                cacheInfos.add(cacheInfo);
                cacheMemory += cachesize;
            }
        }
    }
}
