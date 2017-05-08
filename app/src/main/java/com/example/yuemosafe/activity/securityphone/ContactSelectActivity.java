package com.example.yuemosafe.activity.securityphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yuemosafe.R;
import com.example.yuemosafe.activity.securityphone.adapter.ContactAdapter;
import com.example.yuemosafe.activity.securityphone.bean.ContactInfo;
import com.example.yuemosafe.activity.securityphone.utils.ContactInfoParser;

import java.util.List;


public class ContactSelectActivity extends Activity implements OnClickListener {

	private ListView mListView;
	private ContactAdapter adapter;
	private List<ContactInfo> systemContacts;
	Handler mHandler  = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				if(systemContacts != null){
					adapter = new ContactAdapter(systemContacts,ContactSelectActivity.this);
					mListView.setAdapter(adapter);
				}
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contact_select);
		initView();
	}

	private void initView() {
		((TextView) findViewById(R.id.tv_title)).setText("ѡ����ϵ��");
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_purple));
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.back);
		mListView = (ListView) findViewById(R.id.lv_contact);
		new Thread(){
			public void run() {
				systemContacts = ContactInfoParser.getSystemContact(ContactSelectActivity.this);
				systemContacts.addAll(ContactInfoParser.getSimContacts(ContactSelectActivity.this));
				mHandler.sendEmptyMessage(10);
			};
		}.start();
		mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ContactInfo item = (ContactInfo) adapter.getItem(position);
						Intent intent  = new Intent();
						intent.putExtra("phone", item.phone);
						intent.putExtra("name", item.name);
						setResult(0, intent);
						finish();
					}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;

		}
	}
}
