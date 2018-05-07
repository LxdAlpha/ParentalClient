package com.i61.parent.common.data.WorkCenter;

import com.i61.parent.presenter.SelectAccountPresenter;

import java.io.Serializable;

/**
 * Created by linxiaodong on 2018/3/21.
 */

public class RewardInfo implements Serializable{
    private int courseId;
    private String message;
    private int rewardNum;
    private int state;
    private int userId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRewardNum() {
        return rewardNum;
    }

    public void setRewardNum(int rewardNum) {
        this.rewardNum = rewardNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
