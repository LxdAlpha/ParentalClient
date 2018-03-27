package com.i61.parent.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i61.parent.R;

/**
 * @author lixianhua
 * @Description: 点评页面
 * @time 2018/3/17  15:45
 */
public class ReviewFragment extends TitleBarFragment {
	private static final String TAG = "ReviewFragment";

	public ReviewFragment() {

	}

	private View rootView = null; //缓存fragment的view，避免每次都创建新的

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.frag_review, null);
		} else {
			//缓存的rootView需要判断是否已经在一个ViewGroup中， 如果在，就先移除自己，要不然会发生这个rootview已经有parent的错误。
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	@Override
	protected void setActionBarRes(TitleBarFragment.ActionBarRes actionBarRes) {
		super.setActionBarRes(actionBarRes);
		actionBarRes.titleBarUnVisible = false;
		actionBarRes.title = "点评";
	}

}
