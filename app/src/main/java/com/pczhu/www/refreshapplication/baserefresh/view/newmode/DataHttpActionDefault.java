package com.pczhu.www.refreshapplication.baserefresh.view.newmode;

import com.google.gson.Gson;
import com.pczhu.www.refreshapplication.Failed;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/6/1 下午1:43
 * 版本：V1.0
 * 修改历史：
 */
public class DataHttpActionDefault<T> implements DataHttpActionInterface<T> {
    private EventBus mEventBus;
    private MessageEvent messageEvent;
    private Object obj;
    @Override
    public void sendHttpRequest(RequestParams mRequestParams,
                                final boolean isRefresh,
                                final Class<?> clz,
                                final DataUIViewInterface<T> mDataUIViewInterfaceImpl) {
            mEventBus = EventBus.getDefault();
            messageEvent = new MessageEvent();
            x.http().request(HttpMethod.POST, mRequestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    //解析
                    Gson gson = new Gson();
                    Failed failBean = null;
                    String status = null;

                    messageEvent.setmJson(s);
                    mEventBus.post(messageEvent);
                    try {
                        JSONObject dataJson=new JSONObject(s);
                        status = dataJson.getString("status");
                        if(!"-101".equals(status)){
                            failBean = gson.fromJson(s, Failed.class);
                        }else{
                            obj = gson.fromJson(s, clz);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        failBean =gson.fromJson(s, Failed.class);
                    }
                    //########################################

                    if(failBean == null && obj!=null ){//请求到新数据
                        messageEvent.setmT(obj);
                        mEventBus.post(messageEvent);
                        if(isRefresh){
                            mDataUIViewInterfaceImpl.showRefreshResult(obj);
                        }else{
                            //userlist.addAll(project.getData());
                            mDataUIViewInterfaceImpl.showLoadNextResult(obj);
                        }
                        mDataUIViewInterfaceImpl.dissMissErrorLayout();
                    }else{//请求不到新数据了
                        messageEvent.setmFailed(failBean);
                        mEventBus.post(messageEvent);
                        if(failBean.getStatus().equals("-4")){
                            mDataUIViewInterfaceImpl.tokenOutOfData();
                        }
                        if(failBean.getStatus().equals("-103")
                                ||failBean.getStatus().equals("-102")){//没有更多数据了
                            //controlWhichShow(1);
                            if(isRefresh){
                                mDataUIViewInterfaceImpl.showEmptyNoMore();
                            }else{
                                if(isHasOldData()){
                                    mDataUIViewInterfaceImpl.showEmptyTheEnd();
                                }else{
                                    mDataUIViewInterfaceImpl.showEmptyNoMore();
                                }
                            }
                        }else{
                            if(isHasOldData()){
                                mDataUIViewInterfaceImpl.showErrorNoMore();
                            }else{
                                mDataUIViewInterfaceImpl.showErrorEmpty();

                            }
                        }
                    }


                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    messageEvent.setThrowable(throwable);
                    mEventBus.post(messageEvent);
                    if(isHasOldData()){
                        mDataUIViewInterfaceImpl.showErrorNoMore();
                    }else{
                        mDataUIViewInterfaceImpl.showErrorEmpty();

                    }
                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
                private boolean isHasOldData(){
                    ArrayList mDataList = mDataUIViewInterfaceImpl.getDataList();
                    return mDataList != null && mDataList.size()!=0 ;
                }
            });




    }
}
