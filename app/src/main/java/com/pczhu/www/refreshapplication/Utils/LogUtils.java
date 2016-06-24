package com.pczhu.www.refreshapplication.Utils;

/**
 * 名称：EveryTest
 * 作用：便于调试的Log工具
 * 描述：
 * Log工具，类似android.util.Log。 tag自动产生，格式:
 * customTagPrefix:className.methodName(Line:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(Line:lineNumber)。
 *
 *
 * allowAll 关闭 全部关闭
 * 单个allow 关闭 不影响string
 * 作者：pczhu
 * 创建时间： 15/12/7 上午10:44
 * 版本：V1.0
 * 修改历史：
 */

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class LogUtils {

    public static String customTagPrefix = "testeverything"; // 自定义Tag的前缀，可以是作者名
    private static final boolean isSaveLog = false; // 是否把保存日志到SD卡中
    public static final String ROOT = Environment.getExternalStorageDirectory()
            .getPath() + "/finddreams/"; // SD卡中的根目录
    private static final String PATH_LOG_INFO = ROOT + "info/";
    public static String[] allowString;
    private LogUtils() {
    }




    // 容许打印日志的类型，默认是true，设置为false则不打印
    public static boolean allowAll = true;
    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;

    /**
     * 处理打印异常
     * @param caller
     * @return
     */
    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    /**
     * 自定义的logger
     */
    public static CustomLogger customLogger;

    public interface CustomLogger {
        /**
         * debug打印
         * @param tag 标签
         * @param content 内容
         */
        void d(String tag, String content);

        /**
         * debug打印 包括异常
         * @param tag 标签
         * @param content 内容
         * @param tr 异常
         */
        void d(String tag, String content, Throwable tr);

        /**
         * error打印
         * @param tag 标签
         * @param content 内容
         */
        void e(String tag, String content);

        /**
         *
         * @param tag 标签
         * @param content 内容
         * @param tr 异常
         */
        void e(String tag, String content, Throwable tr);

        /**
         * info打印
         * @param tag 标签
         * @param content 内容
         */
        void i(String tag, String content);

        /**
         * info 打印包括异常
         * @param tag 标签
         * @param content 内容
         * @param tr 异常
         */
        void i(String tag, String content, Throwable tr);

        /**
         * verbose 打印
         * @param tag 标签
         * @param content 内容
         */
        void v(String tag, String content);

        /**
         * verbose 打印 包括异常
         * @param tag 标签
         * @param content 内容
         * @param tr 异常
         */
        void v(String tag, String content, Throwable tr);

        /**
         * warn 打印
         * @param tag 标签
         * @param content 内容
         */
        void w(String tag, String content);

        /**
         * warn 打印 包括异常
         * @param tag 标签
         * @param content 内容
         * @param tr 异常
         */
        void w(String tag, String content, Throwable tr);

        /**
         * warn 打印 只打印异常
         * @param tag 标签
         * @param tr  异常
         */
        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }


    /**
     * debug 1
     * @param content
     */
    public static void d(String content) {
        swichHandle(content,allowD,null,1);
    }

    /**
     * debug 2
     * @param content
     * @param tr
     */
    public static void d(String content, Throwable tr) {
        swichHandle(content,allowD,tr,2);
    }

    /**
     * error 3
     * @param content
     */
    public static void e(String content) {
        swichHandle(content,allowE,null,3);
    }

    /**
     * error 4
     * @param content
     * @param tr
     */
    public static void e(String content, Throwable tr) {
        swichHandle(content,allowE,tr,4);
    }

    public static void i(String content) {
        swichHandle(content,allowI,null,5);
    }

    public static void i(String content, Throwable tr) {
        swichHandle(content,allowI,tr,6);
    }

    public static void v(String content) {
        swichHandle(content,allowV,null,7);
    }

    public static void v(String content, Throwable tr) {
        swichHandle(content,allowV,tr,8);
    }

    public static void w(String content) {
        swichHandle(content,allowW,null,9);
    }

    public static void w(String content, Throwable tr) {
        swichHandle(content,allowW,tr,10);
    }

    public static void w(Throwable tr) {
        swichHandle("",allowW,tr,11);
    }

    public static void wtf(String content) {
        swichHandle(content,allowWtf,null,12);
    }

    public static void wtf(String content, Throwable tr) {
        swichHandle(content,allowWtf,null,13);
    }

    public static void wtf(Throwable tr) {
        swichHandle("",allowWtf,null,14);
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[5];
    }

    /**
     * 打印
     * @param path
     * @param tag
     * @param msg
     */
    public static void point(String path, String tag, String msg) {
        if (isSDAva()) {
            Date date = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.SIMPLIFIED_CHINESE);
            dateFormat.applyPattern("yyyy");
            path = path + dateFormat.format(date) + "/";
            dateFormat.applyPattern("MM");
            path += dateFormat.format(date) +"/" ;
            dateFormat.applyPattern("dd");
            path += dateFormat.format(date) + ".log";
            dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
            String time = dateFormat.format(date);
            File file = new File(path);
            if (!file.exists())
                createDipPath(path);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(time +":"+ tag +":"+msg +"");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param file
     */
    public static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A little trick to reuse a formatter in the same thread
     */
    private static class ReusableFormatter {

        private Formatter formatter;
        private StringBuilder builder;

        public ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }

        public String format(String msg, Object... args) {
            formatter.format(msg, args);
            String s = builder.toString();
            builder.setLength(0);
            return s;
        }
    }

    private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>() {
        protected ReusableFormatter initialValue() {
            return new ReusableFormatter();
        }
    };

    public static String format(String msg, Object... args) {
        ReusableFormatter formatter = thread_local_formatter.get();
        return formatter.format(msg, args);
    }

    public static boolean isSDAva() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageDirectory().exists()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 逻辑处理
     * @param content
     * @param allow
     * @param type
     */
    private static void swichHandle(String content,boolean allow,Throwable tr,int type) {
        if(!allowAll){
            return;
        }
        if(allow){
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            letShow(content, tag, tr, type);
        }else if(allowString != null){
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            for (String str:allowString) {
                if(str!=null && tag.contains(str) ){
                    letShow(content, tag ,tr, type);
                }
            }
        }else{
            return;
        }
    }
    /**
     *
     * @param content 内容
     * @param tag  标签
     * @param type 类型
     */
    private static void letShow(String content, String tag, Throwable tr, int type) {
        switch (type){
            case 1:
                if (customLogger != null) {
                    customLogger.d(tag, content);
                } else {
                    Log.d(tag, content);
                }
                break;
            case 2:
                if (customLogger != null) {
                    customLogger.d(tag, content, tr);
                } else {
                    Log.d(tag, content, tr);
                }
                break;
            case 3:
                if (customLogger != null) {
                    customLogger.e(tag, content);
                } else {
                    Log.e(tag, content);
                }
                if (isSaveLog) {
                    point(PATH_LOG_INFO, tag, content);
                }
                break;
            case 4:
                if (customLogger != null) {
                    customLogger.e(tag, content, tr);
                } else {
                    Log.e(tag, content, tr);
                }
                if (isSaveLog) {
                    point(PATH_LOG_INFO, tag, tr.getMessage());
                }
                break;
            case 5:
                if (customLogger != null) {
                    customLogger.i(tag, content);
                } else {
                    Log.i(tag, content);
                }
                break;
            case 6:
                if (customLogger != null) {
                    customLogger.i(tag, content, tr);
                } else {
                    Log.i(tag, content, tr);
                }
                break;
            case 7:
                if (customLogger != null) {
                    customLogger.v(tag, content);
                } else {
                    Log.v(tag, content);
                }
                break;
            case 8:
                if (customLogger != null) {
                    customLogger.v(tag, content, tr);
                } else {
                    Log.v(tag, content, tr);
                }
                break;
            case 9:
                if (customLogger != null) {
                    customLogger.w(tag, content);
                } else {
                    Log.w(tag, content);
                }
                break;
            case 10:
                if (customLogger != null) {
                    customLogger.w(tag, content, tr);
                } else {
                    Log.w(tag, content, tr);
                }
                break;
            case 11:
                if (customLogger != null) {
                    customLogger.w(tag, tr);
                } else {
                    Log.w(tag, tr);
                }
                break;
            case 12:
                if (customLogger != null) {
                    customLogger.wtf(tag, content);
                } else {
                    Log.wtf(tag, content);
                }
                break;
            case 13:
                if (customLogger != null) {
                    customLogger.wtf(tag, content, tr);
                } else {
                    Log.wtf(tag, content, tr);
                }
                break;
            case 14:
                if (customLogger != null) {
                    customLogger.wtf(tag, tr);
                } else {
                    Log.wtf(tag, tr);
                }
                break;
        }

    }

}

