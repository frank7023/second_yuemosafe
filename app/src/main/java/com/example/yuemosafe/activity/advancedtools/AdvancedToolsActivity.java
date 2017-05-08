package com.example.yuemosafe.activity.advancedtools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuemosafe.R;


public class AdvancedToolsActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_advanced_tools);
        initView();
    }

    /**初始化控件*/
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("高级工具");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);

        findViewById(R.id.advanceview_applock).setOnClickListener(this);
        findViewById(R.id.advanceview_numbelongs).setOnClickListener(this);
        findViewById(R.id.advanceview_smsbackup).setOnClickListener(this);
        findViewById(R.id.advanceview_smsreducition).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.advanceview_applock:
                //进入程序锁页面
                startActivity(AppLockActivity.class);
                break;
            case R.id.advanceview_numbelongs:
                //进入归属地查询页面
                startActivity(NumBelongtoActivity.class);
                break;
            case R.id.advanceview_smsbackup:
                //进入短信备份页面
                startActivity(SMSBackupActivity.class);
                break;
            case R.id.advanceview_smsreducition:
                //进入短信还原页面
                startActivity(SMSReducitionActivity.class);
                break;
        }
    }

    /**
     * 开启新的activity不关闭自己
     * @param cls 新的activity的字节码
     */
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }
}