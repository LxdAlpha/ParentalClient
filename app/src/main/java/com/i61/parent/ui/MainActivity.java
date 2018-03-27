package com.i61.parent.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.ui.fragment.NewsFragment;
import com.i61.parent.ui.fragment.PersonalFragment;
import com.i61.parent.ui.fragment.ReviewFragment;
import com.i61.parent.ui.fragment.TitleBarFragment;
import com.i61.parent.ui.fragment.WorkFragment;
import com.jaeger.library.StatusBarUtil;

import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;

public class MainActivity extends TitleBarActivity implements BottomNavigationBar.OnTabSelectedListener {

	private static ArrayList<TitleBarFragment> fragments;

	private static FragmentManager mFragmentManager;

	private TitleBarFragment currentFragment;


	public static void startActivity(Context cxt) {
		Intent intent = new Intent(cxt, MainActivity.class);
		cxt.startActivity(intent);
	}

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_main);
		//沉浸式状态栏设置
		StatusBarUtil.setColor(this, getResources().getColor(R.color.bar_icon_pressed_color), 0);
		AppApplication.getInstance().setMainActivity(this);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		initBottomNavigationBar();
		fragments = getFragments();
		//设置默认的fragment
		setDefaultFragment();
	}

	private void initBottomNavigationBar() {
		BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
		bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
		bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

		bottomNavigationBar
				.addItem(new BottomNavigationItem(R.drawable.bar_icon_review_normal, getString(R.string.fragment_review)).setActiveColorResource(R.color.bar_icon_pressed_color))
				.addItem(new BottomNavigationItem(R.drawable.bar_icon_work_normal, getString(R.string.fragment_work)).setActiveColorResource(R.color.bar_icon_pressed_color))
				.addItem(new BottomNavigationItem(R.drawable.bar_icon_news_normal, getString(R.string.fragment_news)).setActiveColorResource(R.color.bar_icon_pressed_color))
				.addItem(new BottomNavigationItem(R.drawable.bar_icon_personal_normal, getString(R.string.fragment_personal)).setActiveColorResource(R.color.bar_icon_pressed_color))
				.setFirstSelectedPosition(3)
				.initialise();
		bottomNavigationBar.setTabSelectedListener(this);
	}

	/**
	 * @Description:
	 * @author lixianhua
	 * @time 2018/3/17  15:41
	 */
	private ArrayList<TitleBarFragment> getFragments() {
		ArrayList<TitleBarFragment> fragments = new ArrayList<>();
		fragments.add(new ReviewFragment());
		fragments.add(new WorkFragment());
		fragments.add(new NewsFragment());
		fragments.add(new PersonalFragment());
		return fragments;
	}

	/*
		* 设置默认的fragment
		*/
	public void setDefaultFragment() {
		mFragmentManager = getFragmentManager();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.replace(R.id.main_content, fragments.get(3));
		transaction.commit();
	}

	@Override
	protected void onBackClick() {
		super.onBackClick();
		currentFragment.onBackClick();
	}

	@Override
	protected void onMenuClick() {
		super.onMenuClick();
		currentFragment.onMenuClick();
	}

	@Override
	protected void onTvMenuClick() {
		super.onTvMenuClick();
		currentFragment.onTvMenuClick();
	}

	private static long firstTime;

	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		if (firstTime + 2000 > System.currentTimeMillis()) {

			super.onBackPressed();
			finish();
		} else {
			ViewInject.toast("再按一次退出程序");
		}
		firstTime = System.currentTimeMillis();
	}

	@Override
	public void onTabSelected(int position) {
		if (fragments != null) {
			if (position < fragments.size()) {
				FragmentTransaction ft = mFragmentManager.beginTransaction();
				TitleBarFragment fragment = fragments.get(position);
				if (fragment.isAdded()) {
					ft.replace(R.id.main_content, fragment);
				} else {
					currentFragment = fragment;
					ft.add(R.id.main_content, fragment);
				}
				ft.commitAllowingStateLoss();
			}
		}
	}

	@Override
	public void onTabUnselected(int position) {
		if (fragments != null) {
			if (position < fragments.size()) {
				FragmentTransaction ft = mFragmentManager.beginTransaction();
				TitleBarFragment fragment = fragments.get(position);
				ft.remove(fragment);
				ft.commitAllowingStateLoss();
			}
		}
	}

	@Override
	public void onTabReselected(int position) {

	}
}
