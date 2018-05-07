package com.kesar.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * ClassName: SharedPreferencesUtils 
 * @Description: 设置配置工具类
 * @author kesar
 * @date 2015-12-4
 */
public class SharedPreferencesUtils
{
	/** 设置的文件 */
	public final static String SETTING = "setting";

	/**
	 * 
	 * @Description: 写入值
	 * @param @param context
	 * @param @param key
	 * @param @param value   
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static void putValue(Context context, String key, int value)
	{
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putInt(key, value);
		sp.commit();
	}

	/**
	 * 
	 * @Description: 写入值
	 * @param @param context
	 * @param @param key
	 * @param @param value   
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static void putValue(Context context, String key, boolean value)
	{
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putBoolean(key, value);
		sp.commit();
	}

	/**
	 * 
	 * @Description: 写入值
	 * @param @param context
	 * @param @param key
	 * @param @param value   
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static void putValue(Context context, String key, String value)
	{
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putString(key, value);
		sp.commit();
	}
	
	/**
	 * 
	 * @Description: 删除key
	 * @param @param context
	 * @param @param key   
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static void removeValue(Context context, String key)
	{
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.remove(key);
		sp.commit();
	}
	
	/**
	 * 
	 * @Description: 得到值
	 * @param @param context
	 * @param @param key
	 * @param @param defValue
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static int getValue(Context context, String key, int defValue)
	{
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		int value = sp.getInt(key, defValue);
		return value;
	}

	/**
	 * 
	 * @Description: 得到值
	 * @param @param context
	 * @param @param key
	 * @param @param defValue
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static boolean getValue(Context context, String key, boolean defValue)
	{
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		boolean value = sp.getBoolean(key, defValue);
		return value;
	}

	/**
	 * 
	 * @Description: 得到值
	 * @param @param context
	 * @param @param key
	 * @param @param defValue
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static String getValue(Context context, String key, String defValue)
	{
		// TODO 这里会有个NullPointerException
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		String value = sp.getString(key, defValue);
		return value;
	}
}
