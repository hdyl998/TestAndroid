package com.callphone.test;

import android.os.Environment;

import com.callphone.myapplication.AppPathConfig;

public class DownTest1 {


    public static void main(String[] args) {

        String path = "C:\\";

        SiteInfo siteInfo = new SiteInfo("http://hbxlpublics.oss-cn-shenzhen.aliyuncs.com/updates/test.apk", path, "doubleScreen105.apk", 3);

        DownFile downFile = new DownFile(siteInfo);

        downFile.startDown();
    }
}
