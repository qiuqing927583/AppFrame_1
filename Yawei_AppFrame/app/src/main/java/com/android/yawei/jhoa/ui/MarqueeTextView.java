package com.android.yawei.jhoa.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/***************
 * textview 跑马灯，解决马灯只能在TextView处于焦点状态的时候，它才会滚动，对于实际的开发应用中很不实用，
为了是跑马灯无论在什么情况下都能跑起来，这里需要自定义一个TextView，它继承TextView，并且重写isFocuse()方法，让它永远返回true，
这样跑马灯效果就能一直的跑起来了。
 * @author Yusz
 *
 */
public class MarqueeTextView extends TextView{
	
	public MarqueeTextView(Context context) {  
        super(context);  
    }  
      
    public MarqueeTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public MarqueeTextView(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
    }  
      
    @Override  
    public boolean isFocused() {  
        return true;  
    }  
}
