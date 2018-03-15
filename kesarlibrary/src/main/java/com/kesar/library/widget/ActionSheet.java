package com.kesar.library.widget;

import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.kesar.library.R;
import com.kesar.library.utils.AnimationUtils;
import com.kesar.library.utils.DensityUtils;
import com.kesar.library.utils.KeyBoardUtils;

/**
 * 
 * ClassName: ActionSheet (要通过Builer实例化)
 * 
 * @Description: 底部弹窗
 * @author kesar
 * @date 2015-11-11
 */
public class ActionSheet extends Dialog implements OnClickListener
{
	/* 控件的id */
	private static final int CANCEL_BUTTON_ID = 100;
	private static final int BG_VIEW_ID = 10;

	private Context mContext;
	private MenuItemClickListener mListener;
	private List<String> items;
	private String cancelTitle = "cancel";
	private boolean mCancelableOnTouchOutside;
	private Attributes mAttrs;
	private View mView;
	private LinearLayout mPanel;
	private View mBg;
	private boolean isCancel = true;

	private ActionSheet(Context context, Builder builder)
	{
		super(context, android.R.style.Theme_Light_NoTitleBar);// 全屏
		this.mContext = context;
		KeyBoardUtils.hideKeyboard(context);
		initViews(builder);
		getWindow().setGravity(Gravity.BOTTOM);
		Drawable drawable = new ColorDrawable();
		drawable.setAlpha(0);// 去除黑色背景
		getWindow().setBackgroundDrawable(drawable);
	}

	// /**
	// *
	// * @Description: 隐藏输入法
	// * @param
	// * @return void
	// * @throws
	// * @author kesar
	// * @date 2015-11-12
	// */
	// private void hideInputMethodManager()
	// {
	// InputMethodManager imm = (InputMethodManager) mContext
	// .getSystemService(Context.INPUT_METHOD_SERVICE);
	// if (imm.isActive())
	// {
	// View focusView = ((Activity) mContext).getCurrentFocus();
	// if (focusView != null)
	// imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
	// }
	// }

	/**
	 * 
	 * @Description: 初始化Views
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public void initViews(Builder builder)
	{
		mAttrs = readAttribute(builder.getThemeId());// 获取主题属性
		mView = createView();

		setCancelButtonTitle(builder.getCancelButtonTitle());
		addItems(builder.getItems());
		setCancelableOnTouchMenuOutside(builder.isCancelableOnTouchOutside());
		setItemClickListener(builder.getListener());
	}

	/**
	 * 
	 * @Description: 创建基本的背景视图
	 * @param @return
	 * @return View
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	private View createView()
	{
		FrameLayout parent = new FrameLayout(mContext);
		FrameLayout.LayoutParams parentParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		parentParams.gravity = Gravity.BOTTOM;
		parent.setLayoutParams(parentParams);
		mBg = new View(mContext);
		mBg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
		mBg.setId(BG_VIEW_ID);
		mBg.setOnClickListener(this);

		mPanel = new LinearLayout(mContext);
		FrameLayout.LayoutParams mPanelParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		mPanelParams.gravity = Gravity.BOTTOM;
		mPanel.setLayoutParams(mPanelParams);
		mPanel.setOrientation(LinearLayout.VERTICAL);
		parent.addView(mBg);
		parent.addView(mPanel);
		return parent;
	}

	/**
	 * 
	 * @Description: 创建MenuItem
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	private void createItems()
	{
		if (items != null && items.size() > 0)
		{
			for (int i = 0, length = items.size(); i < length; i++)
			{
				Button bt = new Button(mContext);
				bt.setId(CANCEL_BUTTON_ID + i + 1);
				bt.setOnClickListener(this);
				bt.setBackgroundDrawable(getOtherButtonBg(
						items.toArray(new String[items.size()]), i));
				bt.setText(items.get(i));
				bt.setTextColor(mAttrs.otherButtonTextColor);
				bt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						mAttrs.actionSheetTextSize);
				if (i > 0)
				{
					LinearLayout.LayoutParams params = createButtonLayoutParams();
					params.topMargin = mAttrs.otherButtonSpacing;
					mPanel.addView(bt, params);
				}
				else
					mPanel.addView(bt);
			}
		}
		Button bt = new Button(mContext);
		bt.getPaint().setFakeBoldText(true);
		bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize);
		bt.setId(CANCEL_BUTTON_ID);
		bt.setBackgroundDrawable(mAttrs.cancelButtonBackground);
		bt.setText(cancelTitle);
		bt.setTextColor(mAttrs.cancelButtonTextColor);
		bt.setOnClickListener(this);
		LinearLayout.LayoutParams params = createButtonLayoutParams();
		params.topMargin = mAttrs.cancelButtonMarginTop;
		mPanel.addView(bt, params);

		mPanel.setBackgroundDrawable(mAttrs.background);
		mPanel.setPadding(mAttrs.padding, mAttrs.padding, mAttrs.padding,
				mAttrs.padding);
	}

	/**
	 * 
	 * @Description: 创建BUtton的layout属性
	 * @param @return
	 * @return LinearLayout.LayoutParams
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public LinearLayout.LayoutParams createButtonLayoutParams()
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		return params;
	}

	/**
	 * item按钮的颜色
	 * 
	 * @param titles
	 * @param i
	 * @return
	 */
	private Drawable getOtherButtonBg(String[] titles, int i)
	{
		if (titles.length == 1)
			return mAttrs.otherButtonSingleBackground;
		else if (titles.length == 2)
			switch (i)
			{
			case 0:
				return mAttrs.otherButtonTopBackground;
			case 1:
				return mAttrs.otherButtonBottomBackground;
			}
		else if (titles.length > 2)
		{
			if (i == 0)
				return mAttrs.otherButtonTopBackground;
			else if (i == (titles.length - 1))
				return mAttrs.otherButtonBottomBackground;
			return mAttrs.getOtherButtonMiddleBackground();
		}
		return null;
	}

