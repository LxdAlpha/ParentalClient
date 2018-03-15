package com.i61.parent.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.i61.parent.R;

/**
 * Created by linxiaodong on 2018/3/14.
 */

public class DialogUtil {

    //通用的Dialog弹出提示框，需在application中定义style改良显示效果
    public static class CommonDialog extends Dialog implements View.OnClickListener {
        private TextView contentTxt;
        private TextView titleTxt;
        private TextView submitTxt;
        private TextView cancelTxt;

        private Context mContext;
        private String content;
        private OnCloseListener listener;
        private String positiveName;
        private String negativeName;
        private String title;

        private boolean isTitle; // 当需要显示标题时位true，否则为false

        public CommonDialog(Context context, int themeResId, String content, OnCloseListener listener) {
            super(context, themeResId);
            this.mContext = context;
            this.content = content;
            this.listener = listener;
            isTitle = false;
        }

        public CommonDialog(Context context, int themeResId, String content, String title, OnCloseListener listener) {
            super(context, themeResId);
            this.mContext = context;
            this.content = content;
            this.listener = listener;
            this.title = title;
            isTitle = true;
        }


        public interface OnCloseListener {
            void onClick(Dialog dialog, boolean confirm);
        }

        public CommonDialog setTitle(String title) {
            this.title = title;
            return this;
        }

        public CommonDialog setPositiveButton(String name) {
            this.positiveName = name;
            return this;
        }

        public CommonDialog setNegativeButton(String name) {
            this.negativeName = name;
            return this;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_common);
            setCanceledOnTouchOutside(false);
            initView();
        }

        private void initView() {
            contentTxt = (TextView) findViewById(R.id.content);
            titleTxt = (TextView) findViewById(R.id.title);
            submitTxt = (TextView) findViewById(R.id.submit);
            submitTxt.setOnClickListener(this);
            cancelTxt = (TextView) findViewById(R.id.cancel);
            cancelTxt.setOnClickListener(this);



            contentTxt.setText(content);
            if (!TextUtils.isEmpty(positiveName)) {
                submitTxt.setText(positiveName);
            }

            if (!TextUtils.isEmpty(negativeName)) {
                cancelTxt.setText(negativeName);
            }

            if(isTitle == false){
                titleTxt.setVisibility(View.GONE);
            }else if(!TextUtils.isEmpty(title)){
                titleTxt.setText(title);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancel:
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    this.dismiss();
                    break;
                case R.id.submit:
                    if (listener != null) {
                        listener.onClick(this, true);
                    }
                    break;
            }
        }
    }
}
