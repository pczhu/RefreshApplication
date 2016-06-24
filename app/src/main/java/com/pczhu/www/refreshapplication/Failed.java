package com.pczhu.www.refreshapplication;

import java.io.Serializable;

/**
 * 名称：Failed
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 15/12/16 上午11:24
 * 版本：V1.0
 * 修改历史：
 */
public class Failed implements Serializable{
    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatue(String statue) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
