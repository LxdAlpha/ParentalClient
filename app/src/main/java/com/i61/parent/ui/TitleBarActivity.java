package com.i61.parent.ui;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.i61.parent.AppApplication;
import com.i61.parent.R;

import org.kymjs.kjframe.KJActivity;


/**
 * 应用Activity基类
 *
 * @author kymjs (https://github.com/kymjs)
 * @since 2015-3
 */


public abstract class TitleBarActivity extends KJActivity {
	public ImageView mImgBack;
	public TextView mTvTitle;
	public TextView mTvDoubleClickTip;
	public ImageView mImgMenu;
	public TextView mTvMenu;

	public RelativeLayout mRlTitleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		// 将Activity压入栈中，便于管理
		AppApplication.getInstance().pushActivity(this);
	}

	@Override
	protected void onStart() {
		try {
			mRlTitleBar = (RelativeLayout) findViewById(R.id.titlebar);
			mImgBack = (ImageView) findViewById(R.id.titlebar_img_back);
			mTvTitle = (TextView) findViewById(R.id.titlebar_text_title);
			mTvDoubleClickTip = (TextView) findViewById(R.id.titlebar_text_exittip);
			mImgMenu = (ImageView) findViewById(R.id.titlebar_img_menu);
			mTvMenu = (TextView) findViewById(R.id.titlebar_text_menu);
			mImgBack.setOnClickListener(this);
			mImgMenu.setOnClickListener(this);
			mTvMenu.setOnClickListener(this);

		} catch (NullPointerException e) {
			throw new NullPointerException(
					"TitleBar Notfound from Activity layout");
		}
		super.onStart();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.titlebar_img_back:
				onBackClick();
				break;
			case R.id.titlebar_img_menu:
				onMenuClick();
				break;
			case R.id.titlebar_text_menu:
				onTvMenuClick();
				break;
			default:
				break;
		}
	}

	protected void onBackClick() {
	}

	protected void onMenuClick() {
	}

	protected void onTvMenuClick() {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy()
	{
		// 从栈中移除activity
		AppApplication.getInstance().popActivity(this);
		super.onDestroy();
	}
};
