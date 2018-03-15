package com.i61.parent.view;

/**
 * Created by linxiaodong on 2018/3/8.
 */

public interface LoginView {
    void showProgress();
    void hideProgress();
    void setUserNameError();
    void setPasswordError();
    void showSuccess();
    void showError(String errorMsg);
}
