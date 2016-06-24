package com.pczhu.www.refreshapplication.baserefresh.http;

import com.pczhu.www.refreshapplication.Failed;

import org.xutils.common.Callback;



/**
 * 名称：${FILE_NAME}
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/1/4 下午5:27
 * 版本：V1.0
 * 修改历史：
 */
public abstract class HttpResponseListener<T> {
    public void onStart(){

    }
    public abstract void onSuccessForData(T t);
    public void onSuccessButNoData(Failed failed){

    }
    public void onFailed(Throwable throwable, boolean b){

    }
    public void onSuccess(String s){

    }
    public void onCanel(Callback.CancelledException e){

    }
    public void onFanished(){

    }

}
