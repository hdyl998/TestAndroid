package com.hd.test.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.hd.R;

/**
 * Note：https://www.cnblogs.com/fuly550871915/p/4983682.html
 * Created by Liuguodong on 2019/7/24 10:47
 * E-Mail Address：986850427@qq.com
 */
public class TestEventActvity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);

        //在ViewGroup中，有下面三个方法：
        //（1）dispatchTouchEvent     该方法用来分发事件，一般不会重写这个方法
        //（2）onInterceptTouchEvent  用来拦截事件
        //（3）onTouchEvent           用来处理事件，这个方法应该大家很常见了吧
        //
        //而View中，只有两个方法，即：
        //（1）dispatchTouchEvent     该方法用来分发事件，一般不会重写这个方法
        //（2）onTouchEvent           用来处理事件，这个方法应该大家很常见了吧
    }
}
