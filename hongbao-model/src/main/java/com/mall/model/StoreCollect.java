package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 商铺收藏表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class StoreCollect extends SimpleModel {

    private static final long serialVersionUID = -5786594528006091225L;
    /**
     * 商品分类ID
     */
    private String userId;
    /**
     * 商铺Id
     */
    private String storeId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

}
