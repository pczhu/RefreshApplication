package com.pczhu.www.refreshapplication.baserefresh.view.newmode;

import org.xutils.http.RequestParams;

/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/6/1 上午11:55
 * 版本：V1.0
 * 修改历史：
 */
public class DataPresenter<T> {
    private DataUIViewInterface mDataUIViewInterfaceImpl;
    private DataHttpActionInterface<T> mDataHttpActionInterfaceImpl;
    private DataParserInterface mDataParserInterfaceImpl;
    private Class<?> clz;
    /**
     * 唯一构造方法
     * @param dataUIViewInterface
     */
    public  DataPresenter(DataUIViewInterface dataUIViewInterface,Class<?> clz){
        this.mDataUIViewInterfaceImpl = dataUIViewInterface;
        this.clz = clz;
    }
    /**
     * 加载下一页
     */
    public void loadNext(RequestParams requestParams){
        getDataResult(requestParams,false);
    }
    /**
     * 刷新页面
     */
    public void refresh(RequestParams requestParams){
        getDataResult(requestParams, true);
    }
    @SuppressWarnings("unchecked")
    public void getDataResult(final RequestParams mRequestParams, final boolean isRefresh){
        if(mDataHttpActionInterfaceImpl == null){
            mDataHttpActionInterfaceImpl = new DataHttpActionDefault();
        }
        mDataHttpActionInterfaceImpl.sendHttpRequest(mRequestParams,isRefresh,clz,mDataUIViewInterfaceImpl);

    }

    public DataHttpActionInterface getmDataHttpActionInterfaceImpl() {
        return mDataHttpActionInterfaceImpl;
    }

    public void setmDataHttpActionInterfaceImpl(DataHttpActionInterface<T> mDataHttpActionInterfaceImpl) {
        this.mDataHttpActionInterfaceImpl = mDataHttpActionInterfaceImpl;
    }

    public DataParserInterface getDataParserInterfaceImpl() {
        return mDataParserInterfaceImpl;
    }

    public void setDataParserInterfaceImpl(DataParserInterface dataParserInterfaceImpl) {
        this.mDataParserInterfaceImpl = dataParserInterfaceImpl;
    }

}
