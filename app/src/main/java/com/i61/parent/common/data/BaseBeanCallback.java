package com.i61.parent.common.data;

import com.google.gson.Gson;
import com.i61.parent.model.entities.User;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * @Author: lixianhua
 * @Description:
 * @Date: Created in 2018/3/13 14:21
 */

public abstract class BaseBeanCallback extends Callback<BaseBean> {
	@Override
	public BaseBean parseNetworkResponse(Response response, int id) throws Exception
	{
		String string = response.body().string();
		BaseBean baseBean = new Gson().fromJson(string, BaseBean.class);
		return baseBean;
	}
}