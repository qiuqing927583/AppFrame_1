package com.yawei.cordova.plugins.ionic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.yawei.cordova.webview.CordovaWebview;
import com.android.yawei.jhoa.addressbook.GroupFragmentActivity;
import com.android.yawei.jhoa.bean.FileTobase;
import com.android.yawei.jhoa.bean.RootUserBean;
import com.android.yawei.jhoa.mobile.SDFileListActivity;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.yawei.jhoa.mobile.R;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.List;

/**
 * ionic 调用的插件
 * This class echoes a string called from JavaScript.
 */
public class MyPlugin extends CordovaPlugin {

    public static CallbackContext mcallbackContext;
    public CordovaWebview activity;
    public MyApplication application;

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        mcallbackContext = callbackContext;
        activity = (CordovaWebview) this.cordova.getActivity();
        application = (MyApplication) activity.getApplication();
        //coolMethod
        if(action.equals("usermsg")){
            //获取用户信息
            String oaurl = Constants.WEB_SERVICE_URL.substring(0,Constants.WEB_SERVICE_URL.lastIndexOf("/"));
            oaurl = oaurl.substring(0,oaurl.lastIndexOf("/")+1);
            RootUserBean bean = new RootUserBean();
            bean.setAdGuid(SpUtils.getString(activity, Constants.CUTOVER_AD_GUID,""));
            bean.setDepart(SpUtils.getString(activity, Constants.CUTOVER_AD_DEPART,""));
            bean.setDisplayName(SpUtils.getString(activity, Constants.CUTOVER_DISPLAY_NAME,""));
            bean.setExchangeID(SpUtils.getString(activity, Constants.CUTOVER_EXCHANGE_ID,""));
            bean.setSysflag(SpUtils.getString(activity, Constants.CUTOVER_AD_SYSFLAG,""));
            bean.setUserName(SpUtils.getString(activity, Constants.CUTOVER_AD_LOGNAME,""));
            bean.setOrginuserName(SpUtils.getString(activity, Constants.AD_LOGNAME,""));
            bean.setOrgindisplayName(SpUtils.getString(activity, Constants.AD_LOGNAME,""));
            bean.setOrginadGuid(SpUtils.getString(activity, Constants.AD_GUID,""));
            bean.setParameter(activity.parameter);
            bean.setWebserviceurl(Constants.WEB_SERVICE_URL);
            bean.setOaUrl(oaurl);
            bean.setDeveInfo(JSON.toJSONString(application.getDeveInfo()));
            String usermsg = com.alibaba.fastjson.JSONObject.toJSONString(bean);
            this.coolMethod(usermsg, callbackContext);
        }else if(action.equals("exitApp")){
            //退出
            ExitProcess();
            this.coolMethod("1", callbackContext);
            return true;
        }else if(action.equals("selectFileChooser")){
            activity.isSelFile = true;
            Intent intent4 = new Intent(activity, SDFileListActivity.class);
            activity.startActivity(intent4);
            activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return true;
        }else if(action.equals("downfile")){
            //下载附件
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url =  args.getString(0);
                        String filePath = FileUtils.DownloadFile(url, FileUtils.GetSDPath()+"jhoamobile/temp/webdownload/",false);
                        coolMethod(filePath,callbackContext);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callbackContext.error("下载附件失败！");
                    }

                }
            }).start();
            return true;
        }else if(action.equals("openfile")){
            try {
                String filepath =  args.getString(0);
                FileUtils.openFile(activity,new File(filepath));
                coolMethod("1",callbackContext);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                callbackContext.error("打开附件失败！");
                return  true;
            }
        }else if(action.equals("downandopenfile")){
            final String fileurl =  args.getString(0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String filePath = FileUtils.DownloadFile(fileurl, FileUtils.GetSDPath()+"jhoamobile/temp/webdownload/",false);
                        if(!TextUtils.isEmpty(filePath)&&!"0".equals(filePath)){
                            Message msg = new Message();
                            msg.what  = 5;
                            msg.obj = filePath;
                            activity.handler.sendMessage(msg);
                            coolMethod("1",callbackContext);
                        }else{
                            callbackContext.error("附件下载失败！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callbackContext.error("附件下载失败！");
                    }

                }
            }).start();
            return true;
        }else if(action.equals("openPdfWrite")){
            //调用手写

            return true;
        }else if(action.equals("gotoback")){
            // 返回关闭当前的activity
            coolMethod("1",callbackContext);
            activity.finish();
            return true;
        }else if(action.equals("OpenViewForSign")){

            return true;
        }else if(action.equals("OpenViewForPackage")){
            try {
                String packname = args.getString(0);
                Intent intent = new Intent(activity,Class.forName(packname));
                activity.startActivity(intent);
                coolMethod("1",callbackContext);
                return true;
            }catch (Exception e){
                Toast.makeText(activity,"启动页面错误，请检查配置模块地址是否正确",Toast.LENGTH_LONG).show();
            }
        }else if(action.equals("fileTobase")){
            String fileJson = args.getString(0);
            List<FileTobase> attarr = com.alibaba.fastjson.JSONObject.parseArray(fileJson,FileTobase.class);
            FileUtils.FileToBase(attarr,activity);
            return true;
        }else if(action.equals("selectuser")){
            Constants.IsOkSelectPerson = true;
//            Intent intent11 = new Intent(activity, SelectPersonListActivity.class);
//            activity.startActivity(intent11);
            Intent intent11 = new Intent(activity, GroupFragmentActivity.class);
            intent11.putExtra("sign","4");
            activity.startActivity(intent11);
            return true;
        }else if(action.equals("zxing")){
            activity.ScanQRCode();
            return true;
        }
        return false;
    }

    //退出登录
    private void ExitProcess(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_dialog,(ViewGroup) activity.findViewById(R.id.dialog));
        AlertDialog alertDialog;
        TextView dialogtitleL=(TextView)layout.findViewById(R.id.dialogtitle);
        dialogtitleL.setText("退出登录");
        TextView dialogtipL=(TextView)layout.findViewById(R.id.dialogtip);
        dialogtipL.setText("确定要退出登录吗？");
        alertDialog = new AlertDialog.Builder(activity).setPositiveButton("确定",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.finish();
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
    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
