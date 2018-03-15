package com.i61.parent.model.impl;

import android.text.TextUtils;

import com.i61.parent.common.Urls;
import com.i61.parent.common.data.BaseBean;
import com.i61.parent.common.data.BaseBeanCallback;
import com.i61.parent.model.LoginModel;
import com.i61.parent.model.entities.User;
import com.i61.parent.presenter.OnLoginFinishedListerner;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.MD5Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by linxiaodong on 2018/3/8.
 */

public class LoginModelImpl implements LoginModel {

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
				.execute(new BaseBeanCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						LogUtils.e("onError:", e.getMessage());
					}

					@Override
					public void onResponse(BaseBean response, int id) {
						LogUtils.e("response:", response.toString());
						String errorInfo = "";
						if (response.getResultCode().code == 0) {
							LogUtils.e("onResponse:", response.getValue().toString());
						} else {
							LogUtils.e("onResponse:", response.getResultCode().message);
							errorInfo = response.getResultCode().message;
						}
						if (!TextUtils.isEmpty(errorInfo)) {
							listener.onError(errorInfo);
						} else {
							listener.onSuccess();
						}
					}
				});
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        LogUtils.e("onError:",e.getMessage() );
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        LogUtils.e("onResponse:", response);
//                        boolean error = false;
//                        if(TextUtils.isEmpty(userName)){
//                            listener.onUserNameError();
//                            error = true;
//                        }
//                        if(TextUtils.isEmpty(password)){
//                            listener.onPasswordError();
//                            error = true;
//                        }
//                        if(!error){
//                            listener.onSuccess();
//                        }
//                    }
//                });
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
