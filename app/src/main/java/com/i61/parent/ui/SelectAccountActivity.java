package com.i61.parent.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i61.parent.R;

import java.util.ArrayList;
import java.util.List;

public class SelectAccountActivity extends AppCompatActivity {

    private TextView actionBarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
        actionBarTitle = findViewById(R.id.action_bar_title);
        actionBarTitle.setText("选择帐号");

        List<String> names = new ArrayList<>();
        names.add("测试1");
        names.add("测试2");
        names.add("测试3");
        names.add("测试4");
        names.add("测试5");

        RecyclerView recyclerView = findViewById(R.id.recycleView_select_account);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SelectAccountAdapter adapter = new SelectAccountAdapter(names);
        recyclerView.setAdapter(adapter);
    }
}
