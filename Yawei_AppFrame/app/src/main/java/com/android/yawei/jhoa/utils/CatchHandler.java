package com.android.yawei.jhoa.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

/****捕捉异常*****/
public class CatchHandler implements UncaughtExceptionHandler{ 
	  
    private CatchHandler() { 
    } 

    public static CatchHandler getInstance() {

            return mCatchHandler; 
    } 

    private static CatchHandler mCatchHandler = new CatchHandler();

    private Context mContext; 


    @Override
    public void uncaughtException(Thread thread, Throwable ex) { 
            if (thread.getName().equals("main")) { 
                ToastException(thread, ex); 
                try { 
                       Thread.sleep(1000); 
                } catch (InterruptedException e) { 
                } 
                android.os.Process.killProcess(android.os.Process.myPid()); 
                System.exit(1); 
            } else { 
                handleException(thread, ex); 
            } 

    } 

    public void init(Context context) { 
        mContext = context; 
        Thread.setDefaultUncaughtExceptionHandler(this); 
    } 

    private void ToastException(final Thread thread, final Throwable ex) { 
        new Thread() { 
            @Override
            public void run() { 
            	SysExitUtil.FinishActivity();
            	StringBuilder builder = new StringBuilder(); 
                builder.append("At thread: ").append(thread.getName()) .append("\n"); 
                builder.append("Exception is :\n").append(ex.getMessage()); 
                
                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = root+"/jhoaMobile/Log/"+ GetSystemTime.GetSystemTimeYear2()+".txt";
                SysExitUtil.createPath(root+"/jhoaMobile/Log/");
                
                writeFileData(fileName, GetSystemTime.GetSystemDataTime()+"\n"+thread.getName()+"\n");
                writeFileData(fileName, ex.getMessage()+"\n");
                
                StringBuffer sb = new StringBuffer();      
                Writer writer = new StringWriter();      
                PrintWriter printWriter = new PrintWriter(writer);      
                ex.printStackTrace(printWriter);      
                Throwable cause = ex.getCause();      
                while (cause != null) {      
                    cause.printStackTrace(printWriter);      
                    cause = cause.getCause();      
                }      
                printWriter.close();      
                String result = writer.toString();      
                sb.append(result); 
                writeFileData(fileName, sb.toString()+"\n\n\n");

                //重启app
                Intent i = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(i);
                //杀掉原来的进程
                Looper.prepare();
                Looper.loop();
            } 
        }.start(); 
    } 

    private void handleException(final Thread thread, final Throwable ex) { 
         new AlertDialog.Builder(mContext).setMessage("ex").show();
    } 
    
	private void writeFileData(String fileName, String message) {
		
		 if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			 File file = new File(fileName);
			  
			 try { 
				 if( !file.exists()) {  
			          file.createNewFile();  
			      }
				 FileOutputStream fout = new FileOutputStream(file, true);
		          byte[] bytes = message.getBytes();  
		          fout.write(bytes);  
		          fout.close();  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
	
	     }
    }
}
