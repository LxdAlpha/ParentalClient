package com.i61.parent.model.impl;

import android.text.TextUtils;

import com.i61.parent.model.LoginModel;
import com.i61.parent.model.entities.User;
import com.i61.parent.presenter.OnLoginFinishedListerner;

import java.util.logging.Handler;

/**
 * Created by linxiaodong on 2018/3/8.
 */

public class LoginModelImpl implements LoginModel{
    @Override
    public void login(User user, final OnLoginFinishedListerner listener) {
        final String userName = user.getUserName();
        final String password = user.getPassword();
        new android.os.Handler().postDelayed(new Runnable() {
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
        }, 2000);
    }
}
