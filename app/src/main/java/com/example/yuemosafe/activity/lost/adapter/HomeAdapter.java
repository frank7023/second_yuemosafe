package com.example.yuemosafe.activity.lost.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuemosafe.R;
import com.example.yuemosafe.activity.main.HomeActivity;


/**
 * GridView显示界面用到的Adapter
 * Created by yueyue on 2017/4/22.
 */
public class HomeAdapter extends BaseAdapter{
    int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.trojan, R.drawable.sysoptimize,R.drawable.taskmanager,
            R.drawable.netmanager,R.drawable.atools,R.drawable.settings };
    String[] names = { "手机防盗",   "通讯卫士","软件管家","手机杀毒","缓存清理","进程管理",
            "流量统计", "高级工具", "设置中心" };

    private Activity homeActivity;

    public HomeAdapter(HomeActivity homeActivity) {
        this.homeActivity=homeActivity;
    }


    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null) {

            viewHolder=new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_home, null);
            viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder .tv_name= (TextView) convertView.findViewById(R.id.tv_name);


            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.iv_icon.setImageResource(imageId[position]);
        viewHolder .tv_name.setText(names[position]);

        return convertView;
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
    }

}
