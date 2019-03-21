package com.callphone.myapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by liugd on 2019/3/21.
 */

public class App extends Application {


   static App app;
    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }

    public static Application getContext() {
        return app;
    }
}
