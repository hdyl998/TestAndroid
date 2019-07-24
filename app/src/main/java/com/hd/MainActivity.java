package com.hd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.hd.lib.bufferknife.MyBindView;
import com.hd.lib.bufferknife.MyBufferKnifeUtils;
import com.hd.lib.bufferknife.MyOnClick;
import com.hd.test.event.TestEventActvity;
import com.hd.test.mutitask.AidlMainActivity;


/**
 * Note：None
 * Created by Liuguodong on 2019/7/24 10:03
 * E-Mail Address：986850427@qq.com
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {


    @MyBindView(R.id.button)
    Button button;//安卓事件分发
    @MyBindView(R.id.button2)
    Button button2;//Button2
    @MyBindView(R.id.button3)
    Button button3;//Button3
    @MyBindView(R.id.button4)
    Button button4;//Button4
    @MyBindView(R.id.button5)
    Button button5;//Button5
    int a;//MyBufferKnifeUtils.inject(this);//加上这句话,防止忘记,没生效

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyBufferKnifeUtils.inject(this);

    }

    @MyOnClick({R.id.button, R.id.button2, R.id.button3, R.id.button4, R.id.button5})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                startActivity(new Intent(this, TestEventActvity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(this, AidlMainActivity.class));
                break;
            case R.id.button3:
                break;
            case R.id.button4:
                break;
            case R.id.button5:
                break;
        }
    }
}
