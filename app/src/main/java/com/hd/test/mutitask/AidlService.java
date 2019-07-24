package com.hd.test.mutitask;

/**
 * Note：None
 * Created by Liuguodong on 2019/7/24 11:28
 * E-Mail Address：986850427@qq.com
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.hd.app.MyLogUitls;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huangjie on 2018/5/22.
 */

public class AidlService extends Service {
    private ArrayList<Book> bookList;


    private static final String TAG = "AidlService";
    final RemoteCallbackList<DataCallBack> mCallbacks = new RemoteCallbackList<DataCallBack>();
    private BookManager.Stub mBinder = new BookManager.Stub() {
        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }


        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void register(DataCallBack callBack) throws RemoteException {

            MyLogUitls.print(TAG, "register " + callBack);

            if (callBack != null)
                mCallbacks.register(callBack);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bookList = new ArrayList<>();
        bookList.add(new Book("ID123", "Android开发艺术探索"));
        bookList.add(new Book("ID124", "剑指offer Java版"));

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        callback(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    void callback(int val) {
        final int N = mCallbacks.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).callBack(new Book("ID124", "剑指offer Java版" + val));
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
            }
        }
        mCallbacks.finishBroadcast();
    }
}
