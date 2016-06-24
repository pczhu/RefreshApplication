package com.pczhu.www.refreshapplication.Utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import com.pczhu.www.refreshapplication.RRTApplication;

import java.util.List;


/**
 * 
 * 创建日期 ：2015年2月3日上午10:34:04
 * 
 * 描述： 系统 程序 基本信息工具类
 * 
 * @author zhudaoyan
 * @version 1.0
 * 
 *          修改历史：
 */
public class SystemUtils {
	/**
	 * 获取程序上下文
	 * 
	 * @return 上下文对象
	 */
	public static Context getContext() {
		return RRTApplication.getApplication();
	}

	/**
	 * 获取屏幕的分辨率
	 * 
	 * @return 分辨率数组
	 */
	@SuppressWarnings("deprecation")
	public static int[] getResolution() {
		Context context = getContext();
		if (null == context) {
			return null;
		}
		WindowManager windowMgr = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int[] res = new int[2];
		res[0] = windowMgr.getDefaultDisplay().getWidth();
		res[1] = windowMgr.getDefaultDisplay().getHeight();
		return res;
	}

	/**
	 * 获取android系统版本号
	 * 
	 * @return 版本号字符串
	 * @author fs
	 */
	public static String getOSVersion() {
		String release = android.os.Build.VERSION.RELEASE; // android系统版本号
		release = "android" + release;
		return release;
	}
	
	/**
	 * 获取手机型号
	 * 
	 * @return 手机型号，比如：小米4
	 * @author fs
	 */
	public static String getPhoneModel() {
		String phonemodel = android.os.Build.MODEL;
		return phonemodel;
	}
	/**
	 * 获取手机ID
	 * 
	 * @return 手机IMIE
	 * @author fs
	 */
	public static String getIMEI() {
		Context context = getContext();
		if (null == context) {
			return null;
		}
		String imei = null;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();
		} catch (Exception e) {
			//LogUtils.e(e);
		}
		return imei;
	}

	/**
	 * 获取android系统版本号
	 * 
	 * @return 版本号字符串
	 */
	public static String getOSVersionSDK() {
		return android.os.Build.VERSION.SDK;
	}

    public static boolean isAppOnForeground(Context context) {

             
            ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            String packageName = context.getApplicationContext().getPackageName();

            List<RunningAppProcessInfo> appProcesses = activityManager
                            .getRunningAppProcesses();
            if (appProcesses == null)
                    return false;

            for (RunningAppProcessInfo appProcess : appProcesses) {
                    if (appProcess.processName.equals(packageName)
                                    && (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
                    	
                    	System.out.println("前台这是当前的进程"+appProcess.processName);
                        return true;
                    }
                   System.out.println("后台这是当前的进程"+appProcess.processName);
            }

            return false;
    }
    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取屏幕宽度高度
     * @param context
     * @return  宽高数组 [宽度，高度]
     */
    public static int[] getScreenMaxWidth(Context context){
        //DisplayMetrics metrics = new DisplayMetrics();
        //getW

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return new int[]{width,height};
    }
	public static int checkSDKVersion(Context context){
		int targetSdkVersion = -1;
		try {
			final PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			targetSdkVersion = info.applicationInfo.targetSdkVersion;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return targetSdkVersion;
	}
	/**
	 * 取得了当前版本名称
	 *
	 * @return
	 */
	public static String getVersionName(Context context) {
		String ver = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			ver = packInfo.versionName;

		} catch (PackageManager.NameNotFoundException e) {
			Log.e("nationz", "获取软件的版本号异常：", e);
			return ver;
		}
		return ver;
	}
	/**
	 * 取得了当前版本号
	 *
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int ver = 0;
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			ver = packInfo.versionCode;

		} catch (PackageManager.NameNotFoundException e) {
			Log.e("nationz", "获取软件的版本号异常：", e);
			return ver;
		}
		return ver;
	}


}
