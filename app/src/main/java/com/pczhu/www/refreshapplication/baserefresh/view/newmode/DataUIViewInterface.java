package com.pczhu.www.refreshapplication.baserefresh.view.newmode;

import java.util.ArrayList;

/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/6/1 上午11:55
 * 版本：V1.0
 * 修改历史：
 */
public interface DataUIViewInterface<T> {
    /**
     * 正常显示刷新成功
     */
    void showRefreshResult(Object mT);

    /**
     * 正常显示加载成功
     */
    void showLoadNextResult(Object mT);

    /**
     * 显示由于网络错误造成的没有更多数据的情况
     */
    void showErrorNoMore();

    /**
     * 显示由于网络错误造成的没有数据的情况
     */
    void showErrorEmpty();

    /**
     * 网络请求成功  但是结果为无数据 且 页面原本一条数据都没有
     */
    void showEmptyNoMore();

    /**
     * 网络请求成功  但是结果为无数据  显示页面当前是有的数据 （找不到更多数据）
     */
    void showEmptyTheEnd();
    /**
     * 去掉错误布局
     */
    void dissMissErrorLayout();

    /**
     * 页面现有数据
     * @return
     */
    ArrayList<T> getDataList();

    /**
     * 登录信息过期
     */
    void tokenOutOfData();
}
