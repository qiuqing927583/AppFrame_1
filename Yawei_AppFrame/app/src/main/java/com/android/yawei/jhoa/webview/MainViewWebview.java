package com.android.yawei.jhoa.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.yawei.cordova.webview.CordovaWebview;
import com.android.yawei.jhoa.Interface.UploadBigFileCallback;
import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.addressbook.GroupFragmentActivity;
import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.FileTobase;
import com.android.yawei.jhoa.bean.RootUserBean;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.mobile.SDFileListActivity;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.ui.MyWebChromeClient;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.hxyl.Elink;
import com.yawei.jhoa.mobile.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusz on 2018-2-22.
 */

public class MainViewWebview extends BaseActivity implements UploadBigFileCallback {

    private WebView webview;
    private String url;
    private MyApplication application;
    private long exitTime;
    private CustomProgressDialog progressDialog;
    private boolean isSelFile = false;//是否进入选择附件页
    private boolean isOpenPdfWirte = false;//  是否进入手写页
    private String fileBaseJson;
    private String sign;

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_mainviewwebview);

        try {
            url = getIntent().getStringExtra("loadurl");
            application = (MyApplication) getApplication();
            sign = getIntent().getStringExtra("sign");
            //InitView();
            webview = (WebView)findViewById(R.id.mainview_webview);
            WebSettings webSettings = webview.getSettings();
            // 设置缓存
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //设置WebView属性，能够执行Javascript脚本
            webSettings.setJavaScriptEnabled(true);
            //设置适应Html5
            webSettings.setDomStorageEnabled(true);
            //设置可以访问文件
            webSettings.setAllowFileAccess(true);
            //设置支持缩放
            webSettings.setBuiltInZoomControls(true);
            //加载需要显示的网页
            // 加载 asset目录下的本地html文件： mUrl = "file:///android_asset/web_app.html"
            webview.addJavascriptInterface(new MainViewWebview.JavaScriptObj(), "stub");
            webview.loadUrl(url);
            //设置WebViewClient用来辅助WebView处理各种通知请求事件等，如更新历史记录、网页开始加载/完毕、报告错误信息等
            webview.setWebViewClient(new WebViewClient() {

                // 以下方法避免 自动打开系统自带的浏览器，而是让新打开的网页在当前的WebView中显示
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView()throws Exception{
        webview = (WebView)findViewById(R.id.mainview_webview);
        if(TextUtils.isEmpty(sign)||"0".equals(sign)){
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                    webview.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon){
                    super.onPageStarted(view, url, favicon);
                }
            });
        }
        webview.setWebChromeClient(new MyWebChromeClient());//处理解析，渲染网页等浏览器做的事情，自己封装
        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setDisplayZoomControls(true);
        //解决缓存问题 (不使用缓存，只从网络获取数据.)
        webview.getSettings().setCacheMode(webview.getSettings().LOAD_NO_CACHE);
        //设置总动适应屏幕宽度
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setBuiltInZoomControls(true);//设置是否出现缩放工具
        webview.setSaveEnabled(false);//回收webview的时候不保存状态,如果设置为true时回收的时候会保存状态，如果再次启动的时候会带入所保存的状态，当调用js桥中的方法时，会查找原来的js桥对象
        webview.addJavascriptInterface(new MainViewWebview.JavaScriptObj(), "stub");
        //加载需要显示的网页
        webview.loadUrl(url);
    }

    class JavaScriptObj{
        //获取用户信息
        @JavascriptInterface
        public String GetUserMessage(){
            String oaurl = Constants.WEB_SERVICE_URL.substring(0,Constants.WEB_SERVICE_URL.lastIndexOf("/"));
            oaurl = oaurl.substring(0,oaurl.lastIndexOf("/")+1);
            RootUserBean bean = new RootUserBean();
            bean.setAdGuid(SpUtils.getString(MainViewWebview.this, Constants.CUTOVER_AD_GUID,""));
            bean.setDepart(SpUtils.getString(MainViewWebview.this, Constants.CUTOVER_AD_DEPART,""));
            bean.setDisplayName(SpUtils.getString(MainViewWebview.this, Constants.CUTOVER_DISPLAY_NAME,""));
            bean.setExchangeID(SpUtils.getString(MainViewWebview.this, Constants.CUTOVER_EXCHANGE_ID,""));
            bean.setSysflag(SpUtils.getString(MainViewWebview.this, Constants.CUTOVER_AD_SYSFLAG,""));
            bean.setUserName(SpUtils.getString(MainViewWebview.this, Constants.CUTOVER_AD_LOGNAME,""));
            bean.setOrginuserName(SpUtils.getString(MainViewWebview.this, Constants.AD_LOGNAME,""));
            bean.setOrgindisplayName(SpUtils.getString(MainViewWebview.this, Constants.AD_LOGNAME,""));
            bean.setOrginadGuid(SpUtils.getString(MainViewWebview.this, Constants.AD_GUID,""));
            bean.setOaUrl(oaurl);
            bean.setWebserviceurl(Constants.WEB_SERVICE_URL);
            bean.setDeveInfo(JSON.toJSONString(application.getDeveInfo()));
            String usermsg = JSONObject.toJSONString(bean);
            return usermsg;
        }
        //返回关闭当前的activity
        @JavascriptInterface
        public void GoBack() {
            finish();
        }
        //退出
        @JavascriptInterface
        public void ExitApp(){
            ExitProcess();
        }
        //选附件
        @JavascriptInterface
        public void SelectFileChooser(){
            handler.sendEmptyMessage(4);
        }
        //下载附件
        @JavascriptInterface
        public void DownloadFile(final String downFileUrl){
            try {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = "下载附件中，请稍后...";
                handler.sendMessage(msg);
                Thread fileThread  =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String filePath = FileUtils.DownloadFile(downFileUrl,FileUtils.GetSDPath()+"jhoamobile/temp/webdownload/",false);
                            webview.loadUrl("javascript:DowloadFilePath('"+filePath+"')");
                            handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = "下载失败!";
                            handler.sendMessage(msg);
                            e.printStackTrace();
                        }
                    }
                });
            }catch (Exception e){
                Message msg = new Message();
                msg.what = 2;
                msg.obj = "下载失败!";
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void Openfile(String filepath){
            Message msg = new Message();
            msg.what = 2;
            msg.obj = filepath;
            handler.sendMessage(msg);
        }
        @JavascriptInterface
        public void DownloadAndOpenfile(final String fileUrl){
            Message msg = new Message();
            msg.what = 0;
            msg.obj = "下载附件中，请稍后...";
            handler.sendMessage(msg);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String filePath = FileUtils.DownloadFile(fileUrl, FileUtils.GetSDPath()+"jhoamobile/temp/webdownload",false);
                        if(TextUtils.isEmpty(filePath)|| filePath.equals("0")){
                            Message msg = new Message();
                            msg.what = 3;
                            msg.obj = "附件下载失败";
                            handler.sendMessage(msg);
                        }else{
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = filePath;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        @JavascriptInterface
        public void OpenPdfWrite(String filePath){
            Message msg = new Message();
            msg.what = 5;
            msg.obj = filePath;
            handler.sendMessage(msg);
        }
        //0待办 1已办 2 已发  3 草稿
        @JavascriptInterface
        public void OpenViewForSign(String title,String sign){
            Bundle bundle = new Bundle();
            bundle.putString("title",title);
            bundle.putString("sign",sign);
            Message msg = new Message();
            msg.what = 6;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
        @JavascriptInterface
        public void OpenViewForPackage(String packagename){
            Message msg = new Message();
            msg.what = 7;
            msg.obj = packagename;
            handler.sendMessage(msg);
        }
        @JavascriptInterface
        public void GotoChart(){
            handler.sendEmptyMessage(8);
        }
        @JavascriptInterface
        public void GotoCordova(String path){
            Message msg = new Message();
            msg.what = 9;
            msg.obj = path;
            handler.sendMessage(msg);
        }
        @JavascriptInterface
        public void FileTobase(String FileJson){
            //附件转成base
            List<FileTobase> attarr = JSONObject.parseArray(FileJson,FileTobase.class);
            FileUtils.FileToBase(attarr,MainViewWebview.this);
        }
        @JavascriptInterface
        public void SelectUser(){
            handler.sendEmptyMessage(11);
        }
        @JavascriptInterface
        public String GetFileBase(){
            return fileBaseJson;
        }
    }
    @Override
    public void BigFileUpSucceed(String message, String fileGuid, String sign) {
        fileBaseJson = message;
        Message msg = new Message();
        msg.what = 10;
//        msg.obj = "javascript:FileBase('"+message+"')";
        msg.obj = "javascript:FileBase('1')";
        handler.sendMessage(msg);
    }

    @Override
    public void BigFileUpError(String message, String sign) {
        Message msg = new Message();
        msg.what = 10;
        msg.obj = "javascript:HtmlError('"+message+"')";
        handler.sendMessage(msg);
    }
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what){
                case 0:
                    progressDialog = CustomProgressDialog.createDialog(MainViewWebview.this);
                    progressDialog.setMessage((String)msg.obj);
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    break;
                case 1:
                    if(progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    break;
                case 2:
                    if(progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    FileUtils.openFile(MainViewWebview.this,new File((String)msg.obj));
                    break;
                case 3:
                    if(progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(MainViewWebview.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Intent intent4 = new Intent(MainViewWebview.this, SDFileListActivity.class);
                    startActivity(intent4);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    isSelFile = true;
                    break;
                case 7:
                    try {
                        Intent intent7 = new Intent(MainViewWebview.this,Class.forName((String)msg.obj));
                        startActivity(intent7);
                    }catch (Exception e){
                        Toast.makeText(MainViewWebview.this,"启动页面错误，请检查配置模块地址是否正确",Toast.LENGTH_LONG).show();
                    }
                    break;
                case 8:
                    ArrayList tabList = new ArrayList();
                    tabList.add(Elink.TAB_MESSAGE);
                    tabList.add(Elink.TAB_ORGNIZATION);
                    tabList.add(Elink.TAB_CONTACT);
                    Elink.showMainPage(MainViewWebview.this, tabList);
                    break;
                case 9:
                    String mUrl = Constants.WEB_SERVICE_URL;
                    mUrl = mUrl.substring(0, mUrl.lastIndexOf("/"));
                    mUrl = mUrl.substring(0, mUrl.lastIndexOf("/")+1);
                    Intent intent9 = new Intent(MainViewWebview.this, CordovaWebview.class);
                    intent9.putExtra("loadurl", "file://"+ FileUtils.GetSDPath()+"jhoamobile/html/"+(String)msg.obj);
                    intent9.putExtra("murl", mUrl);
                    intent9.putExtra("appfrom", "");
                    startActivity(intent9);
                    break;
                case 10:
                    //加载回调js
                    String jsfunction = (String)msg.obj;
                    webview.loadUrl(jsfunction);
                    break;
                case 11:
                    Constants.IsOkSelectPerson = true;
                    Intent intent11 = new Intent(MainViewWebview.this, GroupFragmentActivity.class);
                    intent11.putExtra("sign","4");
                    startActivity(intent11);
                    break;
                default:
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };
    protected void onRestart(){
        super.onRestart();
        try {
            if(isSelFile){
                isSelFile = false;
                if( application.getAttachmentList() != null && application.getAttachmentList().size()>0){
                    List<Attachment> attachmentarr = new ArrayList<>();
                    attachmentarr.addAll(application.getAttachmentList());
                    application.removeAllAttachment();
                    String filejson = JSONObject.toJSONString(attachmentarr);
                    webview.loadUrl("javascript:SelectFileBackInfo('"+filejson+"')");
                }
            }
            if(Constants.IsOkSelectPerson ){
                Constants.IsOkSelectPerson = false;
                if(application.getSelectedUsers() != null&& application.getSelectedUsers().size()>0){
                    List<User> useArr = new ArrayList<>();
                    useArr.addAll(application.getSelectedUsers());
                    application.removeAllUser();
                    String userjson = JSONObject.toJSONString(useArr);
                    webview.loadUrl("javascript:CallBackSelectUser('"+userjson+"')");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(resultCode){
            case 1000:
                if(isOpenPdfWirte){
                    isOpenPdfWirte = false;
                }
                Bundle b = data.getExtras();
                String filePath = b.getString("filePath");
                webview.loadUrl("javascript:BackWriter('"+filePath+"')");
                break;
            default:
                break;
        }
    }
    public void WebViewBack(){
        webview.loadUrl("javascript:webgoback()");
    }

    //退出登录
    private void ExitProcess(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_dialog,(ViewGroup) findViewById(R.id.dialog));
        AlertDialog alertDialog;
        TextView dialogtitleL=(TextView)layout.findViewById(R.id.dialogtitle);
        dialogtitleL.setText("退出登录");
        TextView dialogtipL=(TextView)layout.findViewById(R.id.dialogtip);
        dialogtipL.setText("确定要退出登录吗？");
        alertDialog = new AlertDialog.Builder(MainViewWebview.this).setPositiveButton("确定",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    File file = new File(FileUtils.GetSDPath()+"jhoaMobile/temp");
                    new FileUtils().deleteFile(file);
                }
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        alertDialog.setView(layout,-1,-1,-1,-1);
        alertDialog.show();
    }

    public void onDestroy(){
        super.onDestroy();
        try {
            application.removeAllAttachment();
            fileBaseJson = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed(){
        if((System.currentTimeMillis()-exitTime) > 2000){
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(FileUtils.GetSDPath()+"jhoaMobile/temp");
                new FileUtils().deleteFile(file);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }
}
