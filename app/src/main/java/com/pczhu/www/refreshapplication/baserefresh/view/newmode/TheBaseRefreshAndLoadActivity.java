package com.pczhu.www.refreshapplication.baserefresh.view.newmode;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.pczhu.www.refreshapplication.MyProAttentionBean;
import com.pczhu.www.refreshapplication.R;
import com.pczhu.www.refreshapplication.Utils.LogUtils;
import com.pczhu.www.refreshapplication.Utils.ThreadPoolUtils;
import com.pczhu.www.refreshapplication.baserefresh.CardsAnimationAdapter;
import com.pczhu.www.refreshapplication.baserefresh.MyBaseAdapter;
import com.pczhu.www.refreshapplication.baserefresh.view.LoadingFooter;
import com.pczhu.www.refreshapplication.baserefresh.view.OnLoadNextListener;
import com.pczhu.www.refreshapplication.baserefresh.view.PageStaggeredGridView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;


/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/5/24 下午3:31
 * 版本：V1.0
 * 修改历史：
 */
public abstract class TheBaseRefreshAndLoadActivity <T> extends Activity
        implements DataUIViewInterface<T>,
        SwipeRefreshLayout.OnRefreshListener, OnLoadNextListener {
    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PageStaggeredGridView pageStaggeredGridView;
    protected int page = 1;
    protected MyBaseAdapter mainAdapter;
    private OnActionListener onActionListener;
    protected boolean isRefreshFromTop;
    protected TextView tv_error;
    protected Class<?> clz;
    protected Object obj;
    protected ArrayList<T> mDataList;
    protected RequestParams requestParams;


    private DataPresenter<T> mDataPresenter;

    private String mErrorDataString = "网络请求出现问题";
    private String mEmptyDataString = "找不到相关数据";

    /**
     * 默认错误布局文字
     * @param mErrorDataString
     */
    public void setmErrorDataString(String mErrorDataString) {
        this.mErrorDataString = mErrorDataString;
    }

    /**
     * 默认空数据布局文字
     * @param mEmptyDataString
     */
    public void setmEmptyDataString(String mEmptyDataString) {
        this.mEmptyDataString = mEmptyDataString;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mContext = this;

    }

    /**
     * 配置各种变量
     * @param swipeRefreshLayout 刷新控件
     * @param pageStaggeredGridView  GridView
     * @param adapter  Adapter
     * @param clz   解析类型
     * @param requestParams  请求参数
     * @param onActionListener  回调
     */
    public void openListener( SwipeRefreshLayout swipeRefreshLayout,
                              PageStaggeredGridView pageStaggeredGridView,
                              MyBaseAdapter adapter,
                              Class<?> clz,
                              RequestParams requestParams,
                              OnActionListener onActionListener){
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.pageStaggeredGridView = pageStaggeredGridView;
        this.mainAdapter = adapter;
        this.clz = clz;
        this.onActionListener = onActionListener;
        this.requestParams = requestParams;

        try {
            obj = clz.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.orange);
        swipeRefreshLayout.setOnRefreshListener(this);
        pageStaggeredGridView.setLoadNextListener(this);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(mainAdapter);//添加卡片式动画
        animationAdapter.setAbsListView(pageStaggeredGridView);
        pageStaggeredGridView.setAdapter(animationAdapter);
        addErrorView();
        mDataPresenter = new DataPresenter(this,clz);
    }

    /**
     * 进入页面进行第一次访问
     */
    protected void start(){
        onRefresh();
    }
    @Override
    public void showRefreshResult(Object mT) {
        //刷新成功
        swipeRefreshLayout.setRefreshing(false);
        pageStaggeredGridView.setState(LoadingFooter.State.Idle);
        setErrorLayout(View.GONE);
        mDataList = getObjectList(mT);
        mainAdapter.notifyDataSetChanged(mDataList);
    }

    @Override
    public void showLoadNextResult(Object mT) {
        //刷新加载更多成功
        pageStaggeredGridView.setState(LoadingFooter.State.Idle);
        setErrorLayout(View.GONE);
        mDataList.addAll(getObjectList(mT));
        mainAdapter.notifyDataSetChanged(mDataList);

    }

    @Override
    public void showErrorNoMore() {
        Toast.makeText(this, mErrorDataString,Toast.LENGTH_SHORT).show();
        tv_error.setText(mErrorDataString);//先设置错误布局的文字提示
        refreshView(LoadingFooter.State.ERROR_NOMORE);
        setErrorLayout(View.GONE);
    }

    @Override
    public void showErrorEmpty() {
        tv_error.setText(mErrorDataString);//先设置错误布局的文字提示
        refreshView(LoadingFooter.State.ERROR_EMPTY);
        setErrorLayout(View.VISIBLE);
    }

    @Override
    public void showEmptyNoMore() {
        tv_error.setText(mEmptyDataString);//先设置错误布局的文字提示
        refreshView(LoadingFooter.State.EMPTY_NOMORE);
        setErrorLayout(View.VISIBLE);
    }
    @Override
    public void showEmptyTheEnd() {
        Toast.makeText(this,"找不到更多数据",Toast.LENGTH_SHORT).show();
        tv_error.setText(mEmptyDataString);//先设置错误布局的文字提示
        refreshView(LoadingFooter.State.TheEnd);
        setErrorLayout(View.GONE);
    }

    @Override
    public void dissMissErrorLayout() {
        pageStaggeredGridView.setFVisibility(View.GONE);
        setErrorLayout(View.GONE);
    }

    @Override
    public void tokenOutOfData() {
        Toast.makeText(this, "您的登录信息已经过期，请重新登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        isRefreshFromTop = true;
        swipeRefreshLayout.setRefreshing(true);
        page = 1;
        RequestParams requestParams = getRequestParams();
        mDataPresenter.refresh(requestParams);
    }

    @Override
    public void onLoadNext() {
        if(!swipeRefreshLayout.isRefreshing()) {
            page++;
            isRefreshFromTop = false;
            RequestParams requestParams = getRequestParams();
            mDataPresenter.loadNext(requestParams);
        }
    }

    @Override
    public ArrayList<T> getDataList() {
        return mDataList;
    }

    @NonNull
    private RequestParams getRequestParams() {
        //RequestParams requestParams = new RequestParams(url);
        requestParams.setExecutor(ThreadPoolUtils.threadPool);
        requestParams.setConnectTimeout(3 * 1000);
        requestParams.setMaxRetryCount(5);
        requestParams.addBodyParameter("page", page + "");
        return requestParams;
    }

    private void refreshView(LoadingFooter.State error) {
        pageStaggeredGridView.setState(error);
        if (!isRefreshFromTop) {//不是刷新
            if(error != LoadingFooter.State.ERROR_NOMORE)
                pageStaggeredGridView.setFVisibility(View.GONE);
        }else {//是刷新
            swipeRefreshLayout.setRefreshing(false);
            pageStaggeredGridView.setFVisibility(View.GONE);
            LogUtils.i("是刷新"+error.toString());
        }
    }


    /**
     * 拿到集合类型
     * @param obj
     * @return
     */
    public abstract ArrayList getObjectList(Object obj);


    @Override
    public void onStop() {
        super.onStop();
//        if(cancelable!=null)
//            cancelable.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 添加错误布局
     */
    public void addErrorView(){

        View error_view = View.inflate(mContext, R.layout.error_layout,null);

        tv_error = (TextView) error_view.findViewById(R.id.tv_error);
        getRootView()
                .addView(error_view,
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
    }
    protected abstract ViewGroup getRootView();

    /**
     * 设定错误布局的显示状态
     * @param visiblility
     */
    private void setErrorLayout(int visiblility) {
        getRootView()
                .getChildAt(getRootView().getChildCount() - 1)
                .setVisibility(visiblility);
    }
    protected DataPresenter getDataPresenter(){
        return mDataPresenter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessageEvent(MessageEvent<MyProAttentionBean> event) {
        switch (event.getTpye()){
            case 1:
                String msg = event.getmJson();
                if(onActionListener!=null){
                    onActionListener.getJson(msg);
                }
                Toast.makeText(this,"获取到的消息："+msg,Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if(onActionListener!=null){
                    onActionListener.getData(event.getmT());
                }
                break;
            case 3:
                if(onActionListener!=null){
                    onActionListener.getFailed(event.getmFailed());
                }
                break;
            case 4:
                if(onActionListener!=null){
                    onActionListener.getError(event.getThrowable());
                }
                break;
            default:
                break;

        }

    }
}
