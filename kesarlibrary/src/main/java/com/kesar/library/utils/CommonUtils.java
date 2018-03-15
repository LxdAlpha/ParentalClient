package com.kesar.library.utils;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.PowerManager;

/**
 * 
 * ClassName: CommonUtils
 * 
 * @Description: 常用控件
 * @author kesar
 * @date 2015-11-12
 */
public class CommonUtils
{
	/**
	 * 
	 * @Description: 检测网络是否可用
	 * @param @param context
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static boolean isNetWorkConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null)
			{
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 
	 * @Description: 检测Sdcard是否存在
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static boolean isExitsSdcard()
	{
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 
	 * @Description: 得到SD的路径
	 * @param @return
	 * @return File
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static File getSdcardPath()
	{
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * 
	 * @Description: 判断应用是否在后台
	 * @param @param context
	 * @param @return  true 在后台 false 在前台
	 * @return boolean  
	 * @throws
	 * @author kesar
	 * @date 2015-12-10
	 */
	public static boolean isBackground(Context context)
	{
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses)
		{
			if (appProcess.processName.equals(context.getPackageName()))
			{
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
				{
					KLog.debug("后台");
					return true;
				}
				else
				{
					KLog.debug("前台");
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Description: 判断手机是否熄屏 (api 14以上)
	 * @param @param context
	 * @param @return  true 没有熄屏，false 熄屏
	 * @return boolean  
	 * @throws
	 * @author kesar
	 * @date 2015-12-10
	 */
	public static boolean isScreenOn(Context context)
	{
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn=pm.isScreenOn();
		if(isScreenOn)
		{
			KLog.debug("没熄屏");
		}
		else
		{
			KLog.debug("熄屏");
		}
		return isScreenOn;
	}
}
