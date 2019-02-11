package com.android.yawei.jhoa.addressbook;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.SelectUserAdapter;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.factory.ResolveXML;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.fragment.BackHandledFragment;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.webservice.WebService;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusz on 2018-7-5.
 */

public class SreachSelectUserFragment extends BackHandledFragment{
    private static String sreachContent;//搜索内容
    private ListView listView;
    private SwipeRefreshLayout swip;
    SelectUserAdapter adapter;
    private static FragmentCall mcallback;
    private List<User> dataSet;//组成员
    private MyApplication application;

    public static SreachSelectUserFragment NewInstance(FragmentCall callback , String content){
        sreachContent = content;
        mcallback = callback;
        SreachSelectUserFragment fragment = new SreachSelectUserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sreachselectuser, null);
        dataSet = new ArrayList<>();
        adapter = null;
        application = (MyApplication) getActivity().getApplication();
        InitView(view);
        SreachUser();
        return view;
    }
    private View InitView(View rootView){
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(onItemClickListener);
        swip = (SwipeRefreshLayout)rootView.findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SreachUser();
            }
        });
        return rootView;
    }
    private void SreachUser(){
        WebServiceNetworkAccess.GetUserListByQuery(sreachContent, new WebService.Callback() {
            @Override
            public void handle(String result) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                User user  = (User) adapter.getItem(i);;
                ImageView okImg=(ImageView)view.findViewById(R.id.imageSel);
                if(user.getChecked()==1){
                    okImg.setImageResource(R.drawable.del);
                    user.setChecked(0);
                    application.removeUser(user);
                }else{
                    okImg.setImageResource(R.drawable.add);
                    user.setChecked(1);
                    if(!application.isContainsUser(user)){
                        application.addUser(user);
                    }
                }
                List<User> arrUser = application.getSelectedUsers();
                if(arrUser !=  null){
                    mcallback.OnSeclectUserSize(arrUser.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what){
                    case 0:
                        //刷新
                        if(swip.isShown()){
                            swip.setRefreshing(false);
                        }
                        if (msg.obj != null&& !msg.obj.equals("anyType") && !msg.obj.toString().equals("")&& !msg.obj.toString().equals("<root />")) {
                            dataSet.clear();
                            setInboxItem((String) msg.obj);
                            adapter = new SelectUserAdapter(getActivity(),dataSet);
                            listView.setAdapter(adapter);
                        }else if(msg.obj != null && (msg.obj.toString().equals("")|| msg.obj.toString().equals("<root />"))){
                            Toast.makeText(getActivity(), "未搜索到相关信息！", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "搜索信息失败！", Toast.LENGTH_SHORT).show();
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
    // 解析组成员
    private void setInboxItem(String result) throws Exception{
        try {
            List<User> data = ResolveXML.parseGPerson(result);
            dataSet.addAll(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onBackPressed() {
        return false;
    }
}
