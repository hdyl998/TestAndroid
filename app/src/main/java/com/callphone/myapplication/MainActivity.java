package com.callphone.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.widget.TextView;

import com.callphone.test.DownFile;
import com.callphone.test.DownTest1;
import com.callphone.test.LogUtil;
import com.callphone.test.SiteInfo;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    String apkUrl = "http://hbxlpublics.oss-cn-shenzhen.aliyuncs.com/updates/doubleScreenVersion2_2_2.apk";
    Context mContext;

    Handler handler = new Handler();
    String apkName = "apptest.apk";

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        textView = findViewById(R.id.text);


//        File dest = new File(AppPathConfig.RootFile, apkName);
//
//
//
//        installApk(mContext,dest.getPath());

        new Thread(){
            @Override
            public void run() {

                String path = AppPathConfig.BasePath;
                String fileName="doubleScreen105.apk";

                SiteInfo siteInfo = new SiteInfo("http://hbxlpublics.oss-cn-shenzhen.aliyuncs.com/updates/test.apk",

                        path, fileName, 1);

                DownFile downFile = new DownFile(siteInfo);

                downFile.startDown();
                installApk(mContext,path+File.separator+fileName);
            }
        }.start();


        new Thread(runnableDownload).start();
    }



    //下载线程
    private Runnable runnableDownload = new Runnable() {
        @Override
        public void run() {

        }
    };


    /**
     * 获取文件的大小
     *
     * @return
     */
    private long getFileSize() {
        int len = -1;
        try {
            URL url = new URL(siteInfo.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "custom");

            int respCode = connection.getResponseCode();
            if (respCode >= 400) {
                LogUtil.log("Error Code : " + respCode);
                return NOACCESS; // 代表文件不可访问
            }

            String header = null;
            for (int i = 1; ; i++) {
                header = connection.getHeaderFieldKey(i);
                if (header != null) {
                    if ("Content-Length".equals(header)) {
                        len = Integer.parseInt(connection.getHeaderField(header));
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (MalformedURLException e) {
            LogUtil.log(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LogUtil.log(e.getMessage());
            e.printStackTrace();
        }

        LogUtil.log("文件大小为" + len);
        return len;
    }

    private static void installApk(Context context, String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        Uri uri = Uri.fromFile(new File(path));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void close(Closeable is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
