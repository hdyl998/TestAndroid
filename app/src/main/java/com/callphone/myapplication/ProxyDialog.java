package com.callphone.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.callphone.myapplication.util.ProxyUtils;

/**
 * Note：None
 * Created by Liuguodong on 2019/1/21 17:21
 * E-Mail Address：986850427@qq.com
 */
public class ProxyDialog {


    private final static String DEFAULT_IP="192.168.2.102";
    private final static String DEFAULT_PORT="8888";
    TextView textViewIp, textViewPort;
    CheckBox checkBox;

    Dialog dialog;


    public ProxyDialog(Context mContext) {
        View root = View.inflate(mContext, R.layout.view_test_proxy,null);
        textViewIp = root.findViewById(R.id.textViewIp);
        textViewPort = root.findViewById(R.id.textViewPort);
        checkBox = root.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtils.putBoolean(SPConstants.FILE_developer, SPConstants.APPSET_PROXY_enable, isChecked);
            }
        });
        dialog = new AlertDialog.Builder(mContext)
                .setTitle("设置代理配置")
                .setView(root)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpUtils.putString(SPConstants.FILE_developer, SPConstants.APPSET_PROXY_ip, textViewIp.getText().toString());
                        SpUtils.putString(SPConstants.FILE_developer, SPConstants.APPSET_PROXY_port, textViewPort.getText().toString());
                        if (comCallBacks != null) {
                            comCallBacks.call(null);
                        }
                    }
                }).setNegativeButton("取消", null).create();
        textViewIp.setText(SpUtils.getString(SPConstants.FILE_developer, SPConstants.APPSET_PROXY_ip, DEFAULT_IP));
        textViewPort.setText(SpUtils.getString(SPConstants.FILE_developer, SPConstants.APPSET_PROXY_port, DEFAULT_PORT));
        checkBox.setChecked(SpUtils.getBoolean(SPConstants.FILE_developer, SPConstants.APPSET_PROXY_enable, false));
    }

    public void show() {
        dialog.show();
    }


    IComCallBacks comCallBacks;


    public void setOnClickOKCallBacks(IComCallBacks comCallBacks) {
        this.comCallBacks = comCallBacks;
    }

    /***
     * 使能代理功能
     */
    public static void enableProxy() {
        boolean isEnable = SpUtils.getBoolean(SPConstants.FILE_developer, SPConstants.APPSET_PROXY_enable, false);
        if (isEnable) {
            String ip = SpUtils.getString(SPConstants.FILE_developer, SPConstants.APPSET_PROXY_ip, DEFAULT_IP);
            String port = SpUtils.getString(SPConstants.FILE_developer, SPConstants.APPSET_PROXY_port, DEFAULT_PORT);
            ProxyUtils.startProxy(ip, port);
        }
    }

}
