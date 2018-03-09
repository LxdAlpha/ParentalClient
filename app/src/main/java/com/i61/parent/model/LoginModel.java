package com.i61.parent.model;

import com.i61.parent.model.entities.User;
import com.i61.parent.presenter.OnLoginFinishedListerner;

/**
 * Created by linxiaodong on 2018/3/8.
 */

public interface LoginModel {
    void login(User user, OnLoginFinishedListerner listerner);
}
