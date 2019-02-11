package com.android.yawei.jhoa.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.android.yawei.jhoa.adapter.ConLogAdapter;
import com.android.yawei.jhoa.bean.ConLogBean;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.ui.MyListView;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.XmlUtils;
import com.android.yawei.jhoa.webservice.WebService;
import com.yawei.jhoa.mobile.R;


import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class ConLogActivity extends Activity {
    private LinearLayout LinTopBack;
    private MyListView myListView;
    private ImageView internet_error;
    private ConLogAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destoon_login);
        try{
            initView();
            GetUserConLog();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initView(){
        LinTopBack = (LinearLayout)findViewById(R.id.LinTopBack);
        LinTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myListView = (MyListView)findViewById(R.id.myListView);
        internet_error = (ImageView)findViewById(R.id.internet_error);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetUserConLog();
            }
        });
    }
    private void GetUserConLog(){
        WebServiceNetworkAccess.GetUserLoginInfo(SpUtils.getString(this, Constants.CUTOVER_AD_GUID, ""), new WebService.Callback() {
            @Override
            public void handle(String result) {
                Log.d("返回结果",result);
                try {
                    if (result != null && !result.equals("") && !"-2".equals(result) ) {
                        myListView.setVisibility(View.VISIBLE);
                        internet_error.setVisibility(View.GONE);
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    } else {
                        myListView.setVisibility(View.GONE);
                        internet_error.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            try {
                switch (msg.what){
                    case 0:
                        refreshLayout.setRefreshing(false);
                        String strModuleXml = (String) msg.obj;
                        if(strModuleXml != null && !strModuleXml.equals("") && !strModuleXml.equals("anyType")){
//                            List<MoreGroupBean> strMoreGroup = JSONArray.parseArray(strModuleXml, MoreGroupBean.class);
                            JSON json =  XmlUtils.ConvertXMLtoJSON(strModuleXml);
                            String jsonstr = json.toString();
                            jsonstr = "{root:"+jsonstr+"}";
                            JSONObject jsonObject = JSONObject.fromObject(jsonstr);
                            Log.d("jsonObject123",jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("root");
                            List<ConLogBean> strlistArr = new ArrayList<>();
                            for (int j=0;j<jsonArray.size();j++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                ConLogBean bean = new ConLogBean();
                                bean.setMobileBrand(jsonObject1.getString("mobilebrand"));
                                bean.setLoginTime(jsonObject1.getString("logontime"));
                                bean.setMobileType(jsonObject1.getString("mobiletype"));
                                strlistArr.add(bean);
                            }
//                                groupBean.setListOption(strlistArr);
//                                listData.add(groupBean);
                            adapter = new ConLogAdapter(ConLogActivity.this,myListView,ConLogActivity.this,strlistArr,handler);
                            myListView.setAdapter(adapter);
                            //JSONObject json=XmlUtils.xml2JSON(strModuleXml.getBytes());
                            //Log.d("json_",json.toJSONString());
                            //JSONArray jsonArray = JSONArray.parseArray(strModuleXml);
//                            JSONArray jsonArray = jsonObject123.getJSONArray("root");
//                            for (int i=0;i<jsonArray.size();i++){
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
////                                String groupname = jsonObject.getString("groupname");
////                                MoreGroupBean groupBean = new MoreGroupBean();
////                                groupBean.setGroupName(groupname);
//                                JSONArray jsonArray1 = jsonObject.getJSONArray("item");
//                                List<ConLogBean> strlistArr = new ArrayList<>();
//                                for (int j=0;j<jsonArray1.size();j++){
//                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
//                                    ConLogBean bean = new ConLogBean();
//                                    bean.setAccountName(jsonObject1.getString("username"));
//                                    bean.setLoginTime(jsonObject1.getString("logontime"));
//                                    strlistArr.add(bean);
//                                }
////                                groupBean.setListOption(strlistArr);
////                                listData.add(groupBean);
//                                adapter = new ConLogAdapter(ConLogActivity.this,myListView,ConLogActivity.this,strlistArr,handler);
//                                myListView.setAdapter(adapter);
//                            }
//                            adapter.UpData(listData);
//                            adapter.notifyDataSetChanged();
                        }
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


}
