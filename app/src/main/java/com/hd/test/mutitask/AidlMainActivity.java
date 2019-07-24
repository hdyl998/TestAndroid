package com.hd.test.mutitask;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.hd.R;
import com.hd.app.MyLogUitls;

import java.util.List;

/**
 * https://blog.csdn.net/songjinshi/article/details/22918405
 * Note：None
 * Created by Liuguodong on 2019/7/24 11:46
 * E-Mail Address：986850427@qq.com
 */
public class AidlMainActivity extends FragmentActivity {

    private static final String TAG = "AidlMainActivity";
    private ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);


        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyLogUitls.print(TAG, (service instanceof BookManager) + "");
                BookManager bookManager = BookManager.Stub.asInterface(service);
                try {
                    List<Book> bookList = bookManager.getBookList();
                    Toast.makeText(getApplicationContext(), "来自服务端的数据" + bookList.toString(), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    bookManager.register(mCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent(this, AidlService.class);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private DataCallBack mCallback = new DataCallBack.Stub() {

        @Override
        public void callBack(Book data) throws RemoteException {
            MyLogUitls.print(TAG, "来自服务端的数据" + data.toString());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
