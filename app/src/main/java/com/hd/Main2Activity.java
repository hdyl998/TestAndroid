package com.hd;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.hd.lib.bufferknife.MyBufferKnifeUtils;

/**
 * Note：None
 * Created by Liuguodong on 2019/12/13
 * E-Mail Address：986850427@qq.com
 */
public class Main2Activity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        MyBufferKnifeUtils.inject(this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("tttt--onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("tttt--onPause");
    }
}
