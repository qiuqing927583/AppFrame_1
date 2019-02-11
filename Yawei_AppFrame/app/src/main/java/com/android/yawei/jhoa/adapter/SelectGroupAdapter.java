package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.GroupBean;

import java.util.ArrayList;
import java.util.List;

import com.yawei.jhoa.mobile.R;

/******************************
 * 
 * @author Yusz
 * 联系人下拉组适配器
 * 创建时间：2015-6-16
 *
 */

public class SelectGroupAdapter extends BaseAdapter{

	private Context context;
	private Handler handler;
	private List<GroupBean> dataSet=new ArrayList<GroupBean>();

	public SelectGroupAdapter(Context context,List<GroupBean> dataSet,Handler handler) {
		this.dataSet = dataSet;
		this.context = context;
		this.handler=handler;
	}

	
	@Override
	public int getCount() {
		return dataSet.size();
	}

	@Override
	public GroupBean getItem(int arg0) {
		return dataSet.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		try {
			ViewHolder holder = null;
	        if (arg1 == null) { 
	            holder = new ViewHolder();
	            //下拉项布局
	            arg1 = LayoutInflater.from(context).inflate(R.layout.adapter_selectgroup, null);
	            holder.tvGroupName = (TextView) arg1.findViewById(R.id.group_Name); 
	            arg1.setTag(holder); 
	        } else { 
	            holder = (ViewHolder) arg1.getTag();
	        } 
	        holder.tvGroupName.setText(dataSet.get(arg0).getName());
	        
	        if(dataSet.get(arg0).getType().equals("1")){
	        	holder.tvGroupName.setTextColor(Color.BLUE);
			}else{
				holder.tvGroupName.setTextColor(Color.BLACK);
			}
	        //为下拉框选项文字部分设置事件，最终效果是点击将其文字填充到文本框
	        holder.tvGroupName.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Message msg = new Message();
					//设置选中索引
					msg.arg1=arg0;
					msg.what = 10;
					//发出消息
					handler.sendMessage(msg);
				}
			});     
		} catch (Exception e) {
			e.printStackTrace();
		}
        return arg1; 
	}
	class ViewHolder {
		TextView tvGroupName;
		TextView tvGroupType;
		TextView tvGroupGuid;

	}
}


