package com.i61.parent.common.data;

/**
 * Created by liukun on 16/3/5.
 */
public class BaseBean<T> {
    public ResultCode resultCode;
    public T value;

    public ResultCode getResultCode() {
        return resultCode;
    }

    public T getValue() {
        return value;
    }

    public boolean isError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean error;
    public boolean success;

}
