package com.i61.parent.common.data.LoginResponse;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by linxiaodong on 2018/3/15.
 */

public class Account implements Serializable, Comparable<Account>{
    long userId;
    String nickName;
    String  account;
    String headUrl;

    public long getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getAccount() {
        return account;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", account='" + account + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public int compareTo(@NonNull Account account) {
        return (int) (this.getUserId() - account.getUserId());
    }
}
