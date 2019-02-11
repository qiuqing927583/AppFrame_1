package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.SDListBean;

import java.util.List;

import com.yawei.jhoa.mobile.R;

/**************
 * 
 * @author Yusz
 * 创建时间：2015-6-25
 * 功能：附件选择目录适配器
 *
 */

public class SdFileAdapter extends BaseAdapter{

	private List<SDListBean> listData;
	private Context context;
	
	public SdFileAdapter(List<SDListBean> listData,Context context){
		this.listData = listData;
		this.context = context;
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
	public View getView(int postion, View view, ViewGroup arg2) {
		try {
			ViewHolder holder = null; 
	        if (view == null) { 
	            holder = new ViewHolder(); 
	            //下拉项布局
	            view = LayoutInflater.from(context).inflate(R.layout.adapter_sdfile, null);
	            holder.fileName = (TextView) view.findViewById(R.id.filename); 
	            holder.Ico = (ImageView)view.findViewById(R.id.ico);
	            view.setTag(holder); 
	        } else { 
	            holder = (ViewHolder) view.getTag(); 
	        }     
	        holder.fileName.setText(listData.get(postion).getFileName());
	        holder.Ico.setBackgroundResource(listData.get(postion).getDrawableId());
		} catch (Exception e) {
			e.printStackTrace();
		}
        return view; 
	}
	
	class ViewHolder { 
	    TextView fileName; 
	    ImageView Ico; 
	}
	public void UpData(List<SDListBean> data){
		listData = data;
	}
}
