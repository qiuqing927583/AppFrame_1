package com.android.yawei.jhoa.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetSystemTime {

	/**************
	 * 获取系统当前时间    13:1:1
	 * @return
	 */
  	public static String GetSystemDataTime(){
//  		SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
  		SimpleDateFormat formatter  = new SimpleDateFormat("HH:mm:ss");
  		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
  		String time = formatter.format(curDate);
  		return time;
  	}
  	/***********
     * 获取系统当前时间,2015年1月1日  13:1:1
     * @return
     */
  	public static String GetSystemTimeYear(){
  		SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
  		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
  		String time = formatter.format(curDate);
  		return time;
  	}
    /***********
     * 获取系统当前时间,2015-1-1 13:1:1
     * @return
     */
  	public static String GetSystemTimeYear1(){
  		SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
  		String time = formatter.format(curDate);
  		return time;
  	}
    /************
     * 获取系统当前时间,2015-1-1
     * @return
     */
  	public static String GetSystemTimeYear2(){
  		SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd");
  		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
  		String time = formatter.format(curDate);
  		return time;
  	}
  	/**********
  	 * 把传进连的时间转换成2015年1月1日  13:1:1格式之后，然后回去1-1 13：1
	 * @param time
	 * @return
	 */
  	public static String GetSystemTimeDay(String time){
  		DateFormat fmt =new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
  		SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
  		String newtime = "";
  		Date date = null;
  		try {
  			date = fmt.parse(time);
  			newtime = sdf2.format(date);
  		} catch (ParseException e) {
  			e.printStackTrace();
  		}
  		return newtime;
  	}
  	
}
