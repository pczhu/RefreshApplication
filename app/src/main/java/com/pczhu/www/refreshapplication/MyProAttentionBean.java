package com.pczhu.www.refreshapplication;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 名称：我关注的项目JavaBean
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/3/21 上午11:48
 * 版本：V1.0
 * 修改历史：
 */
public class MyProAttentionBean implements Serializable {

  private String status;
    private String msg;
    /**
     * id : 16810
     * name : 186的测试项目2
     * oneword :
     * img_app : http://img.dev2.renrentou.com/s/upload/project/2016/0324/3b4294f9ae5d0b2625accee11af3ab16.jpg
     * trade_one : 教育培训
     * province : 561
     * city : 615
     * area : 617
     * areatype : 1
     * finance_amount : 200000.00
     * invest_amount : 0.00
     * finance_total : 25000000.00
     * lest_finance : 50000.00
     * amount_begin_time : 1459857600
     * funding_cycle : 30
     * finsh_time : 0
     * status : 2
     * is_deposit : 1
     * deposit : 20
     * deposit_already : 0.00
     * pre_year_return_min : 10.0
     * pre_year_return_max : 20.0
     * is_share : 0
     * frequency_fixed : 2
     * frequency_float : 3
     * opening_time : 1462032000
     * is_buyback : 1
     * is_again : 0
     * is_fixed_return : 1
     * is_float_return : 1
     * provincename : 辽宁省
     * cityname : 丹东
     * areaname : 元宝区
     * diqu : 辽宁省丹东元宝区
     * diqu2 : 辽宁省丹东
     * other_return : 1111
     * lest_finance_str : ¥5万
     * finance_total_str : ¥2500万
     * finance_amount_str : ¥20万
     * deposit_str :  20%
     * percent :  0
     * fixed_return :
     * fixed_return_str : %
     * frequency_fixed_str : 季度
     * float_return_min :
     * float_return_max :
     * float_return_str :
     * frequency_float_str : 半年
     * start_time_fixed : 1970-05-01
     * start_time_float : 2016-11-01
     * pre_year_return : 10.0-20.0
     * buyback_cycle :
     * shortname : 辽宁
     * pre_surplus_time : 8
     * timestamp : 1459159961
     * surplus_time : 39
     * progress_bar : 0
     * address :
     * is_areatype : 1
     * agreement : -1
     * shares_type : 固定+浮动
     * month_rate : 0.00
     * year_rate : 0.00
     * share_amount : -
     * project_share_amount : -
     * periods : 0期
     * status_str : 预热
     */

    private ArrayList<DataEntity> data;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(ArrayList<DataEntity> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
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
