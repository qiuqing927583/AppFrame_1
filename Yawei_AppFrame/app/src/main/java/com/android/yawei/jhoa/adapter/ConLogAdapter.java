package com.android.yawei.jhoa.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.Interface.GridItemCilckCallback;
import com.android.yawei.jhoa.bean.AppOptionBean;
import com.android.yawei.jhoa.bean.ConLogBean;
import com.android.yawei.jhoa.bean.MoreGroupBean;
import com.android.yawei.jhoa.ui.MyGridView;
import com.android.yawei.jhoa.ui.MyListView;
import com.yawei.jhoa.mobile.R;

import java.util.List;

/**
 * 登录日志适配器
 * Created by Qiuq on 2019-1-29.
 */

public class ConLogAdapter extends BaseAdapter {

    private Context context;
    private List<ConLogBean> articles;
    private Activity activity;
    private MyListView listview;
    private HolderView holderView = null;
    protected Handler requesetHandler;
    private Intent intent;


    public ConLogAdapter(Context context, MyListView mlistview,
                                        Activity mactivity, List<ConLogBean> articles, Handler requestHandler) {
        this.context = context;
        this.activity = mactivity;
        this.listview = mlistview;
        this.articles = articles;
        this.requesetHandler=requestHandler;
    }
    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            //在listView内初始化
            holderView = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.conlog_iteam_activity, null);
            holderView.info1 = (TextView)convertView.findViewById(R.id.info1);
            holderView.info2 = (TextView)convertView.findViewById(R.id.info2);
            holderView.info3 = (TextView)convertView.findViewById(R.id.info3);
            convertView.setTag(holderView);
        }else{
            //使用这句内存消耗小
            if(convertView.getTag() instanceof HolderView){
                holderView=(HolderView) convertView.getTag();
            }
        }
        //在listView里赋值

        //昵称
        holderView.info1.setText(articles.get(position).getMobileBrand());
        //手机型号
        holderView.info2.setText(articles.get(position).getMobileType());
        //登录时间
        holderView.info3.setText(articles.get(position).getLoginTime());
        return convertView;
    }
    public class HolderView{
        private TextView info1,info2,info3;
    }
}
