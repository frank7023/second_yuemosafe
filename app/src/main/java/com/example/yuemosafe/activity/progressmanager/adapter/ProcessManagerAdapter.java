package com.example.yuemosafe.activity.progressmanager.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yuemosafe.R;
import com.example.yuemosafe.activity.appmanager.utils.DensityUtil;
import com.example.yuemosafe.activity.progressmanager.bean.TaskInfo;

import java.util.List;

/**
 * Created by yueyue on 2017/5/2.
 */

public class ProcessManagerAdapter extends BaseAdapter {

    private Context context;
    private List<TaskInfo> mUsertaskInfos;
    private List<TaskInfo> mSystaskInfos;
    private SharedPreferences mSP;


    public ProcessManagerAdapter(Context context, List<TaskInfo> userTaskInfos,List<TaskInfo> sysTaskInfo) {
        super();
        this.context = context;
        this.mUsertaskInfos = userTaskInfos;
        this.mSystaskInfos = sysTaskInfo;
        mSP = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {

        if(mSystaskInfos.size() >0 & mSP.getBoolean("showSystemProcess", true)){
            return mUsertaskInfos.size()+mSystaskInfos.size()+2;
        }else{
            return mUsertaskInfos.size()+1;
        }
    }

    @Override
    public Object getItem(int position) {
        if(position == 0 || position == mUsertaskInfos.size()+1){
            return null;
        }else if(position <= mUsertaskInfos.size()){
            //用户进程
            return mUsertaskInfos.get(position -1);
        }else{
            //系统进程
            return mSystaskInfos.get(position-mUsertaskInfos.size() -2);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0){
            TextView tv = getTextView();
            tv.setText("用户进程 ："+mUsertaskInfos.size()+"个");
            return tv;
        }else if(position == mUsertaskInfos.size()+1){
            TextView tv = getTextView();
            if(mSystaskInfos.size() >0){
                tv.setText("系统进程："+mSystaskInfos.size()+"个");
                return tv;
            }
        }
        //获取TaskInfo对象
        TaskInfo taskInfo = null;
        if(position <= mUsertaskInfos.size()){
            taskInfo = mUsertaskInfos.get(position-1);
        }else if(mSystaskInfos.size() >0){
            taskInfo = mSystaskInfos.get(position-mUsertaskInfos.size()-2);
        }
        ViewHolder holder = null;
        if(convertView!=null && convertView instanceof RelativeLayout){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = View.inflate(context, R.layout.item_processmanager_list, null);
            holder = new ViewHolder();
            holder.mAppIconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon_processmana);
            holder.mAppMemoryTV =  (TextView) convertView.findViewById(R.id.tv_appmemory_processmana);
            holder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname_processmana);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }
        if(taskInfo != null){
            holder.mAppNameTV.setText(taskInfo.appName);
            holder.mAppMemoryTV.setText("占用内存: "+ Formatter.formatFileSize(context,taskInfo.appMemory));
            holder.mAppIconImgv.setImageDrawable(taskInfo.appIcon);
            if(taskInfo.packageName.equals(context.getPackageName())){
                holder.mCheckBox.setVisibility(View.GONE);
            }else{
                holder.mCheckBox.setVisibility(View.VISIBLE);
            }
            holder.mCheckBox.setChecked(taskInfo.isChecked);
        }
        return convertView;
    }

    /***
     * 创建一个TextView
     * @return
     */
    private TextView getTextView() {
        TextView tv = new TextView(context);
        tv.setBackgroundColor(context.getResources()
                .getColor(R.color.graye5));
        tv.setPadding(DensityUtil.dip2px(context, 5),
                DensityUtil.dip2px(context, 5),
                DensityUtil.dip2px(context, 5),
                DensityUtil.dip2px(context, 5));
        tv.setTextColor(context.getResources().getColor(R.color.black));
        return tv;
    }
    static class ViewHolder{
        ImageView mAppIconImgv;
        TextView mAppNameTV;
        TextView mAppMemoryTV;
        CheckBox mCheckBox;
    }

}
