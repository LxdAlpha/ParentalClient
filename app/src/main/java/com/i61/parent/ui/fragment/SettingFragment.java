package com.i61.parent.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.ui.inter.ChangeFragmentInterface;
import com.i61.parent.util.DialogUtil;
import com.i61.parent.util.PxUtil;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by linxiaodong on 2018/4/8.
 */

public class SettingFragment extends TitleBarFragment{

    ChangeFragmentInterface changeFragmentInterface;   //跳转fragment的接口

    private static final String TAG = "SettingFragment";

    public SettingFragment(){

    }

    private View rootView = null;  //缓存view
    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.frag_setting, null); //加载布局文件
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
        actionBarRes.title = "设置";
        actionBarRes.backImageId = R.drawable.titlebar_back;
        actionBarRes.backText="个人中心";

        //读取“activeAccount”对象，获取当前登录用户的昵称
        Account account = (Account) SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount");
        ((TextView)rootView.findViewById(R.id.frag_setting_nickname)).setText(account.getNickName());

        //“修改孩子的昵称”按钮的响应函数
        rootView.findViewById(R.id.frag_setting_changename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragmentInterface.changeFragment(2);
            }
        });

        //“修改密码”按钮的响应函数
        rootView.findViewById(R.id.frag_setting_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragmentInterface.changeFragment(3);
            }
        });

        //“家长反馈”按钮的响应函数
        rootView.findViewById(R.id.frag_setting_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragmentInterface.changeFragment(4);
            }
        });

        rootView.findViewById(R.id.frag_setting_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogUtil.CommonDialog(getActivity(), R.style.PopupDialog, "您确定要退出该帐号吗？", new DialogUtil.CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            //Toast.makeText(this,"点击确定", Toast.LENGTH_SHORT).show();
                            OkHttpUtils
                                    .get()
                                    .url(Urls.HOST + "uc/logout.json")
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            Log.d("lxd", "退出帐号出错");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            Log.d("lxd", response);
                                            //退出登录时删除相关数据并将标识登录与否的isLogin置为false
                                            String[] deleteSharePreferences = {"activeAccount", "accountList", "workCenter", "userCenter"};
                                            for (String text : deleteSharePreferences) {
                                                SharedPreferencesUtils.removeValue(AppApplication.getContext(), text);
                                                //Log.d("lxd", "remove: " + text);
                                            }
                                            SharedPreferencesUtils.putValue(AppApplication.getContext(), "isLogin", "false");

                                            //让用户重新登录
                                            final Intent intent = AppApplication.getInstance().getPackageManager()
                                                    .getLaunchIntentForPackage(AppApplication.getInstance().getPackageName());
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            AppApplication.getInstance().startActivity(intent);
                                        }
                                    });

                            dialog.dismiss();
                        }
                    }
                }).setTitle("提示").show();
            }
        });
    }


    public void setChangeFragmentInterface(ChangeFragmentInterface changeFragmentInterface) {
        this.changeFragmentInterface = changeFragmentInterface;
    }


    //当前显示fragment是SettingFragment，点击back按钮则取消SettingActivity
    @Override
    public void onBackClick() {
        super.onBackClick();
        getActivity().finish();
    }


}
