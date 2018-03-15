package com.i61.parent.presenter.impl;

import com.i61.parent.model.LoginModel;
import com.i61.parent.model.entities.User;
import com.i61.parent.model.impl.LoginModelImpl;
import com.i61.parent.presenter.LoginPresenter;
import com.i61.parent.presenter.OnLoginFinishedListerner;
import com.i61.parent.view.LoginView;

/**
 * Created by linxiaodong on 2018/3/8.
 */

public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListerner{

    private LoginView loginView;
    private LoginModel loginModel;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImpl();
    }

    @Override
    public void onUserNameError() {
        if(loginView != null){
            loginView.setUserNameError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if(loginView != null){
            loginView.setPasswordError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onSuccess() {
        if(loginView != null){
            loginView.showSuccess();
        }
    }

    @Override
    public void onError(String errorMsg) {
        if(loginView != null){
            loginView.showError(errorMsg);
        }
    }

    @Override
    public void validateCredentials(User user) {
        if(loginView != null){
            loginView.showProgress();
        }
        loginModel.login(user, this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }
}
