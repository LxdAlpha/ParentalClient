package com.i61.parent.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linxiaodong on 2018/3/13.
 */

public class CheckUtil {
    //测试是否是手机号码
    public static boolean isPhoneLegal(String phone) {
        String regExpPhone = "\\d{11}$";
        Pattern phonePattern = Pattern.compile(regExpPhone);
        Matcher phoneMatcher = phonePattern.matcher(phone);
        return phoneMatcher.matches();
    }

    //测试密码是否含有特殊字符且不少于6位不多于20位，符合前面任意情况返回false
    public static boolean isPasswordLegal(String password) {
        String regExpPassword = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern passwordPattern = Pattern.compile(regExpPassword);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        return !passwordMatcher.lookingAt() && password.length()<= 20 && password.length() >= 6;
    }

    //测试网络连接，有wifi或移动数据连接则返回true，均未连接返回false
    public static boolean isNetworkConnect(Context context){
        boolean connect = false;

        //api版本>=23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network[] networks = connMgr.getAllNetworks();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < networks.length; i++){
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
                if(networkInfo.isConnected() == true){
                    connect = true;
                }
            }
        }else{//api版本<23
            //步骤1：通过Context.getSystemService(Context.CONNECTIVITY_SERVICE)获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //步骤2：获取ConnectivityManager对象对应的NetworkInfo对象
            // NetworkInfo对象包含网络连接的所有信息
            // 步骤3：根据需要取出网络连接信息
            // 获取WIFI连接的信息
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            Boolean isWifiConn = networkInfo.isConnected();
            //获取移动数据连接的信息
            networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            Boolean isMobileConn = networkInfo.isConnected();
            if(isMobileConn == true || isWifiConn == true){
                connect = true;
            }
        }
        return connect;
    }
}
