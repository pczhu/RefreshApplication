package com.pczhu.www.refreshapplication;

import android.app.Application;
import android.content.Context;

import com.pczhu.www.refreshapplication.Utils.LogUtils;

import org.xutils.x;

/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/6/1 上午9:38
 * 版本：V1.0
 * 修改历史：
 */
public class RRTApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        LogUtils.allowAll = true;
        LogUtils.allowI = false;
        LogUtils.allowString = new String[]{"MainActivity"};
    }

    public static Context getApplication(){
        return getApplication();
    }
}
