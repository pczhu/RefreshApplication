package com.pczhu.www.refreshapplication.baserefresh.http;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pczhu.www.refreshapplication.Failed;
import com.pczhu.www.refreshapplication.Utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


//import cn.mobile.renrentou.utils.AccountUtils;

/**
 * 名称：${FILE_NAME}
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/1/4 下午5:32
 * 版本：V1.0
 * 修改历史：
 */
public class HttpAction {
    private static Context mContext;
    private static HttpAction httpAction = null;

    public static HttpAction getInstance(Context context){
        HttpAction.mContext = context;
        if(httpAction == null){
            httpAction  = new HttpAction();
        }
        return httpAction;
    }
    public <T> Callback.Cancelable post(RequestParams requestParams, final Class<T> clz,final HttpResponseListener<T> httpResponseListener){
        if(httpResponseListener == null){
            try {
                throw new Exception("没有为网络请求设置回调");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        httpResponseListener.onStart();
        Callback.Cancelable cancelable = x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                httpResponseListener.onSuccess(s);
                Gson gson = new Gson();
                try {
                    T t = gson.fromJson(s, clz);
                    httpResponseListener.onSuccessForData(t);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    try {
                        Failed failed = gson.fromJson(s, Failed.class);
                        httpResponseListener.onSuccessButNoData(failed);
                    } catch (JsonSyntaxException e1) {
                        e1.printStackTrace();
                        httpResponseListener.onFailed(e1, false);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                httpResponseListener.onFailed(throwable, b);
            }

            @Override
            public void onCancelled(CancelledException e) {
                httpResponseListener.onCanel(e);
            }

            @Override
            public void onFinished() {
                httpResponseListener.onFanished();
            }
        });
        return cancelable;
    }

    public static RequestParams getRequestParams(String url){
        RequestParams requestParams = new RequestParams(url);
        requestParams.setExecutor(ThreadPoolUtils.threadPool);
        requestParams.setConnectTimeout(15 * 1000);
        requestParams.setMaxRetryCount(15);
//        String signTime = MD5Util.getTime();
//        requestParams.addBodyParameter("access_token",""+ MD5Util.getAppAccessToken());
//        requestParams.addBodyParameter("sign",""+MD5Util.postAccessToken(signTime));
//        requestParams.addBodyParameter("signtime",signTime);
        return  requestParams;
    }
}
