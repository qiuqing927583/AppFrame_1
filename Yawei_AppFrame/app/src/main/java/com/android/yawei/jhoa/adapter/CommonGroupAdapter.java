package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.GroupBean;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*************
 * 
 * @author Yusz
 * 创建时间：2015-7-7
 * 功能：设置默认组适配器
 *
 */
public class CommonGroupAdapter extends BaseAdapter{

	private Context context = null;
	private List<GroupBean> dataSet = new ArrayList<GroupBean>();
	public Map<Integer,Integer> checkedMap;
	
	public CommonGroupAdapter(Context context, List<GroupBean> dataSet, Map<Integer,Integer> checkedMap) {
		if (context == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
		this.context = context;
		if(this.checkedMap==null){
			this.checkedMap = new HashMap<Integer, Integer>();
		}
	
	}
	public CommonGroupAdapter(Context context, List<GroupBean> dataSet) {
		if (context == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
		this.context = context;
		if(this.checkedMap==null){
			this.checkedMap = new HashMap<Integer, Integer>();
		}
	
	}
	public CommonGroupAdapter(Context context) {
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
		
		final GroupBean groupBean = dataSet.get(position);
		View view = null;
		view = LayoutInflater.from(context).inflate(R.layout.adapter_commongroup, null);
		
		//组名
		TextView textView = (TextView) view.findViewById(R.id.Group_Name);
		textView.setText(groupBean.getName());
		textView.setTextColor(Color.BLACK);
		ImageView okImg=(ImageView)view.findViewById(R.id.okImage);
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
		
		return view;
	}
	
	public void Updata(List<GroupBean> data) {
		this.dataSet = data;
	}

	public void clear() {
		dataSet.clear();
	}

	public GroupBean remove(int location) {
		return dataSet.remove(location);
	}

}
