package com.android.yawei.jhoa.addressbook;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.Interface.BackHandledInterface;
import com.android.yawei.jhoa.adapter.BaseFragmentActivity;
import com.android.yawei.jhoa.bean.GroupBean;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.fragment.AddressBookFragment;
import com.android.yawei.jhoa.fragment.BackHandledFragment;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.PreferenceHelper;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.yawei.jhoa.mobile.R;

/**
 * TODO 新版通讯录、选人
 * Created by Yusz on 2018-6-13.
 */

public class GroupFragmentActivity extends BaseFragmentActivity implements View.OnClickListener,BackHandledInterface,OnBackStackChangedListener,FragmentCall {

    private Bundle mbundle = null;
    private LinearLayout linBack;
    private FragmentManager mFragmentManager;
    private BackHandledFragment mBackHandedFragment;
    private RelativeLayout relativeLayoutBottom;
    private TextView txtSelectSize;
    private Button butSure;

    private MyApplication application;
    private int mTheme = R.style.AppTheme_Default;//换肤
    private GroupBean groupData;
    private String sign;//1组进入，2搜索进入，3点击通讯录直接进入

    protected void onCreate(Bundle savedInstanceState){
        if (mbundle == null) {
            mbundle = savedInstanceState;
            mTheme = PreferenceHelper.getTheme(this);
        } else {
            mTheme = savedInstanceState.getInt("theme");
        }
        setTheme(mTheme);
        super.onCreate(mbundle);
        setContentView(R.layout.activity_groupfragment);
        try {
            SysExitUtil.AddActivity(this);
            application = (MyApplication) getApplication();
            groupData = (GroupBean)getIntent().getSerializableExtra("group");
            sign = getIntent().getStringExtra("sign");
            InitView();
            if(sign.equals("1")){
                GroupUserFragment fragment = GroupUserFragment.NewInstance(this,groupData);
                replaceFragment(fragment);
            }else if(sign.equals("2")){
                String sreachContent = getIntent().getStringExtra("content");
                SreachUserFragment fragment = SreachUserFragment.NewInstance(this,sreachContent);
                replaceFragment(fragment);
            }else if(sign.equals("3")||sign.equals("4")){
                AddressBookFragment fragment = new AddressBookFragment();
                fragment.initFragment(sign,this);
                replaceFragment(fragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void InitView()throws Exception{
        linBack = (LinearLayout)findViewById(R.id.LinTopBack);
        linBack.setOnClickListener(this);
        relativeLayoutBottom = (RelativeLayout)findViewById(R.id.reletiveBottom);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);
        txtSelectSize = (TextView)findViewById(R.id.textUserSize);
        txtSelectSize.setOnClickListener(this);
        butSure = (Button)findViewById(R.id.butSure);
        butSure.setOnClickListener(this);
        if("4".equals(sign)){
            relativeLayoutBottom.setVisibility(View.VISIBLE);
        }
    }
    public void replaceFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().replace(R.id.framelayout, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("tag").commit();
    }
    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()){
                case R.id.LinTopBack:
                    if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                            finish();
                            return;
                        }
                        getSupportFragmentManager().popBackStack();
                    }
                    break;
                case R.id.butSure:
                    if(application.getSelectedUsers().size()>0){
                        Constants.IsOkSelectPerson = true;
                        finish();
                    }else {
                        Toast.makeText(this,"您未选择用户",Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.textUserSize:
                    try {
                        if(application.getSelectedUsers().size()>0){
                            LookoverSelectUserFragment fragment = LookoverSelectUserFragment.NewInstance(this);
                            replaceFragment(fragment);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackStackChanged() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            mFragmentManager.getBackStackEntryAt(count - 1);
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }
    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return;
            }
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void OnClickGroup(GroupBean info) {
        if("4".equals(sign)){
            SelectUserFragment fragment = SelectUserFragment.NewInstance(this,info);
            replaceFragment(fragment);
        }else if("3".equals(sign)){
            GroupUserFragment fragment = GroupUserFragment.NewInstance(this,info);
            replaceFragment(fragment);
        }
    }

    @Override
    public void OnClickUser(User user) {
        UserInfoFragment fragment = UserInfoFragment.NewInstance(this,user);
        replaceFragment(fragment);
    }

    @Override
    public void OnSreachUser(String userName) {
        if("3".equals(sign)){
            SreachUserFragment fragment = SreachUserFragment.NewInstance(this,userName);
            replaceFragment(fragment);
        }else  if("4".equals(sign)){
            SreachSelectUserFragment fragment = SreachSelectUserFragment.NewInstance(this,userName);
            replaceFragment(fragment);
        }
    }

    @Override
    public void OnSeclectUserSize(int size) {
        if(size>0){
            txtSelectSize.setText("已选择："+size+"人");
        }else{
            txtSelectSize.setText("已选择：");
        }
    }

    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            switch (requestCode) {
//                case 1000:
//                    if(resultCode==RESULT_OK){
//                        if(data != null){
//                            String msg = data.getStringExtra("voice");
//                        }
//                    }
//                    break;
//                default:
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    public void onDestroy(){
        super.onDestroy();
        SysExitUtil.RemoveActivity(this);
    }
}
