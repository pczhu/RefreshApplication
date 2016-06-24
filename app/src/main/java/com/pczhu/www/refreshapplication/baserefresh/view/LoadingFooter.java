package com.pczhu.www.refreshapplication.baserefresh.view;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;

import com.flyco.roundview.RoundTextView;
import com.pczhu.www.refreshapplication.R;
import com.pczhu.www.refreshapplication.Utils.LogUtils;
import com.wang.avi.AVLoadingIndicatorView;



/**
 * Created by storm on 14-4-12.
 */
public class LoadingFooter {
    //private long delaytime = 0;
    private long oldtime = 0;
    private Context mContext;
    private RoundTextView mLoadingText;
    protected View mLoadingFooter;
    /**
     * 终止TheEnd
     */
    private AVLoadingIndicatorView avLoadingIndicatorView;
    /**
     * 波浪效果文字
     */
    //TitanicTextView mTitanicText;

    //private Titanic mTitanic;

    protected State mState = State.Idle;

    public static enum State {
        /**
         * 无Loading 正常状态下
         */
        Idle,

        /**
         * Loading中
         */
        Loading,
        /**
         * 请求不到新数据 界面上还有数据  显示没有更多数据
         */
        TheEnd,
        /**
         * 请求不到新数据 界面上还没有数据  显示错误布局 没有相关数据
         */
        EMPTY_NOMORE,
        /**
         * 界面上没有数据 显示错误布局 网络出错
         */
        ERROR_EMPTY,
        /**
         *请求失败 界面上还有数据 显示请求网络出错
         */
        ERROR_NOMORE

    }

    public LoadingFooter(Context context) {
        this.mContext = context;
        mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.loading_footer, null);
        mLoadingFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 屏蔽点击
            }
        });
        mLoadingText = (RoundTextView) mLoadingFooter.findViewById(R.id.textView);
        avLoadingIndicatorView = (AVLoadingIndicatorView) mLoadingFooter.findViewById(R.id.avloadingIndicatorView);
        setState(State.Idle);
    }

    /**
     * 获取布局
     * @return
     */
    public View getView() {
        return mLoadingFooter;
    }

    /**
     * 获得状态
     * @return
     */
    public State getState() {
        return mState;
    }

    public void setState(final State state, long delay) {
        if(delay < 1000){
            delay = 1000;
        }
        mLoadingFooter.postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(state);
            }
        }, delay);
    }
    //                SolidToast.make((Activity) mContext, "没有更多数据",Gravity.BOTTOM)
//                        .setBackgroundColorId(R.color.blue)
//                        .show();
    public void setState(State status) {
//        if (mState == status) {
//            return;
//        }
        mState = status;

       // mLoadingFooter.setVisibility(View.VISIBLE);
        switch (status) {
            case Loading://正在进行中
                mLoadingFooter.setVisibility(View.VISIBLE);
                setRorAVisibility(1);
                oldtime = SystemClock.currentThreadTimeMillis();
                break;
            case TheEnd://没有更多数据  允许
                mLoadingFooter.setVisibility(View.GONE);
                setRorAVisibility(0);
//                mLoadingFooter.setVisibility(View.VISIBLE);
//                mLoadingText.setText("到底啦，找不到更多数据");
                LogUtils.i("状态改变TheEnd");
                break;
            case EMPTY_NOMORE://请求不到新数据 界面上还没有数据  显示错误布局 没有相关数据 不允许
                mLoadingFooter.setVisibility(View.GONE);
                setRorAVisibility(0);
                mLoadingText.setText("找不到数据了");
                LogUtils.i("状态改变EMPTY_NOMORE");
                break;
            case ERROR_EMPTY://界面上没有数据 显示错误布局 网络出错 不允许
                mLoadingFooter.setVisibility(View.GONE);
                setRorAVisibility(2);
                mLoadingText.setText("请求网络出错，下拉重新加载");
                LogUtils.i("状态改变ERROR_EMPTY");
                break;
            case ERROR_NOMORE://请求失败 界面上还有数据 显示请求网络出错 允许
                oldtime = SystemClock.currentThreadTimeMillis();
                mLoadingFooter.setVisibility(View.VISIBLE);
                setRorAVisibility(2);
                mLoadingText.setText("请求网络出错");
                LogUtils.i("状态改变ERROR_NOMORE");
                break;
            case Idle://恢复
                LogUtils.i("状态改变IDLE");
                long delaytime = SystemClock.currentThreadTimeMillis() - oldtime;
                dismiss(delaytime);
                break;
            default:
                break;
        }
    }

    /**
     * 0 不显示 1 显示动画 2 显示文字
     * @param i
     */
    private void setRorAVisibility(int i) {
        if(i == 0){
            avLoadingIndicatorView.setVisibility(View.GONE);
            mLoadingText.setVisibility(View.GONE);
        }else if(i == 1){
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            mLoadingText.setVisibility(View.GONE);
        }else if(i == 2){
            avLoadingIndicatorView.setVisibility(View.GONE);
            mLoadingText.setVisibility(View.VISIBLE);
        }

    }

    private void dismiss(long delaytime) {
        if(delaytime > 1500 ){
            delaytime = 0;
        }else{
            delaytime = 1500;
        }
        mLoadingFooter.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingFooter.setVisibility(View.GONE);
                avLoadingIndicatorView.setVisibility(View.GONE);
                mLoadingText.setVisibility(View.GONE);
               // SolidToast.hideToastView((Activity) mContext);
            }
        }, delaytime);
    }
}
