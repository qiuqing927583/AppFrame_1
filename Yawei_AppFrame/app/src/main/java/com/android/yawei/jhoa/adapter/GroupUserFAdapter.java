package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.User;
import com.yawei.jhoa.mobile.R;

import java.util.List;

/**
 * Created by Yusz on 2018-6-13.
 */

public class GroupUserFAdapter extends BaseAdapter{
    private Context context = null;
    private List<User> listData;

    public GroupUserFAdapter(Context context,List<User> listData) {

        this.context = context;
        this.listData = listData;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_groupuser, null);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.img = (ImageView)convertView.findViewById(R.id.img);
//            holder.chexkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
            holder.nameshort = (TextView)convertView.findViewById(R.id.userShort);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(listData.get(position).getDisplayName());
//        holder.chexkbox.setChecked(listData.get(position).getChecked()==1?true:false);
//        holder.chexkbox.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(listData.get(position).getChecked()==1){
//                    listData.get(position).setChecked(0);
//                    myApp.removeUser(listData.get(position));
//                }else{
//                    listData.get(position).setChecked(1);
//                    myApp.addUser(listData.get(position));
//                }
//            }
//        });
        if(position%5 ==0){
            holder.img.setBackgroundResource(R.drawable.usernamebg1);
        }else if(position%5 ==1){
            holder.img.setBackgroundResource(R.drawable.usernamebg2);
        }else if(position%5 ==2){
            holder.img.setBackgroundResource(R.drawable.usernamebg3);
        }else if(position%5 ==3){
            holder.img.setBackgroundResource(R.drawable.usernamebg4);
        }else if(position%5 ==4){
            holder.img.setBackgroundResource(R.drawable.usernamebg5);
        }
        String name = listData.get(position).getDisplayName();
        if(name.length()>2){
            holder.nameshort.setText(name.substring(name.length()-2, name.length()));
        }else{
            holder.nameshort.setText(name);
        }
        return convertView;
    }
    public class ViewHolder {
        public TextView name;
        public TextView nameshort;
        public ImageView img;
//        public CheckBox chexkbox;
    }
    public void UpData(List<User> listData){
        this.listData = listData;
    }
}
