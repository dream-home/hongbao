package com.yanbao.vo;

/**
 * Created by Administrator on 2017/8/22 0022.
 */
public class GoodsSearchVo {
    /**
     * 商品分类ID
     */
    private String goodsSortId;
    /**
     * 商铺Id
     */
    private String storeId;
    /**
     * 商铺名称
     */
    private String storeName;
    /**
     * 名称
     */
    private String name;
    /**
     * 图片
     */
    private String icon;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 商品介绍
     */
    private String detail;
    /**
     * 商品类型：0：系统发布：1：商家发布
     */
    private Integer goodsType;
    /**
     * 库存
     */
    private Integer stock;

    public String getGoodsSortId() {
        return goodsSortId;
    }

    public void setGoodsSortId(String goodsSortId) {
        this.goodsSortId = goodsSortId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