	/**
	 * 
	 * @Description: 显示菜单
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public void show()
	{
		if (this.isShowing())
		{
			return;
		}
		mBg.startAnimation(AnimationUtils.createAlphaInAnimation());
		mPanel.startAnimation(AnimationUtils.createTranslationInAnimation());
		super.show();
		getWindow().setContentView(mView);
	}

	/**
	 * 
	 * @Description: dissmiss Menu菜单
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public void dismissMenu()
	{
		if (this.isShowing())
		{
			onDismiss();
		}
	}

	/**
	 * 
	 * @Description: dismiss时的处理
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	private void onDismiss()
	{
		Animation an = AnimationUtils.createTranslationOutAnimation();
		an.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				dismiss();
			}
		});
		mBg.startAnimation(AnimationUtils.createAlphaOutAnimation());
		mPanel.startAnimation(an);
	}

	/**
	 * 
	 * @Description: 取消按钮的标题文字(before add items)
	 * @param @param title
	 * @param @return
	 * @return ActionSheet
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public void setCancelButtonTitle(String title)
	{
		this.cancelTitle = title;
	}

	/**
	 * 
	 * @Description: 取消按钮的标题文字
	 * @param @param strId
	 * @param @return
	 * @return ActionSheet
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public void setCancelButtonTitle(int strId)
	{
		setCancelButtonTitle(mContext.getString(strId));
	}

	/**
	 * 
	 * @Description: 点击外部边缘是否可取消
	 * @param @param cancelable
	 * @param @return
	 * @return ActionSheet
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public void setCancelableOnTouchMenuOutside(boolean cancelable)
	{
		mCancelableOnTouchOutside = cancelable;
	}

	/**
	 * 
	 * @Description: 添加选项
	 * @param @param titles
	 * @param @return
	 * @return ActionSheet
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public void addItems(String... titles)
	{
		if (titles == null || titles.length == 0)
		{
			return;
		}
		items = Arrays.asList(titles);
		createItems();
	}

	/**
	 * 
	 * @Description: 添加选项
	 * @param @param items
	 * @param @return
	 * @return ActionSheet
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public void addItems(List<String> items)
	{
		this.items = items;
		createItems();
	}

	/**
	 * 
	 * @Description: 监听item的点击事件
	 * @param @param listener
	 * @param @return
	 * @return ActionSheet
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public void setItemClickListener(MenuItemClickListener listener)
	{
		this.mListener = listener;
	}

	/**
	 * 
	 * @Description: 读取主题的属性
	 * @param @param themeId 主题id
	 * @param @return
	 * @return Attributes
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	private Attributes readAttribute(int themeId)
	{
		Attributes attrs = new Attributes(mContext);
		Theme theme = mContext.getResources().newTheme();
		theme.applyStyle(themeId, true);
		TypedArray a = theme.obtainStyledAttributes(null,
				R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
		Drawable background = a
				.getDrawable(R.styleable.ActionSheet_actionSheetBackground);
		if (background != null)
			attrs.background = background;
		Drawable cancelButtonBackground = a
				.getDrawable(R.styleable.ActionSheet_cancelButtonBackground);
		if (cancelButtonBackground != null)
			attrs.cancelButtonBackground = cancelButtonBackground;
		Drawable otherButtonTopBackground = a
				.getDrawable(R.styleable.ActionSheet_otherButtonTopBackground);
		if (otherButtonTopBackground != null)
			attrs.otherButtonTopBackground = otherButtonTopBackground;
		Drawable otherButtonMiddleBackground = a
				.getDrawable(R.styleable.ActionSheet_otherButtonMiddleBackground);
		if (otherButtonMiddleBackground != null)
			attrs.otherButtonMiddleBackground = otherButtonMiddleBackground;
		Drawable otherButtonBottomBackground = a
				.getDrawable(R.styleable.ActionSheet_otherButtonBottomBackground);
		if (otherButtonBottomBackground != null)
			attrs.otherButtonBottomBackground = otherButtonBottomBackground;
		Drawable otherButtonSingleBackground = a
				.getDrawable(R.styleable.ActionSheet_otherButtonSingleBackground);
		if (otherButtonSingleBackground != null)
			attrs.otherButtonSingleBackground = otherButtonSingleBackground;
		attrs.cancelButtonTextColor = a.getColor(
				R.styleable.ActionSheet_cancelButtonTextColor,
				attrs.cancelButtonTextColor);
		attrs.otherButtonTextColor = a.getColor(
				R.styleable.ActionSheet_otherButtonTextColor,
				attrs.otherButtonTextColor);
		attrs.padding = (int) a.getDimension(
				R.styleable.ActionSheet_actionSheetPadding, attrs.padding);
		attrs.otherButtonSpacing = (int) a.getDimension(
				R.styleable.ActionSheet_otherButtonSpacing,
				attrs.otherButtonSpacing);
		attrs.cancelButtonMarginTop = (int) a.getDimension(
				R.styleable.ActionSheet_cancelButtonMarginTop,
				attrs.cancelButtonMarginTop);
		attrs.actionSheetTextSize = a.getDimensionPixelSize(
				R.styleable.ActionSheet_actionSheetTextSize,
				(int) attrs.actionSheetTextSize);

		a.recycle();
		return attrs;
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == BG_VIEW_ID && !mCancelableOnTouchOutside)
		{
			return;
		}
		if (v.getId() == CANCEL_BUTTON_ID)
		{
			dismissMenu();
		}
		else if (v.getId() != BG_VIEW_ID)
		{
			dismiss();
			if (mListener != null)
			{
				mListener.onItemClick(v.getId() - CANCEL_BUTTON_ID - 1);
			}
			isCancel = false;
		}
	}

	/**
	 * 
	 * ClassName: Attributes
	 * 
	 * @Description: 自定义属性的控件主题
	 * @author kesar
	 * @date 2015-11-11
	 */
	private class Attributes
	{
		private Context mContext;

