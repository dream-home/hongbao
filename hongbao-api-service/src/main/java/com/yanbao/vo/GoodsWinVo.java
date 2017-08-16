package com.yanbao.vo;

import com.mall.model.GoodsWin;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的竞拍中奖表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class GoodsWinVo extends GoodsWin {
    /**
     * 商家赠送EP
     */
    private Double businessSendEp;
    /** 图片 */
    private String storeIcon;

    public Double getBusinessSendEp() {
        return businessSendEp;
    }

    public void setBusinessSendEp(Double businessSendEp) {
        this.businessSendEp = businessSendEp;
    }

    /**
     * 多笔订单购物车商品
     */
    private List<CartGoodsVo> cartList=new ArrayList<>();

    public List<CartGoodsVo> getCartList() {
        return cartList;
    }

    public void setCartList(List<CartGoodsVo> cartList) {
        this.cartList = cartList;
    }

    public String getStoreIcon() {
        return storeIcon;
    }

    public void setStoreIcon(String storeIcon) {
        this.storeIcon = storeIcon;
    }
}

