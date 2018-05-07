package com.i61.parent.model;

import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.presenter.OnAccountStoreListener;

/**
 * Created by linxiaodong on 2018/3/16.
 */

public interface SelectAccountModel {
    void removeAccount(OnAccountStoreListener listener);
    void getUserCenter(OnAccountStoreListener listener);
    void getWorkCenter(OnAccountStoreListener listener);
}