		private Drawable background;
		private Drawable cancelButtonBackground;
		private Drawable otherButtonTopBackground;
		private Drawable otherButtonMiddleBackground;
		private Drawable otherButtonBottomBackground;
		private Drawable otherButtonSingleBackground;
		private int cancelButtonTextColor;
		private int otherButtonTextColor;
		private int padding;
		private int otherButtonSpacing;
		private int cancelButtonMarginTop;
		private float actionSheetTextSize;

		public Attributes(Context context)
		{
			mContext = context;
			this.background = new ColorDrawable(Color.TRANSPARENT);
			this.cancelButtonBackground = new ColorDrawable(Color.BLACK);
			ColorDrawable gray = new ColorDrawable(Color.GRAY);
			this.otherButtonTopBackground = gray;
			this.otherButtonMiddleBackground = gray;
			this.otherButtonBottomBackground = gray;
			this.otherButtonSingleBackground = gray;
			this.cancelButtonTextColor = Color.WHITE;
			this.otherButtonTextColor = Color.BLACK;
			this.padding = dp2px(20);
			this.otherButtonSpacing = dp2px(2);
			this.cancelButtonMarginTop = dp2px(10);
			this.actionSheetTextSize = dp2px(16);
		}

		private int dp2px(int dp)
		{
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
					dp, mContext.getResources().getDisplayMetrics());
		}

