package com.example.yuemosafe.activity.advancedtools;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuemosafe.R;
import com.example.yuemosafe.activity.advancedtools.utils.SmsReducitionUtils;
import com.example.yuemosafe.activity.advancedtools.utils.UIUtils;
import com.example.yuemosafe.activity.advancedtools.widget.MyCircleProgress;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


/**短信还原**/
public class SMSReducitionActivity extends AppCompatActivity implements View.OnClickListener {

    private MyCircleProgress mProgressButton;
    private boolean flag = false;
    private SmsReducitionUtils smsReducitionUtils;
    private static final int CHANGE_BUTTON_TEXT = 100;
    private static final int COPY_SUCCESS_TEXT = 101;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CHANGE_BUTTON_TEXT:
                    mProgressButton.setText("一键还原");
                    break;
                case COPY_SUCCESS_TEXT:
                    mProgressButton.setText("还原成功");
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_smsreducition);
        initView();
        smsReducitionUtils = new SmsReducitionUtils();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("短信还原");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);

        mProgressButton = (MyCircleProgress) findViewById(R.id.mcp_reducition);
        mProgressButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        flag = false;
        smsReducitionUtils.setFlag(flag);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.mcp_reducition:
                if(flag){
                    flag = false;
                    mProgressButton.setText("一键还原");
                }else{
                    flag = true;
                    mProgressButton.setText("取消还原");
                }
                smsReducitionUtils.setFlag(flag);
                new Thread(){

                    public void run() {
                        try {
                            boolean iSReduSuccess = smsReducitionUtils.reducitionSms(SMSReducitionActivity.this,
                                    new SmsReducitionUtils.SmsReducitionCallBack() {

                                        @Override
                                        public void onSmsReducition(int process) {
                                            mProgressButton.setProcess(process);
                                        }

                                        @Override
                                        public void beforeSmsReducition(int size) {
                                            mProgressButton.setMax(size);
                                        }
                                    });
                            if (iSReduSuccess) {
                                UIUtils.showToast(SMSReducitionActivity.this, "还原成功");
                                mHandler.sendEmptyMessage(COPY_SUCCESS_TEXT);

                                mHandler.sendEmptyMessageDelayed(CHANGE_BUTTON_TEXT,2000);
                            } else {
                                UIUtils.showToast(SMSReducitionActivity.this, "还原失败");
                                mHandler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
                            }

                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSReducitionActivity.this, "文件格式错误");
                            mHandler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
                        } catch (IOException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSReducitionActivity.this, "读写错误");
                            mHandler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
                        }
                    }
                }.start();
                break;
        }
    }
}
