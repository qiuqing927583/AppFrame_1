package com.android.yawei.jhoa.mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.yawei.jhoa.USBHelper.UsbFileFolderListActivity;
import com.android.yawei.jhoa.USBHelper.UsbHelper;
import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.adapter.SdFileAdapter;
import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.SDListBean;
import com.android.yawei.jhoa.ui.MyListView;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SDCardInfo;
import com.android.yawei.jhoa.utils.SDCardUtil;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;
import com.yawei.jhoa.mobile.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cwj.androidfilemanage.activity.SDCardActivity;
import cwj.androidfilemanage.base.UseSaveSelectFile;
import cwj.androidfilemanage.bean.FileInfo;
import cwj.androidfilemanage.utils.FileResultCode;

//import com.android.yawei.jhoa.USBHelper.UsbHelper;
//import com.github.mjdev.libaums.UsbMassStorageDevice;

/***********
 * TODO 附件选择sdcard和手机设备存储列表界面
 * @author Yusz
 * 创建时间：2015-6-25
 *
 ***********/
public class SDFileListActivity extends BaseActivity implements OnClickListener{

	private LinearLayout linBack;//返回
	private MyListView listFile;//目录清单
	private List<SDListBean> listData;//数据
	private SdFileAdapter adapter;//适配器
	private ScrollView scollview;//布局滚动，出现scrollview一直滚动到最底部
	private MyApplication application;
	private LinearLayout lindoc;//doc
	private LinearLayout linppt;//ppt
	private LinearLayout linxls;//xls
	private LinearLayout linpdf;//pdf
	private LinearLayout lintxt;//txt
	private LinearLayout linPicAndPhoto;//选择图片和拍照
	private UsbHelper usbHelper;
	static final int REQUEST_CHOOSER = 1234;//附件浏览回调回来code
	public static SDFileListActivity activity;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sdfilelist);
		try {
			listData = new ArrayList<SDListBean>();
			application = (MyApplication) getApplication();
			SysExitUtil.AddActivity(SDFileListActivity.this);
			activity = SDFileListActivity.this;
			
			InitView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void InitView()throws Exception{
		
		linBack = (LinearLayout)findViewById(R.id.LinTopBack);
		linBack.setOnClickListener(this);
		listFile = (MyListView)findViewById(R.id.filelist);
		scollview = (ScrollView)findViewById(R.id.scollview);
		lindoc = (LinearLayout)findViewById(R.id.lindoc);
		lindoc.setOnClickListener(this);
		linppt = (LinearLayout)findViewById(R.id.linppt);
		linppt.setOnClickListener(this);
		linxls = (LinearLayout)findViewById(R.id.linxls);
		linxls.setOnClickListener(this);
		linpdf = (LinearLayout)findViewById(R.id.linpdf);
		linpdf.setOnClickListener(this);
		lintxt = (LinearLayout)findViewById(R.id.lintxt);
		lintxt.setOnClickListener(this);
		linPicAndPhoto = (LinearLayout)findViewById(R.id.linPicPhoto);
		linPicAndPhoto.setOnClickListener(this);
		
		SDListBean bean1 = new SDListBean();
		//金宏办公自己目录
        String EXTERNAL_BASE_PATH = com.android.yawei.jhoa.utils.FileUtils.GetSystemFilePath()+"temp";
		bean1.setFileName("我的文档");
		bean1.setFilePath(EXTERNAL_BASE_PATH);
		bean1.setDrawableId(R.drawable.ic_selfile_folder);
		listData.add(bean1);
		
		List<SDCardInfo> sdCardInfos = SDCardUtil.getSDCardInfo(getApplicationContext());
        for(int i=0;i<sdCardInfos.size();i++){
        	SDCardInfo info = sdCardInfos.get(i);
        	if(info.isMounted()){
        		if(i==0){
	        		SDListBean bean2 = new SDListBean();
	        		bean2.setFileName("存储卡"+"  (内置)");
	        		bean2.setFilePath(info.getMountPoint());
	        		bean2.setDrawableId(R.drawable.ic_sd_storage);
	        		listData.add(bean2);
        		}else{
        			SDListBean bean2 = new SDListBean();
	        		bean2.setFileName("存储卡"+"  (SD)");
	        		bean2.setFilePath(info.getMountPoint());
	        		bean2.setDrawableId(R.drawable.ic_sd_storage);
	        		listData.add(bean2);
        		}
        	}
        }
		
        SDListBean bean2 = new SDListBean();
		bean2.setFileName("设备");
		bean2.setFilePath("/");
		bean2.setDrawableId(R.drawable.ic_phone);
		listData.add(bean2);
		adapter = new SdFileAdapter(listData, SDFileListActivity.this);
		listFile.setAdapter(adapter);
		listFile.setOnItemClickListener(onItemClick);
		Handler handler = new Handler();  
		handler.postDelayed(runnable, 200);
		updateUsbFile(0);
	}
	/**
	 * 更新 USB 文件列表
	 */
	private void updateUsbFile(int position)throws Exception {
		usbHelper = new UsbHelper(getApplicationContext(), null);
		UsbMassStorageDevice[] usbMassStorageDevices = usbHelper.getDeviceList();
		if (UsbHelper.GetMobileUSB(getApplicationContext())) {
			//存在USB
			usbHelper.readDevice(usbMassStorageDevices[position]);
			SDListBean bean2 = new SDListBean();
			bean2.setFileName("USB");
			bean2.setDrawableId(R.drawable.ic_usb);
			listData.add(bean2);
			adapter.UpData(listData);
			adapter.notifyDataSetChanged();
		}
	}
	private Runnable runnable = new Runnable() {
	    @Override  
	    public void run() {  
	        scollview.scrollTo(0, 0);// 改变滚动条的位置  
	    }  
	};
	
	private OnItemClickListener onItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
			
			try {
				SDListBean bean = (SDListBean) adapter.getItem(postion);
				if(bean.getFilePath() != null && !bean.getFilePath().equals("")){
//					Intent intent = new Intent(SDFileListActivity.this,FileChooserActivity.class);
//					intent.putExtra("path", bean.getFilePath());//把定位的路径传出去
//					intent.putExtra("rootname", bean.getFileName());
//					startActivityForResult(intent, REQUEST_CHOOSER);
					UseSaveSelectFile.newInstance().InitFileList();
					Intent intent = new Intent(SDFileListActivity.this, SDCardActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("path",bean.getFilePath());
					bundle.putString("name", "SD卡");
					intent.putExtras(bundle);
					startActivityForResult(intent,1);
				}else if(bean.getFileName().equals("USB")){
					Intent intent = new Intent(SDFileListActivity.this,UsbFileFolderListActivity.class);
					startActivityForResult(intent, REQUEST_CHOOSER);
				}else{
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("*/file");
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					try {
						startActivityForResult(Intent.createChooser(intent, "请选择文件!"), REQUEST_CHOOSER);
					} catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(SDFileListActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	};
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.LinTopBack:
			finish();
			break;
		case R.id.lindoc:
			intent = new Intent(SDFileListActivity.this, SreachSDFileActivity.class);
			intent.putExtra("suffix", ".doc");
			startActivity(intent);
			break;
		case R.id.linppt:
			intent = new Intent(SDFileListActivity.this, SreachSDFileActivity.class);
			intent.putExtra("suffix", ".ppt");
			startActivity(intent);
			break;
		case R.id.linxls:
			intent = new Intent(SDFileListActivity.this, SreachSDFileActivity.class);
			intent.putExtra("suffix", ".xls");
			startActivity(intent);
			break;
		case R.id.linpdf:
			intent = new Intent(SDFileListActivity.this, SreachSDFileActivity.class);
			intent.putExtra("suffix", ".pdf");
			startActivity(intent);
			break;
		case R.id.lintxt:
			intent = new Intent(SDFileListActivity.this, SreachSDFileActivity.class);
			intent.putExtra("suffix", ".txt");
			startActivity(intent);
			break;
		case R.id.linPicPhoto:
			AndroidImagePicker.getInstance().pickMulti(SDFileListActivity.this, true, new AndroidImagePicker.OnImagePickCompleteListener() {
				@Override
				public void onImagePickComplete(List<ImageItem> items) {
					if(items != null && items.size() > 0){
						try {
							for(ImageItem item:items){
								Attachment attachment = new Attachment();
								attachment.setAttUrl(item.path);
								attachment.setAttFileNameString(item.name);
								attachment.setFileSize(String.valueOf(new File(item.path).length()));
								//长度和类型暂时不存
								application.addAttachment(attachment);
							}
							finish();
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}
			});
			break;
		default:
			break;
		}
	}
	//附件选择成功之后回调
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if(requestCode==REQUEST_CHOOSER){
				if (resultCode == RESULT_OK) {
					if (data != null) {
						// uri中存储获取选中文件信息
						final Uri uri = data.getData();
						try {
							// 获取被选择文件的路径
							final String path = FileUtils.getPath(getApplicationContext(), uri);
							if(path != null && !path.equals("")){
								//把选择的附件缓存到application中
								Attachment attachment = new Attachment();
								String fileName = path.substring(path.lastIndexOf("/")+1);
								attachment.setAttUrl(path);
								attachment.setAttFileNameString(fileName);
								attachment.setFileSize(String.valueOf(new File(path).length()));
								//长度和类型暂时不存
								application.addAttachment(attachment);
								finish();
							}
						} catch (Exception e) {
							Toast.makeText(SDFileListActivity.this, "添加附件失败!", Toast.LENGTH_LONG).show();
						}
					}
				}
			}
			switch (resultCode){
				case FileResultCode.SURESELECTFILE:
					List<FileInfo> listObj =  (List<FileInfo>) data.getSerializableExtra("data");
					for(FileInfo info:listObj){
						//把选择的附件缓存到application中
						Attachment attachment = new Attachment();
						attachment.setAttUrl(info.getFilePath());
						attachment.setAttFileNameString(info.getFileName());
						attachment.setFileSize(String.valueOf(new File(info.getFilePath()).length()));
						//长度和类型暂时不存
						application.addAttachment(attachment);
					}
					finish();
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        super.onActivityResult(requestCode, resultCode, data);
    }
	public void onDestroy(){
		super.onDestroy();
		SysExitUtil.RemoveActivity(SDFileListActivity.this);
	}
}
