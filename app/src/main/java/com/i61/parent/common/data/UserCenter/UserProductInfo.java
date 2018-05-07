package com.i61.parent.common.data.UserCenter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by linxiaodong on 2018/3/21.
 */

public class UserProductInfo implements Serializable{
    Date courseEndDate;
    int freeCourseNumber;
    int giftCourseNumber;
    int hisCourseWatchedTimes;
    int hisCourseWatchTimes;
    int learnedCourseNumber;
    int learnedFreeCourseNumber;
    int learnedGiftCourseNumber;
    int leftCourseCount;
    int purchaseCourseNumber;
    int remainCourseNumber;
    String stringCourseEndDate;
    int userId;

    public Date getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(Date courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public int getFreeCourseNumber() {
        return freeCourseNumber;
    }

    public void setFreeCourseNumber(int freeCourseNumber) {
        this.freeCourseNumber = freeCourseNumber;
    }

    public int getGiftCourseNumber() {
        return giftCourseNumber;
    }

    public void setGiftCourseNumber(int giftCourseNumber) {
        this.giftCourseNumber = giftCourseNumber;
    }

    public int getHisCourseWatchedTimes() {
        return hisCourseWatchedTimes;
    }

    public void setHisCourseWatchedTimes(int hisCourseWatchedTimes) {
        this.hisCourseWatchedTimes = hisCourseWatchedTimes;
    }

    public int getHisCourseWatchTimes() {
        return hisCourseWatchTimes;
    }

    public void setHisCourseWatchTimes(int hisCourseWatchTimes) {
        this.hisCourseWatchTimes = hisCourseWatchTimes;
    }

    public int getLearnedCourseNumber() {
        return learnedCourseNumber;
    }

    public void setLearnedCourseNumber(int learnedCourseNumber) {
        this.learnedCourseNumber = learnedCourseNumber;
    }

    public int getLearnedFreeCourseNumber() {
        return learnedFreeCourseNumber;
    }

    public void setLearnedFreeCourseNumber(int learnedFreeCourseNumber) {
        this.learnedFreeCourseNumber = learnedFreeCourseNumber;
    }

    public int getLearnedGiftCourseNumber() {
        return learnedGiftCourseNumber;
    }

    public void setLearnedGiftCourseNumber(int learnedGiftCourseNumber) {
        this.learnedGiftCourseNumber = learnedGiftCourseNumber;
    }

    public int getLeftCourseCount() {
        return leftCourseCount;
    }

    public void setLeftCourseCount(int leftCourseCount) {
        this.leftCourseCount = leftCourseCount;
    }

    public int getPurchaseCourseNumber() {
        return purchaseCourseNumber;
    }

    public void setPurchaseCourseNumber(int purchaseCourseNumber) {
        this.purchaseCourseNumber = purchaseCourseNumber;
    }

    public int getRemainCourseNumber() {
        return remainCourseNumber;
    }

    public void setRemainCourseNumber(int remainCourseNumber) {
        this.remainCourseNumber = remainCourseNumber;
    }

    public String getStringCourseEndDate() {
        return stringCourseEndDate;
    }

    public void setStringCourseEndDate(String stringCourseEndDate) {
        this.stringCourseEndDate = stringCourseEndDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
