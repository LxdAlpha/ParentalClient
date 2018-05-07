package com.i61.parent.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.i61.parent.AppApplication;

/**
 * Created by linxiaodong on 2018/3/19.
 */

public class ScreenUtil {

    /**
     *
     * @return 返回一个数组，第一个是屏幕的宽度，第二个是屏幕的高度，单位均是dp
     */
    public static int[] getScreenParameterDp(){
        WindowManager wm = (WindowManager) AppApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height= dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width/density);//屏幕宽度(dp)
        int screenHeight = (int)(height/density);//屏幕高度(dp)
        int[] result = new int[2];
        result[0] = screenWidth;
        result[1] = screenHeight;
        return result;
    }

    /**
     *
     * @return 返回一个数组，第一个是屏幕的宽度，第二个是屏幕的高度，单位均是px
     */
    public static int[] getScreenParameterPx(){
        WindowManager wm = (WindowManager) AppApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height= dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int[] result = new int[2];
        result[0] = width;
        result[1] = height;
        return result;
    }
}
