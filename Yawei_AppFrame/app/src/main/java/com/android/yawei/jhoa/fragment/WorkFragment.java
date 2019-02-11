package com.android.yawei.jhoa.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.yawei.cordova.webview.CordovaWebview;
import com.android.yawei.jhoa.Interface.GridItemCilckCallback;
import com.android.yawei.jhoa.adapter.MoreFunctionAdapter;
import com.android.yawei.jhoa.adapter.MoreFunctionChildAdapter;
import com.android.yawei.jhoa.addressbook.GroupFragmentActivity;
import com.android.yawei.jhoa.bean.AppOptionBean;
import com.android.yawei.jhoa.bean.MoreGroupBean;
import com.android.yawei.jhoa.db.AppModuleDatabase;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.mobile.MoreFunctionActivity;
import com.android.yawei.jhoa.mobile.MyPersonalCenterActivity;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.VerifyPermissions;
import com.android.yawei.jhoa.voice.baidu.voice.IStatus;
import com.android.yawei.jhoa.webservice.WebService;
import com.android.yawei.jhoa.webview.LoadHtmlWebview;
import com.hxyl.Elink;
import com.yawei.jhoa.mobile.R;
import com.yawei.zxing.client.QRCodeIntent;

import java.util.ArrayList;
import java.util.List;

/**
 * 办公fragment
 * Created by Yusz on 2018-6-11.
 */

public class WorkFragment extends Fragment implements View.OnClickListener,GridItemCilckCallback,IStatus {

    private RelativeLayout reTopHeader;
    private LinearLayout myapplin;
    private LinearLayout linredact;//编辑
    private LinearLayout linSure;//完成
    private ListView listview_MoreOption;
    private List<MoreGroupBean> listData;
    private List<AppOptionBean> listMyOption;
    private MoreFunctionAdapter adapter;
    private GridView appGridView;

