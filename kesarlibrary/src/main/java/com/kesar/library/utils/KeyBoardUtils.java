package com.kesar.library.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 
 * ClassName: KeyBoardUtils
 * 
 * @Description: 输入法相关的工具类
 * @author kesar
 * @date 2015年10月9日
 */
public class KeyBoardUtils
{
	/**
	 * 
	 * @Description: 隐藏软键盘
	 * @param @param activity
	 * @param @param callBack
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015年10月9日
	 */
	public static void hideKeyboard(Context context)
	{
		InputMethodManager manager = getManager(context);
		if (null != manager && manager.isActive())
		{
			View v = ((Activity) context).getCurrentFocus();
			if (v != null)
			{
				manager.hideSoftInputFromWindow(v.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	
	public static void hideKeyboard(Context context,View v)
	{
		InputMethodManager manager = getManager(context);
		if (null != manager && manager.isActive())
		{
			if (v != null)
			{
				manager.hideSoftInputFromWindow(v.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 
	 * @Description: 显示软键盘
	 * @param @param activity
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015年10月9日
	 */
	public static void showKeyboard(Context context)
	{
		InputMethodManager manager = getManager(context);
		if (null != manager)
		{
			View v = ((Activity) context).getCurrentFocus();
			if (v != null)
			{
				manager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
			}
		}
	}
	
	/**
	 * 
	 * @Description: 显示键盘
	 * @param @param context
	 * @param @param v   
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-12-18
	 */
	public static void showKeyboard(Context context,View v)
	{
		InputMethodManager manager = getManager(context);
		if (null != manager)
		{
			if (v != null)
			{
				manager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
			}
		}
	}

	/**
	 * 
	 * @Description: 获取Manager
	 * @param @param context
	 * @param @return   
	 * @return InputMethodManager  
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static InputMethodManager getManager(Context context)
	{
		return (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	/**
	 * 
	 * @Description: 判断键盘是否显示(建议使用{@link #isKeyBoardShow(Context context,View view)})
	 * @param @param context 
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author kesar
	 * @date 2015-12-18
	 */
	public static boolean isKeyBoardShow(Context context)
	{
		return getManager(context).isActive();
	}
	
	/**
	 * 
	 * @Description: 判断键盘是否显示
	 * @param @param context
	 * @param @param view 
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author kesar
	 * @date 2015-12-18
	 */
	public static boolean isKeyBoardShow(Context context,View view)
	{
		return getManager(context).isActive(view);
	}
}
