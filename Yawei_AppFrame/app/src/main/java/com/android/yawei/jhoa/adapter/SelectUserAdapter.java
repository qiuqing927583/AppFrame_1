package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.utils.MyApplication;
import com.yawei.jhoa.mobile.R;

import java.util.List;

/**
 *
 * 通讯录选中的人
 * Created by Yusz on 2018-7-5.
 */

public class SelectUserAdapter extends BaseAdapter{
    private Context context = null;
    private List<User> listData;

    public SelectUserAdapter(Context context,List<User> listData) {

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
            convertView = View.inflate(context, R.layout.adapter_selectuser, null);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.nameshort = (TextView)convertView.findViewById(R.id.userShort);
            holder.imageSel = (ImageView)convertView.findViewById(R.id.imageSel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(listData.get(position).getDisplayName());

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
        try {
            MyApplication application = (MyApplication) context.getApplicationContext();
            if(application.isContainsUser(listData.get(position))){
                listData.get(position).setChecked(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(listData.get(position).getChecked()==1){
            holder.imageSel.setImageResource(R.drawable.add);
        }else{
            holder.imageSel.setImageResource(R.drawable.del);
        }
        return convertView;
    }
    public class ViewHolder {
        public TextView name;
        public TextView nameshort;
        public ImageView img;
        public ImageView imageSel;
    }
    public void UpData(List<User> listData){
        this.listData = listData;
    }
}
