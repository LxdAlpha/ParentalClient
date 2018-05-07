package com.i61.parent.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.i61.parent.R;
import com.i61.parent.ui.fragment.ChangeNickNameFragment;
import com.i61.parent.ui.fragment.ChangePasswordFragment;
import com.i61.parent.ui.fragment.ParentFeedbackFragment;
import com.i61.parent.ui.fragment.SettingFragment;
import com.i61.parent.ui.fragment.TitleBarFragment;
import com.i61.parent.ui.inter.ChangeFragmentInterface;
import com.jaeger.library.StatusBarUtil;

import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;

public class SettingActivity extends TitleBarActivity {

    private static ArrayList<TitleBarFragment> fragments; //存放有关设置的所有fragment
    private static FragmentManager mFragmentManager;
    private TitleBarFragment currentFragment;            //当前显示的fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_setting);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bar_icon_pressed_color), 0);          //沉浸式状态栏设置
    }

    //设置
    @Override
    public void initWidget() {
        super.initWidget();
        fragments = getFragments();
        Intent intent = getIntent();
        int kind = intent.getIntExtra("kind", -1);
        if(kind != -1){
            setFragment(kind);
        }
    }

    //加载所有fragment
    private ArrayList<TitleBarFragment> getFragments(){
        ArrayList<TitleBarFragment> fragments = new ArrayList<>();

        //添加settingFragment到数组里面
        SettingFragment settingFragment = new SettingFragment();
        settingFragment.setChangeFragmentInterface(new ChangeFragmentInterface() {
            @Override
            public void changeFragment(int kind) {
                setFragment(kind);
            }
        });
        fragments.add(settingFragment);

        ChangeNickNameFragment changeNickNameFragment = new ChangeNickNameFragment();
        changeNickNameFragment.setChangeFragmentInterface(new ChangeFragmentInterface() {
            @Override
            public void changeFragment(int kind) {
                setFragment(kind);
            }
        });
        fragments.add(changeNickNameFragment);

        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        changePasswordFragment.setChangeFragmentInterface(new ChangeFragmentInterface() {
            @Override
            public void changeFragment(int kind) {
                setFragment(kind);
            }
        });
        fragments.add(changePasswordFragment);

        ParentFeedbackFragment parentFeedbackFragment = new ParentFeedbackFragment();
        parentFeedbackFragment.setChangeFragmentInterface(new ChangeFragmentInterface() {
            @Override
            public void changeFragment(int kind) {
                setFragment(kind);
            }
        });
        fragments.add(parentFeedbackFragment);

        return fragments;
    }

    //设置fragment
    public void setFragment(int kind){
        mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.setting_content, fragments.get(kind - 1));
        transaction.commit();
        currentFragment = fragments.get(kind - 1);
    }


    @Override
    protected void onBackClick() {
        super.onBackClick();
        currentFragment.onBackClick();  //调用当前活动fragment的响应函数
    }

    @Override
    protected void onMenuClick() {
        super.onMenuClick();
        currentFragment.onMenuClick();

    }

    //实现TitleBarActivity中的OnTvMenuClick函数
    @Override
    protected void onTvMenuClick() {
        super.onTvMenuClick();
        currentFragment.onTvMenuClick(); //调用当前活动fragment的响应函数
    }



    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
