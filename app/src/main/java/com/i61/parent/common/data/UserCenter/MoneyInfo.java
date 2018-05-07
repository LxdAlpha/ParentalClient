package com.i61.parent.common.data.UserCenter;

import java.io.Serializable;

/**
 * Created by linxiaodong on 2018/3/21.
 */

public class MoneyInfo implements Serializable{
    int bursary;
    int bursaryTotal;
    int drawMoney;
    int drawMoneyTotal;
    int userId;
    String userName;
    int weekDrawMoney;

    public int getBursary() {
        return bursary;
    }

    public void setBursary(int bursary) {
        this.bursary = bursary;
    }

    public int getBursaryTotal() {
        return bursaryTotal;
    }

    public void setBursaryTotal(int bursaryTotal) {
        this.bursaryTotal = bursaryTotal;
    }

    public int getDrawMoney() {
        return drawMoney;
    }

    public void setDrawMoney(int drawMoney) {
        this.drawMoney = drawMoney;
    }

    public int getDrawMoneyTotal() {
        return drawMoneyTotal;
    }

    public void setDrawMoneyTotal(int drawMoneyTotal) {
        this.drawMoneyTotal = drawMoneyTotal;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWeekDrawMoney() {
        return weekDrawMoney;
    }

    public void setWeekDrawMoney(int weekDrawMoney) {
        this.weekDrawMoney = weekDrawMoney;
    }
}
