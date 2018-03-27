package com.i61.parent.model.impl;

import android.text.TextUtils;

import com.i61.parent.common.Urls;
import com.i61.parent.common.data.GsonResponsePasare;
import com.i61.parent.common.data.login.Value;
import com.i61.parent.model.LoginModel;
import com.i61.parent.model.entities.User;
import com.i61.parent.presenter.OnLoginFinishedListerner;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.MD5Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by linxiaodong on 2018/3/8.
 */

public class LoginModelImpl implements LoginModel {

	private static final String TAG = "LoginModelImpl";

	@Override
	public void login(User user, final OnLoginFinishedListerner listener) {
		final String userName = user.getUserName();
		final String password = user.getPassword();
		if (TextUtils.isEmpty(userName)) {
			listener.onUserNameError();
			return;
		}
		if (TextUtils.isEmpty(password)) {
			listener.onPasswordError();
			return;
		}
		OkHttpUtils
				.get()
				.url(Urls.HOST + "uc/login.json")
				.addParams("phoneNum", userName)
				.addParams("md5Password", MD5Utils.md5Password(password))
				.build()
//				.execute(new BaseBeanCallback<Value>() {
//					@Override
//					public void onError(Call call, Exception e, int id) {
//
//					}
//
//					@Override
//					public void onResponse(BaseBean response, int id) {
//						LogUtils.e("onResponse:", response.toString());
//
//						LogUtils.e("onResponse", response.getValue().toString());
//						boolean error = false;
//						if (TextUtils.isEmpty(userName)) {
//							listener.onUserNameError();
//							error = true;
//						}
//						if (TextUtils.isEmpty(password)) {
//							listener.onPasswordError();
//							error = true;
//						}
//						if (!error) {
//							listener.onSuccess();
//						}
//					}
//				});
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						LogUtils.e("onError:", e.getMessage());
					}

					@Override
					public void onResponse(String response, int id) {
						LogUtils.e("onResponse:", response);
						GsonResponsePasare<Value> pasare = new GsonResponsePasare<Value>(){};
						Value value = pasare.deal(response);
						LogUtils.e("value", value.getAccountList().get(0).getNickName());
						boolean error = false;
						if (TextUtils.isEmpty(userName)) {
							listener.onUserNameError();
							error = true;
						}
						if (TextUtils.isEmpty(password)) {
							listener.onPasswordError();
							error = true;
						}
						if (!error) {
							listener.onSuccess();
						}
					}
				});
	   /* new android.os.Handler().postDelayed(new Runnable() {
			@Override
            public void run() {
                boolean error = false;
                if(TextUtils.isEmpty(userName)){
                    listener.onUserNameError();
                    error = true;
                }
                if(TextUtils.isEmpty(password)){
                    listener.onPasswordError();
                    error = true;
                }
                if(!error){
                    listener.onSuccess();
                }
            }
        }, 2000);*/
	}
}
