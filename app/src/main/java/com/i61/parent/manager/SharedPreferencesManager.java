package com.i61.parent.manager;

import android.content.SharedPreferences;

import com.i61.parent.AppApplication;
import com.i61.parent.utils.SharedPreferencesUtils;

/**
 * @Author: lixianhua
 * @Description:
 * @Date: Created in 2018/3/14 16:52
 */

public class SharedPreferencesManager {
	public static String FILE_USER = "user_";

	public final static String PREF_USERNAME = "username";
	public final static String PREF_PASSWORD = "password";
	public final static String PREF_COOKIE = "cookie";

	/**
	 * @param @param username 登陆的用户名
	 * @return void
	 * @throws
	 * @Description: 保存username(登陆后必须使用，用于保存最近登陆的用户名)
	 * @author kesar
	 * @date 2015-12-3
	 */
	public static void saveUserName(String username) {
		SharedPreferencesUtils.putValue(AppApplication.getInstance(),
				PREF_USERNAME, username);
		FILE_USER += username;
	}

	/**
	 * @param @return 获取username
	 * @return String
	 * @throws
	 * @Description: 获取username(登陆前必须使用，用户获取自动登陆用的用户名)
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static String getUserName() {
		String username = SharedPreferencesUtils.getValue(AppApplication.getInstance(),
				PREF_USERNAME, "");

		return username.equals("") ? null : username;
	}

	/**
	 * @param
	 * @return void
	 * @throws
	 * @Description: 移除用户名
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static void removeUserName() {
		SharedPreferencesUtils.removeValue(AppApplication.getInstance(),
				PREF_USERNAME);
	}

	/**
	 * @param @param cookie
	 * @return void
	 * @throws
	 * @Description: 保存cookie
	 * @author kesar
	 * @date 2015-12-5
	 */
	public static void setCookie(String cookie) {
		SharedPreferencesUtils.putValue(AppApplication.getInstance(),
				PREF_COOKIE, cookie);
	}

	/**
	 * @param @return
	 * @return String
	 * @throws
	 * @Description: 获取cookie
	 * @author kesar
	 * @date 2015-12-5
	 */
	public static String getCookie() {
		String cookie = SharedPreferencesUtils.getValue(AppApplication.getInstance(),
				PREF_COOKIE, "");

		return cookie.equals("") ? null : cookie;
	}

	/**
	 * @param
	 * @return void
	 * @throws
	 * @Description: 移除cookie
	 * @author kesar
	 * @date 2015-12-5
	 */
	public static void removeCookie() {
		SharedPreferencesUtils.removeValue(AppApplication.getInstance(),
				PREF_COOKIE);
	}

	/**
	 * @param @param username
	 * @param @param password
	 * @return void
	 * @throws
	 * @Description: 保存用户名和密码的数据
	 * @author kesar
	 * @date 2015-12-3
	 */
	public static void saveUserInfo(String username, String password) {
		SharedPreferences sp = AppApplication.getSharedPreferences(FILE_USER);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(PREF_USERNAME, username);
		editor.putString(PREF_PASSWORD, password);
		editor.commit();
	}
}
