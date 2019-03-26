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
import com.callphone.test.FileUtil;
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

        new Thread() {
            @Override
            public void run() {

                String path = AppPathConfig.BasePath;
                String fileName = "doubleScreen105.apk";
                String url = "http://hbxlpublics.oss-cn-shenzhen.aliyuncs.com/updates/test.apk";
                try {
                    MyThread thread = new MyThread(url, path + File.separator + fileName);
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


//        new Thread(runnableDownload).start();
    }

    String info;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            textView.setText(info);
        }
    };


    public interface CallBack{

    }


    public static class MyThread extends Thread {


        protected String url;               // 文件所在url
        protected long startPos;            // 分段传输的开始位置
        protected long endPos;              // 结束位置
        protected int threadID;             // 线程编号
        protected boolean downOver = false; // 下载完成标志
        protected boolean stop = false;     // 当前分段结束标志
        FileUtil fileUtil = null;           // 文件工具

        protected long progress;


        private long time = System.currentTimeMillis();

        public MyThread(String url, String fileName) throws IOException {
            super();
            this.url = url;
            File file = null;
            if ((file = new File(fileName)).exists()) {
                file.delete();
            }
            fileUtil = new FileUtil(fileName, startPos);
            this.fileName = fileName;
        }

        String fileName;


        @Override
        public void run() {
            long curTime;
            int loopCount = 0;

            int size = getFileSize(url);
            if (size <= 0) {
                LogUtil.log("Error fizeSize is error" + size);
                return;
            }
            startPos = 0;
            endPos = size;

            while (startPos < endPos && !stop) {
                try {
                    URL ourl = new URL(url);
                    HttpURLConnection httpConnection = (HttpURLConnection) ourl.openConnection();
                    String prop = "bytes=" + startPos + "-";
                    httpConnection.setRequestProperty("RANGE", prop); //设置请求首部字段 RANGE


                    LogUtil.log(prop);

                    InputStream input = httpConnection.getInputStream();
                    byte[] b = new byte[1024];
                    int bytes = 0;
                    while ((bytes = input.read(b)) > 0 && startPos < endPos && !stop) {

                        int count = fileUtil.write(b, 0, bytes);
                        progress += count;
                        startPos += count;
                    }

                    LogUtil.log("Thread" + threadID + " is done");
                    downOver = true;

                    installApk(App.getContext(), fileName);


                    LogUtil.log("startPos" + startPos + " endPos" + endPos);

                    break;


                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                curTime = System.currentTimeMillis();
                loopCount++;

                if (curTime - time > MAX_WAIT_TIME) {
                    LogUtil.log(String.format("结束下载,超时%d 开始下载时间 %d 结束时间%d", MAX_WAIT_TIME, time, curTime));
                    break;
                }

                if (loopCount > MAX_LOOP_TIME) {
                    LogUtil.log(String.format("结束下载,循环次数%d", loopCount));
                    break;
                }


            }

        }

        private static final int MAX_LOOP_TIME = 50;

        public static final int MAX_WAIT_TIME = 50000;


        public long getProgress() {
            return progress;
        }

        /**
         * 打印响应的头部信息
         *
         * @param conn
         */
        public void printResponseHeader(HttpURLConnection conn) {
            for (int i = 0; ; i++) {
                String fieldsName = conn.getHeaderFieldKey(i);
                if (fieldsName != null) {
                    LogUtil.log(fieldsName + ":" + conn.getHeaderField(fieldsName));
                } else {
                    break;
                }
            }
        }

        /**
         * 停止分段传输
         */
        public void setSplitTransStop() {
            stop = true;
        }

    }


    private static final int NOACCESS = -2; // 文件不可访问

    /**
     * 获取文件的大小
     *
     * @return
     */
    private static int getFileSize(String htmUrl) {
        int len = -1;
        try {
            URL url = new URL(htmUrl);
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
