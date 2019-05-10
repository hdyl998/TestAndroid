package com.callphone.myapplication.util;

import java.util.Properties;

/**
 * Note：None
 * Created by Liuguodong on 2019/1/18 10:56
 * E-Mail Address：986850427@qq.com
 */
public class ProxyUtils {





    public static void startProxy(String ip, String port){
        Properties pro=System.getProperties();
        // 设置http访问要使用的代理服务器的地址
        pro.setProperty("http.proxyHost",ip);
        // 设置http访问要使用的代理服务器的端口
        pro.setProperty("http.proxyPort",port);

        pro.setProperty("https.proxyHost", ip);
        pro.setProperty("https.proxyPort", port);
        // socks代理服务器的地址与端口
        pro.setProperty("socksProxyHost", ip);
        pro.setProperty("socksProxyPort", port);
    }

    public static void clearProxy(){

        Properties pro=System.getProperties();
        pro.remove("http.proxyHost");
        pro.remove("http.proxyPort");

        pro.remove("https.proxyHost");
        pro.remove("https.proxyPort");

        pro.remove("socksProxyHost");
        pro.remove("socksProxyPort");
    }
}
