package com.kesar.library.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * 
 * ClassName: ScreenUtils 
 * @Description: 屏幕工具类
 * @author kesar
 * @date 2015-12-15
 */
public class ScreenUtils
{
	/**
	 * 
	 * @Description: 得到屏幕尺寸
	 * @param @param context
	 * @param @return
	 * @return Point
	 * @throws
	 * @author kesar
	 * @date 2015-12-1
	 */
	public static Point getScreenSize(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Point point = new Point();
		wm.getDefaultDisplay().getSize(point);
		return point;
	}
}
