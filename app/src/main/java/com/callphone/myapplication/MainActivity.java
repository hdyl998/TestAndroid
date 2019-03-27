package com.callphone.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.widget.TextView;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    String apkUrl = "http://hbxlpublics.oss-cn-shenzhen.aliyuncs.com/updates/test.apk";
    Context mContext;

    Handler handler = new Handler();
    String apkName = "apptest.apk";

    TextView textView;

    private final static int DOWNLOAD_TYPE_AUTO = 0;//下载方式自动
    private final static int DOWNLOAD_TYPE_NORMAL = 1; //下载方式,普通下载
    private final static int DOWNLOAD_TYPE_DUANDIAN = 2;//下载方式,断点续传


    private int download_type = DOWNLOAD_TYPE_AUTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        textView = findViewById(R.id.text);

        new DownloadThread(new DownloadListener() {
            @Override
            public void onSuccess(DownLoadInfo info) {
                MyLogUitls.print("下载完成");
                MyLogUitls.print("runnableDownload end" + info);
            }

            @Override
            public void onError(DownLoadInfo info) {
                MyLogUitls.print("download error" + info.text);
                MyLogUitls.print("runnableDownload end" + info);
            }
        }).start();
    }


    interface DownloadListener {
        void onSuccess(DownLoadInfo info);

        void onError(DownLoadInfo info);
    }

    /**
     * 获取url的文件的大小
     * -2表示错误 -1为未知
     *
     * @return
     */
    public static long getUrlFileSize(String htmUrl) {
        long len = -2;
        try {
            URL url = new URL(htmUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "custom");

            int respCode = connection.getResponseCode();
            if (respCode >= 400) {
                MyLogUitls.print("Error Code : " + respCode);
                return len; // 代表文件不可访问
            }
            len = connection.getContentLength();

            MyLogUitls.print("文件大小为 getContentLength" + len);
//            String header = null;
//            for (int i = 1; ; i++) {
//                header = connection.getHeaderFieldKey(i);
//                if (header != null) {
//                    if ("Content-Length".equals(header)) {
//                        len = Long.parseLong(connection.getHeaderField(header));
//                        break;
//                    }
//                } else {
//                    break;
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyLogUitls.print("文件大小为" + len);
        return len;
    }

    /***
     * 断点下载
     */
    private class DownloadThread extends Thread {


        private String fullPath;
        private File aimFile;

        protected long startPos = 0;            // 分段传输的开始位置
        protected long endPos;              // 结束位置
        protected boolean downOver = false; // 下载完成标志
        protected boolean stop = false;     // 当前分段结束标志
        protected FileUtil fileUtil;           // 文件工具

        protected long progress;
        private long time = System.currentTimeMillis();

        private static final int MAX_LOOP_TIME = 80;//重试80次,如果中断,则下载失败

        public static final int MAX_WAIT_TIME = 15 * 60 * 1000;//15分钟
        // 普通下载重试10次
        private final static int MAX_NORMAL_RETRY_TIME = 10;

        private static final String TAG = "DownloadThread";

        private DownloadListener listener;

        DownloadThread(DownloadListener listener) {
            init();
            this.listener = listener;
        }


        private void init() {
            this.fullPath = AppPathConfig.RootFile + File.separator + apkName;
            if ((aimFile = new File(fullPath)).exists()) {
                aimFile.delete();
                MyLogUitls.print(TAG, "文件存在,删除!" + fullPath);
            }
        }

        @Override
        public void run() {
            long size = getUrlFileSize(apkUrl);

            switch (download_type) {
                case DOWNLOAD_TYPE_AUTO:

                    if (size == -2) {//异常
                        MyLogUitls.print(TAG, "Error fizeSize is error 文件不可访问" + size);
                        listener.onError(new DownLoadInfo("文件不可访问"));
                    } else if (size == -1) {//表示大小未知,用普通下载方式
                        MyLogUitls.print(TAG, "采用普通下载");
                        downLoadNormal();
                    } else {//大于0正常,用断点续传方式
                        MyLogUitls.print(TAG, "采用断点续传下载");
                        endPos = size;
                        downLoadWithCutPoint();
                    }
                    break;
                case DOWNLOAD_TYPE_DUANDIAN:
                    MyLogUitls.print(TAG, "采用断点续传下载");
                    endPos = size;
                    downLoadWithCutPoint();
                    break;
                case DOWNLOAD_TYPE_NORMAL:
                    MyLogUitls.print(TAG, "采用普通下载");
                    downLoadNormal();
                    break;
            }


        }


        //普通方式下载
        private void downLoadNormal() {
            int loopCount = 0;
            String failDesc = null;
            int length = 0;
            while (loopCount < MAX_NORMAL_RETRY_TIME) {
                progress = 0;
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    // 获得存储卡的路径
                    java.net.URL url = new URL(apkUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Connection", "close");
                    conn.setConnectTimeout(30 * 1000);
                    conn.setReadTimeout(30 * 1000);

                    conn.connect();
                    // 获取文件大小
                    endPos = length = conn.getContentLength();
                    String mStrTotal = Formatter.formatFileSize(mContext, Long.valueOf(length));
                    MyLogUitls.print("下载开始普通下载大小为：" + mStrTotal + "apk url:" + apkUrl);

                    // 创建输入流
                    is = conn.getInputStream();

                    File file = new File(AppPathConfig.RootFile);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File apkFile = new File(AppPathConfig.RootFile, apkName + "temp");//临时文件
                    fos = new FileOutputStream(apkFile);
                    // 缓存
                    byte buf[] = new byte[1024 * 4];

                    long recordTime = System.currentTimeMillis();
                    // 写入到文件中
                    for (; ; ) {
                        int numread = is.read(buf);
                        // 计算进度条位置
                        if (numread == -1) {
                            MyLogUitls.print(failDesc = "下载完成");
                            if (apkFile.exists()) {
                                apkFile.renameTo(aimFile);
                            }
                            downOver = true;
                            // 下载完成
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                        progress += numread;
                        if (System.currentTimeMillis() - recordTime > 500) {
                            recordTime = System.currentTimeMillis();
                            MyLogUitls.print(TAG, String.format("progress %.2f 当前%d 总%d", 100f * progress / endPos, progress, endPos));
                        }
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    loopCount++;
                } finally {
                    UpdateUtils.close(fos, is);
                }
                if (downOver) {
                    break;
                }

                if (loopCount > MAX_NORMAL_RETRY_TIME) {
                    MyLogUitls.print(failDesc = String.format("结束下载,循环次数%d", loopCount));
                    break;
                }
                MyLogUitls.print("继续下载 已耗时(秒)" + ((System.currentTimeMillis() - time) / 1000) + " loopCount " + loopCount);
            }
            int costSeconds = (int) ((System.currentTimeMillis() - time) / 1000);
            DownLoadInfo downLoadInfo = new DownLoadInfo(length, aimFile.length(), costSeconds, loopCount);
            downLoadInfo.setXuchuan(false);
            downLoadInfo.setSuccess(downOver);
            downLoadInfo.setText(failDesc);
            if (downOver) {
                listener.onSuccess(downLoadInfo);
            } else {
                listener.onError(downLoadInfo);
            }
        }

        //断点续传
        private void downLoadWithCutPoint() {
            try {
                fileUtil = new FileUtil(fullPath, startPos);
            } catch (IOException e) {
                e.printStackTrace();
                MyLogUitls.print(TAG, e.toString());
            }
            long curTime;
            int loopCount = 0;

            String failDesc = null;
            while (startPos < endPos && !stop) {
                InputStream input = null;
                try {
                    URL ourl = new URL(apkUrl);
                    HttpURLConnection httpConnection = (HttpURLConnection) ourl.openConnection();
                    httpConnection.setConnectTimeout(10000);
                    httpConnection.setReadTimeout(10000);
                    String prop = "bytes=" + startPos + "-";
                    httpConnection.setRequestProperty("RANGE", prop); //设置请求首部字段 RANGE
                    MyLogUitls.print(prop);

                    input = httpConnection.getInputStream();
                    byte[] b = new byte[1024];
                    int bytes = 0;
                    long recordTime = System.currentTimeMillis();

                    while ((bytes = input.read(b)) > 0 && startPos < endPos && !stop) {
                        int count = fileUtil.write(b, 0, bytes);
                        progress += count;
                        startPos += count;
                        if (System.currentTimeMillis() - recordTime > 500) {
                            recordTime = System.currentTimeMillis();
                            MyLogUitls.print(TAG, String.format("progress %.2f 当前%d 总%d", 100f * progress / endPos, progress, endPos));
                        }
                    }
                    downOver = true;
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    UpdateUtils.close(input);
                }
                curTime = System.currentTimeMillis();
                loopCount++;
                if (curTime - time > MAX_WAIT_TIME) {
                    MyLogUitls.print(failDesc = String.format("结束下载,超时%d 开始下载时间 %d 结束时间%d", MAX_WAIT_TIME, time, curTime));
                    break;
                }
                if (loopCount > MAX_LOOP_TIME) {
                    MyLogUitls.print(failDesc = String.format("结束下载,循环次数%d", loopCount));
                    break;
                }
                MyLogUitls.print("继续下载 已耗时(秒)" + ((curTime - time) / 1000) + " loopCount " + loopCount);
            }
            //关闭文件读写
            UpdateUtils.close(fileUtil);
            int costSeconds = (int) ((System.currentTimeMillis() - time) / 1000);
            DownLoadInfo downLoadInfo = new DownLoadInfo(endPos, aimFile.length(), costSeconds, loopCount);
            downLoadInfo.setXuchuan(true);
            downLoadInfo.setSuccess(downOver);
            if (downOver) {
                MyLogUitls.print(failDesc = "下载成功!");
                downLoadInfo.setText(failDesc);
                listener.onSuccess(downLoadInfo);
            } else {
                downLoadInfo.setText(failDesc);
                MyLogUitls.print("下载失败!");
                listener.onError(downLoadInfo);
            }

        }


    }

    private static class DownLoadInfo {
        public boolean isSuccess;
        public int loopCount;
        public int costSeconds;//花费时间(秒)
        public long serverLen;//服务器上的文件大小
        public long actualLen;//实际大小
        public boolean isXuchuan;
        public String text;

        public DownLoadInfo(String text) {
            this.isSuccess = false;
            this.text = text;
        }


        public DownLoadInfo(long serverLen, long actualLen, int costSeconds, int loopCount) {
            this.serverLen = serverLen;
            this.actualLen = actualLen;
            this.costSeconds = costSeconds;
            this.loopCount = loopCount;
        }

        public DownLoadInfo() {

        }


        public void setXuchuan(boolean xuchuan) {
            isXuchuan = xuchuan;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        /***
         * 是否文件可用
         * @return
         */
        public boolean isFileSizeOK() {
            if (isSuccess) {
                return serverLen == actualLen;
            }
            return false;
        }

        @Override
        public String toString() {
            return "DownLoadInfo{" +
                    "isSuccess=" + isSuccess +
                    ", loopCount=" + loopCount +
                    ", costSeconds=" + costSeconds +
                    ", serverLen=" + serverLen +
                    ", actualLen=" + actualLen +
                    ", isXuchuan=" + isXuchuan +
                    ", text='" + text + '\'' +
                    ", isFileSizeOK=" + isFileSizeOK() +
                    '}';
        }
    }


    /***
     * 随机文件读写器
     */
    public static class FileUtil implements Closeable {
        private RandomAccessFile file;
        private long startPos; // 文件存储的起始位置

        public FileUtil(String fileName, long startPos) throws IOException {
            file = new RandomAccessFile(fileName, "rw");
            this.startPos = startPos;
            file.seek(startPos);
        }

        public int write(byte[] data, int start, int len) {
            int res = -1;
            try {
                file.write(data, start, len);
                res = len;
            } catch (IOException e) {
                MyLogUitls.print(e.getMessage());
                e.printStackTrace();
            }
            return res;
        }

        @Override
        public void close() throws IOException {
            file.close();
        }
    }
}
