package com.i61.parent.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.common.data.LoginResponse.LoginResponse;
import com.i61.parent.presenter.SelectAccountPresenter;
import com.i61.parent.presenter.impl.SelectAccountPresenterImpl;
import com.i61.parent.util.ToastUtil;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.i61.parent.view.SelectAccountView;

import java.util.ArrayList;
import java.util.List;

public class SelectAccountActivity extends AppCompatActivity  implements SelectAccountView{

    private TextView actionBarTitle;
    private SelectAccountPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //todo 开发期间先删除相关存储信息便于测试
        //SharedPreferencesUtils.readObject(this, "userCenter");
        //SharedPreferencesUtils.readObject(this, "workCenter");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
        actionBarTitle = findViewById(R.id.action_bar_title);
        actionBarTitle.setText("选择帐号");

        //点击返回按钮或文字须将保存的孩子信息删除
        findViewById(R.id.action_bar_backword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.removeActiveAccount();
            }
        });

        findViewById(R.id.action_bar_backIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.removeActiveAccount();
            }
        });
        presenter = new SelectAccountPresenterImpl(this);



        RecyclerView recyclerView = findViewById(R.id.recycleView_select_account);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //SelectAccountAdapter adapter = new SelectAccountAdapter(names);
        SelectAccountAdapter adapter = new SelectAccountAdapter(presenter);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void back() {
        this.finish();
    }

    @Override
    public void intent() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void showSaveFailed() {
        ToastUtil.showErrorToast(this, "获取帐号信息出错");
    }

}
