package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.mobile.GroupPersonListActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yawei.jhoa.mobile.R;

public class GroupPersonAdapter extends BaseAdapter {
	private Context context = null;
	private List<User> dataSet;
	public Map<Integer,Integer> checkedMap;
	
	public GroupPersonAdapter(Context context, List<User> dataSet, Map<Integer,Integer> checkedMap) {
		if (context == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
		this.context = context;
		if(this.checkedMap==null){
			this.checkedMap = new HashMap<Integer, Integer>();
		}
	
	}
	public GroupPersonAdapter(Context context, List<User> dataSet) {
		if (context == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
		this.context = context;
		if(this.checkedMap==null){
			this.checkedMap = new HashMap<Integer, Integer>();
		}
	
	}
	public GroupPersonAdapter(Context context) {
		if (context == null) {
			throw new NullPointerException();
		}
		this.context = context;
	
	}
	@Override
	public int getCount() {
		return dataSet.size();
	}

	@Override
	public User getItem(int position) {
		return dataSet.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final User user = dataSet.get(position);
		View view = null;
		view = LayoutInflater.from(context).inflate(R.layout.adapter_groupperson, null);
		
		//组名
		TextView textView = (TextView) view.findViewById(R.id.user_Name);
		textView.setText(user.getFulldisplayName());
		textView.setTextColor(Color.BLACK);
		ImageView okImg=(ImageView)view.findViewById(R.id.okImage);
		ImageView moreImg=(ImageView)view.findViewById(R.id.moreImage);
	    TextView isCheck=(TextView)view.findViewById(R.id.ischeck);
		if(checkedMap.get(position)==null){
			//未选中值为-11
			 checkedMap.put(position, -11);
		}
		if(checkedMap.get(position)!=-11){
			okImg.setImageResource(R.drawable.add);
			isCheck.setText("true");
		}else{
			okImg.setImageResource(R.drawable.del);
			isCheck.setText("false");
		}
		moreImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", user);
					bundle.putInt("sign", position);
					Message msg = new Message();
					//设置选中索引
//					msg.obj=user;
					msg.setData(bundle);
					msg.what = 11;
					//发出消息
					GroupPersonListActivity.Activity_EntityActivity.handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});     
		return view;
	}
	
	public void UpData(List<User> dataSet) {
		this.dataSet = dataSet;
	}

	public void clear() {
		dataSet.clear();
	}

	public User remove(int location) {
		return dataSet.remove(location);
	}

}
