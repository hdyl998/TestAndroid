package com.hd.test.event;

/**
 * Note：None
 * Created by Liuguodong on 2019/7/24 10:50
 * E-Mail Address：986850427@qq.com
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("付勇焜---->", "MyView dispatchTouchEvent ");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("付勇焜---->", "MyView onTouchEvent "+event.getAction());
        boolean isTouch= super.onTouchEvent(event);
        Log.d("付勇焜---->", "MyView onTouchEvent "+isTouch);
        return isTouch;
    }
}