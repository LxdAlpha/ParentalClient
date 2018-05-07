package com.kesar.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 * ClassName: ExpandGridView
 * 
 * @Description: 格子布局，修复GridView在ListView中显示不全
 * @author kesar
 * @date 2015年11月6日
 */
@SuppressLint("NewApi")
public class ExpandGridView extends GridView
{

	public ExpandGridView(Context context)
	{
		super(context);
	}

	public ExpandGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ExpandGridView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	public ExpandGridView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
