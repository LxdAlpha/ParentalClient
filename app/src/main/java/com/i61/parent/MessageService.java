package com.i61.parent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.i61.parent.common.Urls;
import com.i61.parent.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

//轮询信息的服务
public class MessageService extends Service {

    private static RequestCall requestCall;
    private Timer timer;

    public MessageService() {
    }

    //访问查询信息数量的接口
    private RequestCall getRequestCall() {
        if (this.requestCall != null) {
            return this.requestCall;
        } else {
            requestCall = OkHttpUtils.get().url(Urls.HOST + "msg/getMsgCount.json").build();
            return requestCall;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("lxd", "onCreate executed");
        requestCall = OkHttpUtils.get().url(Urls.HOST + "msg/getMsgCount.json").build(); //新建requestCall对象
        timer = new Timer();
    }

    //服务业务执行逻辑
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lxd", "onStartCommand executed");
        Task task = new Task();
        timer.schedule(task, 0, 600000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel(); //取消执行定时任务
        Log.d("lxd", "onDestroy executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //访问接口，获取新信息数量
    class Task extends TimerTask {
        @Override
        public void run() {
            getRequestCall().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtils.e("onError", e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    Gson gson = new Gson();
                    int count = gson.fromJson(jsonObject.get("value").getAsJsonObject().get("msgCount"), Integer.class);
                    if(count > 0){ //如果有新的信息，发出广播
                        Intent intent = new Intent("com.i61.parent.MESSAGE_BROADCAST");
                        sendBroadcast(intent);
                    }
                    Log.d("lxd", count + "");
                }
            });
        }
    }
}
