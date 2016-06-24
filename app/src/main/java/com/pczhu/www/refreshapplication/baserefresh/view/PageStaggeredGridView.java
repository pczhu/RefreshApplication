package com.pczhu.www.refreshapplication.baserefresh.view;

/**
 * 名称：CustomTestxUtils
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 15/11/20 上午11:03
 * 版本：V1.0
 * 修改历史：
 */

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;

import com.etsy.android.grid.StaggeredGridView;
import com.pczhu.www.refreshapplication.Utils.LogUtils;


/**
 * 自定义不规则GridView
 * Created by storm on 14-5-6.
 */
public class PageStaggeredGridView extends StaggeredGridView implements AbsListView.OnScrollListener {
    private LoadingFooter mLoadingFooter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnLoadNextListener mLoadNextListener;
    private int stateScroll = SCROLL_STATE_IDLE;
    public PageStaggeredGridView(Context context) {
        super(context);
        init();
    }

    public PageStaggeredGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageStaggeredGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mLoadingFooter = new LoadingFooter(getContext());
        addFooterView(mLoadingFooter.getView());
        setOnScrollListener(this);

    }

    public void setLoadNextListener(OnLoadNextListener listener) {
        mLoadNextListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.stateScroll = scrollState;
    }
    /**
     * firstVisibleItem 表示在当前屏幕显示的第一个listItem在整个listView里面的位置（下标从0开始）
     * visibleItemCount表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数
     * totalItemCount表示ListView的ListItem总数
     * listView.getLastVisiblePosition()表示在现时屏幕最后一个ListItem
     * (最后ListItem要完全显示出来才算)在整个ListView的位置（下标从0开始）
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0 && swipeRefreshLayout!= null)
            swipeRefreshLayout.setEnabled(true);
        else if(firstVisibleItem != 0 && swipeRefreshLayout != null)
            swipeRefreshLayout.setEnabled(false);
        LogUtils.i(mLoadingFooter.getState().toString());

        if (mLoadingFooter.getState() == LoadingFooter.State.Loading) {
            LogUtils.i("加载中LoadingFooter.State.Loading");
            return;
        }
        if(mLoadingFooter.getState() == LoadingFooter.State.ERROR_NOMORE){
            LogUtils.i("加载中LoadingFooter.State.ERROR_NOMORE");
            return;
        }
        if(mLoadingFooter.getState() == LoadingFooter.State.TheEnd){
            setState(LoadingFooter.State.TheEnd);
            LogUtils.i("无数据LoadingFooter.State.TheEnd");
            return;
        }
        if(mLoadingFooter.getState() == LoadingFooter.State.ERROR_EMPTY
                || mLoadingFooter.getState() == LoadingFooter.State.EMPTY_NOMORE){//没有网还没有数据展示了错误页面的时候+没有网还有数据的时候
            LogUtils.i("错误页面展示了LoadingFooter.State.ERROR");
            return;
        }
        if(stateScroll == SCROLL_STATE_IDLE){
            LogUtils.i("SCROLL_STATE_IDLE");
            return;
        }

        if (firstVisibleItem + visibleItemCount >= totalItemCount
                && totalItemCount != 0
                && totalItemCount != getHeaderViewsCount() + getFooterViewsCount()
                && mLoadNextListener != null
                && !canChildScrollDown()) {
            LogUtils.i("开始Loading");
            mLoadingFooter.setState(LoadingFooter.State.Loading);
            mLoadNextListener.onLoadNext();
        }
    }

    float x1;
    float y1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件

        float x2;
        float y2;
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 50) {
                int i = getLastVisiblePosition()+1;
                LogUtils.i("当前显示数据：" + i + ";总共的数据：" + getCount() + "状态" + mLoadingFooter.getState().toString());
                if(mLoadingFooter.getState() == LoadingFooter.State.EMPTY_NOMORE
                        || mLoadingFooter.getState() == LoadingFooter.State.ERROR_EMPTY){
                    return super.onTouchEvent(event);
                }
                //TODO 添加对ERROR的判断
                if(mLoadingFooter.getState() == LoadingFooter.State.TheEnd && i == getCount()){
                   // SolidToast.make((Activity) getContext(), "没有更多数据",Gravity.BOTTOM).setBackgroundColorId(R.color.blue).show();
                    setState(LoadingFooter.State.TheEnd);
                    LogUtils.i("进入TheEnd");
                    return super.onTouchEvent(event);
                }
                if(canChildScrollDown()){
                    return super.onTouchEvent(event);
                }
                if(mLoadingFooter.getState() == LoadingFooter.State.ERROR_NOMORE && mLoadNextListener != null){
                    LogUtils.i("加载更多失败 继续加载更多");
                    mLoadingFooter.setState(LoadingFooter.State.Loading);
                    mLoadNextListener.onLoadNext();
                }
                //处理特殊情况2：如果数据不满一个屏幕 如何加载更多
                if(stateScroll== SCROLL_STATE_IDLE && mLoadNextListener != null) {
                    LogUtils.i("onTouch : SCROLL_STATE_IDLE");
                    mLoadingFooter.setState(LoadingFooter.State.Loading);
                    mLoadNextListener.onLoadNext();
                }
                //处理特殊情况3：如果数据正好满一个屏幕 如何加载更多

            }
        }
        return super.onTouchEvent(event);
    }

    public void setState(LoadingFooter.State status) {
        mLoadingFooter.setState(status);
    }

    public void setState(LoadingFooter.State status, long delay) {
        mLoadingFooter.setState(status, delay);
    }
    public LoadingFooter.State getState(){
        return mLoadingFooter.getState();
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }
    public void setFVisibility(int visivility){
        mLoadingFooter.getView().setVisibility(visivility);
    }
    public void setSelection(){
        this.setSelection(-1);
    }

    /**
     * 能继续向下滑动么
     * 为了以后贴到不是AbsListview中也可以用 多做了判断
     * @return
     */
    public boolean canChildScrollDown() {

        if (Build.VERSION.SDK_INT < 14) {
            if (this instanceof AbsListView) {

                if (this.getChildCount() > 0) {//如果item数目大于0
                    int lastChildBottom = this.getChildAt(this.getChildCount() - 1).getBottom();//最后一个item的下边沿
                    return this.getLastVisiblePosition() == this.getAdapter().getCount() - 1 && lastChildBottom <= this.getMeasuredHeight();//(当前最下边的位置 == 最后一个item) && （最后一个item的下边沿 <= 控件整体高度）到最底下了 肯定可以往下滑动
                } else {
                    return false;//如果连数据都没有肯定 false 不能滑动
                }

            } else {
                //负数向上  正数向下
                return ViewCompat.canScrollVertically(this.getChildAt(0), 1) || this.getChildAt(0).getScrollY() > 0;//如果第一个item可以往下滑动  ||  第一个控件的scrollY > 0 (意思就是一部分藏在上部分)
            }
        } else {
            return ViewCompat.canScrollVertically(this.getChildAt(0), 1);
        }
    }
    /**
     * 能继续向上滑动么
     * 为了以后贴到不是AbsListview中也可以用 多做了判断
     * @return
     */
    public boolean canChildScrollUp(){
        if(Build.VERSION.SDK_INT < 14){
            if(this instanceof AbsListView){
                if(this.getChildCount() > 0){
                    int firstChildTop = this.getChildAt(0).getTop();
                    return this.getFirstVisiblePosition() > 0 || firstChildTop < this.getPaddingTop();
                }else{
                    return false;
                }
            }else{
                return ViewCompat.canScrollVertically(this.getChildAt(0),-1) || this.getChildAt(0).getScaleY() < 0;
            }
        }else{
            return ViewCompat.canScrollVertically(this.getChildAt(0),-1);
        }
    }
}