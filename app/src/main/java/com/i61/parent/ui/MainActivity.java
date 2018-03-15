package com.i61.parent.ui;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.i61.parent.R;
import com.i61.parent.util.DialogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DialogUtil.CommonDialog(this, R.style.PopupDialog, "您确定要退出该帐号吗？",new DialogUtil.CommonDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if(confirm){
                    //Toast.makeText(this,"点击确定", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }).setTitle("提示").show();
    }
}
