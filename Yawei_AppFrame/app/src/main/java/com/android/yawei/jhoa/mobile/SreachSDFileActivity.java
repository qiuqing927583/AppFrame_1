package com.android.yawei.jhoa.mobile;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.adapter.SreachSDFileAdapter;
import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.SreachSDFileBean;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SysExitUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.yawei.jhoa.mobile.R;

/*****************
 * TODO 附件，点击上面搜索手机对应格式的文件
 * @author Yusz
 * 创建时间：2015-10-9
 *****************/
//public class SreachSDFileActivity<Progress, Params> extends BaseActivity implements OnClickListener{
public class SreachSDFileActivity extends BaseActivity implements OnClickListener{
	private LinearLayout linBack;//返回
	private ListView listview;//
	private SreachSDFileAdapter adapter;
	private List<SreachSDFileBean> fileBean;//搜索出来的文件
	private String fileSuffix;//文件后缀
	private CustomProgressDialog progressDialog;

	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sreachsdfile);
		
		progressDialog = CustomProgressDialog.createDialog(SreachSDFileActivity.this);
		progressDialog.setMessage("正在搜索中,请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		SysExitUtil.AddActivity(SreachSDFileActivity.this);
		
		fileBean = new ArrayList<SreachSDFileBean>();
		fileSuffix = getIntent().getStringExtra("suffix");
		
		InitView();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String[] columns = new String[] {
							FileColumns.DATA,
							FileColumns.TITLE};
									
					Uri uri = MediaStore.Files.getContentUri("external");
					Cursor cursor;
					if(fileSuffix.equals(".pdf") || fileSuffix.equals(".txt")){
						cursor= getContentResolver().query(uri,columns, buildDocSelection(), null, null);
					}else{ 
						cursor = getContentResolver().query(uri,columns, null, null, FileColumns.TITLE + " asc");
					}
					
			        cursor.moveToFirst();  
			        while (!cursor.isAfterLast()) {  
			            String data = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
			            if(data.contains(fileSuffix)){
				            SreachSDFileBean bean = new SreachSDFileBean();
						   	bean.setBookName(new File(data).getName());// 加入名称
						   	bean.setPath(new File(data).getPath());// 加入路径   
						   	bean.setSize(new File(data).length());// 加入文件大小
						   	fileBean.add(bean);
			            }
			            cursor.moveToNext();  
			        }
			        cursor.close();
			        if(fileBean.size()>0){
						handler.sendEmptyMessage(0);
					}else{
						handler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
			
	}
	
	@SuppressWarnings("serial")
	private String buildDocSelection()throws Exception{
	    StringBuilder selection = new StringBuilder();
	    HashSet<String> sDocMimeTypesSet = null;
	    if(fileSuffix.equals(".txt")){
	    	sDocMimeTypesSet = new HashSet<String>() {
	    	    {
	    	        add("text/plain");
	    	    }
	    	};
	    }else if(fileSuffix.equals(".doc")){
	    	sDocMimeTypesSet = new HashSet<String>() {
	    	    {
	    	        add("application/msword");
	    	        add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
	    	    }
	    	};
	    }else if(fileSuffix.equals(".pdf")){
	    	sDocMimeTypesSet = new HashSet<String>() {
	    	    {
	    	        add("application/pdf");
	    	    }
	    	};
	    }else if(fileSuffix.equals(".xls")){
	    	sDocMimeTypesSet = new HashSet<String>() {
	    	    {
	    	        add("application/vnd.ms-excel");
	    	        add("application/x-excel");
	    	        add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    	    }
	    	};
	    	
	    }else if(fileSuffix.equals(".ppt")){
	    	sDocMimeTypesSet = new HashSet<String>() {
	    	    {
	    	    	add("application/mspowerpoint");
	    	    	add("application/vnd.openxmlformatsofficedocument.presentationml.presentation");	    	    	
	    	    }
	    	};
	    	
	    }
	    Iterator<String> iter = sDocMimeTypesSet.iterator();
	    while(iter.hasNext()) {
	        selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
	    }
	    return  selection.substring(0, selection.lastIndexOf(")") + 1);
	}
	private void InitView(){
		
		linBack = (LinearLayout)findViewById(R.id.LinTopBack);
		linBack.setOnClickListener(this);
		listview = (ListView)findViewById(R.id.listview);
		listview.setOnItemClickListener(onItemClickListener);
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int postion,long arg3) {
			
			try {
				SreachSDFileBean bean = (SreachSDFileBean) adapter.getItem(postion);
				MyApplication application = (MyApplication) getApplication();
				//把选择的附件缓存到application中
				Attachment attachment = new Attachment();

				attachment.setAttUrl(bean.getPath());
				attachment.setAttFileNameString(bean.getBookName());
				//长度和类型暂时不存

				application.addAttachment(attachment);
				SDFileListActivity.activity.finish();
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
		
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//TODO 消息handler
			try {
				switch (msg.what) {
				case 0:
					//搜索完成之后刷新列表
					try {
						if(progressDialog != null && progressDialog.isShowing())
							progressDialog.dismiss();
						if(adapter ==  null){
							adapter = new SreachSDFileAdapter(SreachSDFileActivity.this, fileBean);
							listview.setAdapter(adapter);
						}else{
							adapter.UpData(fileBean);
							adapter.notifyDataSetChanged();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 1:
					Toast.makeText(SreachSDFileActivity.this,"查找发生错误", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					Toast.makeText(SreachSDFileActivity.this,"未搜索到相应文件", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.LinTopBack:
			finish();
			break;

		default:
			break;
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
		SysExitUtil.RemoveActivity(SreachSDFileActivity.this);
	}
	
	
//	public List<SreachSDFileBean> SearchFiles(File filePath){
//		File [] files =filePath.listFiles();
//		String tempString = fileSuffix;
//		if (files != null){
//			for(int i =0;i<files.length;i++){
//				if(files[i].isDirectory()&& files[i].getPath().indexOf("/.") == -1){//忽略“.”隐藏文件
//					 if(files[i].canRead()){
//						 SearchFiles(files[i]);
//					 }
//				}else{
//					if(files[i].getName().toLowerCase().contains(tempString)){
//						SreachSDFileBean bean = new SreachSDFileBean();
//					   	bean.setBookName(files[i].getName());// 加入名称
//					   	bean.setPath(files[i].getPath());// 加入路径   
//					   	bean.setSize(files[i].length());// 加入文件大小
//					   	fileBean.add(bean);
//					}
//				}
//			}
//		}
//		return fileBean;
//	}
	
}
