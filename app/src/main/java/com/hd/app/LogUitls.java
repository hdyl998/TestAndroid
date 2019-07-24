package com.hd.app;

import android.util.Log;

/**
 * Note：None
 * Created by Liuguodong on 2019/4/25 17:42
 * E-Mail Address：986850427@qq.com
 */
class LogUitls {


    public static void print(String onReceivedError, String s) {
        if(s==null){
            s="empty";
        }
        Log.e(onReceivedError,s);
    }
}
