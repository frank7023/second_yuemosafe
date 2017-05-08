package com.example.yuemosafe.activity.scanvirus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuemosafe.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class VirusScanActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG="VirusScanActivity";
    private TextView mLastTimeTV;
    private SharedPreferences mSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_virus_scan);
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        copyDB("antivirus.db");
        initViw();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLastTimeTV.setText(mSP.getString("lastVirusScan", "您还没有查杀病毒！"));
    }

    /**
     * 初始化控件
     */
    private void initViw() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.light_blue));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("病毒查杀");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mLastTimeTV = (TextView) findViewById(R.id.tv_lastscantime);
        findViewById(R.id.rl_allscanvirus).setOnClickListener(this);
    }

    /**
     * 把assets的antivirus.db数据库复制到data/data/com.example.yuemosafe/files文件夹下
     * @param dbName
     */
    private void copyDB(final String dbName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                File file = new File(getFilesDir(), dbName);
                if (file.exists() && file.length()>0) {
                    Log.i(TAG,"antivirus.db数据库早已存在了,不必再复制");
                    return;
                }
                    InputStream is = getAssets().open(dbName);
                    FileOutputStream fos = openFileOutput(dbName, MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len=-1;
                    while ((len=is.read(buffer))!=-1) {
                        fos.write(buffer,0,len);
                    }
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.rl_allscanvirus:
                startActivity(new Intent(this,VirusScanSpeedActivity.class));
                break;
        }
    }
}
