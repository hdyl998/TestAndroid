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
import android.widget.LinearLayout;

public class ViewGroupA extends LinearLayout {

    public ViewGroupA(Context context) {
        super(context);
    }

    public ViewGroupA(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupA(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("付勇焜----->", "ViewGroupA dispatchTouchEvent ");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("付勇焜----->", "ViewGroupA onInterceptTouchEvent ");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("付勇焜----->", "ViewGroupA onTouchEvent ");
        return super.onTouchEvent(event);
    }
}