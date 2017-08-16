package com.yanbao.vo;

import java.util.List;

/**
 * Created by zzwei on 2017/6/27 0027.
 */
public class FeedBackVo {
    /**
     * 反馈用户Id
     */
    private String userId;
    /**
     * 分类标题
     */
    private String title;
    /**
     * 分类类型
     */
    private Integer type;
    /**
     * 反馈描述
     */
    private String detail;
    /**
     * 反馈图片
     */
    private List<String> icons;
    /**
     * 反馈图片
     */
    private List<String> delIcons;

    private  String bucket;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getIcons() {
        return icons;
    }

    public void setIcons(List<String> icons) {
        this.icons = icons;
    }

    public List<String> getDelIcons() {
        return delIcons;
    }

    public void setDelIcons(List<String> delIcons) {
        this.delIcons = delIcons;
    }
}