    private CustomProgressDialog progressDialog;
    private String userGuid;
    private String userExchangeID;
    private MyApplication application;
    private AppModuleDatabase dbhelper;
    private boolean isRedact;//是否是编辑状态
    private final int REQUEST_CODE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_morefunction, container, false);
        View newView = null;
        try {
            InitData();
            newView = InitView(rootView);
            GetMoreModule();
        }catch (Exception e){
            e.printStackTrace();
        }
        return newView;
    }
    private View InitView(View view)throws Exception{
        reTopHeader = (RelativeLayout)view.findViewById(R.id.reletivelayout);
        reTopHeader.setVisibility(View.GONE);
        listview_MoreOption = (ListView)view.findViewById(R.id.moreList);
        adapter = new MoreFunctionAdapter(listData,getContext(),this);
        listview_MoreOption.setAdapter(adapter);
        appGridView =  (GridView) view.findViewById(R.id.myappgridview);
        listMyOption = dbhelper.GetAppByTable();
        MoreFunctionChildAdapter moudleadapter = new MoreFunctionChildAdapter(listMyOption,getContext(),this);
        moudleadapter.SetRedact(1);
        appGridView.setAdapter(moudleadapter);
        return view;
    }
    private void InitData()throws Exception{
        listData = new ArrayList<>();
        userGuid = SpUtils.getString(getContext(),Constants.CUTOVER_AD_GUID,"");
        userExchangeID = SpUtils.getString(getContext(),Constants.CUTOVER_EXCHANGE_ID,"");
        application = (MyApplication) getActivity().getApplication();
        dbhelper = application.GetModuleSQLHepler();
        String[] str = getResources().getStringArray(R.array.moreoption);
        for(int i=0;i<str.length;i++) {
            MoreGroupBean groupBean = new MoreGroupBean();
            String[] str1 = str[i].split("[|]");
            groupBean.setGroupName(str1[0]);
            String[] str2 = str1[1].split("、");
            List<AppOptionBean> listOption = new ArrayList<>();
            for (int j = 0; j < str2.length; j++) {
                String[] str3 = str2[j].split(",");
                if (str3[0].equals("daiban")) {
                    AppOptionBean optionBean = new AppOptionBean();
                    optionBean.setModulename(str3[1]);
                    optionBean.setEnName(str3[0]);
                    optionBean.setImageId(R.drawable.ic_daiban);
                    listOption.add(optionBean);
                } else if (str3[0].equals("yiban")) {
                    AppOptionBean optionBean = new AppOptionBean();
                    optionBean.setModulename(str3[1]);
                    optionBean.setEnName(str3[0]);
                    optionBean.setImageId(R.drawable.ic_yiban);
                    listOption.add(optionBean);
                } else if (str3[0].equals("yifa")) {
                    AppOptionBean optionBean = new AppOptionBean();
                    optionBean.setModulename(str3[1]);
                    optionBean.setEnName(str3[0]);
                    optionBean.setImageId(R.drawable.ic_yifa);
                    listOption.add(optionBean);
                } else if (str3[0].equals("caogao")) {
                    AppOptionBean optionBean = new AppOptionBean();
                    optionBean.setModulename(str3[1]);
                    optionBean.setEnName(str3[0]);
                    optionBean.setImageId(R.drawable.ic_caogao);
                    listOption.add(optionBean);
                }else if(str3[0].equals("important")){
                    //急要件
                    AppOptionBean optionBean = new AppOptionBean();
                    optionBean.setModulename(str3[1]);
                    optionBean.setEnName(str3[0]);
                    optionBean.setImageId(R.drawable.ic_important_email);
                    listOption.add(optionBean);
                } else if (str3[0].equals("chart")) {
                    AppOptionBean optionBean = new AppOptionBean();
                    optionBean.setModulename(str3[1]);
                    optionBean.setEnName(str3[0]);
                    optionBean.setImageId(R.drawable.ic_goutong);
                    listOption.add(optionBean);
                } else if (str3[0].equals("contact")) {
                    AppOptionBean optionBean = new AppOptionBean();
                    optionBean.setModulename(str3[1]);
                    optionBean.setEnName(str3[0]);
                    optionBean.setImageId(R.drawable.ic_tongxunlu);
                    listOption.add(optionBean);
                } else if (str3[0].equals("note")) {
                    AppOptionBean optionBean = new AppOptionBean();
                    optionBean.setModulename(str3[1]);
                    optionBean.setEnName(str3[0]);
                    optionBean.setImageId(R.drawable.ic_jishiben);
                    listOption.add(optionBean);
                } else if (str3[0].equals("self")) {
                    AppOptionBean optionBean = new AppOptionBean();
                    optionBean.setModulename(str3[1]);
                    optionBean.setEnName(str3[0]);
                    optionBean.setImageId(R.drawable.ic_wode);
                    listOption.add(optionBean);
                }
            }
            groupBean.setListOption(listOption);
            listData.add(groupBean);
        }
    }
    private void GetMoreModule(){
        WebServiceNetworkAccess.GetModuleInfoByUserGuid(userGuid,userExchangeID,Constants.APPGUID, new WebService.Callback() {
            @Override
            public void handle(String result) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }
    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()){
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @Override
//    public void OnClickGridItem(Object object) {
//        if(isRedact)//是否是编辑状态
//            return;
//        AppOptionBean bean = (AppOptionBean) object;
//
//    }

    @Override
    public void MoudleAddorDeleteRefresh() {
        try {
            adapter.UpData(listData);
            adapter.notifyDataSetChanged();
            MoreFunctionChildAdapter moudleadapter = new MoreFunctionChildAdapter(dbhelper.GetAppByTable(),getContext(),this);
            moudleadapter.SetRedact(1);
            appGridView.setAdapter(moudleadapter);
        }catch (Exception e){

        }

    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            try {
                switch (msg.what){
                    case 0:
                        String strModuleXml = (String) msg.obj;
                        if(strModuleXml != null && !strModuleXml.equals("") && !strModuleXml.equals("anyType")){
//                            List<MoreGroupBean> strMoreGroup = JSONArray.parseArray(strModuleXml, MoreGroupBean.class);
                            JSONArray jsonArray = JSONArray.parseArray(strModuleXml);
                            for (int i=0;i<jsonArray.size();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String groupname = jsonObject.getString("groupname");
                                MoreGroupBean groupBean = new MoreGroupBean();
                                groupBean.setGroupName(groupname);
                                JSONArray jsonArray1 = jsonObject.getJSONArray("moduleinfo");
                                List<AppOptionBean> strlistArr = new ArrayList<>();
                                for (int j=0;j<jsonArray1.size();j++){
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    AppOptionBean bean = new AppOptionBean();
                                    bean.setModulename(jsonObject1.getString("modulename"));
                                    bean.setModuletype(jsonObject1.getString("moduletype"));
                                    bean.setType(jsonObject1.getString("moduletype"));
                                    bean.setModuleurl(jsonObject1.getString("moduleurl"));
                                    bean.setImageurl(jsonObject1.getString("imageurl"));
                                    strlistArr.add(bean);
                                }
                                groupBean.setListOption(strlistArr);
                                listData.add(groupBean);
                            }
                            adapter.UpData(listData);
                            adapter.notifyDataSetChanged();
                        }
                        if(progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
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
    //TODO 二维码
    /*权限申请,相机，存储，蜂鸣*/
    private void requestPermission(Class activity) {
        String[] permission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.VIBRATE,
                Manifest.permission.CAMERA,
        };
        boolean flag = true;
        for (String aPermission : permission) {
            if (ActivityCompat.checkSelfPermission(getActivity(), aPermission) != PackageManager.PERMISSION_GRANTED) {
                flag = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), aPermission)) {
                    Toast.makeText(getActivity(), "您已禁止所需要权限，需要重新开启",Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), permission, REQUEST_CODE);
                }
            } else {
                flag &= flag;
            }
        }
        if (flag) {
            Intent intent = new Intent(getActivity(), activity);
            //预览框的宽高
            intent.putExtra(QRCodeIntent.FRAME_WIDTH, 250);
            intent.putExtra(QRCodeIntent.FRAME_HEIGHT, 250);
            intent.putExtra(QRCodeIntent.SET_RESULT, true);
            startActivityForResult(intent, 10);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {
            //接收返回值
            if (data != null && !TextUtils.isEmpty(data.getStringExtra(QRCodeIntent.SCAN_RESULT))) {
                Toast.makeText(getActivity(), data.getStringExtra(QRCodeIntent.SCAN_RESULT),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void OnClickGridItem(Object object) {
        if(isRedact)//是否是编辑状态
            return;
        AppOptionBean bean = (AppOptionBean) object;
        if(bean.getModuleurl() != null && !bean.getModuleurl().isEmpty()){
            //0：H5应用； 1：混合应用；2：独立应用；3：自带应用
            if(bean.getModuletype().equals("0")){
                Intent intent = new Intent(getContext(), LoadHtmlWebview.class);
                String []data = bean.getModuleurl().split("[|]");
                if(data.length>1){
                    intent.putExtra("loadurl", data[0]);
                    intent.putExtra("sign", data[1]);
                }else{
                    intent.putExtra("loadurl", bean.getModuleurl());
                }
                startActivity(intent);
            }else if(bean.getModuletype().equals("1")){
                String mUrl = Constants.WEB_SERVICE_URL;
                mUrl = mUrl.substring(0, mUrl.lastIndexOf("/"));
                mUrl = mUrl.substring(0, mUrl.lastIndexOf("/")+1);
                String []data = bean.getModuleurl().split("[|]");
                String paraJson = "";
                if(data.length>1){
                    String []parameter = data[1].split(";");
                    for(int i=0;i<parameter.length;i++){
                        String[] strpara = parameter[i].split("=");
                        paraJson += "\""+ strpara[0]+"\""+":"+"\""+strpara[1]+"\""+",";
                    }
                    paraJson = paraJson.substring(0,paraJson.lastIndexOf(","));
                    paraJson = "{"+paraJson+"}";
                }
                Intent intent = new Intent(getContext(), CordovaWebview.class);
                intent.putExtra("loadurl", "file://"+ FileUtils.GetSystemFilePath()+"html/"+data[0]);
                intent.putExtra("murl", mUrl);
                intent.putExtra("parameter",paraJson);
                intent.putExtra("appfrom", "");
                startActivity(intent);
            }else  if(bean.getModuletype().equals("2")){
                try{
                    Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(bean.getModuleurl());
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getContext(),"启动程序错误，您可能未安装所需要启动的程序!",Toast.LENGTH_LONG).show();
                }

            }else if(bean.getModuletype().equals("3")){
                if(bean.getModuleurl().equals("chart")){
                    try {
                        if(Constants.ChatIsInit){
                            ArrayList tabList = new ArrayList();
                            tabList.add(Elink.TAB_MESSAGE);
                            tabList.add(Elink.TAB_ORGNIZATION);
                            tabList.add(Elink.TAB_CONTACT);
                            Elink.showMainPage(getContext(),tabList);
                        }else{
                            Toast.makeText(getContext(),"沟通环境未初始化成功请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(bean.getModuleurl().equals("zxing")){
                    VerifyPermissions.RequestPermissionZxing((Activity) getContext(),REQUEST_CODE);
                }else {
                    try {
                        String []data = bean.getModuleurl().split("[|]");
                        Intent intent = new Intent( getContext(),Class.forName(data[0]));
                        if(data.length>1){
                            String []parameter = data[1].split(";");
                            for(int i=0;i<parameter.length;i++){
                                String[] strpara = parameter[i].split("=");
                                intent.putExtra(strpara[0],strpara[1]);
                            }
                        }
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        Toast.makeText(getContext(),"启动页面错误，请检查配置模块地址是否正确",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        }else{
            if (bean.getEnName().equals("chart")){
                if(Constants.ChatIsInit){
                    ArrayList tabList = new ArrayList();
                    tabList.add(Elink.TAB_MESSAGE);
                    tabList.add(Elink.TAB_ORGNIZATION);
                    tabList.add(Elink.TAB_CONTACT);
                    Elink.showMainPage(getContext(),tabList);
                }else{
                    Toast.makeText(getContext(),"沟通环境未初始化成功请稍后再试",Toast.LENGTH_LONG).show();
                }
            }else if (bean.getEnName().equals("contact")){
//                Intent intent = new Intent(MoreFunctionActivity.this, GroupPersonListActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getContext(), GroupFragmentActivity.class);
                intent.putExtra("sign","3");
                startActivity(intent);
            }else if (bean.getEnName().equals("self")){
//                Intent intent = new Intent(MoreFunctionActivity.this, GroupPersonListActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getContext(),MyPersonalCenterActivity.class);
                startActivity(intent);
            }
        }
    }

}
