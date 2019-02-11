package com.android.yawei.jhoa.mobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.cordova.webview.CordovaWebview;
import com.android.yawei.jhoa.adapter.BaseFragmentActivity;
import com.android.yawei.jhoa.fragment.AddressBookFragment;
import com.android.yawei.jhoa.fragment.ImmediateMessageFragemnt;
import com.android.yawei.jhoa.fragment.SelfSentreFragment;
import com.android.yawei.jhoa.fragment.WorkFragment;
import com.android.yawei.jhoa.ui.XCRoundImageView;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.yawei.android.appframework.controls.OnFragmentLoading;
import com.yawei.android.appframework.controls.TabPageControl;
import com.yawei.jhoa.mobile.R;
import com.yawei.zxing.client.CaptureActivity;
import com.yawei.zxing.client.QRCodeIntent;

import elink.mobile.im.activity.LoginActivity;
import elink.mobile.im.activity.MesCenterFragment;

/**
 * Created by Yusz on 2018-6-11.
 */

public class MainActivityV2 extends BaseFragmentActivity implements OnFragmentLoading,View.OnClickListener {

    private TabPageControl tabPageControl;
    public static MainActivityV2 activity;
    private XCRoundImageView imghead;
    private LinearLayout linHeader;
    private LinearLayout linemail;
    private LinearLayout linScan;//扫一扫
    private LinearLayout linvoice;
    private TextView txtTitle;
    private TextView username;

