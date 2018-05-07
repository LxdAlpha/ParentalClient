package com.i61.parent.common.data.WorkCenter;

import java.io.Serializable;

/**
 * Created by linxiaodong on 2018/3/21.
 */

public class CourseInfo implements Serializable{
    String challengeMessage;
    String challengeTitle;
    int courseId;
    int workType;

    public String getChallengeMessage() {
        return challengeMessage;
    }

    public void setChallengeMessage(String challengeMessage) {
        this.challengeMessage = challengeMessage;
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "challengeMessage='" + challengeMessage + '\'' +
                ", challengeTitle='" + challengeTitle + '\'' +
                ", courseId=" + courseId +
                ", workType=" + workType +
                '}';
    }
}
