package com.yanbao.vo;

import java.util.List;

/**
 * Created by zzwei on 2017/6/6 0006.
 */
public class DelImgVo {
    private String bucket;
    private List<String> iconList;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public List<String> getIconList() {
        return iconList;
    }

    public void setIconList(List<String> iconList) {
        this.iconList = iconList;
    }
}
