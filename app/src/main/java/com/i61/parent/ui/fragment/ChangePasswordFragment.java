package com.i61.parent.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.BaseBean;
import com.i61.parent.common.data.BaseBeanCallback;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.common.data.ResultCode;
import com.i61.parent.ui.inter.ChangeFragmentInterface;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.MD5Utils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by linxiaodong on 2018/4/9.
 */

//更改密码的fragment
public class ChangePasswordFragment extends TitleBarFragment{

    private static final String TAG = "ChangeNickNameFragment";

    ChangeFragmentInterface changeFragmentInterface;//跳转fragment的接口

    public ChangePasswordFragment() {
    }

    private View rootView = null;  //缓存view

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.frag_password, null); //加载布局文件
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();  //缓存的rootView需要判断是否已经在一个ViewGroup中， 如果在，就先移除自己，要不然会发生这个rootview已经有parent的错误。
            if(parent != null){
                parent.removeView(rootView);
            }
        }
        rootView.findViewById(R.id.frag_password_notice).setVisibility(View.GONE);
        return rootView;
    }


    @Override
    protected void setActionBarRes(ActionBarRes actionBarRes) {
        super.setActionBarRes(actionBarRes);
        actionBarRes.titleBarUnVisible = false;
        actionBarRes.title = "修改密码";
        actionBarRes.backImageId = R.drawable.titlebar_back;
        actionBarRes.backText="设置";
        actionBarRes.menuText="保存";
    }

    public void setChangeFragmentInterface(ChangeFragmentInterface changeFragmentInterface) {
        this.changeFragmentInterface = changeFragmentInterface;
    }

    //针对以当前fragment显示时点击back按钮的消息响应
    @Override
    public void onBackClick() {
        changeFragmentInterface.changeFragment(1);  //跳转返回到设置SettingFragment
    }

    //针对以当前fragemnt显示时点击菜单（保存）按钮的消息响应
    @Override
    public void onTvMenuClick() {
        String oldPassword = String.valueOf(((com.i61.parent.ui.ClearableEditText)rootView.findViewById(R.id.frag_password_old)).getText());
        String newPassword = String.valueOf(((com.i61.parent.ui.ClearableEditText)rootView.findViewById(R.id.frag_password_new)).getText());
        String confirmPassword = String.valueOf(((com.i61.parent.ui.ClearableEditText)rootView.findViewById(R.id.frag_password_confirm)).getText());
        if(oldPassword.equals("")){ //未输入旧密码
            Log.d("lxd", "未输入密码");
            rootView.findViewById(R.id.frag_password_notice).setVisibility(View.VISIBLE);
            ((TextView)rootView.findViewById(R.id.frag_password_notice)).setText("请输入旧密码");
        }else if(!oldPassword.equals("") && newPassword.equals("")){ //输入了旧密码但未输入新密码
            Log.d("lxd", "请输入新密码");
            ((TextView)rootView.findViewById(R.id.frag_password_notice)).setText("请输入新密码");
            rootView.findViewById(R.id.frag_password_notice).setVisibility(View.VISIBLE);
        }else if(!oldPassword.equals("") && !newPassword.equals("") && confirmPassword.equals("")){ //输入了旧密码、新密码但未输入密码确认
            Log.d("lxd", "请确认新密码");
            ((TextView)rootView.findViewById(R.id.frag_password_notice)).setText("请确认新密码");
            rootView.findViewById(R.id.frag_password_notice).setVisibility(View.VISIBLE);
        }else if(!newPassword.equals(confirmPassword)){ //输入了旧密码、新密码、密码确认，但新密码和新密码确认不匹配
            Log.d("lxd", "您输入的新密码和密码确认不匹配");
            ((TextView)rootView.findViewById(R.id.frag_password_notice)).setText("您输入的新密码和密码确认不匹配");
            rootView.findViewById(R.id.frag_password_notice).setVisibility(View.VISIBLE);
        }else{
            OkHttpUtils.post().url(Urls.HOST + "uc/updatePassword.json")
                    .addParams("oldMd5Pwd", MD5Utils.md5Password(oldPassword))
                    .addParams("newMd5Pwd", MD5Utils.md5Password(newPassword))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.d("lxd", e.getMessage());
                            LogUtils.e("onError:", e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d("lxd", response);
                            LogUtils.d("onResponse:", response);
                            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                            JsonElement jsonElement = jsonObject.get("resultCode");
                            ResultCode resultCode = new Gson().fromJson(jsonElement, ResultCode.class);

                            if(resultCode.code == -5){ //用户输入了旧密码、新密码、密码确认，但旧密码不正确
                                ((TextView)rootView.findViewById(R.id.frag_password_notice)).setText("您输入的旧密码不正确哦");
                                rootView.findViewById(R.id.frag_password_notice).setVisibility(View.VISIBLE);
                            }else if(resultCode.code == 0){ //用户输入了旧密码、新密码、密码确认，新密码和新密码确认匹配
                                //更改完密码后重新登录
                                OkHttpUtils
                                        .get()
                                        .url(Urls.HOST + "uc/logout.json")
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                //Log.d("lxd", "退出帐号出错");
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                //退出登录时删除相关数据并将标识登录与否的isLogin置为false
                                                String[] deleteSharePreferences = {"activeAccount", "accountList", "workCenter", "userCenter"};
                                                for (String text : deleteSharePreferences) {
                                                    SharedPreferencesUtils.removeValue(AppApplication.getContext(), text);
                                                    //Log.d("lxd", "remove: " + text);
                                                }
                                                SharedPreferencesUtils.putValue(AppApplication.getContext(), "isLogin", "false");
                                                final Intent intent = AppApplication.getInstance().getPackageManager()
                                                        .getLaunchIntentForPackage(AppApplication.getInstance().getPackageName());
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                AppApplication.getInstance().startActivity(intent);
                                            }
                                        });
                            }
                        }
                    });


        }

    }

    //每次重新显示时需先将错误提示信息取消显示
    @Override
    public void onResume() {
        super.onResume();
        rootView.findViewById(R.id.frag_password_notice).setVisibility(View.GONE);
    }
}
