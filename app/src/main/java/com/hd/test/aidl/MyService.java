package com.hd.test.aidl;

/**
 * Note：None
 * Created by Liuguodong on 2019/7/24 14:48
 * E-Mail Address：986850427@qq.com
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.hd.test.mutitask.Book;
import com.hd.test.mutitask.DataCallBack;
import com.hd.test.mutitask.ITaskBinder;
import com.hd.test.mutitask.ITaskCallback;


public class MyService extends Service {
    private static final String TAG = "aidltest";

    @Override
    public void onCreate() {
        printf("service create");

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    callback(i);
                }
            }
        }.start();


    }

    @Override
    public void onStart(Intent intent, int startId) {
        printf("service start id=" + startId);
        callback(startId);
    }

    @Override
    public IBinder onBind(Intent t) {
        printf("service on bind");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        printf("service on destroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        printf("service on unbind");
        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
        printf("service on rebind");
        super.onRebind(intent);
    }

    private void printf(String str) {
        Log.v(TAG, "###################------ " + str + "------");
    }

    void callback(int val) {
        final int N = mCallbacks.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).actionPerformed(val);
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
            }
        }
        mCallbacks.finishBroadcast();
    }

    private final ITaskBinder.Stub mBinder = new ITaskBinder.Stub() {

        public void stopRunningTask() {

        }

        public boolean isTaskRunning() {
            return false;
        }

        public void registerCallback(ITaskCallback cb) {
            if (cb != null) {
                mCallbacks.register(cb);
            }
        }

        public void unregisterCallback(ITaskCallback cb) {
            if (cb != null) {
                mCallbacks.unregister(cb);
            }
        }
    };

    final RemoteCallbackList<ITaskCallback> mCallbacks = new RemoteCallbackList<ITaskCallback>();

}
