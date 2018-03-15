package com.i61.parent.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i61.parent.R;
import com.i61.parent.ui.LoginActivity;

/**
 * Created by linxiaodong on 2018/3/12.
 */

public class ToastUtil {
    //登录界面的错误提示信息，可传入不同文本内容
    public static void showErrorToast(Context context, String text){
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        LinearLayout relativeLayout = toastView.findViewById(R.id.toast_linear);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) PxUtil.dpToPx(context, 200), (int)PxUtil.dpToPx(context, 100));
        relativeLayout.setLayoutParams(layoutParams);
        TextView textView = toastView.findViewById(R.id.tv_toast_clear);
        textView.setText(text);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(toastView);
        toast.show();
    }
}
