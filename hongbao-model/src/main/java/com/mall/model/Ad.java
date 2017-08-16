package com.mall.model;


import com.yanbao.core.model.SimpleModel;

/**
 * 广告类
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class Ad extends SimpleModel {

    private static final long serialVersionUID = -7693797776371276572L;
    /**
     * 广告标题
     */
    private String title;
    /**
     * 广告图片
     */
    private String adImg;
    /**
     * 广告链接
     */
    private String href;
    /**
     * 排序号
     */
    private Integer rank;
    /**
     * 类型
     */
    private Integer type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdImg() {
        return adImg;
    }

    public void setAdImg(String adImg) {
        this.adImg = adImg;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
