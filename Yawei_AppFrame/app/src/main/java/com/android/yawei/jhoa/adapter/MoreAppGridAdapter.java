package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.MoreAppBean;
import com.android.yawei.jhoa.utils.FileUtils;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.util.List;

import com.yawei.jhoa.mobile.R;

/**********
 * 
 * @author Yusz
 * 更多界面适配器
 *
 */
public class MoreAppGridAdapter extends BaseAdapter{
	private List<MoreAppBean> listData;
	private Context context;
	private LayoutInflater inflater;
	  
	public MoreAppGridAdapter(List<MoreAppBean> listData, Context context){
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
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			ViewHolder viewHolder=null;
			inflater = LayoutInflater.from(context);
	        if(convertView == null) {
	            viewHolder=new ViewHolder();
	            convertView = inflater.inflate(R.layout.adapter_moreappgrid, null);
	            viewHolder.iv_face=(ImageView)convertView.findViewById(R.id.ItemImage);
	            viewHolder.iv_face1=(ImageView)convertView.findViewById(R.id.ItemImage1);
	            viewHolder.text_title = (TextView)convertView.findViewById(R.id.ItemText);
	            viewHolder.text_title1 = (TextView)convertView.findViewById(R.id.ItemText1);
	            viewHolder.rel = (RelativeLayout)convertView.findViewById(R.id.rel);
	            viewHolder.imagNew = (ImageView)convertView.findViewById(R.id.newImg);
	            convertView.setTag(viewHolder);
	        } else {
	            viewHolder=(ViewHolder)convertView.getTag();
	        }
	        
	        viewHolder.text_title.setText(listData.get(position).getAppName());
	        viewHolder.text_title1.setText(listData.get(position).getAppName());
	        
	        if(listData.get(position).getAppDrawable() != 0){
	        	viewHolder.iv_face.setBackgroundResource(listData.get(position).getAppDrawable());
	        	viewHolder.iv_face.setVisibility(View.VISIBLE);
	        	viewHolder.iv_face1.setVisibility(View.GONE);
	        	viewHolder.text_title.setVisibility(View.VISIBLE);
	        	viewHolder.text_title1.setVisibility(View.GONE);
	        }else{
	        	try {
	        		File file = new File(FileUtils.GetSDPath()+"jhoaMobile/Bitmap");
	        		if(!file.exists())
	        			file.mkdirs();
	        		//初始化，上下文，使用磁盘缓存--缓存位置
	        		BitmapUtils bitmapUtils = new BitmapUtils(context,FileUtils.GetSDPath()+"jhoaMobile/Bitmap");
	        		bitmapUtils.configMemoryCacheEnabled(false);//是否使用内存缓存，如果很少的网络图片可以使用内存缓存，如果不确定或者是图片多请使用磁盘缓存
	        		bitmapUtils.configDiskCacheEnabled(true);//是否使用磁盘缓存，磁盘是高速缓存
	    			bitmapUtils.display(viewHolder.iv_face1, listData.get(position).getImgUrl());
	    			viewHolder.iv_face.setVisibility(View.GONE);
	            	viewHolder.iv_face1.setVisibility(View.VISIBLE);
	            	viewHolder.text_title.setVisibility(View.GONE);
	            	viewHolder.text_title1.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	        if(listData.get(position).getIsnew().equals("1")){
	        	viewHolder.imagNew.setVisibility(View.VISIBLE);
	        }else{
	        	viewHolder.imagNew.setVisibility(View.GONE);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	
	class ViewHolder {
		 public ImageView iv_face;
		 public ImageView iv_face1;
		 public TextView text_title;
		 public TextView text_title1;
		 public RelativeLayout rel;
		 public ImageView imagNew;
   }
}
