package com.android.yawei.jhoa.USBHelper;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.utils.FileUtils;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.UsbFile;
import com.yawei.jhoa.mobile.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Yusz on 2018-7-2.
 */

public class UsbFileFolderListActivity extends BaseActivity implements USBBroadCastReceiver.UsbListener{

    private LinearLayout linBack;
    private ListView usbListview;
    private UsbHelper usbHelper;
    private ArrayList<UsbFile> usbList;
    private UsbFileAdapter usbAdapter;
    private TextView showProgressTv;

    protected void onCreate(Bundle bundle){

        super.onCreate(bundle);
        setContentView(R.layout.activity_usbfilefolderlist);
        try {
            InitView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void InitView()throws Exception{
        linBack = (LinearLayout)findViewById(R.id.LinTopBack);
        linBack.setOnClickListener(onClickListener);
        showProgressTv = (TextView)findViewById(R.id.showProgressTv);
        usbListview = (ListView)findViewById(R.id.usbListview);
        usbHelper = new UsbHelper(this, this);
        usbList = new ArrayList<>();
        usbAdapter = new UsbFileAdapter(this, usbList);
        usbListview.setAdapter(usbAdapter);
        usbListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UsbFile file = usbList.get(position);
                openUsbFile(file);
            }
        });
        updateUsbFile(0);
    }
    /**
     * 更新 USB 文件列表
     */
    private void updateUsbFile(int position) {
        UsbMassStorageDevice[] usbMassStorageDevices = usbHelper.getDeviceList();
        if (usbMassStorageDevices.length > 0) {
            //存在USB
            usbList.clear();
            usbList.addAll(usbHelper.readDevice(usbMassStorageDevices[position]));
            usbAdapter.UpData(usbList);
            usbAdapter.notifyDataSetChanged();
        } else {
            usbList.clear();
            usbAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void insertUsb(UsbDevice device_add) {
        if (usbList.size() == 0) {
            updateUsbFile(0);
        }
    }

    @Override
    public void removeUsb(UsbDevice device_remove) {

    }

    @Override
    public void getReadUsbPermission(UsbDevice usbDevice) {

    }

    @Override
    public void failedReadUsb(UsbDevice usbDevice) {

    }
    /**
     * 打开 USB File
     *
     * @param file USB File
     */
    private void openUsbFile(UsbFile file) {
        if (file.isDirectory()) {
            //文件夹更新列表
            usbList.clear();
            usbList.addAll(usbHelper.getUsbFolderFileList(file));
            usbAdapter.UpData(usbList);
            usbAdapter.notifyDataSetChanged();
        } else {
            //开启线程，将文件复制到本地
            copyUSbFile(file);
        }
    }
    /**
     * 复制 USB 文件到本地
     *
     * @param file USB文件
     */
    private void copyUSbFile(final UsbFile file) {
        //复制到本地的文件路径
        final String filePath = FileUtils.GetSystemFilePath() + "temp/" + file.getName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //复制结果
                final boolean result = usbHelper.saveUSbFileToLocal(file, filePath, new UsbHelper.DownloadProgressListener() {
                    @Override
                    public void downloadProgress(final int progress) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //选择完附件回传
                                    String text = "进度 : " + progress+"%";
                                    showProgressTv.setText(text);
                                    if(progress>=100){
                                        Uri uri = Uri.fromFile(new File(filePath));
                                        setResult(RESULT_OK, new Intent().setData(uri));
                                        finish();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                //主线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!result) {
                            Toast.makeText(UsbFileFolderListActivity.this, "复制失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()){
                    case R.id.LinTopBack:
                        if (usbHelper.getParentFolder() != null) {
                            //不是根目录，返回上一目录
                            openUsbFile(usbHelper.getParentFolder());
                        }else {
                            finish();
                        }
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            if(usbHelper.getParentFolder() != null){
                openUsbFile(usbHelper.getParentFolder());
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
