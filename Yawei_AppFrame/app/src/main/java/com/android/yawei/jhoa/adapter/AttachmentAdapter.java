package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.yawei.jhoa.bean.Attachment;
import com.yawei.jhoa.mobile.R;

import java.util.List;
/**
 * TODO 签批页查看其他附件
 * class description: OpenAttachmentFragment
 * author: Yusz
 * Date: 2018-12-18
 */
public class AttachmentAdapter extends BaseAdapter{
	private List<Attachment> listData;
	private Context context;
	private Handler handler;
	private boolean showlook;
	
	public AttachmentAdapter(List<Attachment> listData, Context context, Handler handler,boolean showlook){
		this.listData = listData;
		this.context = context;
		this.handler = handler;
		this.showlook = showlook;
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
	public View getView(final int postion, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder=null;
        if(convertView == null) {
            viewHolder=new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.adapter_attachment, null);
            viewHolder.fileName = (TextView)convertView.findViewById(R.id.fileName);
            viewHolder.lookFile = (TextView)convertView.findViewById(R.id.lookfile);
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
		viewHolder.fileName.setText(postion+1+"、 "+listData.get(postion).getAttFileNameString());
        if(showlook){
			viewHolder.lookFile.setVisibility(View.VISIBLE);
		}else{
			viewHolder.lookFile.setVisibility(View.GONE);
		}
		viewHolder.lookFile.setText("[查看]");
        viewHolder.lookFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("fileurl", listData.get(postion).getAttUrl());
				bundle.putString("fileguid", listData.get(postion).getAttGuidString());
				Message msg = new Message();
				msg.what = 6;
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		});
       
        return convertView;
	}
	class ViewHolder {
		 public TextView fileName;
		 public TextView lookFile;
	}
	public void UpData(List<Attachment> listData){
		this.listData = listData;
	}
}
