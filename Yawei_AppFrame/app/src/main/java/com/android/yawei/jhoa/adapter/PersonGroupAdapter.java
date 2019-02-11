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

import com.android.yawei.jhoa.bean.GroupBean;
import com.android.yawei.jhoa.mobile.PersonGroupListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yawei.jhoa.mobile.R;

/**************
 * 
 * @author Yusz
 * 创建时间：2017-7-7
 *
 */
public class PersonGroupAdapter extends BaseAdapter{

	private Context context = null;
	private List<GroupBean> dataSet = new ArrayList<GroupBean>();
	public Map<Integer,Integer> checkedMap;
	
	public PersonGroupAdapter(Context context, List<GroupBean> dataSet, Map<Integer,Integer> checkedMap) {
		if (context == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
		this.context = context;
		if(this.checkedMap==null){
			this.checkedMap = new HashMap<Integer, Integer>();
		}
	
	}
	public PersonGroupAdapter(Context context, List<GroupBean> dataSet) {
		if (context == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
		this.context = context;
		if(this.checkedMap==null){
			this.checkedMap = new HashMap<Integer, Integer>();
		}
	
	}
	public PersonGroupAdapter(Context context) {
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
	public GroupBean getItem(int position) {
		return dataSet.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			final GroupBean groupBean = dataSet.get(position);
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_persongroup, null);
			
			//组名
			TextView textView = (TextView) convertView.findViewById(R.id.Group_Name);
			textView.setText(groupBean.getName());
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
					try {
					Message msg = new Message();
					//设置选中索引
					Bundle bundleData=new Bundle();
					bundleData.putString("gguid", groupBean.getGuid());
					bundleData.putString("gname", groupBean.getName());
					bundleData.putString("gtype", groupBean.getType());
					msg.setData(bundleData);
					msg.what = 11;
					//发出消息
					PersonGroupListActivity.Activity_EntityActivity.handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});     
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	public void UpData(List<GroupBean> data) {
		this.dataSet = data;
	}

	public void clear() {
		dataSet.clear();
	}

	public GroupBean remove(int location) {
		return dataSet.remove(location);
	}

}
