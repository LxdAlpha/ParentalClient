package com.i61.parent.common.data.login;

/**
 * Created by linxiaodong on 2018/3/15.
 */

public class LoginResponse {
    ResultCodee resultCode;
    Value value;
    String success;
    String error;

    public ResultCodee getResultCode() {
        return resultCode;
    }

    public Value getValue() {
        return value;
    }

    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
