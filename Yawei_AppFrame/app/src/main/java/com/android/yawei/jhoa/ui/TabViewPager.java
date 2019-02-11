package com.android.yawei.jhoa.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * TODO
 * class description: TabViewPager
 * author: Yusz
 * Date: 2018-12-18
 */
public class TabViewPager extends ViewPager {

    private boolean isCanScroll = false;//false是可以滚动，true禁止滚动

    public TabViewPager(Context context) {
        super(context);
    }
    public TabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //设置是否可以滚动
    public void setNoScroll(boolean noScroll) {
        this.isCanScroll = noScroll;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanScroll){
            return false;
        }else{
            return super.onTouchEvent(arg0);
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll){
             return false;
        }else{
            return super.onInterceptTouchEvent(arg0);
        }
    }
}
