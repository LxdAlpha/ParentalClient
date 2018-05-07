package com.i61.parent.common.data.UserCenter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linxiaodong on 2018/3/21.
 */

public class UserCenterValue implements Serializable{
    List<ClassmateMoney> classmatesMoneyInfo;
    MoneyInfo moneyInfo;
    List<Course> userCourseInfos;
    UserProductInfo userProductInfo;

    public List<ClassmateMoney> getClassmatesMoneyInfo() {
        return classmatesMoneyInfo;
    }

    public void setClassmatesMoneyInfo(List<ClassmateMoney> classmatesMoneyInfo) {
        this.classmatesMoneyInfo = classmatesMoneyInfo;
    }

    public MoneyInfo getMoneyInfo() {
        return moneyInfo;
    }

    public void setMoneyInfo(MoneyInfo moneyInfo) {
        this.moneyInfo = moneyInfo;
    }

    public List<Course> getUserCourseInfos() {
        return userCourseInfos;
    }

    public void setUserCourseInfos(List<Course> userCourseInfos) {
        this.userCourseInfos = userCourseInfos;
    }

    public UserProductInfo getUserProductInfo() {
        return userProductInfo;
    }

    public void setUserProductInfo(UserProductInfo userProductInfo) {
        this.userProductInfo = userProductInfo;
    }
}
