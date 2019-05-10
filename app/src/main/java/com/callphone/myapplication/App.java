package com.callphone.myapplication;

import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.callphone.myapplication.util.ProxyUtils;
import com.callphone.myapplication.util.SpConsts;
import com.callphone.myapplication.util.SpUtils;

/**
 * Created by liugd on 2019/3/21.
 */

public class App extends Application {


    static App mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ProxyDialog.enableProxy();
    }

    public static Application getContext() {
        return mContext;
    }

}
