package com.i61.parent.common.data.WorkCenter;

import java.io.Serializable;

/**
 * Created by linxiaodong on 2018/3/21.
 */

public class WorkImageInfo implements Serializable{
    private long id;
    private String smallUrl;
    private String title;
    private int type;
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
