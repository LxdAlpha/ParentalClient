package com.kesar.library.widget;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 
 * ClassName: KPopupWindow 
 * @Description: 下弹出框
 * @author kesar
 * @date 2015-12-2
 */
public class KPopupWindow extends PopupWindow
{
	public KPopupWindow()
	{}

	public KPopupWindow(View view,View parent)
	{
		this(view,parent, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
	}
	
	public KPopupWindow(View view,View parent,int width,int height,boolean focusable)
	{
		super(view, width, height, focusable);
		setAnimationStyle(android.R.style.Animation_InputMethod);
		setFocusable(true);
		setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
}
