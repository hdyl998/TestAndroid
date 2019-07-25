package com.hd.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hd.R;
import com.hd.app.util.Utils;

public class MainActivity extends AppCompatActivity {


    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setTheme(R.style.dialogThemeBase);
        setContentView(R.layout.activity_main_home);

        getWindow().setGravity(Gravity.RIGHT);
    }


    public void onClick(View view) {
        setProxy();
    }

    public void onClick2(View view) {
        textView.setText("开始请求");
        new Thread(runnable).start();

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final String isSuccess = Utils.isNetworkOnline();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(isSuccess + "");
                }
            });

        }
    };

    Handler handler = new Handler();


    ProxyDialog proxyDialog;

    private void setProxy() {
        if (proxyDialog == null) {
            proxyDialog = new ProxyDialog(this);
            proxyDialog.setOnClickOKCallBacks(new IComCallBacks() {
                @Override
                public void call(Object obj) {
                    restart();
                }
            });
        }
        proxyDialog.show();
    }

    private void restart() {
        Utils.killSelfAndRestart(this);
    }

    private static final String TAG = "MainTabActivity";


}
