package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.addressbook.DelectSelUserCall;
import com.android.yawei.jhoa.bean.User;
import com.yawei.jhoa.mobile.R;

import java.util.List;

/**
 * Created by Yusz on 2018-7-6.
 */

public class LookoverSelectUserAdapter extends BaseAdapter{

    private Context context = null;
    private List<User> listData;
    private DelectSelUserCall mcallback;

    public LookoverSelectUserAdapter(Context context, List<User> listData, DelectSelUserCall callback) {

        this.context = context;
        this.listData = listData;
        this.mcallback = callback;
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
            convertView = View.inflate(context, R.layout.adapter_lookoverselectuser, null);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.nameshort = (TextView)convertView.findViewById(R.id.userShort);
            holder.txtDelect = (TextView) convertView.findViewById(R.id.txtDelect);
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
        holder.txtDelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.DelectSelUser(position);
            }
        });
        return convertView;
    }
    public class ViewHolder {
        public TextView name;
        public TextView nameshort;
        public ImageView img;
        public TextView txtDelect;
    }
    public void UpData(List<User> listData){
        this.listData = listData;
    }
}
