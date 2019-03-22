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
import com.callphone.test.SiteInfo;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
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

                SiteInfo siteInfo = new SiteInfo("http://hbxlpublics.oss-cn-shenzhen.aliyuncs.com/updates/doubleScreen105.apk", path, fileName, 3);

                DownFile downFile = new DownFile(siteInfo);

                downFile.startDown();
                installApk(mContext,path+File.separator+fileName);
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

    private void setStartEnd(HttpURLConnection conn, int startPos, int endPos) {
        conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
    }


    public static final String FILE = "file";
    public static final String START = "key";
    public static final String LENGTH = "len";

    boolean isFinished = false;


    int lengthTotal = 0;
    int sum = 0;

    //下载线程
    private Runnable runnableDownload = new Runnable() {
        @Override
        public void run() {
            MyLogUitls.print("runnableDownload start" + Thread.currentThread().getId());

            FileOutputStream fos = null;
            InputStream is = null;
            try {
                // 获得存储卡的路径
                java.net.URL url = new URL(apkUrl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Connection", "close");

                MyLogUitls.print("setConnectTimeout" + conn.getConnectTimeout());
                MyLogUitls.print("setReadTimeout" + conn.getReadTimeout());

                conn.setConnectTimeout(50 * 1000);
                conn.setReadTimeout(50 * 1000);
//

                if (sum != 0) {
                    setStartEnd(conn, sum, lengthTotal);
                }


                conn.connect();
                // 获取文件大小
                int length = conn.getContentLength();


                MyLogUitls.print(length + " LEN");

//               if(true){
//                   return;
//               }


                String mStrTotal = Formatter.formatFileSize(mContext, Long.valueOf(length));
                MyLogUitls.print("下载开始大小为：" + mStrTotal + "apk url:" + apkUrl + " version:");
                // 创建输入流
                is = conn.getInputStream();
                File file = new File(AppPathConfig.RootFile);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdirs();
                }


//                RandomAccessFile threadFile = new RandomAccessFile(
//                        AppPathConfig.RootFile+ File.separator +
//                                apkName + "temp", "rwd");
//                threadFile.seek(startPos);


                File apkFile = new File(AppPathConfig.RootFile, apkName + "temp");//临时文件


                if (lengthTotal == 0) {
                    lengthTotal = length;

                    if (apkFile.exists()) {
                        apkFile.delete();
                    }
                }

                fos = new FileOutputStream(apkFile, true);


                long time = System.currentTimeMillis();
                // 缓存
                byte buf[] = new byte[10240];

                // 写入到文件中
                for (; ; ) {
                    int numread = is.read(buf);
                    // 计算进度条位置
                    if (numread == -1) {
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

                            installApk(mContext, dest.getPath());

                        }
                        isFinished = true;
                        // 下载完成
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                    sum += numread;
                    fos.flush();

                    if (System.currentTimeMillis() - time > 1000) {
                        time = System.currentTimeMillis();
                        info = Formatter.formatFileSize(mContext, Long.valueOf(sum));
                        handler.post(runnable);
                    }


                    if (sum % 100000 == 0) {
                        throw new RuntimeException("报了个错");
                    }
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
//                isUpdate = false;


            } finally {
                close(is);
                close(fos);
            }
            if (!isFinished) {
                new Thread(runnableDownload).start();
            }
            MyLogUitls.print("runnableDownload end" + Thread.currentThread().getId());
        }
    };

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
