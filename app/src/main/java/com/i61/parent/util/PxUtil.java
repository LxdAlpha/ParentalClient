package com.i61.parent.util;

import android.content.Context;

/**
 * Created by linxiaodong on 2018/3/13.
 */

public class PxUtil {

    public static float dpToPx(Context context, int dp) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }
    public static float pxTodp(Context context, int px) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density; return px / density;
    }
 }
