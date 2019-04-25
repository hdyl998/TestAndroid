package com.callphone.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {


    MyWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        webView = findViewById(R.id.webView);
        webView.loadUrl("http://dev-newbp.yixinfa.cn/#/launcher/519046093701");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.onDestory();
    }
}
