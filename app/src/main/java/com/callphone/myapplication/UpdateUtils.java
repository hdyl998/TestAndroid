package com.callphone.myapplication;

import java.io.Closeable;
import java.io.IOException;

/**
 * Note：None
 * Created by Liuguodong on 2019/3/27 11:35
 * E-Mail Address：986850427@qq.com
 */
public class UpdateUtils {

    /***
     * 安全关闭
     * @param closeables
     */
    public static void close(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
