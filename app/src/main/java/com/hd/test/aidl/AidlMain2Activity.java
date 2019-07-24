package com.hd.test.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hd.R;
import com.hd.test.mutitask.AidlService;
import com.hd.test.mutitask.ITaskBinder;
import com.hd.test.mutitask.ITaskCallback;

/**
 * Note：None
 * Created by Liuguodong on 2019/7/24 14:53
 * E-Mail Address：986850427@qq.com
 */
public class AidlMain2Activity extends Activity {

    private static final String TAG = "aidltest";
    private Button btnOk;
    private Button btnCancel;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_aidl2);

        btnOk = (Button) findViewById(R.id.button1);
        btnCancel = (Button) findViewById(R.id.button2);
        btnOk.setText("Start Service");
        btnCancel.setText("Stop Service");
        btnCancel.setEnabled(false);

        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onOkClick();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCancelClick();
            }
        });
    }

    void onOkClick() {
        printf("send intent to start");
        Bundle args = new Bundle();
        Intent intent = new Intent(this, MyService.class);
        intent.putExtras(args);
//        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        btnCancel.setEnabled(true);
    }

    void onCancelClick() {

        printf("send intent to stop");
        //unbindService(mConnection);
//        Intent intent = new Intent(this, MyService.class);
//        stopService(intent);
        btnCancel.setEnabled(false);
    }

    private void printf(String str) {
        Log.v(TAG, "###################------ " + str + "------");
    }

    ITaskBinder mService;

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ITaskBinder.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {

            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private ITaskCallback mCallback = new ITaskCallback.Stub() {

        public void actionPerformed(int id) {
            printf("callback id=" + id);
        }
    };

}
