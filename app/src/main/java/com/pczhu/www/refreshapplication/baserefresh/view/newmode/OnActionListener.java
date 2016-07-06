package com.pczhu.www.refreshapplication.baserefresh.view.newmode;

import com.pczhu.www.refreshapplication.Failed;

/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/6/30 上午10:41
 * 版本：V1.0
 * 修改历史：
 */
public abstract class OnActionListener<T>{
    /**
     * 拿到数据
     * @param t
     */
    public void getData(T t){

    }

    /**
     * 获取数据
     * @param str
     */
    public void getJson(String str){

    }

    /**
     * 拿到失败数据
     * @param failed
     */
    public void getFailed(Failed failed){

    }

    /**
     * 拿到错误数据
     * @param failed
     */
    public void getError(Throwable throwable){

    }
}
