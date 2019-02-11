package com.android.yawei.jhoa.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.yawei.jhoa.Interface.DialogButClick;
import com.android.yawei.jhoa.mobile.JhoaLoginActivity;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.FileUtils;
import com.yawei.jhoa.mobile.R;

import java.io.File;

/**
 * TODO 构建统一的全局统一Dialog
 * class : GlobalDialog
 * author: Yusz
 * Date: 2018-11-16
 */

public class GlobalDialog {

    static AlertDialog finalAlertDialog =null;
    static final int clickbut1 = 1;
    static final int clickbut2 = 2;
    static DialogButClick mcallback;
    static MyHandler handler;
    /**
     *
     * @param context
     * @param title
     * @param msg
     * @param but1Name 确定
     * @param but2Name 取消  如果不传值没有此按钮
     * @param callback
     * @return
     * @throws Exception
     */
    public static AlertDialog ShowDialog(Activity context, String title, String msg, String but1Name , String but2Name
            , final DialogButClick callback){
        mcallback = callback;
        handler = new MyHandler();
        View layout = context.getLayoutInflater().inflate(R.layout.activity_dialog,(ViewGroup) context.findViewById(R.id.dialog));
        AlertDialog  alertDialog = null;
        TextView dialogtitle=(TextView)layout.findViewById(R.id.dialogtitle);
        dialogtitle.setText(title);
        TextView dialogtip=(TextView)layout.findViewById(R.id.dialogtip);
        dialogtip.setText(msg);
        dialogtip.setTextSize(18f);
        if(but2Name != null && !but2Name.equals("")){
            alertDialog = new AlertDialog.Builder(context).setPositiveButton(but1Name,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    handler.sendEmptyMessage(clickbut1);
                }
            }).setNegativeButton(but2Name, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.sendEmptyMessage(clickbut2);
                }
            }).create();
        }else{
            alertDialog = new AlertDialog.Builder(context).setPositiveButton(but1Name,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    handler.sendEmptyMessage(clickbut1);
                }
            }).create();
        }

        alertDialog.setView(layout,-1,-1,-1,-1);
        finalAlertDialog = alertDialog;
        return alertDialog;
    }
    public static AlertDialog ShowDialogByImage(Activity context, String title, String msg, String but1Name , String but2Name
            , final DialogButClick callback)throws Exception {
        View layout = context.getLayoutInflater().inflate(R.layout.activity_dialog_image,(ViewGroup) context.findViewById(R.id.dialog));
        AlertDialog  alertDialog = null;
        TextView dialogtitle=(TextView)layout.findViewById(R.id.dialogtitle);
        dialogtitle.setText(title);
        TextView dialogtip=(TextView)layout.findViewById(R.id.dialogtip);
        dialogtip.setText(msg);
        dialogtip.setTextSize(18f);
        if(but2Name != null && !but2Name.equals("")){
            alertDialog = new AlertDialog.Builder(context).setPositiveButton(but1Name,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    handler.sendEmptyMessage(clickbut1);
                }
            }).setNegativeButton(but2Name, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.sendEmptyMessage(clickbut2);
                }
            }).create();
        }else{
            alertDialog = new AlertDialog.Builder(context).setPositiveButton(but1Name,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    handler.sendEmptyMessage(clickbut1);
                }
            }).create();
        }

        alertDialog.setView(layout,-1,-1,-1,-1);
        finalAlertDialog = alertDialog;
        return alertDialog;
    }
    private static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case clickbut1:
                    mcallback.onClickCustomDialogBut1(finalAlertDialog);
                    break;
                case clickbut2:
                    mcallback.onClickCustomDialogBut2(finalAlertDialog);
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
