package com.example.yuemosafe.activity.advancedtools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yuemosafe.R;

/**
 * Created by yueyue on 2017/5/4.
 */

public class AdvancedToolsView extends RelativeLayout {

    private String desc;
    private Drawable drawable;
    private TextView mDesriptionTV;
    private ImageView mLeftImgv;

    public AdvancedToolsView(Context context) {
        super(context);
        initView(context);
    }

    public AdvancedToolsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.AdvancedToolsView);
        desc = typedArray.getString(R.styleable.AdvancedToolsView_desc);
        drawable = typedArray.getDrawable(R.styleable.AdvancedToolsView_android_src);

        typedArray.recycle();//回收资源
        initView(context);
    }

    public AdvancedToolsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.ui_advancedtools_view, null);
        this.addView(view);//布局挂载到当前AdvancedToolsView
        mDesriptionTV = (TextView) findViewById(R.id.tv_decription);
        mLeftImgv = (ImageView) findViewById(R.id.imgv_left);
        mDesriptionTV.setText(desc);
        if (drawable != null) {
            mLeftImgv.setImageDrawable(drawable);
        }


    }
}