    public static final String TAG_EXIT = "exit";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
                intent = new Intent(MainActivityV2.this,JhoaLoginActivity.class);
                startActivity(intent);
            }
        }
    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainv2);
        try {
            activity = this;
            SysExitUtil.AddActivity(this);
            InitTab();
            InitView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void InitView()throws Exception{
        imghead = (XCRoundImageView)findViewById(R.id.imghead);
        linHeader = (LinearLayout)findViewById(R.id.linHeader);
        txtTitle = (TextView)findViewById(R.id.title);
        username = (TextView) findViewById(R.id.username);
        username.setText(SpUtils.getString(MainActivityV2.this,Constants.CUTOVER_DISPLAY_NAME,""));
        linHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityV2.this,MyPersonalCenterActivity.class);
                startActivity(intent);
            }
        });
        linemail = (LinearLayout)findViewById(R.id.linemail);
        linemail.setOnClickListener(this);
        linScan = (LinearLayout)findViewById(R.id.linScan);
        linScan.setOnClickListener(this);
        linvoice = (LinearLayout)findViewById(R.id.linvoice);
        linvoice.setOnClickListener(this);
        if(!Constants.OpenBaiduViceo){
            linvoice.setVisibility(View.GONE);
        }
    }
    private void InitTab()throws Exception{
        tabPageControl = (TabPageControl)findViewById(R.id.tabControl);
        String []strTabName = getResources().getStringArray(R.array.mainv2option);
        for (int i=0;i<strTabName.length;i++){
            String[] strarr = strTabName[i].split("[|]");
            if(strarr[0].equals("sms")){
                ImmediateMessageFragemnt fragment1 = new ImmediateMessageFragemnt();
                tabPageControl.addTabItem(strarr[1], R.drawable.tab_icmainsms, fragment1);
            }else if(strarr[0].equals("oa")){
                WorkFragment fragment = new WorkFragment();
                tabPageControl.addTabItem(strarr[1], R.drawable.tab_icmainwork, fragment);
            }else if(strarr[0].equals("address")){
                AddressBookFragment fragment = new AddressBookFragment();
                tabPageControl.addTabItem(strarr[1], R.drawable.tab_icmainaddressbook, fragment);
            }else if(strarr[0].equals("self")){
                SelfSentreFragment fragment = new SelfSentreFragment();
                tabPageControl.addTabItem(strarr[1], R.drawable.tab_icmainpersion, fragment);
            }
        }
        tabPageControl.setCurrentTab(1);
        tabPageControl.setTabFillViewPort(true);
//		tabPageControl.setTabItemSelectedResource(R.drawable.daohangbg1);
        tabPageControl.setTabIconPosition(TabPageControl.TabIconPosition.TOP);

        tabPageControl.setTabTextFontSize(16f);
        tabPageControl.setTabPosition(TabPageControl.TabPosition.BOTTOM);
        tabPageControl.setTabPadding(5);
        tabPageControl.setTabTextColor(getResources().getColor(R.color.icontabunsel),getResources().getColor(R.color.icontabsel));
        tabPageControl.loadFragments(this, this.getSupportFragmentManager());
//		tabPageControl.setTabBackgroundColor(R.drawable.maintabbgk);
        tabPageControl.setTabBackground(R.drawable.maintabbgk);
        tabPageControl.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                String []strTabName = getResources().getStringArray(R.array.mainv2option);
                String[] strarr = strTabName[arg0].split("[|]");
                if(strarr[0].equals("sms")){
                    txtTitle.setText(strarr[1]);
                    txtTitle.setVisibility(View.VISIBLE);
                    linHeader.setVisibility(View.GONE);
                    linemail.setVisibility(View.GONE);
                    linScan.setVisibility(View.GONE);//扫一扫
                    linvoice.setVisibility(View.GONE);
                }else if(strarr[0].equals("oa")){
                    txtTitle.setVisibility(View.GONE);
                    linHeader.setVisibility(View.VISIBLE);
                    linemail.setVisibility(View.VISIBLE);
                    linScan.setVisibility(View.VISIBLE);//扫一扫
                    linvoice.setVisibility(View.VISIBLE);
                }else if(strarr[0].equals("address")){
                    txtTitle.setText(strarr[1]);
                    txtTitle.setVisibility(View.VISIBLE);
                    linHeader.setVisibility(View.GONE);
                    linemail.setVisibility(View.GONE);
                    linScan.setVisibility(View.GONE);//扫一扫
                    linvoice.setVisibility(View.GONE);
                }else if(strarr[0].equals("self")){
                    txtTitle.setText(strarr[1]);
                    txtTitle.setVisibility(View.VISIBLE);
                    linHeader.setVisibility(View.GONE);
                    linemail.setVisibility(View.GONE);
                    linScan.setVisibility(View.GONE);//扫一扫
                    linvoice.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.linemail:
                String paraJson = "{\"code\":\"10\"}";
                intent = new Intent(MainActivityV2.this, CordovaWebview.class);
                intent.putExtra("parameter",paraJson);
                startActivity(intent);
                break;
            case R.id.linScan:
                requestPermission(CaptureActivity.class);
                break;
            case R.id.linvoice:
                intent = new Intent(MainActivityV2.this, VoiceAssistantActivity.class);
                startActivity(intent);
                break;
        }
    }
    int num = -1;
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
            if (ActivityCompat.checkSelfPermission(MainActivityV2.this, aPermission) != PackageManager.PERMISSION_GRANTED) {
                flag = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityV2.this, aPermission)) {
                    Toast.makeText(MainActivityV2.this, "您已禁止所需要权限，需要重新开启",Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivityV2.this, permission, 10);
                }
            } else {
                flag &= flag;
            }
        }
        if (flag) {
            Intent intent = new Intent(MainActivityV2.this, activity);
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
        if (10 == requestCode) {
            //接收返回值
            if (data != null && !TextUtils.isEmpty(data.getStringExtra(QRCodeIntent.SCAN_RESULT))) {
                Toast.makeText(MainActivityV2.this, data.getStringExtra(QRCodeIntent.SCAN_RESULT),Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            Bitmap bitmap = SpUtils.getBitmap(getApplicationContext(),SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_LOGNAME, ""));
            if(bitmap != null){
                imghead.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Fragment getFragment(int position) {
        return null;
    }

}
