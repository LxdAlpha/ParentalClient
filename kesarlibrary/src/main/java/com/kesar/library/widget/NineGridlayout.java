package com.kesar.library.widget;

import java.util.List;

import com.kesar.library.utils.DensityUtils;
import com.kesar.library.utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 
 * ClassName: NineGridlayout
 * 
 * @Description: 九宫格
 * @author kesar
 * @date 2015-12-15
 */
public class NineGridlayout extends ViewGroup
{
	private int gap = 5; // 图片之间的间隔
	private int columns; // 列数
	private int rows; // 行数
	private List<String> mImageDatas; // 图片数据
	private int mItemSize; // 每个子图的宽度
	private final String mDefaultImgColor = "#f5f5f5";

	public NineGridlayout(Context context)
	{
		super(context);
	}

	public NineGridlayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		Point p = ScreenUtils.getScreenSize(context);
		mItemSize = (p.x - DensityUtils.dip2px(context, 100)) / 3;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
	}

	/**
	 * 
	 * @Description: 获取图片间隔
	 * @param @return
	 * @return int
	 * @throws
	 * @author kesar
	 * @date 2015-12-15
	 */
	public int getGap()
	{
		return gap;
	}

	/**
	 * 
	 * @Description: 设置图片间隔
	 * @param @param gap
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-12-15
	 */
	public void setGap(int gap)
	{
		this.gap = gap;
	}

	/**
	 * 
	 * @Description: 设置小图的尺寸(在使用setImagesData方法之前)
	 * @param @param dp_size 单位是dp的尺寸
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-12-15
	 */
	public void setItemSize(int dp_size)
	{
		mItemSize = DensityUtils.dip2px(getContext(), dp_size);
	}

	/**
	 * 
	 * @Description: 设置图片数据
	 * @param @param lists
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-12-15
	 */
	public void setImagesData(List<String> lists)
	{
		if (lists == null || lists.isEmpty())
		{
			return;
		}
		// 初始化布局
		generateChildrenLayout(lists.size());
		// 这里做一个重用view的处理
		if (mImageDatas == null)
		{
			int i = 0;
			while (i < lists.size())
			{
				CustomImageView iv = generateImageView();
				addView(iv, generateDefaultLayoutParams());
				i++;
			}
		}
		else
		{
			int oldViewCount = mImageDatas.size();
			int newViewCount = lists.size();
			if (oldViewCount > newViewCount)
			{
				removeViews(newViewCount - 1, oldViewCount - newViewCount);
			}
			else if (oldViewCount < newViewCount)
			{
				for (int i = 0; i < newViewCount - oldViewCount; i++)
				{
					CustomImageView iv = generateImageView();
					addView(iv, generateDefaultLayoutParams());
				}
			}
		}
		mImageDatas = lists;
		layoutChildrenView();
	}

	/**
	 * @Description: 根据图片个数确定行列数量
	 * @param @param length
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-12-15
	 */
	private void generateChildrenLayout(int length)
	{
		if (length <= 3)
		{
			rows = 1;
			columns = length;
		}
		else if (length <= 6)
		{
			rows = 2;
			columns = 3;
			if (length == 4)
			{
				columns = 2;
			}
		}
		else
		{
			rows = 3;
			columns = 3;
		}
	}

	private void layoutChildrenView()
	{
		int childrenCount = mImageDatas.size();
		// int singleWidth = (totalWidth - gap * (3 - 1)) / 3;
		int singleHeight = mItemSize;
		// 根据子view数量确定高度
		LayoutParams params = getLayoutParams();
		params.height = singleHeight * rows + gap * (rows - 1);
		setLayoutParams(params);
		for (int i = 0; i < childrenCount; i++)
		{
			CustomImageView childrenView = (CustomImageView) getChildAt(i);
			childrenView.setTag(i);
			childrenView.setImageUrl(mImageDatas.get(i),mItemSize,mItemSize);
			int[] position = findPosition(i);
			int left = (mItemSize + gap) * position[1];
			int top = (singleHeight + gap) * position[0];
			int right = left + mItemSize;
			int bottom = top + singleHeight;
			childrenView.layout(left, top, right, bottom);
		}
	}

	private int[] findPosition(int childNum)
	{
		int[] position = new int[2];
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				if ((i * columns + j) == childNum)
				{
					position[0] = i;// 行
					position[1] = j;// 列
					break;
				}
			}
		}
		return position;
	}

	/**
	 * 
	 * @Description: 生成子图Imageview
	 * @param @return
	 * @return CustomImageView
	 * @throws
	 * @author kesar
	 * @date 2015-12-15
	 */
	private CustomImageView generateImageView()
	{
		CustomImageView iv = new CustomImageView(getContext());
		iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		iv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mOnItemClickListener != null)
				{
					mOnItemClickListener.OnClick((CustomImageView) v,
							Integer.parseInt(v.getTag() + ""));
				}
			}
		});
		iv.setBackgroundColor(Color.parseColor(mDefaultImgColor));
		return iv;
	}

	private OnItemClickListener mOnItemClickListener; // 子图点击监听器

	/**
	 * 
	 * ClassName: OnItemClickListener
	 * 
	 * @Description: 子图点击监听器
	 * @author kesar
	 * @date 2015-12-15
	 */
	public interface OnItemClickListener
	{
		void OnClick(CustomImageView view, int position);
	}

	/**
	 * 
	 * @Description: 设置子图点击事件
	 * @param @param onItemClickListener
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-12-15
	 */
	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
		this.mOnItemClickListener = onItemClickListener;
	}

	/**
	 * 
	 * ClassName: CustomImageView
	 * 
	 * @Description: 9宫格中每个子图的imageview
	 * @author kesar
	 * @date 2015-12-15
	 */
	public static class CustomImageView extends ImageView
	{
		private String url;
		private int width = 0; // 图片宽
		private int height = 0; // 图片高
		private boolean isAttachedToWindow;
		private final String mErrorImgColor = "#f5f5f5";
		private final String mLoadImgColor = "#f5f5f5";

		public CustomImageView(Context context, AttributeSet attrs)
		{
			super(context, attrs);
		}

		public CustomImageView(Context context)
		{
			super(context);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
			{
				Drawable drawable = getDrawable();
				if (drawable != null)
				{
					drawable.mutate().setColorFilter(Color.GRAY,
							PorterDuff.Mode.MULTIPLY);
				}
				break;
			}
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
			{
				Drawable drawableUp = getDrawable();
				if (drawableUp != null)
				{
					drawableUp.mutate().clearColorFilter();
				}
				break;
			}
			}
			return super.onTouchEvent(event);
		}

		@Override
		public void onAttachedToWindow()
		{
			isAttachedToWindow = true;
			setImageUrl(url,width,height);
			super.onAttachedToWindow();
		}

		@Override
		public void onDetachedFromWindow()
		{
			Picasso.with(getContext()).cancelRequest(this);
			isAttachedToWindow = false;
			setImageDrawable(null);
			setImageBitmap(null);
			super.onDetachedFromWindow();
		}

		/**
		 * 
		 * @Description: 设置图片的url并加载图片
		 * @param @param url
		 * @return void
		 * @throws
		 * @author kesar
		 * @date 2015-12-15
		 */
		public void setImageUrl(String url)
		{
			setImageUrl(url,0,0);
		}
		
		/**
		 * 
		 * @Description: 设置图片的url并加载图片
		 * @param @param url
		 * @param @param width
		 * @param @param height   
		 * @return void  
		 * @throws
		 * @author kesar
		 * @date 2015-12-15
		 */
		public void setImageUrl(String url, int width, int height)
		{
			this.width=width;
			this.height=height;
			
			if (!TextUtils.isEmpty(url))
			{
				this.url = url;
				if (isAttachedToWindow)
				{
					if (width != 0 && height != 0)
					{
						Picasso.with(getContext())
								.load(url)
								.error(new ColorDrawable(Color
										.parseColor(mErrorImgColor)))
								.placeholder(
										new ColorDrawable(Color
												.parseColor(mLoadImgColor)))
								.resize(width, height).into(this);
					}
					else
					{
						Picasso.with(getContext())
								.load(url)
								.error(new ColorDrawable(Color
										.parseColor(mErrorImgColor)))
								.placeholder(
										new ColorDrawable(Color
												.parseColor(mLoadImgColor)))
								.into(this);
					}
				}
			}
		}
	}
}
