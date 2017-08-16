package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 首页功能菜单类
 *
 * @date 2016年12月7日
 */
public class IndexBanner extends SimpleModel {

    private static final long serialVersionUID = -7693797776371276572L;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片
     */
    private String img;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
}
