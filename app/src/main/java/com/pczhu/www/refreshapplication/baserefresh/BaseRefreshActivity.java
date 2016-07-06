package com.pczhu.www.refreshapplication.baserefresh;

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

import com.google.gson.Gson;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.pczhu.www.refreshapplication.Failed;
import com.pczhu.www.refreshapplication.R;
import com.pczhu.www.refreshapplication.Utils.LogUtils;
import com.pczhu.www.refreshapplication.Utils.ThreadPoolUtils;
import com.pczhu.www.refreshapplication.baserefresh.view.LoadingFooter;
import com.pczhu.www.refreshapplication.baserefresh.view.OnLoadNextListener;
import com.pczhu.www.refreshapplication.baserefresh.view.PageStaggeredGridView;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;




/**
 * 名称：CustomTestxUtils
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 15/11/24 下午12:06
 * 版本：V1.0
 * 修改历史：
 */
public abstract class BaseRefreshActivity<T> extends Activity implements SwipeRefreshLayout.OnRefreshListener, OnLoadNextListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private PageStaggeredGridView pageStaggeredGridView;
    protected int page = 1;
    protected MyBaseAdapter mainAdapter;
    private OnActionListener onActionListener;
    protected boolean isRefreshFromTop;
    protected TextView tv_error;
    protected Class<?> clz;
    protected Object obj;
    protected ArrayList<T> userlist;
    protected RequestParams requestParams;


    private String mEmptyDataString = "没找到相关数据";
    private String mErrorDataString = "网络请求出现问题";
    //private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    @Override
    public void onLoadNext() {

        page++;
        loadData(page);
    }
    @Override
    public void onRefresh() {
        //pageStaggeredGridView.setState(LoadingFooter.State.Idle);
        loadFirst();
    }
    private void loadFirst(){
        page = 1;
        loadData(page);

    }

    private void loadData(int page) {

        isRefreshFromTop = (1 == page);//如果是1 就是刷新了


        if (!swipeRefreshLayout.isRefreshing() && isRefreshFromTop) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    LogUtils.d("UI开启");
                }
            });
        }

        //SystemClock.sleep(2000);
        sendToHttp(page);
    }

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

        public void testData(Failed failed,T obj){

        }
    }
    //    public void start(OnActionListener onActionListener){
//        this.onActionListener = onActionListener;
//    }
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

    }
    public void resetRequestParams(RequestParams requestParams){
        this.requestParams = requestParams;

    }
    public void start(){
        loadFirst();
    }
    protected abstract ViewGroup getRootView();

    private Callback.Cancelable cancelable;
    private void sendToHttp(final int page) {
        getRequestParams(page);
        cancelable = x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i("JSON数据请求"+s);
                        if(onActionListener!=null) {
                            onActionListener.getJson(s);
                        }
                        getData(s);
                    }
                });
            }
            @Override
            public void onError(Throwable throwable, boolean b) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isRefreshFromTop) {
                            swipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(false);
                                    LogUtils.d("UI关闭");
                                }
                            });
                        }
