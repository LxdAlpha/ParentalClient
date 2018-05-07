package com.i61.parent.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.i61.parent.R;
import com.jaeger.library.StatusBarUtil;

public class ShowMessageActivity extends TitleBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int kind = intent.getIntExtra("kind", -1);
        if(kind == 1){
            findViewById(R.id.howtogetcoin).setVisibility(View.VISIBLE);
            findViewById(R.id.howtogetbursary).setVisibility(View.GONE);
            findViewById(R.id.bursarycando).setVisibility(View.GONE);
        }else if(kind == 2){
            findViewById(R.id.howtogetcoin).setVisibility(View.GONE);
            findViewById(R.id.howtogetbursary).setVisibility(View.VISIBLE);
            findViewById(R.id.bursarycando).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_show_message);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bar_icon_pressed_color), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTvTitle.setText("画币");
        mImgMenu.setVisibility(View.GONE);
    }

    @Override
    protected void onBackClick() {
        super.onBackClick();
        this.finish();
    }
}
