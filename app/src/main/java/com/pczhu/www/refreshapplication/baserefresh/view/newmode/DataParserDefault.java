package com.pczhu.www.refreshapplication.baserefresh.view.newmode;

/**
 * 名称：
 * 作用：
 * 描述：
 * 作者：pczhu
 * 创建时间： 16/6/1 下午2:21
 * 版本：V1.0
 * 修改历史：
 */
public class DataParserDefault<T> implements DataParserInterface<T> {
    @Override
    public T getParsedDataObject(String s) {
/*
        Gson gson = new Gson();
        Failed failBean = null;
        String status = null;
        try {
            T mT = gson.fromJson(s, Class.forName(T));
            JSONObject dataJson=new JSONObject(s);
            status = dataJson.getString("status");
            if(!"-101".equals(status)){
                failBean =gson.fromJson(s, Failed.class);
            }
        }catch (Exception e){
            e.printStackTrace();
            failBean =gson.fromJson(s, Failed.class);
        }
*/


        return null;
    }
}
