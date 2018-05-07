package com.i61.parent.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.i61.parent.AppApplication;
import com.i61.parent.AppApplication;
import com.i61.parent.Broadcast.MessageCountReceiver;
import com.i61.parent.MessageService;
import com.i61.parent.R;
import com.i61.parent.ui.fragment.NewsFragment;
import com.i61.parent.ui.fragment.PersonalFragment;
import com.i61.parent.ui.fragment.ReviewFragment;
import com.i61.parent.ui.fragment.TitleBarFragment;
import com.i61.parent.ui.fragment.WorkFragment;
import com.i61.parent.ui.inter.ChangeFragmentInterface;
import com.i61.parent.ui.inter.MessageInterface;
import com.i61.parent.ui.inter.ResumeInterface;
import com.jaeger.library.StatusBarUtil;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.WorkCenter.WorkCenterValue;
import com.i61.parent.util.DialogUtil;
import com.i61.parent.util.PxUtil;
import com.i61.parent.util.ScreenUtil;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.i61.parent.view.SelectAccountView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.time.LocalDate;

import okhttp3.Call;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


import org.kymjs.kjframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends TitleBarActivity implements BottomNavigationBar.OnTabSelectedListener{

    private static ArrayList<TitleBarFragment> fragments;

    private static FragmentManager mFragmentManager;

    private TitleBarFragment currentFragment;

    private BottomNavigationBar bottomNavigationBar = null;

    private ShapeBadgeItem mShapeBadgeItem; //“消息”菜单项右上角的红点

    private MessageCountReceiver messageCountReceiver;





    public static void startActivity(Context cxt) {
        Intent intent = new Intent(cxt, MainActivity.class);
        cxt.startActivity(intent);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_main);
        //沉浸式状态栏设置
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bar_icon_pressed_color), 0);
        AppApplication.getInstance().setMainActivity(this);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initBottomNavigationBar();
        fragments = getFragments();
        //设置默认的fragment
        setDefaultFragment();

        /*
        Timer timer = new Timer();
        Task task = new Task();
        timer.schedule(task, 10000, 10000);
        */

        //开启检测新信息服务
        Intent startIntent = new Intent(this, MessageService.class);
        startService(startIntent);

        //新建立广播接收器
        messageCountReceiver = new MessageCountReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.i61.parent.MESSAGE_BROADCAST");
        registerReceiver(messageCountReceiver, intentFilter);
        messageCountReceiver.setMessageInterface(new MessageInterface() {  //实现更改界面接口
            @Override
            public void updateUI() {
                Log.d("lxd", "updateUI");
                mShapeBadgeItem.show();
            }
        });


    }




    private void initBottomNavigationBar() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        //消息提示红点
        mShapeBadgeItem = new ShapeBadgeItem()
                .setShapeColorResource(R.color.qmui_config_color_red)
                .setGravity(Gravity.TOP | Gravity.END)
                .setShape(ShapeBadgeItem.SHAPE_OVAL)
                .setHideOnSelect(false);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.bar_icon_review_normal, getString(R.string.fragment_review)).setActiveColorResource(R.color.bar_icon_pressed_color))
                .addItem(new BottomNavigationItem(R.drawable.bar_icon_work_normal, getString(R.string.fragment_work)).setActiveColorResource(R.color.bar_icon_pressed_color))
                .addItem(new BottomNavigationItem(R.drawable.bar_icon_news_normal, getString(R.string.fragment_news)).setActiveColorResource(R.color.bar_icon_pressed_color).setBadgeItem(mShapeBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.bar_icon_personal_normal, getString(R.string.fragment_personal)).setActiveColorResource(R.color.bar_icon_pressed_color))
                .setFirstSelectedPosition(3)
                .initialise();

        //先不显示消息菜单项的红点，待访问接口确定情况后在确定
        mShapeBadgeItem.hide();


        bottomNavigationBar.setTabSelectedListener(this);

        this.bottomNavigationBar = bottomNavigationBar;
    }

    /**
     * @Description:
     * @author lixianhua
     * @time 2018/3/17  15:41
     */
    private ArrayList<TitleBarFragment> getFragments() {
        final ArrayList<TitleBarFragment> fragments = new ArrayList<>();
        fragments.add(new ReviewFragment());
        fragments.add(new WorkFragment());
        fragments.add(new NewsFragment());
        fragments.add(new PersonalFragment());
        return fragments;
    }

    /*
        * 设置默认的fragment
        */
    public void setDefaultFragment() {
        mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragments.get(3));
        transaction.commit();
    }


    @Override
    protected void onBackClick() {
        super.onBackClick();
        currentFragment.onBackClick();
    }

    @Override
    protected void onMenuClick() {
        super.onMenuClick();
        currentFragment.onMenuClick();
    }

    @Override
    protected void onTvMenuClick() {
        super.onTvMenuClick();
        currentFragment.onTvMenuClick();
    }

    private static long firstTime;

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        if (firstTime + 2000 > System.currentTimeMillis()) {

            super.onBackPressed();
            finish();
        } else {
            ViewInject.toast("再按一次退出程序");
        }
        firstTime = System.currentTimeMillis();
    }

    @Override
    public void onTabSelected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                TitleBarFragment fragment = fragments.get(position);

                //当用户切换到消息页之后，“消息”菜单项右上方的红点消失
                if(position == 2){
                    mShapeBadgeItem.hide();
                }


                /*
                //存在点评页面在切换时不能正常显示的问题
                if (fragment.isAdded()) {
                    ft.replace(R.id.main_content, fragment);
                } else {
                    currentFragment = fragment;
                    ft.add(R.id.main_content, fragment);
                }
                */


                ft.replace(R.id.main_content, fragment);
                currentFragment = fragment;

                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                TitleBarFragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        unregisterReceiver(messageCountReceiver);  //取消注册广播接收器
        Intent stopIntent = new Intent(getApplicationContext(), MessageService.class);//终止新信息信息服务
        stopService(stopIntent);
    }

    private void finishActivity() {
        SharedPreferencesUtils.removeValue(AppApplication.getContext(), "activeAccount");
        SharedPreferencesUtils.removeValue(this, "userCenter");
        SharedPreferencesUtils.removeValue(this, "workCenter");
        SharedPreferencesUtils.removeValue(this, "accountList");

        unregisterReceiver(messageCountReceiver);  //取消注册广播接收器
        Intent stopIntent = new Intent(getApplicationContext(), MessageService.class);//终止新信息信息服务
        stopService(stopIntent);

        this.finish();
    }

    //重写onActivityResult函数让包含的fragment的onActivityResult能够被调用
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                if(resultCode == RESULT_OK){
                    bottomNavigationBar.selectTab(3, true); //根据resultCode更改界面
                }
                break;
            case 3:
                if(resultCode == RESULT_OK){
                    bottomNavigationBar.selectTab(3, true);
                }
                break;
            case 4:
                if(resultCode == RESULT_OK){
                    bottomNavigationBar.selectTab(1, true);
                }
                break;
            case 5:
                if(resultCode == RESULT_OK){
                    bottomNavigationBar.selectTab(0, true);
                }
                break;
        }
    }








    private void setFragment(int position){
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                TitleBarFragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    ft.replace(R.id.main_content, fragment);
                } else {
                    currentFragment = fragment;
                    ft.add(R.id.main_content, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }
    }



}


