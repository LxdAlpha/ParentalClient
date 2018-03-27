package com.i61.parent.common.data;

import com.google.gson.Gson;
import com.i61.parent.utils.LogUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: lixianhua
 * @Description:
 * @Date: Created in 2018/3/16 14:22
 */

public class GsonResponsePasare<T> implements ParameterizedType {

	public T deal(String response) {
//            Type gsonType = new ParameterizedType() {//...};//不建议该方式，推荐采用GsonResponsePasare实现ParameterizedType.因为getActualTypeArguments这里涉及获取GsonResponsePasare的泛型集合
		Type gsonType = this;

		CommonResponse<T> commonResponse = new Gson().fromJson(response, gsonType);
		LogUtils.e("Data is : " + commonResponse.value, "Class Type is : " + commonResponse.value.getClass().toString());
		return commonResponse.value;
	}

	@Override
	public Type[] getActualTypeArguments() {
		Class clz = this.getClass();
		//这里必须注意在外面使用new GsonResponsePasare<GsonResponsePasare.DataInfo>(){};实例化时必须带上{},否则获取到的superclass为Object
		Type superclass = clz.getGenericSuperclass(); //getGenericSuperclass()获得带有泛型的父类
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterized = (ParameterizedType) superclass;
		return parameterized.getActualTypeArguments();
	}

	@Override
	public Type getOwnerType() {
		return null;
	}

	@Override
	public Type getRawType() {
		return CommonResponse.class;
	}
}