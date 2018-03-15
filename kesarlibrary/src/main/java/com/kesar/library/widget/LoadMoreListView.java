package com.kesar.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * ClassName: LoadMoreListView
 * 
 * @Description: 加载更多地listview
 * @author kesar
 * @date 2015-11-16
 */
public class LoadMoreListView extends ListView
{
	private OnLoadMoreListener mOnLoadMoreListener;
	private Footer mFooter;
	private boolean isEnd = false;

	public LoadMoreListView(Context context)
	{
		super(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
		setOnScrollListener(new OnScrollListener()
		{
			private int lastItemIndex = Integer.MIN_VALUE;// 当前ListView中最后一个Item的索引

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				// &&mFooter!=null&&mFooter.footView!=null&&mFooter.footView.hasWindowFocus()
				System.err.println("getItemCount():" + getItemCount());
				System.err.println("lastItemIndex:" + lastItemIndex);
				System.err.println("getFooterViewsCount():"
						+ getFooterViewsCount());
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastItemIndex == getItemCount() - 1
						&& getFooterViewsCount() != 0)
				{
					if (mOnLoadMoreListener != null)
					{
						showLoading();
						mOnLoadMoreListener.onLoad();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{
				lastItemIndex = firstVisibleItem + visibleItemCount - 1
						- getFooterViewsCount() - getHeaderViewsCount();
				if (getFooterViewsCount() != 0)
				{
					int visibleListItemCount = visibleItemCount
							- getFooterViewsCount();
					if (visibleListItemCount == 0
							|| (visibleListItemCount >= getItemCount()
									&& isLastItemVisible() && isFirstItemVisible()))
					{
						lastItemIndex = Integer.MIN_VALUE;
						removeFooterView(mFooter.footView);
					}
				}
				else
				{
					if (visibleItemCount != 0 && visibleItemCount <= getCount()
							&& !isLastItemVisible())
					{
						addFooterView(mFooter.footView);
					}
				}

				// 判断footerview是否显示
				if (!isFooterViewVisible())
				{
					if (isEnd)
					{
						showEnd();
					}
					else
					{
						showNormal();
					}
				}
			}
		});
	}

	/**
	 * 
	 * @Description: 设置FooterView
	 * @param @param v
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-16
	 */
	public void setFooterView(View v)
	{
		addFooterView(v);
		if (mFooter == null)
		{
			mFooter = new Footer();
		}
		mFooter.footView = v;
		ViewGroup group = (ViewGroup) v;
		for (int i = 0, count = group.getChildCount(); i < count; i++)
		{
			View view = group.getChildAt(i);
			if (view instanceof ProgressBar)
			{
				mFooter.progressBar = (ProgressBar) view;
			}
			else if (view instanceof TextView)
			{
				mFooter.textView = (TextView) view;
			}
		}
	}

	/**
	 * 
	 * @Description: 设置加载更多监听器
	 * @param @param onLoadMoreListener
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-14
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
	{
		this.mOnLoadMoreListener = onLoadMoreListener;
	}

	/**
	 * 
	 * ClassName: OnLoadMoreListener
	 * 
	 * @Description: 加载更多地监听器
	 * @author kesar
	 * @date 2015-11-14
	 */
	public interface OnLoadMoreListener
	{
		void onLoad();
	}

	/**
	 * 
	 * @Description: 判断最后listView中最后一个item(除了footview)是否完全显示出来 listView
	 *               是集合的那个ListView
	 * @param @return true完全显示出来，否则false
	 * @return boolean
	 * @throws
	 * @author kesar
	 * @date 2015-11-16
	 */
	protected boolean isLastItemVisible()
	{
		if (getChildCount() == 0)
		{
			return true;
		}
		else
		{
			View lastVisibleChild = this.getChildAt(getChildCount() - 1);
			if (lastVisibleChild.equals(mFooter.footView))
			{
				lastVisibleChild = this.getChildAt(getChildCount() - 2);
			}
			return lastVisibleChild.getBottom() <= this.getBottom();
		}
	}

	/**
	 * 
	 * @Description: 判断第一个item是否完全显示
	 * @param @return true完全显示出来，否则false
	 * @return boolean
	 * @throws
	 * @author kesar
	 * @date 2015-11-16
	 */
	protected boolean isFirstItemVisible()
	{
		if (getChildCount() == 0)
		{
			return true;
		}
		else
		{
			View firstVisibleChild = this.getChildAt(0);
			return firstVisibleChild.getTop() >= this.getTop();
		}
	}

	/**
	 * 
	 * @Description: 判断是否footerview是否显示
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author kesar
	 * @date 2015-11-16
	 */
	protected boolean isFooterViewVisible()
	{
		if (getFooterViewsCount() == 0 || mFooter == null
				|| mFooter.footView == null)
		{
			return false;
		}
		else
		{
			return mFooter.footView.getTop() <= this.getBottom();
		}
	}

	/**
	 * 
	 * @Description: 得到item的数量(footer和header除外的数量)
	 * @param @return
	 * @return int
	 * @throws
	 * @author kesar
	 * @date 2015-11-16
	 */
	protected int getItemCount()
	{
		return getCount() - getFooterViewsCount() - getHeaderViewsCount();
	}

	/**
	 * 
	 * @Description: 是否到底
	 * @param @param isEnd
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-16
	 */
	public void setEnd(boolean isEnd)
	{
		this.isEnd = isEnd;
		if (isEnd)
		{
			showEnd();
		}
		else
		{
			showNormal();
		}
	}

	/**
	 * 
	 * @Description: 显示正在加载
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-16
	 */
	protected void showLoading()
	{
		if (mFooter!=null&&mFooter.state != Footer.STATE_LOADING)
		{
			mFooter.progressBar.setVisibility(View.VISIBLE);
			mFooter.textView.setText("正在加载...");
			mFooter.state = Footer.STATE_LOADING;
		}
	}

	/**
	 * 
	 * @Description: 显示正常的状态
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-16
	 */
	protected void showNormal()
	{
		if (mFooter!=null&&mFooter.state != Footer.STATE_NORMAL)
		{
			mFooter.progressBar.setVisibility(View.GONE);
			mFooter.textView.setText("加载更多");
			mFooter.state = Footer.STATE_NORMAL;
		}
	}

	/**
	 * 
	 * @Description: 显示已到底
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-16
	 */
	protected void showEnd()
	{
		if (mFooter!=null&&mFooter.state != Footer.STATE_END)
		{
			mFooter.progressBar.setVisibility(View.GONE);
			mFooter.textView.setText("已加载全部");
			mFooter.state = Footer.STATE_END;
		}
	}
}

/**
 * 
 * ClassName: Footer
 * 
 * @Description: 将页脚的view封装在一起
 * @author kesar
 * @date 2015-11-16
 */
class Footer
{
	public static final int STATE_NORMAL = 0x1; // 默认状态
	public static final int STATE_LOADING = 0x2; // 正在加载
	public static final int STATE_END = 0x3; // 已加载全部

	View footView; // 整个大view
	ProgressBar progressBar; // 加载view
	TextView textView; // 信息view
	int state = 0x1; // footer的状态
}
