package com.callphone.myapplication;

import android.os.DeadObjectException;
import android.os.DeadSystemException;

/**
 * Note：None
 * Created by Liuguodong on 2019/5/13 17:46
 * E-Mail Address：986850427@qq.com
 */
public class MyTest {
    public static void main(String[] args) {
        try {
            throw new ThreadDeath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("success");
    }
}
