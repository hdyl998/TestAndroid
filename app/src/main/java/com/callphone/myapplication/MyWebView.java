package com.callphone.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Date:2017/11/7 14:00
 * Author:liugd
 * Modification:
 * ................
 * 佛祖保佑，永无BUG
 **/

public class MyWebView extends WebView {


    private final static String TAG = MyWebView.class.getSimpleName();
    public final static String NATIVE_API = "nativeApi";

    private MyWebChromeClient chromeClient;

    //当前页是否能回退
    boolean isCurentPageCanGoBack = false;




    public MyWebView(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initViews();
    }

    @TargetApi(21)
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        initViews();
    }




    private WebViewProgressBar progressBar;// 等待对话框
    private Context mContext;


    private void initViews() {


        progressBar = new WebViewProgressBar(mContext);
        this.addView(progressBar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
        WebSettings webSettings = getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);        //支持插件
        webSettings.setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        webSettings.setSupportMultipleWindows(true);//设置支持多窗口
        webSettings.setTextZoom(100);//禁止系统缩放字体大小
//        webSettings.setSupportZoom(true);//设置支持缩放
//        webSettings.setBuiltInZoomControls(true);//设置支持缩放
//       webSettings.setUseWideViewPort(true);//用户可以可以网页比例
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setJavaScriptEnabled(true);// 可以使用javaScriptEnalsed
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//js可以自动打开窗口
        webSettings.setAllowFileAccess(true);//支持引用文件
        webSettings.setAppCacheEnabled(true);        //设置支持本地存储
        webSettings.setDomStorageEnabled(true);//可以手动开启DOM Storage
        if (Build.VERSION.SDK_INT >= 17) {// 不需要请求控制直接播放媒体文件
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        //可能遇到的问题：有可能会遇到有的图片加载不出来，那是因为webview 从Lollipop(5.0)开始webview默认不允许混合模式，https当中不能加载http资源，需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        addJavascriptInterface(new JsObject(), NATIVE_API);
        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(chromeClient = new MyWebChromeClient());// 设置浏览器可弹窗
    }

    private final class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUitls.print("shouldOverrideUrlLoading", url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (url != null && url.length() < 500) {
                LogUitls.print("url-新页面开始", url);
            }
            progressBar.startProgress();
            isErrorNotResolved = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


            progressBar.finishProgress();

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            progressBar.onError();
            LogUitls.print("onReceivedError", error + " request:" + request);
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            LogUitls.print("onReceivedError1", failingUrl + " description:" + description + " errorCode " + errorCode);
            //description:net::ERR_NAME_NOT_RESOLVED errorCode -2
            isErrorNotResolved = errorCode == -2;
        }


        //网址是否能解析
        private boolean isErrorNotResolved = false;
    }


//    //    private H5SelectFileDialog fileChooseDialog;
//    private ValueCallback valueCallback;//回调
//    private boolean isHightLevel;//高版本要传数组


    /**
     * 浏览器可弹窗
     *
     * @author Administrator
     */
    private final class MyWebChromeClient extends WebChromeClient {


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.onProgressChanged(newProgress);
        }



    }



//    public static final int FILE_CHOOSER_RESULT_CODE = 8833;

    private final class JsObject {// @JavascriptInterface是为了支4.2及以上的js交互

        @JavascriptInterface
        public void app_back() {
            if (canGoBack()) {
                goBack();
            }
        }



        @JavascriptInterface
        public String toString() {
            return NATIVE_API;
        }
    }


    /***
     * 当销毁时
     */
    public void onDestory() {
        try {

            this.clearHistory();
            this.destroy();


            if (this.getParent() != null) {
                ((ViewGroup) this.getParent()).removeView(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
