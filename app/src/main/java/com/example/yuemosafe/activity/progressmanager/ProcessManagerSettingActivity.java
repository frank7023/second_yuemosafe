package com.example.yuemosafe.activity.progressmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.yuemosafe.R;
import com.example.yuemosafe.activity.progressmanager.service.AutoKillProcessService;
import com.example.yuemosafe.activity.progressmanager.utils.SystemInfoUtils;


public class ProcessManagerSettingActivity extends AppCompatActivity implements View.OnClickListener, OnCheckedChangeListener{

    private ToggleButton mShowSysAppsTgb;
    private ToggleButton mKillProcessTgb;
    private SharedPreferences mSP;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_process_manager_setting);
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        initView();
    }

    /**初始化控件*/
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.bright_green));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        ((TextView) findViewById(R.id.tv_title)).setText("进程管理设置");
        mShowSysAppsTgb = (ToggleButton) findViewById(R.id.tgb_showsys_process);
        mKillProcessTgb = (ToggleButton) findViewById(R.id.tgb_killprocess_lockscreen);
        mShowSysAppsTgb.setChecked(mSP.getBoolean("showSystemProcess", true));
        running = SystemInfoUtils.isServiceRunning(this, "cn.itcast.mobliesafe.chapter07.service.AutoKillProcessService");
        mKillProcessTgb.setChecked(running);
        initListener();
    }

    /**初始化监听*/
    private void initListener() {
        mKillProcessTgb.setOnCheckedChangeListener(this);
        mShowSysAppsTgb.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tgb_showsys_process:
                saveStatus("showSystemProcess",isChecked);
                break;
            case R.id.tgb_killprocess_lockscreen:
                Intent service = new Intent(this,AutoKillProcessService.class);
                if(isChecked){
                    //开启服务
                    startService(service );
                }else{
                    //关闭服务
                    stopService(service);
                }
                break;

        }

    }

    private void saveStatus(String string, boolean isChecked) {
        Editor edit = mSP.edit();
        edit.putBoolean(string, isChecked);
        edit.commit();
    }
}
