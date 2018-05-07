package com.i61.parent.model.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.i61.parent.AppApplication;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.BaseBean;
import com.i61.parent.common.data.BaseBeanCallback;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.common.data.UserCenter.Course;
import com.i61.parent.common.data.UserCenter.MoneyInfo;
import com.i61.parent.common.data.UserCenter.UserCenterValue;
import com.i61.parent.common.data.WorkCenter.WorkCenterValue;
import com.i61.parent.model.SelectAccountModel;
import com.i61.parent.presenter.OnAccountStoreListener;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by linxiaodong on 2018/3/16.
 */

public class SelectAccountModelImpl implements SelectAccountModel{

    @Override
    public void removeAccount(OnAccountStoreListener listener) {
        SharedPreferencesUtils.removeValue(AppApplication.getContext(), "activeAccount");
        listener.onRemove();
    }

    //注意必须先获取userCenter再获取workCenter信息
    @Override
    public void getUserCenter(final OnAccountStoreListener listener) {

        //Log.d("lxd", "存储的activeAccount:" + ((Account)SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount")).getUserId());

        OkHttpUtils.get().url(Urls.HOST + "uc/userCenter.json")
                .addParams("userId", ((Account)SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount")).getUserId()+"")
                .build()
                .execute(new BaseBeanCallback<BaseBean<UserCenterValue>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError:", e.getMessage());
                        listener.onSaveFailed();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if(((BaseBean<UserCenterValue>)response).success == true && ((BaseBean<UserCenterValue>)response).getResultCode().success == true){
                            LogUtils.d("onResponse:", response.toString());
                            Log.d("lxd", ((MoneyInfo)((BaseBean<UserCenterValue>)response).value.getMoneyInfo()).getUserName()+"");
                            SharedPreferencesUtils.saveObject(AppApplication.getContext(), ((BaseBean<UserCenterValue>)response).value, "userCenter");
                            //Log.d("lxd", "存入内存的userCenter的内容：" + ((UserCenterValue)SharedPreferencesUtils.readObject(AppApplication.getContext(), "userCenter")).getUserCourseInfos().size());
                            //ArrayList<ClassmateMoney> test = (ArrayList<ClassmateMoney>) ((BaseBean<UserCenterValue>)response).value.getClassmatesMoneyInfo();
                            listener.onSave();

                            //注意必须先获取userCenter再获取workCenter信息
                            getWorkCenter(listener);

                        }
                    }
                });



    }


    @Override
    public void getWorkCenter(final OnAccountStoreListener listener) {

        OkHttpUtils.get().url(Urls.HOST + "wc/workCenter.json")
                .addParams("courseId", 0+"")
                .build()
                .execute(new BaseBeanCallback<BaseBean<WorkCenterValue>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError:", e.getMessage());
                        listener.onSaveFailed();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if(((BaseBean<WorkCenterValue>)response).success == true && ((BaseBean<WorkCenterValue>)response).getResultCode().success == true){
                            LogUtils.d("onResponse:", response.toString());
                            SharedPreferencesUtils.saveObject(AppApplication.getContext(), ((BaseBean<WorkCenterValue>)response).value, "workCenter");
                            //Log.d("lxd", "存入内存的workCenter的内容：" + ((WorkCenterValue)SharedPreferencesUtils.readObject(AppApplication.getContext(), "workCenter")).getNeedSubmitWorkCourseList().size());
                            listener.onSave();
                        }
                    }
                });
    }






}
