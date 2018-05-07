package com.i61.parent.common.data;

/**
 * Created by linxiaodong on 2018/4/26.
 */

/*
 *用于表示消息的类
 */

import java.util.Date;
public class MsgInfoV2 {
    private int msgId;
    private String title;
    private String content;
    private Long pushTime;
    private String userName;
    private int userId;
    private int teacherId;
    private int creator;
    private int msgType;
    private int status;
    private int readState; // 0：未读  1：已读
    private int retCode;
    private String remark;
    private Long insertTime;

    public MsgInfoV2() {
    }


    public int getMsgId() {
        return msgId;
    }
    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Long getPushTime() {
        return pushTime;
    }
    public void setPushTime(Long pushTime) {
        this.pushTime = pushTime;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    public int getCreator() {
        return creator;
    }
    public void setCreator(int creator) {
        this.creator = creator;
    }
    public int getMsgType() {
        return msgType;
    }
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getReadState() {
        return readState;
    }
    public void setReadState(int readState) {
        this.readState = readState;
    }
    public int getRetCode() {
        return retCode;
    }
    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Long getInsertTime() {
        return insertTime;
    }
    public void setInsertTime(Long insertTime) {
        this.insertTime = insertTime;
    }
}
