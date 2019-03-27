package com.callphone.myapplication;

import android.util.Log;

/**
 * Created by liugd on 2019/3/16.
 */

public class MyLogUitls {
    public static void print(String s) {
        Log.e(TAG,s);
    }

    public static void print(String tag,String s) {
        Log.e(tag,s);
    }

    private static final String TAG = "MyLogUitls";
}
