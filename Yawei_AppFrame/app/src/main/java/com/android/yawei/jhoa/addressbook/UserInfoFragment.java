package com.android.yawei.jhoa.addressbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.bean.GetUserPhoneBean;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.fragment.BackHandledFragment;
import com.android.yawei.jhoa.parser.GetUserPhoneParser;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.XmlUtils;
import com.android.yawei.jhoa.webservice.WebService;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.hxyl.Elink;
import com.yawei.jhoa.mobile.R;

import java.util.UUID;

/**
 * Created by Yusz on 2018-6-13.
 */

public class UserInfoFragment extends BackHandledFragment implements View.OnClickListener,Elink.IEventHandler{

    private static FragmentCall mcallBack;
    private static User muserInfo;
    private MyApplication application;
    private FloatingActionsMenu actionMeun;
    private TextView txtName;
    private TextView txtDepart;
    private TextView txtPhoneNum;
    private ImageButton butSendEmail;//发邮件
    private ImageButton butCallPhone;//打电话
    private ImageButton butSendSMS;//发短信
    private FloatingActionButton action_xyj;//写便笺
    private FloatingActionButton action_sendMsg;//写便笺

    public static UserInfoFragment NewInstance(FragmentCall callback, User userifo){
        mcallBack = callback;
        muserInfo = userifo;
        UserInfoFragment fragment = new UserInfoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinfo, null);
        application = (MyApplication) getActivity().getApplication();
        InitView(view);
        GetUserInfo(muserInfo);
        return view;
    }
    private View InitView(View view){
        txtName = (TextView)view.findViewById(R.id.name);
        txtName.setText(muserInfo.getDisplayName());
        txtDepart = (TextView)view.findViewById(R.id.depart);
        txtDepart.setText(muserInfo.getPath());
        txtPhoneNum = (TextView)view.findViewById(R.id.phonenum);
        butSendEmail = (ImageButton)view.findViewById(R.id.sendEmail);//发邮件
        butSendEmail.setOnClickListener(this);
        butCallPhone= (ImageButton)view.findViewById(R.id.callPhone);//打电话
        butCallPhone.setOnClickListener(this);
        butSendSMS= (ImageButton)view.findViewById(R.id.sendSMS);//发短信
        butSendSMS.setOnClickListener(this);
        actionMeun = (FloatingActionsMenu)view.findViewById(R.id.multiple_actions);
        action_xyj = (FloatingActionButton)view.findViewById(R.id.action_xyj);//写便笺
        action_xyj.setOnClickListener(this);
        action_sendMsg=(FloatingActionButton)view.findViewById(R.id.action_sendMsg);
        action_sendMsg.setOnClickListener(this);
        if(Constants.OpenChat&&Constants.ChatIsInit){
            action_sendMsg.setVisibility(View.VISIBLE);
        }else{
            action_sendMsg.setVisibility(View.GONE);
        }
        return view;
    }
    private void GetUserInfo(User user){
        WebServiceNetworkAccess.GetUserInfoByGuid(user.getAdGuid(), new WebService.Callback() {
            @Override
            public void handle(String result) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what){
                case 0:
                    String userinfoxml = (String)msg.obj;
                    if(userinfoxml != null && !"".equals(userinfoxml)&&!"anyType".equals(userinfoxml)){
                        GetUserPhoneBean phoneBean = ParseXMl(userinfoxml);
                        if(phoneBean !=null){
                            txtPhoneNum.setText(phoneBean.getUsertel());
                            muserInfo.setUserName(phoneBean.getAccountname());
                        }
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
    //解析手机号列表数据
    public GetUserPhoneBean ParseXMl(String xml)throws Exception{
        if (xml == null){
            throw new NullPointerException();
        }
        GetUserPhoneBean dataSet = new GetUserPhoneBean();
        GetUserPhoneParser bp = new GetUserPhoneParser(dataSet);
        try{
            XmlUtils.saxParse(xml, bp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataSet;
    }
    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onClick(View view) {
        try {
            Intent intent;
            switch (view.getId()){
            case R.id.sendEmail:
                break;
            case R.id.callPhone:
                //打电话
                String callPhone = txtPhoneNum.getText().toString();
                if(callPhone.trim().equals("")){
                    Toast.makeText(getContext(),"电话号码为空，无法打电话！",Toast.LENGTH_LONG).show();
                    return;
                }
                intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+callPhone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.sendSMS:
                //发短信
                String sendsms = txtPhoneNum.getText().toString();
                if(sendsms.trim().equals("")){
                    Toast.makeText(getContext(),"电话号码为空，无法发短信！",Toast.LENGTH_LONG).show();
                    return;
                }
                Uri smsToUri = Uri.parse("smsto:"+sendsms);
                Intent intentsms = new Intent(Intent.ACTION_SENDTO, smsToUri);
                intentsms.setAction(Intent.ACTION_SENDTO);
                intentsms.putExtra("sms_body", "");//默认发送的内容
                startActivity(intentsms);
                break;
            case R.id.action_xyj:
                break;
            case R.id.action_sendMsg:
                actionMeun.collapse();
                Elink.registerHandler(this);
                Elink.queryUserFromServerByLoginName(muserInfo.getUserName());
                break;
            default:
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void handlerEvent(int i, Object data) {
        try {
            switch (i){
                case -0x0202:
                    Bundle bundle = (Bundle) data;
                    String userName = bundle.getString("userName");
                    UUID uid = (UUID) bundle.getSerializable("userId");
                    Elink.startChart(getActivity(),uid,userName);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onDestroy(){
        super.onDestroy();
        try {
            if(Constants.ChatIsInit&&Constants.OpenChat){
                Elink.unRegisterHandler(this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
