package com.i61.parent.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.i61.parent.ui.inter.MessageInterface;

//有新信息广播接收器
public class MessageCountReceiver extends BroadcastReceiver {
    MessageInterface messageInterface;

    @Override
    public void onReceive(Context context, Intent intent) {
        messageInterface.updateUI();  //调用更改界面接口
    }

    public void setMessageInterface(MessageInterface messageInterface) {
        this.messageInterface = messageInterface;
    }
}
