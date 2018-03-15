package com.i61.parent.presenter;

/**
 * Created by linxiaodong on 2018/3/8.
 */

public interface OnLoginFinishedListerner {
    void onUserNameError();
    void onPasswordError();
    void onSuccess();
    void onError(String errorMsg);
}
