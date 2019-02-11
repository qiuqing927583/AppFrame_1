package com.android.yawei.jhoa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.yawei.jhoa.adapter.BookUserGroupAdapter;
import com.android.yawei.jhoa.addressbook.FragmentCall;
import com.android.yawei.jhoa.addressbook.GroupFragmentActivity;
import com.android.yawei.jhoa.bean.GroupBean;
import com.android.yawei.jhoa.factory.ResolveXML;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.mobile.PersonGroupListActivity;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SoftKeyboardUtil;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.voice.ShowVoiceActivity;
import com.android.yawei.jhoa.webservice.WebService;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 通讯录组
 * Created by Yusz on 2018-6-12.
 */

public class AddressBookFragment extends Fragment implements View.OnClickListener{

    private ListView listView;
    private SwipeRefreshLayout swip;
    private BookUserGroupAdapter adapter;
    private EditText editSreach;//搜索输入框
    private FloatingActionButton customGroup;//自定义组
    private FloatingActionButton voiceSearch;//语音搜索
    private FloatingActionsMenu actionMeun;

    private String userGuid;
    private String exchageId;
    private List<GroupBean> arrGroup;

    private String msign;//3通讯录，4选人
    private FragmentCall mcall;

    public void initFragment(String sign, FragmentCall fragmentCall){
        msign = sign;
        mcall= fragmentCall;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_addressbook, container, false);
        InitData();
        View newView = InitView(rootView);
        GetUserGroup();
        return newView;
    }
    private View InitView(View view){
        listView = (ListView)view.findViewById(R.id.listaddress);
        listView.setOnItemClickListener(onItemClickListener);
        customGroup = (FloatingActionButton)view.findViewById(R.id.customGroup);
        customGroup.setOnClickListener(this);
        voiceSearch = (FloatingActionButton)view.findViewById(R.id.voiceSearch);
        voiceSearch.setOnClickListener(this);
        if(!Constants.OpenBaiduViceo){
            voiceSearch.setVisibility(View.GONE);
        }
        actionMeun = (FloatingActionsMenu)view.findViewById(R.id.multiple_actions);
        if("4".equals(msign)){
            actionMeun.setVisibility(View.GONE);
        }
        swip = (SwipeRefreshLayout)view.findViewById(R.id.swip);
        swip.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetUserGroup();
            }
        });
        editSreach = (EditText)view.findViewById(R.id.et_search);
        editSreach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEND || (event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                    String strEditText=editSreach.getText().toString().trim();
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            if(!strEditText.equals("")){
                                if(!strEditText.trim().equals("")){
                                    if("3".equals(msign)||"4".equals(msign)){
                                        mcall.OnSreachUser(editSreach.getText().toString());
                                    }else {
                                        Intent intent = new Intent(getActivity(), GroupFragmentActivity.class);
                                        intent.putExtra("sign","2");
                                        intent.putExtra("content",editSreach.getText().toString());
                                        startActivity(intent);
                                    }
                                }
                            }
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
        return view;
    }
    private void InitData(){
        userGuid = SpUtils.getString(getContext(), Constants.CUTOVER_AD_GUID,"");
        exchageId = SpUtils.getString(getContext(),Constants.CUTOVER_EXCHANGE_ID,"");
        arrGroup = new ArrayList<>();
    }
    private void GetUserGroup(){
        Log.d("userGuid",userGuid);
        Log.d("exchageId",exchageId);
        WebServiceNetworkAccess.GetUserGroups(userGuid, exchageId,new WebService.Callback() {
            @Override
            public void handle(String result) {
                Log.d("异步获取组信息",result);
                // TODO 异步获取组信息（公共组和个人组）
                Message msg_ = new Message();
                msg_.what = 0;
                msg_.obj = result;
                handler.sendMessage(msg_);
            }
        });
    }
    public void setSreachContent(String content){
        if(editSreach != null){
            editSreach.setText(content);
        }
    }
    private OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if("3".equals(msign)||"4".equals(msign)){
                mcall.OnClickGroup(arrGroup.get(i));
            }else{
                Intent intent = new Intent(getActivity(), GroupFragmentActivity.class);
                intent.putExtra("group",arrGroup.get(i));
                intent.putExtra("sign","1");
                startActivity(intent);
            }
        }
    };
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try{
                switch (msg.what){
                case 0:
                    swip.setRefreshing(false);
                    String groupXMl = (String) msg.obj;
                    if(groupXMl != null && !groupXMl.equals("")&&!"anyType".equals(groupXMl)){
                        arrGroup = ResolveXML.parseGroup(groupXMl);
                        adapter = new BookUserGroupAdapter(getActivity(),arrGroup);
                        listView.setAdapter(adapter);
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
    } ;

    @Override
    public void onClick(View view) {
        try {
            Intent intent = null;
            switch (view.getId()){
            case R.id.customGroup:
                //自定组
                intent = new Intent(getActivity(),PersonGroupListActivity.class);
                startActivity(intent);
                break;
            case R.id.voiceSearch:
                intent = new Intent(getActivity(), ShowVoiceActivity.class);
                startActivityForResult(intent,1000);
                break;
            }
            actionMeun.collapse();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode){
            case 1000:
                if(resultCode==getActivity().RESULT_OK){
                    if(data != null){
                        String msg = data.getStringExtra("voice");
                        editSreach.setText(msg);
                        editSreach.requestFocus();
                        editSreach.setSelection(editSreach.getText().toString().length());
                        SoftKeyboardUtil.ShowKeyboard(getActivity());
                    }
                }
                break;
            default:
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
