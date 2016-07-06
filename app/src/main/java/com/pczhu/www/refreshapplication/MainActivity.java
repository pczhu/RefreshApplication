package com.pczhu.www.refreshapplication;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pczhu.www.refreshapplication.baserefresh.view.PageStaggeredGridView;
import com.pczhu.www.refreshapplication.baserefresh.view.newmode.MessageEvent;
import com.pczhu.www.refreshapplication.baserefresh.view.newmode.TheBaseRefreshAndLoadActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_mypro_attention)
public class MainActivity extends TheBaseRefreshAndLoadActivity<MyProAttentionBean.DataEntity> {
    private static final String TAG = "MyProAttentionActivity";

    @ViewInject(R.id.swipe_container)
    private SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.grid_view)
    private PageStaggeredGridView pageStaggeredGridView;
    @ViewInject(R.id.root)
    private FrameLayout frameLayout;
    RequestParams myRequestParams;
    private MyProAttentionActivityAdapter myProAttentionActivityAdapter;
    private EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = EventBus.getDefault();
        eventBus.register(this);

        pageStaggeredGridView.setSwipeRefreshLayout(swipeRefreshLayout);
        if(myProAttentionActivityAdapter == null)
            myProAttentionActivityAdapter = new MyProAttentionActivityAdapter(this,null);
        myRequestParams = new RequestParams("http://app.renrentou.com/project/getProjectList");

        openListener(swipeRefreshLayout,
                pageStaggeredGridView,
                myProAttentionActivityAdapter,
                MyProAttentionBean.class,
                myRequestParams,
                null
        );
        start();

    }

    @Override
    protected ViewGroup getRootView() {
        return frameLayout;
    }

    @Override
    public ArrayList getObjectList(Object obj) {
        return ((MyProAttentionBean)obj).getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }
}
