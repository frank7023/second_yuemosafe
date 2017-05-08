package com.example.yuemosafe.activity.advancedtools;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuemosafe.R;
import com.example.yuemosafe.activity.advancedtools.utils.SmsBackUpUtils;
import com.example.yuemosafe.activity.advancedtools.utils.UIUtils;
import com.example.yuemosafe.activity.advancedtools.widget.MyCircleProgress;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 短信备份
 **/
public class SMSBackupActivity extends AppCompatActivity implements View.OnClickListener {

    private MyCircleProgress mProgressButton;
    /**
     * 标识符，用来标识备份状态的
     */
    private boolean flag = false;
    private SmsBackUpUtils smsBackUpUtils;
    private static final int CHANGE_BUTTON_TEXT = 100;
    private static final int COPY_SUCCESS_TEXT = 101;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CHANGE_BUTTON_TEXT:
                    mProgressButton.setText("一键备份");
                    break;
                case COPY_SUCCESS_TEXT:
                    mProgressButton.setText("备份成功");
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_smsbackup);
        smsBackUpUtils = new SmsBackUpUtils();
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("短信备份");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);

        mProgressButton = (MyCircleProgress) findViewById(R.id.mcp_smsbackup);
        mProgressButton.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        if (flag) {
            flag = false;
        }
        smsBackUpUtils.setFlag(flag);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.mcp_smsbackup:
                if (flag) {
                    flag = false;
                    mProgressButton.setText("一键备份");
                } else {
                    flag = true;
                    mProgressButton.setText("取消备份");
                }
                smsBackUpUtils.setFlag(flag);
                new Thread() {

                    public void run() {
                        try {
                            boolean backUpSms = smsBackUpUtils.backUpSms(SMSBackupActivity.this,
                                    new SmsBackUpUtils.BackupStatusCallback() {
                                        @Override
                                        public void onSmsBackup(int process) {
                                            mProgressButton.setProcess(process);
                                        }

                                        @Override
                                        public void beforeSmsBackup(int size) {
                                            if (size <= 0) {
                                                flag = false;
                                                smsBackUpUtils.setFlag(flag);
                                                UIUtils.showToast(SMSBackupActivity.this, "您还没有短信！");
                                                mHandler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
                                            } else {
                                                mProgressButton.setMax(size);
                                            }
                                        }
                                    });
                            if (backUpSms) {
                                UIUtils.showToast(SMSBackupActivity.this, "备份成功");
                                mHandler.sendEmptyMessage(COPY_SUCCESS_TEXT);

                                mHandler.sendEmptyMessageDelayed(CHANGE_BUTTON_TEXT,2000);
                            } else {
                                UIUtils.showToast(SMSBackupActivity.this, "备份失败");
                                mHandler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSBackupActivity.this, "文件生成失败");
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSBackupActivity.this, "SD卡不可用或SD卡内存不足");
                        } catch (IOException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSBackupActivity.this, "读写错误");
                        }
                    }
                }.start();
                break;
        }
    }
}
