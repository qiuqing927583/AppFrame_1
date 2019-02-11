package com.android.yawei.jhoa.addressbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.yawei.jhoa.adapter.LookoverSelectUserAdapter;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.fragment.BackHandledFragment;
import com.android.yawei.jhoa.utils.MyApplication;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusz on 2018-7-6.
 */

public class LookoverSelectUserFragment extends BackHandledFragment implements DelectSelUserCall{

    private static FragmentCall mcallback;
    private MyApplication application;
    private ListView listView;
    private LookoverSelectUserAdapter adapter;
    private List<User> userData;

    public static LookoverSelectUserFragment NewInstance(FragmentCall callback){
        mcallback = callback;
        LookoverSelectUserFragment fragment = new LookoverSelectUserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        application = (MyApplication) getActivity().getApplication();
        try {
            userData = new ArrayList<>();
            userData = application.getSelectedUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        View rootView = inflater.inflate(R.layout.fragment_lookoverselectuser, container, false);
        View view = InitView(rootView);
        return view;
    }

    private View InitView(View view){
        listView = (ListView)view.findViewById(R.id.listview);
        adapter = new LookoverSelectUserAdapter(getActivity(),userData,this);
        listView.setAdapter(adapter);
        return  view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void DelectSelUser(int postion) {
        DelectSelectUser(postion);
    }
    //删除某一个用户
    private void DelectSelectUser(final int postion){
        View layout = getLayoutInflater().inflate(R.layout.activity_dialog,(ViewGroup) getActivity().findViewById(R.id.dialog));
        AlertDialog alertDialog;
        TextView dialogtitle=(TextView)layout.findViewById(R.id.dialogtitle);
        dialogtitle.setText("提示");
        TextView dialogtip=(TextView)layout.findViewById(R.id.dialogtip);
//	    dialogtip.setText("点击\"是\"则提交签批信息并处理结束，点击\"否\"则会只提交签批信息.");
        dialogtip.setText("您确定删除此用户？");
        dialogtip.setTextSize(18f);
        alertDialog = new AlertDialog.Builder(getActivity()).setPositiveButton("确定",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
                try {
                    application.removeUser(userData.get(postion));
//                    userData.remove(userData.get(postion));
//                    userData=application.getSelectedUsers();
                    mcallback.OnSeclectUserSize(application.getSelectedUsers().size());
                    handler.sendEmptyMessage(0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        alertDialog.setView(layout,-1,-1,-1,-1);
        alertDialog.show();
//        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(18f);
//        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(18f);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
            case 0:
                adapter.UpData(userData);
                adapter.notifyDataSetChanged();
                break;
            }
            super.handleMessage(msg);
        }
    };
}
