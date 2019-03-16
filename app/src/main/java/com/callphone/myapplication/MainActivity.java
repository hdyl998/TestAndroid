package com.callphone.myapplication;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    String apkUrl = "http://hbxlpublics.oss-cn-shenzhen.aliyuncs.com/updates/20190316.apk";
    Context mContext;

    Handler handler = new Handler();
    String apkName = "apptest";

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        textView = findViewById(R.id.text);




        new Thread(runnableDownload).start();
    }

    String info;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            textView.setText(info);
        }
    };


    //下载线程
    private Runnable runnableDownload = new Runnable() {
        @Override
        public void run() {
            MyLogUitls.print("runnableDownload start");
            try {
                // 获得存储卡的路径
                java.net.URL url = new URL(apkUrl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("Connection", "close");

                MyLogUitls.print("setConnectTimeout" + conn.getConnectTimeout());
                MyLogUitls.print("setReadTimeout" + conn.getReadTimeout());
//                conn.setConnectTimeout(10 * 1000);
//                conn.setReadTimeout(10 * 1000);
                conn.connect();
                // 获取文件大小
                int length = conn.getContentLength();
                String mStrTotal = Formatter.formatFileSize(mContext, Long.valueOf(length));
                MyLogUitls.print("下载开始大小为：" + mStrTotal + "apk url:" + apkUrl + " version:");
                // 创建输入流
                InputStream is = conn.getInputStream();
                File file = new File(AppPathConfig.RootFile);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdirs();
                }
                File apkFile = new File(AppPathConfig.RootFile, apkName + "temp");//临时文件
                FileOutputStream fos = new FileOutputStream(apkFile);

                int sum = 0;

                long time = System.currentTimeMillis();
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                for (; ; ) {
                    int numread = is.read(buf);
                    // 计算进度条位置
                    if (numread <= 0) {
                        MyLogUitls.print("下载完成");
                        if (apkFile.exists()) {
                            File dest = new File(AppPathConfig.RootFile, apkName);
                            if (dest.exists()) {
                                dest.delete();
                            }
                            apkFile.renameTo(dest);
//                            //存起来，已经下载了
//                            SpUtils.putString(mContext, FILE_NAME, KEY_CONFIG_NAME, version);
//                            //需要安装的版本
//                            SpUtils.putString(mContext, FILE_NAME, KEY_NEED_INSTALL, version);
//                            //安装线程
//                            new Thread(runnableInstallApk).start();

                            info = "下载完成";
                            handler.post(runnable);

                        }
                        // 下载完成
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                    sum += numread;


                    if (System.currentTimeMillis()-time  > 1000) {
                        time = System.currentTimeMillis();
                        info = Formatter.formatFileSize(mContext, Long.valueOf(sum));
                        handler.post(runnable);
                    }
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
//                isUpdate = false;
            }
            MyLogUitls.print("runnableDownload end");
        }
    };
}
