package com.i61.parent.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.Constant;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.common.data.ResultCode;
import com.i61.parent.ui.inter.ChangeFragmentInterface;
import com.i61.parent.util.SystemUtil;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by linxiaodong on 2018/4/10.
 */

//家长反馈
public class ParentFeedbackFragment extends TitleBarFragment{
    private static final String TAG = "ParentFeedbackFragment";

    ChangeFragmentInterface changeFragmentInterface;       //跳转fragment的接口

    public ParentFeedbackFragment() {
    }

    private View rootView = null;    //缓存view

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.frag_feedback, null); //加载布局文件
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();  //缓存的rootView需要判断是否已经在一个ViewGroup中， 如果在，就先移除自己，要不然会发生这个rootview已经有parent的错误。
            if(parent != null){
                parent.removeView(rootView);
            }
        }



        //监听输入框设置输入框最多只能输入300个字
        ((EditText)rootView.findViewById(R.id.frag_feedback_content)).addTextChangedListener(new TextWatcher() {
            private int selectionStart;
            private int selectionEnd;
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rootView.findViewById(R.id.frag_feedback_notice).setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                selectionStart = ((EditText)rootView.findViewById(R.id.frag_feedback_content)).getSelectionStart();
                selectionEnd = ((EditText)rootView.findViewById(R.id.frag_feedback_content)).getSelectionEnd();
                if (temp.length() > 300) { //当长度大于300时，每次新输入的字都删除
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    ((EditText) rootView.findViewById(R.id.frag_feedback_content)).setText(editable);
                    ((EditText) rootView.findViewById(R.id.frag_feedback_content)).setSelection(tempSelection);
                }
                int remain = 300 - editable.length(); //可输入剩余字数
                ((TextView)rootView.findViewById(R.id.frag_feedback_count)).setText("你还可以输入" + remain + "字");
            }
        });

        //每次初始化页面先默认不显示提示信息
        rootView.findViewById(R.id.frag_feedback_notice).setVisibility(View.GONE);

        //设置提交按钮的响应函数
        rootView.findViewById(R.id.frag_feedback_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(((EditText) rootView.findViewById(R.id.frag_feedback_content)).getText());
                if(text.length() == 0){
                    ((TextView)rootView.findViewById(R.id.frag_feedback_notice)).setVisibility(View.VISIBLE);
                    ((TextView)rootView.findViewById(R.id.frag_feedback_notice)).setText("你还没填写反馈内容哦");
                }else if(text.length() < 10){
                    ((TextView)rootView.findViewById(R.id.frag_feedback_notice)).setVisibility(View.VISIBLE);
                    ((TextView)rootView.findViewById(R.id.frag_feedback_notice)).setText("请输入10个字以上哦");
                }else{
                    //从存储的activeAccount对象中获取account
                    String account = ((Account)SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount")).getAccount();
                    //Log.d("lxd", SystemUtil.getSystemModel());
                    //Log.d("lxd", SystemUtil.getSystemVersion());
                    OkHttpUtils.post().url(Urls.HOST + "uc/userFeedback.json")
                            .addParams("problem", ((TextView)rootView.findViewById(R.id.frag_feedback_notice)).getText().toString())
                            .addParams("tel", account)
                            .addHeader("PhoneType", SystemUtil.getSystemModel())
                            .addHeader("SystemVersion", SystemUtil.getSystemVersion())
                            .addHeader("AppVersion", Constant.AppVersion)
                            .addHeader("UUID", "")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    LogUtils.e("onError", e.getMessage());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    LogUtils.d("onResponse", response);
                                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                                    JsonElement jsonElement = jsonObject.get("resultCode");
                                    ResultCode resultCode = new Gson().fromJson(jsonElement, ResultCode.class);
                                    if(resultCode.detail.equals("success")){
                                        Toast.makeText(AppApplication.getContext(), "画啦啦已经收到您的反馈，我们会尽快处理的，谢谢！", Toast.LENGTH_SHORT).show();
                                        changeFragmentInterface.changeFragment(1);
                                    }
                                }
                            });
                }
            }
        });

        return rootView;
    }

    //设置顶部ActionBar的文字
    @Override
    protected void setActionBarRes(ActionBarRes actionBarRes) {
        super.setActionBarRes(actionBarRes);
        actionBarRes.titleBarUnVisible = false;
        actionBarRes.title = "家长反馈";
        actionBarRes.backImageId = R.drawable.titlebar_back;
        actionBarRes.backText="设置";
    }

    public void setChangeFragmentInterface(ChangeFragmentInterface changeFragmentInterface) {
        this.changeFragmentInterface = changeFragmentInterface;
    }

    //针对以当前fragment显示时点击back按钮的消息响应
    @Override
    public void onBackClick() {
        changeFragmentInterface.changeFragment(1);  //跳转返回到设置SettingFragment
    }
}
