package com.pczhu.www.refreshapplication;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pczhu.www.refreshapplication.baserefresh.MyBaseAdapter;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * 名称：我关注的项目Adapter
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/3/21 下午5:26
 * 版本：V1.0
 * 修改历史：
 */
public class MyProAttentionActivityAdapter extends MyBaseAdapter<MyProAttentionBean.DataEntity> {
    ImageOptions imageOptions;
    LinearLayout.LayoutParams layoutParams;
    public MyProAttentionActivityAdapter(Context context, ArrayList<MyProAttentionBean.DataEntity> userList) {
        super(context, userList);
        imageOptions = ImageOptions.DEFAULT;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyProAttentionBean.DataEntity bean = userList.get(position);
        Holder holder;
        if(convertView == null){
            convertView = View.inflate(mContext,R.layout.item_myproject_attention,null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        x.image().bind(holder.img,bean.getImg_app(),ImageOptions.DEFAULT);
        holder.tv_title.setText(bean.getName());
        return  convertView;
    }


    public class Holder{
        @ViewInject(R.id.img)
        private ImageView img;
        @ViewInject(R.id.name)
        private TextView tv_title;

        public Holder(){

        }
        public Holder(View view){
            x.view().inject(this,view);
        }
    }
}
