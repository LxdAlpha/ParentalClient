package com.i61.parent.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.i61.parent.AppApplication;
import com.i61.parent.ui.TitleBarActivity;

import org.kymjs.kjframe.ui.KJFragment;

/**
 * 具有ActionBar的Activity的基类
 *
 * @author kymjs (https://github.com/kymjs)
 * @since 2015-3
 */
public abstract class TitleBarFragment extends KJFragment {

	private static final String TAG = "TitleBarFragment";

	/**
	 * 封装一下方便一起返回(JAVA没有结构体这么一种东西实在是个遗憾)
	 *
	 * @author kymjs (https://github.com/kymjs)
	 */
	public class ActionBarRes {
		public CharSequence title;
		public int backImageId;
		public Drawable backImageDrawable;
		public int menuImageId;
		public Drawable menuImageDrawable;
		public boolean titleBarUnVisible;
		public CharSequence menuText;
	}

	private final ActionBarRes actionBarRes = new ActionBarRes();
	public static TitleBarActivity outsideAty;
	protected AppApplication app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (getActivity() instanceof TitleBarActivity) {
			outsideAty = (TitleBarActivity) getActivity();
		}
		app = (AppApplication) getActivity().getApplication();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		setActionBarRes(actionBarRes);
		setTitle(actionBarRes.title);
		if (actionBarRes.backImageId == 0) {
			setBackImage(actionBarRes.backImageDrawable);
		} else {
			setBackImage(actionBarRes.backImageId);
		}
		if (actionBarRes.menuImageId == 0) {
			setMenuImage(actionBarRes.menuImageDrawable);
		} else {
			setMenuImage(actionBarRes.menuImageId);
		}
		setTitleBarUnVisible(actionBarRes.titleBarUnVisible);
		setMenuText(actionBarRes.menuText);
	}

	/**
	 * 方便Fragment中设置ActionBar资源
	 *
	 * @param actionBarRes
	 * @return
	 */
	protected void setActionBarRes(ActionBarRes actionBarRes) {
	}

	/**
	 * 当ActionBar上的返回键被按下时
	 */
	public void onBackClick() {}

	/**
	 * 当ActionBar上的菜单键被按下时
	 */
	public void onMenuClick() {}

	/**
	 * 当ActionBar上的菜单文字被按下时
	 */
	public void onTvMenuClick() {}

	/**
	 * 设置标题
	 *
	 * @param text
	 */
	protected void setTitle(CharSequence text) {
		if (outsideAty != null) {
			outsideAty.mTvTitle.setText(text);
		}
	}

	/**
	 * 设置返回键图标
	 */
	protected void setBackImage(int resId) {
		if (outsideAty != null) {
			outsideAty.mImgBack.setImageResource(resId);
		}
	}

	/**
	 * 设置返回键图标
	 */
	protected void setBackImage(Drawable drawable) {
		if (outsideAty != null) {
			outsideAty.mImgBack.setImageDrawable(drawable);
		}
	}

	/**
	 * 设置菜单键图标
	 */
	protected void setMenuImage(int resId) {
		if (outsideAty != null) {
			outsideAty.mImgMenu.setImageResource(resId);
		}
	}

	/**
	 * 设置菜单键图标
	 */
	protected void setMenuImage(Drawable drawable) {
		if (outsideAty != null) {
			outsideAty.mImgMenu.setImageDrawable(drawable);
		}
	}

	public void setTitleBarUnVisible(boolean unVisible) {
		if (outsideAty != null) {
			if (unVisible) {
				outsideAty.mRlTitleBar.setVisibility(View.GONE);
			} else {
				outsideAty.mRlTitleBar.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 设置菜单键文字
	 *
	 * @param text
	 */
	protected void setMenuText(CharSequence text) {
		if (outsideAty != null) {
			outsideAty.mTvMenu.setText(text);
		}
	}

}
