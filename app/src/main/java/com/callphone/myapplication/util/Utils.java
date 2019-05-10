package com.callphone.myapplication.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.callphone.myapplication.MyLogUitls;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Note：None
 * Created by Liuguodong on 2019/5/10 15:14
 * E-Mail Address：986850427@qq.com
 */
public class Utils {

    /***
     * 杀死APP并重启
     * @param mContext
     */
    public static void killSelfAndRestart(Context mContext) {
        Intent intent = mContext.getPackageManager()
                .getLaunchIntentForPackage(mContext.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(mContext, -1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager almgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (almgr != null) {
            almgr.set(AlarmManager.RTC, System.currentTimeMillis() + 50, restartIntent);
        } else {
            MyLogUitls.print(TAG, "get alarm_service return null");
        }
        MyLogUitls.print(TAG, "to kill self");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private static final String TAG = "Utils";



    /***
     * 关闭
     * @param closeable
     */
    public static void quietlyClose(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String isNetworkOnline() {

        String result;
        BufferedReader input = null;
        try {
            URL url = new URL("http://api2.miyu51.com/index.php?s=index/index/home/");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(6000);
            httpConn.setUseCaches(false);
            httpConn.setReadTimeout(6000);
            int responseCode = httpConn.getResponseCode();

            result=inputStreamToString(httpConn.getInputStream());


            MyLogUitls.print(TAG,responseCode+"");
            MyLogUitls.print(TAG,result);

            MyLogUitls.print(TAG,(responseCode < HttpURLConnection.HTTP_BAD_REQUEST)+"");

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Utils.quietlyClose(input);
        }
        return "error";
    }


    /**
     * 方法名: inputStreamToString
     * <p/>
     * 功能描述:InputSteam转String
     *
     * @param inputStream 输入流
     * @return 类型String:转换内容
     * <p/>
     * </br>throws
     */
    public static String inputStreamToString(InputStream inputStream) {
        OutputStream baos = new ByteArrayOutputStream(1024);
        String data = "";
        try {
            byte[] bytes = new byte[1024];
            while (true) {
                int numread = inputStream.read(bytes);
                if (numread <= 0) {
                    break;
                }
                baos.write(bytes, 0, numread);
            }
            data = baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            quietlyClose(inputStream);
            quietlyClose(baos);
        }
        return data;
    }
}
