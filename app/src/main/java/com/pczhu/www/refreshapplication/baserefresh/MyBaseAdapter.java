package com.pczhu.www.refreshapplication.baserefresh;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * 名称：CustomTestxUtils
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 15/11/20 上午11:59
 * 版本：V1.0
 * 修改历史：
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected ArrayList<T> userList;
    protected Context mContext;
    public MyBaseAdapter(Context context, ArrayList<T> userList) {
        this.userList = userList;
        this.mContext = context;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getCount() {
        return userList!= null?userList.size() : 0;
    }

    public void notifyDataSetChanged(ArrayList<T> userList) {
        this.userList = userList;
        super.notifyDataSetChanged();
    }

    public T getOneDataCell(int position){
        return userList.get(position);
    }
}
