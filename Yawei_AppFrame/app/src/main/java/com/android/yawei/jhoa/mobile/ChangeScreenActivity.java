package com.android.yawei.jhoa.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.utils.Constants;
import com.yawei.jhoa.mobile.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ChangeScreenActivity extends Activity {
    /**
     * 屏幕横竖屏切换，和生命周期管理
     */

    private TextView text_screen;
    private int index=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape_settings);
        text_screen = (TextView) findViewById(R.id.text_screen);
        //获取当前的时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        text_screen.setText("onCreate" + time);
        //给TextView设置点击事件清除文字
        text_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_screen.setText("重新来！");
            }
        });
    }


    //改变横竖屏切换的方法
    public void changeScreen(View view) {
//        /**
////         * int ORIENTATION_PORTRAIT = 1;  竖屏
////         * int ORIENTATION_LANDSCAPE = 2; 横屏
////         */
////        //获取屏幕的方向  ,数值1表示竖屏，数值2表示横屏
////        int screenNum = getResources().getConfiguration().orientation;
////        //（如果竖屏）设置屏幕为横屏
////        if (screenNum == 1) {
////            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
////            getResources().getConfiguration().orientation=0;
////            //设置为置屏幕为竖屏
////        } else {
////            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
////            getResources().getConfiguration().orientation=1;
////        }
        Configuration configuration=getResources().getConfiguration();
        if(configuration.orientation==Configuration.ORIENTATION_LANDSCAPE){
            //当前是横屏，需要更改为竖屏
            ChangeScreenActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(configuration.orientation==Configuration.ORIENTATION_PORTRAIT){
            //当前是竖屏，需要更改为横屏
            ChangeScreenActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }

//    //屏幕方向发生改变的回调方法
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
////
////        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
////            text_screen.setText("当前屏幕为横屏");
////            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
////        } else {
////            text_screen.setText("当前屏幕为竖屏");
////            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
////        }
////        super.onConfigurationChanged(newConfig);
////        Log.e("TAG", "onConfigurationChanged");
////        //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  //设置横屏
//        super.onConfigurationChanged(newConfig);
//        String screen=newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE?"横屏":"竖屏";
//        Toast.makeText(this,"当前屏幕的状态是:"+screen,Toast.LENGTH_SHORT).show();
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        text_screen.append("\n onStart");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        text_screen.append("\n onRestart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        text_screen.append("\n onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        text_screen.append("\n onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        text_screen.append("\n onStop");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        text_screen.append("\n onDestroy");
//    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "竖屏", Toast.LENGTH_SHORT).show();
            // 加入横屏要处理的代码
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 加入竖屏要处理的代码
            Toast.makeText(this, "横屏", Toast.LENGTH_SHORT).show();
        }
    }

}
