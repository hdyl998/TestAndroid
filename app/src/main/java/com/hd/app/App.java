package com.hd.app;

import android.app.Application;

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
