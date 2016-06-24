package com.pczhu.www.refreshapplication.baserefresh.view.newmode;

import org.xutils.http.RequestParams;

/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/6/1 下午12:22
 * 版本：V1.0
 * 修改历史：
 */
public interface DataHttpActionInterface<T> {
     void sendHttpRequest(RequestParams requestParams,boolean isRefresh,Class<?> clz,DataUIViewInterface<T> mDataUIViewInterfaceImpl);
}
