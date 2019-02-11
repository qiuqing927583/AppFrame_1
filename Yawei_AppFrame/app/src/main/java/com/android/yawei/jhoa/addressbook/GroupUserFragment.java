package com.android.yawei.jhoa.addressbook;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.GroupUserFAdapter;
import com.android.yawei.jhoa.bean.GroupBean;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.factory.ResolveXML;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.fragment.BackHandledFragment;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.webservice.WebService;
import com.yawei.android.appframework.ui.RefreshLayoutListView;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusz on 2018-6-13.
 */

public class GroupUserFragment extends BackHandledFragment {

    RefreshLayoutListView listView;
    SwipeRefreshLayout swip;
    GroupUserFAdapter adapter;

    private static FragmentCall mcallback;
    private static GroupBean mgroupData;
    private static String parameter;
    private String exchageId;
    private List<User> dataSet;//组成员
    private static int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groupuser, null);
        exchageId = SpUtils.getString(getContext(), Constants.CUTOVER_EXCHANGE_ID,"");
        dataSet = new ArrayList<>();
        adapter = null;
        InitView(view);
        GetGroupUser();
        return view;
    }
    private View InitView(View rootView){
        listView = (RefreshLayoutListView)rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(onItemClickListener);
        swip = (SwipeRefreshLayout)rootView.findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page =1;
                GetGroupUser();
            }
        });
        listView.setLoadMoreListen(new RefreshLayoutListView.OnLoadMore() {
            @Override
            public void loadMore() {
                page++;
                GetGroupUser();
            }
        });
        return rootView;
    }
    public static GroupUserFragment NewInstance(FragmentCall callback , GroupBean groupData){
        mcallback = callback;
        mgroupData = groupData;
        if(groupData.getType()!=null && groupData.getType().equals("1")){//公共組
            parameter=groupData.getName();
        }else{
            parameter=groupData.getGuid();
        }
        page = 1;
        GroupUserFragment fragment = new GroupUserFragment();
        return fragment;
    }
    private void GetGroupUser(){
        WebServiceNetworkAccess.GetUsersInGroupByPageNo(parameter, exchageId, mgroupData.getType(), String.valueOf(page), new WebService.Callback() {
            @Override
            public void handle(String result) {
                Message msg_ = new Message();
                msg_.what = 0;
                msg_.obj = result;
                handler.sendMessage(msg_);
            }
        });
    }
    private OnItemClickListener onItemClickListener = new OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mcallback.OnClickUser(dataSet.get(i));
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what){
                case 0:
                    if(swip.isShown()){
                        swip.setRefreshing(false);
                    }
                    if (msg.obj != null&& !msg.obj.equals("anyType") && !msg.obj.toString().equals("")&& !msg.obj.toString().equals("<root />")) {
                        if(page==1){
                            dataSet.clear();
                            setInboxItem((String) msg.obj);
                        }else{
                            setInboxItem((String) msg.obj);
                        }

                    }else if(msg.obj != null && (msg.obj.toString().equals("")|| msg.obj.toString().equals("<root />"))){
                        if (page==1)
                            Toast.makeText(getContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                        listView.onLoadComplete(true);
                    }
                    break;
                case 1:
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
            List<User> data = new ArrayList<User>();
            data = ResolveXML.parseGPerson(result);
            if(data.size()<Integer.parseInt(Constants.PAGE_SIZE)){
                listView.onLoadComplete(true);
            }else{
                listView.onLoadComplete(false);
            }
            dataSet.addAll(data);
            //设定初始化组人员的适配器
            if (data.size() != 0){
                if (adapter == null){
                    adapter = new GroupUserFAdapter(getActivity(),dataSet);
                    listView.setAdapter(adapter);
                }else {
                    adapter.UpData(dataSet);
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onBackPressed() {
        return false;
    }
}
