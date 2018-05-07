package com.i61.parent.common.data.LoginResponse;



import java.io.Serializable;
import java.util.List;

/**
 * Created by linxiaodong on 2018/3/15.
 */

public class LoginResponseValue implements Serializable{
    List<Account> accountList;
    String appVersion;

    public List<Account> getAccountList() {
        return accountList;
    }

    public String getAppVersion() {
        return appVersion;
    }

    @Override
    public String toString() {
        return "Value{" +
                "accountList=" + accountList +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }
}
