package com.i61.parent.common.data;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.i61.parent.model.entities.User;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * @Author: lixianhua
 * @Description:
 * @Date: Created in 2018/3/13 14:21
 */

public abstract class BaseBeanCallback<T> extends Callback {

    public Type mType;

    public BaseBeanCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    @Override
    public BaseBean parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        BaseBean baseBean = new Gson().fromJson(string, mType);
        return baseBean;
    }
}