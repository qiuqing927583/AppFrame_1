package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.User;
import com.yawei.jhoa.mobile.R;

import java.util.List;

/**
 * 写信页面选中的发送人
 * class description: SenderUserAdapter
 * author: Yusz
 * Date: 2018-7-24
 */
public class SenderUserAdapter extends BaseAdapter{
    List<User> data;
    Context context;
    private ViewHolder viewHolder;

    public SenderUserAdapter(List<User> data, Context context){
        this.data = data;
        this.context = context;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView  == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = View.inflate(context, R.layout.gridview_seluser_item, null);
            viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder  = (ViewHolder)convertView.getTag();
        }
        String displayname = data.get(position).getDisplayName();
        viewHolder.tv_name.setText(displayname);
//        linuser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    int position = Integer.parseInt(v.getTag().toString());
//                    userList.remove(position);
//                    application.RemoveSelectedUsers(position);
//                    gridviewAdapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        return convertView;
    }
    public class ViewHolder{
        public TextView tv_name;
    }
    public void UpData(List<User> data){
        this.data = data;
    }
}
