package com.i61.parent.common.data;

/**
 * Created by liukun on 16/3/5.
 */
public class BaseBean<T> {
    public ResultCode resultCode;
    public T value;
    public boolean error;
    public boolean success;

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

    @Override
    public String toString() {
        return "BaseBean{" +
                "resultCode=" + resultCode +
                ", value=" + value +
                ", error=" + error +
                ", success=" + success +
                '}';
    }
}
