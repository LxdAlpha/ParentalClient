package com.i61.parent.manager;

import android.content.Context;

import com.i61.parent.AppApplication;
import com.i61.parent.common.Constant;

import org.kymjs.kjframe.KJDB;

/**
 * @Author: lixianhua
 * @Description:
 * @Date: Created in 2018/3/14 16:26
 */

public class DataBaseManager {

	private static KJDB mKjdb;

	/**
	 * @Description:
	 * @author lixianhua
	 * @time 2018/3/14  16:34
	 */
	public static void init(Context context) {
		mKjdb = KJDB.create(context, Constant.DataBaseName);
	}

	/**
	 * @Description: 移除数据库
	 * @author lixianhua
	 * @time 2018/3/14  16:37
	 */
	public static void removeDb() {
		try {
			mKjdb.dropDb();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mKjdb = KJDB.create(AppApplication.getInstance()
					.getApplicationContext(), Constant.DataBaseName);
		}
	}
}
