package com.android.yawei.jhoa.mobile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.photoview.bm.library.PhotoView;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.tbs.webview.X5WebView;
import com.yawei.jhoa.mobile.R;
import com.yawei.pdf.pdfviewer.PDFView;
import com.yawei.pdf.pdfviewer.listener.OnLoadCompleteListener;
import com.yawei.pdf.pdfviewer.listener.OnPageChangeListener;
import com.yawei.pdf.pdfviewer.listener.OnPageErrorListener;
import com.yawei.pdf.pdfviewer.scroll.DefaultScrollHandle;
import com.yawei.pdf.pdfviewer.util.FitPolicy;

import java.io.File;

/**
 * TODO 打开附件
 * Created by Yusz on 2018-7-4.
 */

public class OpenAttachmentActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener,TbsReaderView.ReaderCallback {

    private PhotoView photoView;
    private LinearLayout imgBack;
    private RelativeLayout tencentView;//用来装载腾讯view
    private X5WebView tencentweb;
    private TextView txt_fileName;
    private TbsReaderView mTbsReaderView;//腾讯文件加载view
    private PDFView pdfView;
    private String attType;//附件类型：0图片，1pdf文件,2doc,docx,xml等office文件，3ofd
    private String filePath;//文件路径

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_openattachment);
        try {
            attType = getIntent().getStringExtra("type");
            filePath = getIntent().getStringExtra("filePath");
            InitView();
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView()throws Exception{
        imgBack = (LinearLayout) findViewById(R.id.LinTopBack);
        imgBack.setOnClickListener(onClickListener);
        photoView = (PhotoView)findViewById(R.id.picPhotoview);
        photoView.enable();
        pdfView = (PDFView)findViewById(R.id.pdfview);
        txt_fileName = (TextView)findViewById(R.id.fileName);
        String name = filePath.substring(filePath.lastIndexOf("/")+1,filePath.length());
        txt_fileName.setText(name);
        tencentView = (RelativeLayout)findViewById(R.id.tencentView);
        tencentweb = (X5WebView)findViewById(R.id.tencentweb);
    }
    private void InitData()throws Exception{
        if("0".equals(attType)){
            Bitmap bitmap = SysExitUtil.getLoacalBitmap(filePath); //从本地取图片(在cdcard中获取)
            photoView.setVisibility(View.VISIBLE);
            photoView.setImageBitmap(bitmap);
        }else if("1".equals(attType)){
            if(new File(filePath).exists()){
                try {
                    pdfView.setBackgroundColor(getResources().getColor(R.color.pdfviewground));
                    pdfView.fromFile(new File(filePath))
                            .defaultPage(0)
                            .onPageChange(this)
                            .enableAnnotationRendering(true)
                            .onLoad(this)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .spacing(10) // in dp
                            .onPageError(this)
                            .pageFitPolicy(FitPolicy.WIDTH)
                            .load();
                    pdfView.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else if("2".equals(attType)){
            String fileSuffix = filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length());
            if(fileSuffix.equals("xml")||fileSuffix.equals("html")||fileSuffix.equals("htm")||fileSuffix.equals("js")){
                tencentweb.setVisibility(View.VISIBLE);
                WebSettings webSetting = tencentweb.getSettings();
                webSetting.setAllowFileAccess(true);
                webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                webSetting.setSupportZoom(true);
                webSetting.setBuiltInZoomControls(true);
                webSetting.setUseWideViewPort(true);
                webSetting.setSupportMultipleWindows(false);
                webSetting.setAppCacheEnabled(true);
                webSetting.setDomStorageEnabled(true);
                webSetting.setJavaScriptEnabled(true);
                webSetting.setGeolocationEnabled(true);
                webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
                webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
                webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
                webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
                tencentweb.loadUrl("file:///"+filePath);
//                getWindow().setFormat(PixelFormat.TRANSLUCENT);
//                tencentweb.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);

            }else {
                tencentView.setVisibility(View.VISIBLE);
                mTbsReaderView = new TbsReaderView(this, this);
                tencentView.addView(mTbsReaderView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if(new File(filePath).exists()){
                    Bundle bundle = new Bundle();
                    bundle.putString("filePath", filePath);
                    bundle.putString("tempPath", FileUtils.GetSystemFilePath()+"/plugin/");
                    boolean result = mTbsReaderView.preOpen(filePath.substring(filePath.lastIndexOf(".") + 1), false);
                    if (result) {
                        mTbsReaderView.openFile(bundle);
                    }
                }
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()){
                    case R.id.LinTopBack:
                        finish();
                        break;
                    default:
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onPageError(int page, Throwable t) {
        Toast.makeText(OpenAttachmentActivity.this,"打开文件失败！",Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
//        AnimationUtil.with().bottomMoveToViewLocation(topRelative, 500);

    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTbsReaderView != null){
            mTbsReaderView.onStop();
        }
    }
}
