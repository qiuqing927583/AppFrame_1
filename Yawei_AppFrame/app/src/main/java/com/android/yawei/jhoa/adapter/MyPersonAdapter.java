package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.mobile.MyPersonListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yawei.jhoa.mobile.R;

/******************
 * 
 * @author Yusz
 * 创建时间：2015-7-9
 * 功能：自定组组成员
 *
 */
public class MyPersonAdapter extends BaseAdapter{

	private Context context = null;
	private List<User> dataSet = new ArrayList<User>();
	public Map<Integer,Integer> checkedMap;
	
	public MyPersonAdapter(Context context, List<User> dataSet, Map<Integer,Integer> checkedMap) {
		if (context == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
		this.context = context;
		if(this.checkedMap==null){
			this.checkedMap = new HashMap<Integer, Integer>();
		}
	
	}
	public MyPersonAdapter(Context context, List<User> dataSet) {
		if (context == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
		this.context = context;
		if(this.checkedMap==null){
			this.checkedMap = new HashMap<Integer, Integer>();
		}
	
	}
	public MyPersonAdapter(Context context) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			final User user = dataSet.get(position);
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_myperson, null);
			
			//组名
			TextView textView = (TextView) convertView.findViewById(R.id.user_Name);
			textView.setText(user.getDisplayName());
			textView.setTextColor(Color.BLACK);
			ImageView okImg=(ImageView)convertView.findViewById(R.id.okImage);
			ImageView moreImg=(ImageView)convertView.findViewById(R.id.moreImage);
		    TextView isCheck=(TextView)convertView.findViewById(R.id.ischeck);
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
					Message msg = new Message();
					//设置选中索引
					msg.obj=user.getPath();
					msg.what = 11;
					//发出消息
					MyPersonListActivity.Activity_EntityActivity.handler.sendMessage(msg);
				}
			});     
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	public void UpData(List<User> data) {
		this.dataSet = data;
	}

	public void clear() {
		dataSet.clear();
	}

	public User remove(int location) {
		return dataSet.remove(location);
	}

}
