package com.i61.parent;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.common.data.UserCenter.UserCenterValue;
import com.i61.parent.common.data.WorkCenter.WorkCenterValue;
import com.i61.parent.manager.SharedPreferencesManager;
import com.i61.parent.ui.CameraActivity;
import com.i61.parent.ui.MainActivity;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.kymjs.kjframe.utils.KJLoger;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
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

	private static Context context; //存储Context对象，在任何地方均可使用


	@Override
	public void onCreate() {
		super.onCreate();

		context = getApplicationContext();

		Fresco.initialize(this);

		instance = this;

		// 初始化保存活动的activity的栈
		mActivitiesStack = new Stack<Activity>();

		CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(new LoggerInterceptor("OkHttp"))
				.connectTimeout(10000L, TimeUnit
						.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
				.cookieJar(cookieJar)
				.build();
		OkHttpUtils.initClient(okHttpClient);


		//将Configuration类中的fontScale属性设置为默认，防止因用户修改系统默认字体大小导致布局错位
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());


		/*
		String[] deleteSharePreferences = {"activeAccount", "accountList", "workCenter", "userCenter"};
		for(String text : deleteSharePreferences){
			SharedPreferencesUtils.removeValue(this, text);
			//Log.d("lxd", "remove: " + text);
		}
		*/

		/*
		Log.d("lxd", "SelectAccountAdapter中存入的activeAccount：" + ((Account)SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount")).getNickName());
		Log.d("lxd", "存入内存的userCenter的内容：" + ((UserCenterValue)SharedPreferencesUtils.readObject(AppApplication.getContext(), "userCenter")).getUserCourseInfos().size());
		Log.d("lxd", "存入内存的workCenter的内容：" + ((WorkCenterValue)SharedPreferencesUtils.readObject(AppApplication.getContext(), "workCenter")).getNeedSubmitWorkCourseList().size());
		Log.d("lxd", "存入的accountList的内容：" + ((ArrayList<Account>) SharedPreferencesUtils.readObject(AppApplication.getContext(), "accountList")).size());
        */

		//判断用户是否已经登陆过且没有退出登录，若是则直接跳转到MainActivity，不显示登录界面

		if(SharedPreferencesUtils.getValue(this, "isLogin", "false").equals("true")){
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}


		XGPushConfig.enableDebug(this,true);                     //开启debug日志数据
		XGPushManager.registerPush(this, new XGIOperateCallback() { //token注册
			@Override
			public void onSuccess(Object data, int flag) {
				//token在设备卸载重装的时候有可能会变
				Log.d("TPush", "注册成功，设备token为：" + data);

				Intent intent = new Intent(AppApplication.getContext(), CameraActivity.class);
				intent.putExtra("source", 1);
				Log.d("lxd", intent.toUri(Intent.URI_INTENT_SCHEME));
			}
			@Override
			public void onFail(Object data, int errCode, String msg) {
				Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
			}
		});

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
	 *  @Description: 将活动的activity压入管理activity的栈中
	 *  @param
	 *  @return
	 *  @author lixianhua
	 *  @time 2018/3/14  15:31
	 */
	public void pushActivity(Activity activity)
	{
		mActivitiesStack.push(activity);
		KJLoger.debug("已增加aty，剩下" + mActivitiesStack.size());
	}

	/**
	 *  @Description: 移除最顶端的activity
	 *  @author lixianhua
	 *  @time 2018/3/14  15:38
	 */
	public void popActivity(Activity activity)
	{
		if (mActivitiesStack.isEmpty())
		{
			return;
		}
		if (mActivitiesStack.peek().equals(activity))
		{
			mActivitiesStack.pop();
		}
		else
		{
			mActivitiesStack.remove(activity);
		}
		KJLoger.debug("已移除aty，剩下" + mActivitiesStack.size());
	}

	/**
	 *  @Description: 关闭所有activity
	 *  @author lixianhua
	 *  @time 2018/3/14  15:39
	 */
	public void finishAllActivitis()
	{
		while (!mActivitiesStack.isEmpty())
		{
			mActivitiesStack.pop().finish();
		}
	}
	/**
	 *  @Description: 得到栈顶的activity
	 *  @author lixianhua
	 *  @time 2018/3/14  16:11
	 */
	public Activity peekActivity()
	{
		if (mActivitiesStack.isEmpty())
		{
			return null;
		}
		return mActivitiesStack.peek();
	}

	/**
	 *  @Description: 得到SharedPreferences
	 *  @author lixianhua
	 *  @time 2018/3/14  16:11
	 */
	public static SharedPreferences getSharedPreferences(String name)
	{
		return instance.getSharedPreferences(name, MODE_PRIVATE);
	}

	/**
	 *  @Description: 得到Notification的管理器
	 *  @author lixianhua
	 *  @time 2018/3/14  16:11
	 */
	public static NotificationManager getNotificationManager()
	{
		return (NotificationManager) instance.getSystemService(NOTIFICATION_SERVICE);
	}

	public static Context getContext() {
		return context;
	}

}
