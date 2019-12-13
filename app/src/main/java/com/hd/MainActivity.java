package com.hd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.hd.lib.bufferknife.MyBindView;
import com.hd.lib.bufferknife.MyBufferKnifeUtils;
import com.hd.lib.view.FlowLayout;
import com.hd.test.aidl.AidlMain2Activity;
import com.hd.test.event.TestEventActvity;
import com.hd.test.mutitask.AidlMainActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Note：None
 * Created by Liuguodong on 2019/7/24 10:03
 * E-Mail Address：986850427@qq.com
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {


    @MyBindView(R.id.floatLayout)
    FlowLayout floatLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        MyBufferKnifeUtils.inject(this);

        initDatas();
        for (MenuItem dataItem : dataItems) {
            addView(dataItem.aClass, dataItem.text);
        }
    }

    private void initDatas() {
        dataItems.add(new MenuItem(TestEventActvity.class, "事件"));
        dataItems.add(new MenuItem(AidlMainActivity.class, "Aidl"));
        dataItems.add(new MenuItem(AidlMain2Activity.class, "Aidl2"));
        dataItems.add(new MenuItem(Main2Activity.class,"test2"));
    }

    List<MenuItem> dataItems = new ArrayList<>();


    private void addView(final Class clazz, String btnName) {
        Button button = new Button(this);
        button.setText(btnName);
        button.setTag(clazz);
        button.setOnClickListener(this);
        floatLayout.addView(button);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, (Class<?>) v.getTag()));
    }
}
