package com.android.yawei.jhoa.USBHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mjdev.libaums.fs.UsbFile;
import com.yawei.jhoa.mobile.R;

import java.io.File;
import java.util.List;

/**
 * Created by Yusz on 2018-7-2.
 */

public class UsbFileAdapter extends BaseAdapter{
    private List<UsbFile> list;

    private Context context;

    public UsbFileAdapter(Context context, List<UsbFile> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_usbfile, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UsbFile t = list.get(position);
        if (t instanceof UsbFile) {
            UsbFile usbFile = (UsbFile) t;
            //设置文件ICON
            holder.icon.setImageResource(getResIdFromFileName(usbFile.isDirectory(), usbFile.getName()));
            holder.tv.setText(usbFile.getName());
        } else if (t instanceof File) {
            File file = (File) t;
            //设置文件ICON
            holder.icon.setImageResource(getResIdFromFileName(file.isDirectory(),file.getName()));
            holder.tv.setText(file.getName());
        }
        return convertView;
    }

    /**
     * 通过文件名（路径）返回对应的 ICON 资源 ID
     *
     * @param isFolder 是否是文件夹
     * @param fileName 文件名（路径）
     * @return 对应的资源ID
     */
    private static int getResIdFromFileName(boolean isFolder, String fileName) {
        fileName = fileName.toLowerCase();
        if (isFolder) {
            //文件夹
            return R.drawable.ic_folder;
        } else {
            if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                //图片文件
                return R.drawable.ic_image;
            } else if (fileName.endsWith(".txt")) {
                //TXT 文件
                return R.drawable.ic_file;
            } else if (fileName.endsWith(".pdf")) {
                //PDF 文件
                return R.drawable.ic_pdf;
            } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                return R.drawable.ic_xls;
            } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
                return R.drawable.ic_ppt;
            } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
                return R.drawable.ic_doc;
            } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi")) {
                //视频文件
                return R.drawable.ic_video;
            } else {
                //未知文件
                return R.drawable.ic_unkown_file;
            }
        }
    }

    private class ViewHolder {

        private TextView tv;
        private ImageView icon;

        ViewHolder(View view) {
            tv = (TextView) view.findViewById(R.id.file_name_tv);
            icon = (ImageView) view.findViewById(R.id.file_icon_iv);
        }
    }
    public void UpData(List<UsbFile> list){
        this.list = list;
    }
}
