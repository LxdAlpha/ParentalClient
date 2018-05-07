package com.i61.parent.model.impl;

import android.text.TextUtils;
import android.util.Log;

import com.i61.parent.AppApplication;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.BaseBean;
import com.i61.parent.common.data.BaseBeanCallback;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.common.data.LoginResponse.LoginResponseValue;

import com.i61.parent.model.LoginModel;
import com.i61.parent.model.entities.User;
import com.i61.parent.presenter.OnLoginFinishedListerner;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.MD5Utils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

					.execute(new BaseBeanCallback<BaseBean<LoginResponseValue>>() {
						@Override
						public void onError(Call call, Exception e, int id) {
							LogUtils.e("onError:", e.getMessage() + "bdfbdfbdfbfdbfdbfdbfdbfdbfdbfdbfdbfdbfdbfdbfdbdfbfdbfdbf");
						}

						@Override
						public void onResponse(Object response, int id) {
							LogUtils.d("onResponse:", response.toString());
							String errorInfo = "";
							if (((BaseBean<LoginResponseValue>)response).getResultCode().code == 0) {
								LogUtils.d("onResponse:", ((BaseBean<LoginResponseValue>)response).getValue().toString());
							} else {
								LogUtils.d("onResponse:", ((BaseBean<LoginResponseValue>)response).getResultCode().message);
								errorInfo = ((BaseBean<LoginResponseValue>)response).getResultCode().message;
							}
							if (!TextUtils.isEmpty(errorInfo)) {
								listener.onError(errorInfo);
							} else {
								listener.onSuccess();
								List<Account> test = ((BaseBean<LoginResponseValue>)response).getValue().getAccountList();
								Collections.sort(test);
								//Log.d("lxd", "test: " + test.get(0).getNickName() + "," + test.get(0).getUserId() + ", " + test.get(1).getNickName() + ", " + test.get(1).getUserId());
								SharedPreferencesUtils.saveObject(AppApplication.getContext(), test, "accountList");
								//SharedPreferencesUtils.saveObject(AppApplication.getContext(), ((BaseBean<LoginResponseValue>)response).getValue().getAccountList(), "accountList");
								//Log.d("lxd", "((BaseBean<LoginResponseValue>)response).getValue():" + ((BaseBean<LoginResponseValue>)response).getValue().getAccountList().get(0).getNickName() + ", " + ((BaseBean<LoginResponseValue>)response).getValue().getAccountList().get(1).getNickName());
								//ArrayList<Account> accountArrayList = (ArrayList<Account>)SharedPreferencesUtils.readObject(AppApplication.getContext(), "accountList");
								//Log.d("lxd", "LoginModelImpl:" + accountArrayList.get(0).getNickName() + ", " + accountArrayList.get(1).getNickName());


								//登录成功则存储一个键值对表示已登录，在退出登录时要置为false
								SharedPreferencesUtils.putValue(AppApplication.getContext(), "isLogin", "true");
							}
						}
					});


				/*  //最初版本代码，存在Gson无法转换BaseBean中Value对象的问题
				.execute(new BaseBeanCallback<Value>() {
					@Override
					public void onError(Call call, Exception e, int id) {
						LogUtils.e("onError:", e.getMessage());
					}

					@Override
					public void onResponse(Object response, int id) {
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
				*/

				/*  //测试获取泛型参数类型可行性
				.execute(new ResultCallBack<ResultData<Value>>() {
					@Override
					public void onError(Call call, Exception e, int id) {

					}

					@Override
					public void onResponse(Object response, int id) {
						Log.d("lxd", ((ResultData<Value>)response).getValue().getAccountList().get(0).getNickName());
					}
				});
				*/
                /* //直接获取Json数据
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError:",e.getMessage() );
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("onResponse:", response);
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
                            SharedPreferencesUtils.putValue(AppApplication.getContext(), "accountList", response);
                        }
                    }
                });
                */


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
