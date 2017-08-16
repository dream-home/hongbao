package com.mall.model;

import com.yanbao.core.model.SimpleModel;


/**
 * 商品详情类
 *
 * @date 2016年12月7日
 */
public class GoodsDetail extends SimpleModel {

    private static final long serialVersionUID = -7693797776371276572L;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 模块大标题
     */
    private String model;
    /**
     * 商品模块添加内容
     */
    private String content;
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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
