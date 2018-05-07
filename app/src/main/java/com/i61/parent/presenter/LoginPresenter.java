package com.i61.parent.presenter;

import com.i61.parent.model.entities.User;

/**
 * Created by linxiaodong on 2018/3/8.
 */

public interface LoginPresenter {
    void validateCredentials(User user);
    void onDestroy();
}
