package com.i61.parent.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.ui.inter.ChangeFragmentInterface;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by linxiaodong on 2018/4/9.
 */

public class ChangeNickNameFragment extends TitleBarFragment{
    private static final String TAG = "ChangeNickNameFragment";

    ChangeFragmentInterface changeFragmentInterface;//跳转fragment的接口

    public ChangeNickNameFragment() {
    }

    private View rootView = null;  //缓存view

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.frag_nickname, null); //加载布局文件
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();  //缓存的rootView需要判断是否已经在一个ViewGroup中， 如果在，就先移除自己，要不然会发生这个rootview已经有parent的错误。
            if(parent != null){
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    protected void setActionBarRes(ActionBarRes actionBarRes) {
        super.setActionBarRes(actionBarRes);
        actionBarRes.titleBarUnVisible = false;
        actionBarRes.title = "修改昵称";
        actionBarRes.backImageId = R.drawable.titlebar_back;
        actionBarRes.backText="设置";
        actionBarRes.menuText="保存";
        ((EditText)rootView.findViewById(R.id.frag_nickname_edittext)).setHint(((Account)SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount")).getNickName());
    }

    public void setChangeFragmentInterface(ChangeFragmentInterface changeFragmentInterface) {
        this.changeFragmentInterface = changeFragmentInterface;
    }

    //针对以当前fragment显示时点击back按钮的消息响应
    @Override
    public void onBackClick() {
        changeFragmentInterface.changeFragment(1);  //跳转返回到设置SettingFragment
        rootView.findViewById(R.id.frag_nickname_notice).setVisibility(View.GONE); //取消显示提示未填信息
    }

    //针对以当前fragemnt显示时点击菜单（保存）按钮的消息响应
    @Override
    public void onTvMenuClick() {
        //如果没有任何输入则显示提醒信息
        if(String.valueOf(((EditText)rootView.findViewById(R.id.frag_nickname_edittext)).getText()).equals("")){
            rootView.findViewById(R.id.frag_nickname_notice).setVisibility(View.VISIBLE);
        }else{  //如果有输入则提交昵称修改至后台
            OkHttpUtils.post().url(Urls.HOST + "uc/updateNickName.json")
                    .addParams("newName", String.valueOf(((EditText)rootView.findViewById(R.id.frag_nickname_edittext)).getText()))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.e("onError", e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.d("onResponse", response);
                            //更新本地存储的“activeAccount”对象中的nickName信息
                            Account account = (Account) SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount");
                            account.setNickName(String.valueOf(((EditText)rootView.findViewById(R.id.frag_nickname_edittext)).getText()));
                            SharedPreferencesUtils.saveObject(AppApplication.getContext(), account, "activeAccount");
                            changeFragmentInterface.changeFragment(1); //跳转到设置页面
                        }
                    });
        }
    }
}
