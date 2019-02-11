package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.SreachSDFileBean;

import java.util.List;

import com.yawei.jhoa.mobile.R;

public class SreachSDFileAdapter extends BaseAdapter{
	
	private Context context;
	public List<SreachSDFileBean> listData;

	public SreachSDFileAdapter(Context context,List<SreachSDFileBean> listData){
		this.context = context;
		this.listData = listData;
	}
	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int postion) {
		return listData.get(postion);
	}

	@Override
	public long getItemId(int postion) {
		return postion;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup arg2) {
		try {
			ViewHolder viewHolder=null;
			LayoutInflater inflater = LayoutInflater.from(context);
	        if(convertView == null) {
	            viewHolder=new ViewHolder();
	            convertView = inflater.inflate(R.layout.adapter_sreachsdfile, null);
	            viewHolder.filename = (TextView)convertView.findViewById(R.id.filename);
	            viewHolder.filePath = (TextView)convertView.findViewById(R.id.filePath);
	            convertView.setTag(viewHolder);
	        }else {
	            viewHolder=(ViewHolder)convertView.getTag();
	        }
	        
	        
	        if(listData.get(postion).getPath() != null && listData.get(postion).getBookName() != null ){
	        	viewHolder.filename.setText(listData.get(postion).getBookName());
	        	viewHolder.filePath.setText(listData.get(postion).getPath());
	        	viewHolder.filePath.setTextColor(Color.GRAY);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return convertView;
	}
	public void UpData(List<SreachSDFileBean> listData){
		this.listData = listData;
	}
	
	class ViewHolder {
		TextView filename;
		TextView filePath;
	}

}
