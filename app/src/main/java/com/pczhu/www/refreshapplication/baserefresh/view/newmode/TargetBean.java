package com.pczhu.www.refreshapplication.baserefresh.view.newmode;

import java.util.ArrayList;

/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/6/1 下午1:36
 * 版本：V1.0
 * 修改历史：
 */
public class TargetBean extends ResultData {
    private ArrayList<DataEntity> data;


    public void setData(ArrayList<DataEntity> data) {
        this.data = data;
    }


    public ArrayList<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String id;
        private String name;

        private String img_app;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg_app() {
            return img_app;
        }

        public void setImg_app(String img_app) {
            this.img_app = img_app;
        }
    }

}
