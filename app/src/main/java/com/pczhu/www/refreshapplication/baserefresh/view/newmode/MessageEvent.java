package com.pczhu.www.refreshapplication.baserefresh.view.newmode;

import com.pczhu.www.refreshapplication.Failed;

/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/6/1 下午1:49
 * 版本：V1.0
 * 修改历史：
 */
public class MessageEvent<Object> {
    private Object mT;
    private Throwable throwable;
    private Failed mFailed;
    private String mJson;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Failed getmFailed() {
        return mFailed;
    }

    public void setmFailed(Failed mFailed) {
        this.mFailed = mFailed;
    }

    public java.lang.Object getmT() {
        return mT;
    }

    public void setmT(Object mT) {
        this.mT = mT;
    }

    public String getmJson() {
        return mJson;
    }

    public void setmJson(String mJson) {
        this.mJson = mJson;
    }
}
