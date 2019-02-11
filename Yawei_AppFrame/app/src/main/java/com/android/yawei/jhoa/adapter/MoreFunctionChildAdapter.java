package com.android.yawei.jhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.Interface.GridItemCilckCallback;
import com.android.yawei.jhoa.bean.AppOptionBean;
import com.android.yawei.jhoa.db.AppModuleDBHelper;
import com.android.yawei.jhoa.db.AppModuleDatabase;
import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.MyApplication;
import com.lidroid.xutils.BitmapUtils;
import com.yawei.jhoa.mobile.R;

import java.io.File;
import java.util.List;

/**
 * 新开发的更多子
 * Created by Yusz on 2018-1-16.
 */

public class MoreFunctionChildAdapter extends BaseAdapter {

    private List<AppOptionBean> listData;
    private Context context;
    private int redact;//是否编辑模式；0非编辑；1判断是否出现删除添加按钮
    private MyApplication application;
    private GridItemCilckCallback callback;

    public MoreFunctionChildAdapter( List<AppOptionBean> listData,Context context,GridItemCilckCallback callback){
        this.listData = listData;
        this.context = context;
        this.callback = callback;
        application = (MyApplication)context.getApplicationContext();
        redact = 0;
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
        ViewHolder viewHolder=null;
        if(view == null) {
            viewHolder=new ViewHolder();
            LayoutInflater inflater= LayoutInflater.from(context);
            view=inflater.inflate(R.layout.adapter_morefunctionchild, null);
            viewHolder.iv_face = (ImageView)view.findViewById(R.id.ItemImage);
            viewHolder.text_title = (TextView)view.findViewById(R.id.ItemText);
            viewHolder.textEmailCount = (TextView)view.findViewById(R.id.emailCount);
            viewHolder.imageAdd = (ImageView)view.findViewById(R.id.appAdd);
            viewHolder.imageDel = (ImageView)view.findViewById(R.id.appDel);
            view.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)view.getTag();
        }
        if(listData.get(i).getImageurl() != null && !listData.get(i).getImageurl().equals("")){
            File file = new File(FileUtils.GetSDPath()+"jhoaMobile/Bitmap");
            if(!file.exists())
                file.mkdirs();
            //初始化，上下文，使用磁盘缓存--缓存位置
            BitmapUtils bitmapUtils = new BitmapUtils(context,FileUtils.GetSDPath()+"jhoaMobile/Bitmap");
            bitmapUtils.configMemoryCacheEnabled(false);//是否使用内存缓存，如果很少的网络图片可以使用内存缓存，如果不确定或者是图片多请使用磁盘缓存
            bitmapUtils.configDiskCacheEnabled(true);//是否使用磁盘缓存，磁盘是高速缓存
            bitmapUtils.display(viewHolder.iv_face, listData.get(i).getImageurl());
        }else{
            viewHolder.iv_face.setBackgroundResource(listData.get(i).getImageId());
        }
        viewHolder.text_title.setText(listData.get(i).getModulename());
        if(listData.get(i).getEmailSize() != null&&!listData.get(i).getEmailSize().equals("")&&!listData.get(i).getEmailSize().equals("0")){
            viewHolder.textEmailCount.setVisibility(View.VISIBLE);
            if(Integer.valueOf(listData.get(i).getEmailSize()).intValue()>99){
                viewHolder.textEmailCount.setText("99+");
            }else{
                viewHolder.textEmailCount.setText(listData.get(i).getEmailSize());
            }
        }else{
            viewHolder.textEmailCount.setVisibility(View.GONE);
        }
        if(redact == 1){
            try {
                AppModuleDatabase db = application.GetModuleSQLHepler();
                if(db.GetAppByTableIsReadly(AppModuleDBHelper.USER_MOUDLE_TABLE,listData.get(i).getModulename())){
                    viewHolder.imageDel.setVisibility(View.VISIBLE);
                    viewHolder.imageAdd.setVisibility(View.GONE);
                }else{
                    viewHolder.imageDel.setVisibility(View.GONE);
                    viewHolder.imageAdd.setVisibility(View.VISIBLE);
                }
                if(listData.get(i).getModulename().equals("更多")){
                    viewHolder.imageDel.setVisibility(View.GONE);
                    viewHolder.imageAdd.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            viewHolder.imageDel.setVisibility(View.GONE);
            viewHolder.imageAdd.setVisibility(View.GONE);
        }
        viewHolder.imageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AppModuleDatabase db = application.GetModuleSQLHepler();
                    db.DelAppMoudleToTable(AppModuleDBHelper.USER_MOUDLE_TABLE,listData.get(i).getModulename());
                    callback.MoudleAddorDeleteRefresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        viewHolder.imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AppModuleDatabase db = application.GetModuleSQLHepler();
                    List<AppOptionBean> array= db.GetAppByTable();
                    if(array.size()<8){
                        db.InsertDataToUserappTable(listData.get(i));
                        callback.MoudleAddorDeleteRefresh();
                    }else{
                        Toast.makeText(context,"我的应用超出最大添加限度",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
    class ViewHolder {
        public ImageView iv_face;
        public TextView text_title;
        public TextView textEmailCount;
        private ImageView imageAdd;
        private ImageView imageDel;
    }
    public void Updata(List<AppOptionBean> listData){
        this.listData = listData;
    }
    public void SetRedact(int redact){
        this.redact = redact;
    }

}
