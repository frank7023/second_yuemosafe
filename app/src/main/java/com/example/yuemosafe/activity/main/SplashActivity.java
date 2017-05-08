package com.example.yuemosafe.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yuemosafe.R;
import com.example.yuemosafe.activity.lost.utils.MyUtils;
import com.example.yuemosafe.activity.lost.utils.VersionUpdateUtils;

/**
 * 欢迎界面,主要用于版本的更新
 */
public class SplashActivity extends AppCompatActivity {

    private TextView tv_splash_version;
    private VersionUpdateUtils mUpdateUtils;
    private RelativeLayout rl_root_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initAnimation();


        // TODO: 2017/4/23 3.0版本应该加入联网判断,没有网络就直接进入Home界面,移动网络情况下应该
        //提示一下用户,wifi自动更新


    }

    /**
     * 活动的背景动画
     */
    private void initAnimation() {

        //给背景设置相关动画-缩放动画,透明动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(2000);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setDuration(2000);
        //给布局的背景图片设置动画
        rl_root_splash.startAnimation(animationSet);

       // animationSet.start();
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //获得当前应用的版本号
                String versionCode = MyUtils.getVersion(getApplicationContext());

                tv_splash_version.setText("当前版本号:" + versionCode);
                mUpdateUtils = new VersionUpdateUtils(versionCode, SplashActivity.this);
              //  mUpdateUtils.getCloudVersion();
                mUpdateUtils.enterHome();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


    /**
     * 找到我们需要的组件
     */
    private void initView() {
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        rl_root_splash = (RelativeLayout) findViewById(R.id.rl_root_splash);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_CANCELED) {//确定是安装apk界面返回的消息
            //用户主动取消安装新版的APK,那么就进入当前版本的Home界面
            mUpdateUtils.enterHome();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁前注销handler所有的消息进制,防止内存泄漏
        mUpdateUtils.removeHandler();
    }
}
