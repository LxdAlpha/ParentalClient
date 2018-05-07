package com.i61.parent.common.data.LoginResponse;

import java.io.Serializable;

/**
 * Created by linxiaodong on 2018/3/15.
 */

public class LoginResponse implements Serializable{
    ResultCodee resultCode;
    LoginResponseValue value;
    String success;
    String error;

    public ResultCodee getResultCode() {
        return resultCode;
    }

    public LoginResponseValue getValue() {
        return value;
    }

    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
