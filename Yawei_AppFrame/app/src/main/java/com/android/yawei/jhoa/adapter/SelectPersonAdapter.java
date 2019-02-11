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
import com.android.yawei.jhoa.mobile.SelectPersonListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yawei.jhoa.mobile.R;

/***
 * Activity
 * 老的发邮件选人
 */
public class SelectPersonAdapter extends BaseAdapter{

	private Context context;
	private List<User> dataset;
	public Map<Integer,Integer> checkedMap;
	
	public SelectPersonAdapter(Context context,List<User> dataset){
		this.context = context;
		this.dataset = dataset;
		if(this.checkedMap==null){
			this.checkedMap = new HashMap<Integer, Integer>();
		}
	}
	@Override
	public int getCount() {
		return dataset.size();
	}

	@Override
	public Object getItem(int position) {
		return dataset.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			final User user = dataset.get(position);
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_selectperson, null);
			//组名
			TextView textView = (TextView) convertView.findViewById(R.id.user_Name);
			textView.setText(user.getFulldisplayName());
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
					SelectPersonListActivity.selectPerson.handler.sendMessage(msg);
				}
			});     
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	public void append(List<User> append) {
		dataset.addAll(append);
	}

	public void clear() {
		dataset = new ArrayList<User>();
	}

	public User remove(int location) {
		return dataset.remove(location);
	}
}