//                        else {
//                            pageStaggeredGridView.setState(LoadingFooter.State.ERROR_EMPTY);
//                        }
                        controlWhichShow(2);

                    }
                });

            }
            @Override
            public void onCancelled(CancelledException e) {
                controlWhichShow(2);
            }
            @Override
            public void onFinished() {
                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }

        });
    }
    private void getData(String s){

        Gson gson = new Gson();
        Failed failBean = null;
        String status = null;
        try {
            obj = gson.fromJson(s, clz);
            JSONObject  dataJson=new JSONObject(s);
            status = dataJson.getString("status");
            if(!"-101".equals(status)){
                failBean =gson.fromJson(s, Failed.class);
            }
        }catch (Exception e){
            e.printStackTrace();
            failBean =gson.fromJson(s, Failed.class);
        }
        if(onActionListener!=null){
            onActionListener.testData(failBean,obj);
        }
        if(failBean == null && obj!=null ){//请求到新数据
            if(isRefreshFromTop){
                // userlist = project.getData();
                userlist = getObjectList(obj);
            }else{
                //userlist.addAll(project.getData());
                userlist.addAll(getObjectList(obj));
            }
            if(onActionListener!=null) {
                onActionListener.getData(obj);
            }
            //onActionListener.getData(userlist);
            mainAdapter.notifyDataSetChanged(userlist);

            if(isRefreshFromTop)
                swipeRefreshLayout.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                                LogUtils.d("UI关闭");
                                pageStaggeredGridView.setState(LoadingFooter.State.Idle);
                            }
                        });
            else
                pageStaggeredGridView.setState(LoadingFooter.State.Idle);

            showError(0,false);//如果已经有错误布局 。删除

        }else{//请求不到新数据了
            if(failBean.getStatus().equals("-4")){
                Toast.makeText(this, "您的登录信息已经过期，请重新登录",Toast.LENGTH_SHORT).show();
//                RRTController.getInstance(this).rrtlogout(true);
//                AppManager.getAppManager().removeTopActivity();
            }
            if(failBean.getStatus().equals("-103")
                    ||failBean.getStatus().equals("-102")){//没有更多数据了
                if(isRefreshFromTop){
                    swipeRefreshLayout.setRefreshing(false);
                }
//                else {
//                    pageStaggeredGridView.setState(LoadingFooter.State.Idle);
//                }
                controlWhichShow(1);
            }else{
                if(isRefreshFromTop){
                    swipeRefreshLayout.setRefreshing(false);
                }
//                else {
//                    pageStaggeredGridView.setState(LoadingFooter.State.ERROR);
//                }
                controlWhichShow(2);
            }
        }
    }
    @NonNull
    private void getRequestParams(int page) {
        //RequestParams requestParams = new RequestParams("http://app.renrentou.com/star/GetInvestor");
        requestParams.removeParameter("page");
        requestParams.addBodyParameter("page", page + "");
        requestParams.setExecutor(ThreadPoolUtils.threadPool);
        requestParams.setConnectTimeout(15 * 1000);
        requestParams.setMaxRetryCount(15);
    }

    /**
     * 添加错误布局
     */
    public void addErrorView(Context context){

        View error_view = View.inflate(context, R.layout.error_layout,null);

        tv_error = (TextView) error_view.findViewById(R.id.tv_error);
        getRootView()
                .addView(error_view,
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
    }
    /**
     * 判断如何显示
     * @param statue  状态  1没有数据 2访问失败
     */
    public void controlWhichShow(int statue){
        boolean flag = true;//默认要显示错误布局的情况
        if(userlist!=null && userlist.size()!=0){//屏幕已经有数据的情况下
            if(statue == 1){
                Toast.makeText(this, "没有找到更多数据",Toast.LENGTH_SHORT).show();
            }else if(statue == 2){
                Toast.makeText(this, "网络请求出现问题",Toast.LENGTH_SHORT).show();
            }
            flag = false;//不显示错误布局
        }
        showError(statue, flag);

    }
    /**
     * 显示文字
     * @param statue
     *              1 没有数据  2 请求失败
     * @param flag
     *              true 显示错误布局 false 不显示错误布局
     */
    public void showError(int statue,boolean flag){
        if(statue == 1){
            //1 true 请求不到新数据 界面上还没有数据  显示错误布局 没有相关数据
            if(flag == true){
                tv_error.setText(mEmptyDataString);//先设置错误布局的文字提示
                refreshView(LoadingFooter.State.EMPTY_NOMORE);
            }else{
                //1 false 请求不到新数据 界面上还有数据  显示没有更多数据
                tv_error.setText(mEmptyDataString);//先设置错误布局的文字提示
                refreshView(LoadingFooter.State.TheEnd);
            }
        }else if(statue == 2){
            //2 true 请求失败  界面上没有数据 显示错误布局 网络出错
            if(flag == true){
                tv_error.setText(mErrorDataString);//先设置错误布局的文字提示
                refreshView(LoadingFooter.State.ERROR_EMPTY);
            }else{
                //2 false 请求失败 界面上还有数据 显示请求网络出错
                tv_error.setText(mErrorDataString);//先设置错误布局的文字提示
                refreshView(LoadingFooter.State.ERROR_NOMORE);
            }
        }else if(statue == 0){
            pageStaggeredGridView.setFVisibility(View.GONE);
        }
        if(flag){//显示错误布局
            getRootView()
                    .getChildAt(getRootView().getChildCount() - 1)
                    .setVisibility(View.VISIBLE);
        }else{//不显示错误布局
            getRootView()
                    .getChildAt(getRootView().getChildCount() - 1)
                    .setVisibility(View.GONE);
        }
    }

    private void refreshView(LoadingFooter.State error) {
        if (!isRefreshFromTop) {//不是刷新
            pageStaggeredGridView.setState(error);
            if(error != LoadingFooter.State.ERROR_NOMORE)
                pageStaggeredGridView.setFVisibility(View.GONE);
            LogUtils.i("不是刷新"+error.toString());
        }else {//是刷新
            swipeRefreshLayout.setRefreshing(false);
            pageStaggeredGridView.setState(error);
            //TODO 避免同时出现错误布局和提示
            pageStaggeredGridView.setFVisibility(View.GONE);
//            if (pageStaggeredGridView.getState() == LoadingFooter.State.TheEnd){
//                pageStaggeredGridView.setState(LoadingFooter.State.Idle);
//                LogUtils.i("去掉标记");
//            }
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
        if(cancelable!=null)
            cancelable.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("清除数据信息"+x.app().getCacheDir().toString());
        if(userlist!=null)
            userlist.clear();
        if(requestParams!=null) {
            requestParams.clearParams();
            requestParams = null;
        }

    }
    protected void setmEmptyDataString(String mEmptyDataString) {
        this.mEmptyDataString = mEmptyDataString;
    }

    protected void setmErrorDataString(String mErrorDataString) {
        this.mErrorDataString = mErrorDataString;
    }
}
