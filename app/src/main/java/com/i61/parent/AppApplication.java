package com.i61.parent;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.SharedPreferences;

import com.i61.parent.ui.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.kymjs.kjframe.utils.KJLoger;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @Author: lixianhua
 * @Description:
 * @Date: Created in 2018/3/13 9:44
 */

public class AppApplication extends Application {

	private static AppApplication instance;

	private Stack<Activity> mActivitiesStack;

	private MainActivity mMainActivity;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		// 初始化保存活动的activity的栈
		mActivitiesStack = new Stack<Activity>();

		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(new LoggerInterceptor("OkHttp"))
				.connectTimeout(AppConfig.httpTimeOut, TimeUnit.MILLISECONDS)
				.readTimeout(AppConfig.httpTimeOut, TimeUnit.MILLISECONDS)
				.build();
		OkHttpUtils.initClient(okHttpClient);
	}

	public static AppApplication getInstance() {
		return instance;
	}

	/**
	 * @Description: 设置MainActivity
	 * @author lixianhua
	 * @time 2018/3/14  15:29
	 */
	public void setMainActivity(MainActivity mMainActivity) {
		this.mMainActivity = mMainActivity;
	}

	/**
	 * @Description: 取到MainActivity
	 * @author lixianhua
	 * @time 2018/3/14  15:31
	 */
	public MainActivity getMainActivity() {
		return mMainActivity;
	}

	/**
	 * @Description: 得到所有activity的栈
	 * @author lixianhua
	 * @time 2018/3/14  15:31
	 */
	public Stack<Activity> getActivitiesStack() {
		return mActivitiesStack;
	}

	/**
	 * @Description: 将活动的activity压入管理activity的栈中
	 * @author lixianhua
	 * @time 2018/3/14  15:31
	 */
	public void pushActivity(Activity activity) {
		mActivitiesStack.push(activity);
		KJLoger.debug("已增加aty，剩下" + mActivitiesStack.size());
	}

	/**
	 * @Description: 移除最顶端的activity
	 * @author lixianhua
	 * @time 2018/3/14  15:38
	 */
	public void popActivity(Activity activity) {
		if (mActivitiesStack.isEmpty()) {
			return;
		}
		if (mActivitiesStack.peek().equals(activity)) {
			mActivitiesStack.pop();
		} else {
			mActivitiesStack.remove(activity);
		}
		KJLoger.debug("已移除aty，剩下" + mActivitiesStack.size());
	}

	/**
	 * @Description: 关闭所有activity
	 * @author lixianhua
	 * @time 2018/3/14  15:39
	 */
	public void finishAllActivitis() {
		while (!mActivitiesStack.isEmpty()) {
			mActivitiesStack.pop().finish();
		}
	}

	/**
	 * @Description: 得到栈顶的activity
	 * @author lixianhua
	 * @time 2018/3/14  16:11
	 */
	public Activity peekActivity() {
		if (mActivitiesStack.isEmpty()) {
			return null;
		}
		return mActivitiesStack.peek();
	}

	/**
	 * @Description: 得到SharedPreferences
	 * @author lixianhua
	 * @time 2018/3/14  16:11
	 */
	public static SharedPreferences getSharedPreferences(String name) {
		return instance.getSharedPreferences(name, MODE_PRIVATE);
	}

	/**
	 * @Description: 得到Notification的管理器
	 * @author lixianhua
	 * @time 2018/3/14  16:11
	 */
	public static NotificationManager getNotificationManager() {
		return (NotificationManager) instance.getSystemService(NOTIFICATION_SERVICE);
	}
}
