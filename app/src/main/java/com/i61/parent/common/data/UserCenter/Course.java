package com.i61.parent.common.data.UserCenter;

import java.io.Serializable;

/**
 * Created by linxiaodong on 2018/3/21.
 */

public class Course implements Serializable{
    int courseId;
    int index;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
