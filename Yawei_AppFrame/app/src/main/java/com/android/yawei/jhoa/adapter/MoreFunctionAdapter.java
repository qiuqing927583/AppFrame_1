package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yawei.jhoa.Interface.GridItemCilckCallback;
import com.android.yawei.jhoa.bean.AppOptionBean;
import com.android.yawei.jhoa.bean.MoreGroupBean;
import com.android.yawei.jhoa.ui.MyGridView;
import com.yawei.jhoa.mobile.R;

import java.util.List;

/**
 * 新的更多适配器
 * Created by Yusz on 2018-1-16.
 */

public class MoreFunctionAdapter extends BaseAdapter {

    private List<MoreGroupBean> listDta;
    private Context context;
    private GridItemCilckCallback callback;
    private int redact;//是否编辑模式；0非编辑；1编辑出现删除按钮；2编辑出现添加按钮
    public MoreFunctionAdapter(List<MoreGroupBean> listDta, Context context, GridItemCilckCallback callback){
        this.listDta = listDta;
        this.context = context;
        this.callback = callback;
        redact = 0;
    }

    @Override
    public int getCount() {
        return listDta.size();
    }

    @Override
    public Object getItem(int i) {
        return listDta.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view == null) {
            viewHolder=new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(context);
            view=inflater.inflate(R.layout.adapter_morefunction, null);
            viewHolder.gridView = (MyGridView)view.findViewById(R.id.gridview);
            viewHolder.txt_Title = (TextView)view.findViewById(R.id.gridTitle);
            viewHolder.img_divider = (ImageView)view.findViewById(R.id.divider);
            view.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.txt_Title.setText(listDta.get(i).getGroupName());
        final MoreFunctionChildAdapter adapter = new MoreFunctionChildAdapter(listDta.get(i).getListOption(),context,callback);
        adapter.SetRedact(redact);
        viewHolder.gridView.setAdapter(adapter);
        viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    callback.OnClickGridItem((AppOptionBean)adapter.getItem(i));
            }
        });
//        if(i != listDta.size()-1){
//            viewHolder.img_divider.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.img_divider.setVisibility(View.GONE);
//        }
        return view;
    }
    class ViewHolder {
        public MyGridView gridView;
        public TextView txt_Title;
        public ImageView img_divider;
    }
    public void UpData(List<MoreGroupBean> listDta){
        this.listDta = listDta;
    }
    public void SetRedact(int redact){
        this.redact = redact;
    }
}
