package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.PersonSettingBean;

import java.util.List;

import com.yawei.jhoa.mobile.R;

/****************888
 * 
 * @author Yusz
 * 个性化适配器
 *
 */
public class PersonaSettingAdapter extends BaseAdapter{

	private List<PersonSettingBean> listData;
	private Context context;
	private LayoutInflater inflater;
	
	public PersonaSettingAdapter(List<PersonSettingBean> listData,Context context){
		
		this.listData = listData;
		this.context = context;
		
	}
	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder viewHolder=null;
			inflater = LayoutInflater.from(context);
	        if(convertView == null) {
	            viewHolder=new ViewHolder();
	            convertView = inflater.inflate(R.layout.adapter_personasetting, null);
	            viewHolder.imgcolor = (ImageView)convertView.findViewById(R.id.imgcolor);
	            viewHolder.textname = (TextView)convertView.findViewById(R.id.textname);
	            convertView.setTag(viewHolder);
	        } else {
	            viewHolder=(ViewHolder)convertView.getTag();
	        }
	        
	        viewHolder.imgcolor.setBackgroundResource(listData.get(position).getColorId());
	        viewHolder.textname.setText(listData.get(position).getColorName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	class ViewHolder {
		 public ImageView imgcolor;
		 public TextView textname;
    }

}
