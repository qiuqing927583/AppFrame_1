package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.yawei.jhoa.voice.baidu.voiceutil.VoiceBean;
import com.yawei.jhoa.mobile.R;

import java.util.List;

/**
 * class description: VoiceSayAdapter
 * author: Yusz
 * Date: 2018-8-8
 */

public class VoiceSayAdapter extends BaseAdapter{
    private List<VoiceBean> listData;
    private Context context;
    private ViewHolder viewHolder = null;

    public VoiceSayAdapter(Context context,List<VoiceBean> listData){
        this.context = context;
        this.listData = listData;
    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        try {
            if(view  == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.adapter_voicesay, null);
                viewHolder.assistantContent = (TextView)view.findViewById(R.id.assistantContent);
                viewHolder.myContent = (TextView)view.findViewById(R.id.myContent);
                view.setTag(viewHolder);
            }else{
                viewHolder  = (ViewHolder)view.getTag();
            }
            if(listData.get(i).getType()==0){
                viewHolder.assistantContent.setText(listData.get(i).getSuccess());
                viewHolder.assistantContent.setVisibility(View.VISIBLE);
                viewHolder.myContent.setVisibility(View.GONE);
            }else{
                viewHolder.myContent.setText(listData.get(i).getSuccess());
                viewHolder.assistantContent.setVisibility(View.GONE);
                viewHolder.myContent.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    public class ViewHolder{
        public TextView assistantContent;
        public TextView myContent;
    }
    public void Updata(List<VoiceBean> data){
        this.listData = data;
    }
}
