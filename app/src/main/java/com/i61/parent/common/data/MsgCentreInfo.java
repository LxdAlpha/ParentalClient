package com.i61.parent.common.data;

/**
 * Created by linxiaodong on 2018/4/12.
 */


public class MsgCentreInfo {
    private int msgType;            //信息种类，1：通知 2：画币 3：奖学金 4：作业 5：点评
    private String msgTypeTitle;   //信息名称
    private int msgTypeCount;      //信息数量

    public int getMsgType() {
        return msgType;
    }
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    public String getMsgTypeTitle() {
        return msgTypeTitle;
    }
    public void setMsgTypeTitle(String msgTypeTitle) {
        this.msgTypeTitle = msgTypeTitle;
    }
    public int getMsgTypeCount() {
        return msgTypeCount;
    }
    public void setMsgTypeCount(int msgTypeCount) {
        this.msgTypeCount = msgTypeCount;
    }
}
