package com.hd.test.mutitask;
import com.hd.test.mutitask.ITaskCallback;
/**
 * Note：None
 * Created by Liuguodong on 2019/7/24 14:46
 * E-Mail Address：986850427@qq.com
 */

interface ITaskBinder {
    boolean isTaskRunning();

    void stopRunningTask();

    void registerCallback(ITaskCallback cb);

    void unregisterCallback(ITaskCallback cb);
}
  