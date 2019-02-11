package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.yawei.jhoa.bean.GroupBean;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusz on 2018-6-13.
 */

public class BookUserGroupAdapter extends BaseAdapter{

    private Context context = null;
    private List<GroupBean> dataSet = new ArrayList<GroupBean>();

    public BookUserGroupAdapter(Context context, List<GroupBean> dataSet) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.dataSet = dataSet;
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
        ViewHolder viewHolder=null;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView == null) {
            viewHolder=new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_bookusergroup, null);
            viewHolder.txtGroupName = (TextView)convertView.findViewById(R.id.groupName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.txtGroupName.setText(dataSet.get(position).getName());
        return convertView;
    }
    class ViewHolder {
        public TextView txtGroupName;
    }

    public void UpData(List<GroupBean> data) {
        this.dataSet = data;
    }

}
