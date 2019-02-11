package com.android.yawei.jhoa.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.ui.MyWebChromeClient;
import com.baidu.mapapi.map.Text;
import com.yawei.jhoa.mobile.R;

/**
 * TODO ofd在线预览
 * class description: OfficeWebview
 * author: Yusz
 * Date: 2018-12-25
 */
public class OfficeWebview extends BaseActivity {

    private String title;
    private String url;
    private LinearLayout linBack;
    private TextView txtTitle;
    private WebView webview;

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_officewebview);
        try {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            InitView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void InitView()throws Exception{
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        linBack = (LinearLayout)findViewById(R.id.LinTopBack);

        if(!TextUtils.isEmpty(title)){
            txtTitle.setText(title);
        }
        linBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webview = (WebView)findViewById(R.id.office_webview);
        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setDisplayZoomControls(true);
        //解决缓存问题 (不使用缓存，只从网络获取数据.)
        webview.getSettings().setCacheMode(webview.getSettings().LOAD_NO_CACHE);
        //设置总动适应屏幕宽度
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setBuiltInZoomControls(true);//设置是否出现缩放工具
        //开启 DOM 存储功能
        webview.getSettings().setDomStorageEnabled(true);
        //加载需要显示的网页
        webview.loadUrl(url);
    }
}
