package com.i61.parent.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

import com.i61.parent.R;
import com.i61.parent.ui.fragment.MessageFragment;
import com.jaeger.library.StatusBarUtil;

//显示消息，消息共有五种
public class MessageActivity extends TitleBarActivity{

    FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_message);
        //沉浸式状态栏设置
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bar_icon_pressed_color), 0);
        ((TextView)findViewById(R.id.titlebar_text_exittip)).setText("返回");

    }


    @Override
    public void initWidget() {
        super.initWidget();
        mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        MessageFragment messageFragment = new MessageFragment();
        transaction.replace(R.id.main_content, new MessageFragment());
        transaction.commit();
    }

    //点击标题栏返回按钮
    @Override
    protected void onBackClick() {
        super.onBackClick();
        this.finish();
    }

    //点击手机返回按钮
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    public void finishActivity(){
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
