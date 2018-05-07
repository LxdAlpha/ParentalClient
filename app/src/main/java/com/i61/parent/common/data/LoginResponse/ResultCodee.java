package com.i61.parent.common.data.LoginResponse;

import java.io.Serializable;

/**
 * Created by linxiaodong on 2018/3/15.
 */

public class ResultCodee implements Serializable{
    int code;
    String detail;
    String message;
    boolean success;

    public int getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