		public Drawable getOtherButtonMiddleBackground()
		{
			if (otherButtonMiddleBackground instanceof StateListDrawable)
			{
				TypedArray a = mContext.getTheme().obtainStyledAttributes(null,
						R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
				otherButtonMiddleBackground = a
						.getDrawable(R.styleable.ActionSheet_otherButtonMiddleBackground);
				a.recycle();
			}
			return otherButtonMiddleBackground;
		}

	}

	/**
	 * 
	 * ClassName: MenuItemClickListener
	 * 
	 * @Description: 菜单点击事件的监听器
	 * @author kesar
	 * @date 2015-11-11
	 */
	public interface MenuItemClickListener
	{
		void onItemClick(int itemPosition);
	}

	/**
	 * 
	 * ClassName: Builder
	 * 
	 * @Description: builder
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static class Builder
	{
		private String mCancelButtonTitle = "cancel";
		private List<String> mItems;
		private int mThemeId;
		private boolean mCancelableOnTouchOutside; // 是否触摸空白，取消dialog
		private MenuItemClickListener mListener;
		private Context mContext;

		public Builder(Context mContext)
		{
			this.mContext = mContext;
		}

		public Builder setCancelButtonTitle(int stringId)
		{
			this.mCancelButtonTitle = mContext.getString(stringId);
			return this;
		}

		public Builder setCancelButtonTitle(String text)
		{
			this.mCancelButtonTitle = text;
			return this;
		}

		public Builder setItems(String... titles)
		{
			this.mItems = Arrays.asList(titles);
			return this;
		}

		public Builder setItems(List<String> items)
		{
			this.mItems = items;
			return this;
		}

		public Builder setTheme(int themeId)
		{
			this.mThemeId = themeId;
			return this;
		}

		public Builder setCancelableOnTouchOutside(
				boolean cancelableOnTouchOutside)
		{
			this.mCancelableOnTouchOutside = cancelableOnTouchOutside;
			return this;
		}

		public Builder setListener(MenuItemClickListener listener)
		{
			this.mListener = listener;
			return this;
		}

		public String getCancelButtonTitle()
		{
			return mCancelButtonTitle;
		}

		public List<String> getItems()
		{
			return mItems;
		}

		public MenuItemClickListener getListener()
		{
			return mListener;
		}

		public int getThemeId()
		{
			return mThemeId;
		}

		public boolean isCancelableOnTouchOutside()
		{
			return mCancelableOnTouchOutside;
		}

		public ActionSheet create()
		{
			return new ActionSheet(mContext, this);
		}

		/**
		 * 
		 * @Description: 创建默认的ActionSheet
		 * @param @return
		 * @return ActionSheet
		 * @throws
		 * @author kesar
		 * @date 2015-11-12
		 */
		public ActionSheet createDefault()
		{
			mCancelableOnTouchOutside = true;
			mCancelButtonTitle = "取消";
			mThemeId = R.style.ActionSheetStyleIOS7;
			return create();
		}
	}
}