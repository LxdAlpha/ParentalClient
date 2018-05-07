package com.i61.parent.common.data.WorkCenter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linxiaodong on 2018/3/21.
 */

public class WorkCenterValue implements Serializable{
    private CourseInfo courseInfo;
    private List<CourseInfo> needSubmitWorkCourseList;
    private RewardInfo rewardInfo;
    private List<WorkImageInfo> workImageInfoList;

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public List<CourseInfo> getNeedSubmitWorkCourseList() {
        return needSubmitWorkCourseList;
    }

    public void setNeedSubmitWorkCourseList(List<CourseInfo> needSubmitWorkCourseList) {
        this.needSubmitWorkCourseList = needSubmitWorkCourseList;
    }

    public RewardInfo getRewardInfo() {
        return rewardInfo;
    }

    public void setRewardInfo(RewardInfo rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

    public List<WorkImageInfo> getWorkImageInfoList() {
        return workImageInfoList;
    }

    public void setWorkImageInfoList(List<WorkImageInfo> workImageInfoList) {
        this.workImageInfoList = workImageInfoList;
    }

    @Override
    public String toString() {
        return "WorkCenterValue{" +
                "courseInfo=" + courseInfo +
                ", needSubmitWorkCourseList=" + needSubmitWorkCourseList +
                ", rewardInfo=" + rewardInfo +
                ", workImageInfoList=" + workImageInfoList +
                '}';
    }
}
