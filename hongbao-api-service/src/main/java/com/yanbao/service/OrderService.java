package com.yanbao.service;

import com.mall.model.Goods;
import com.mall.model.GoodsWin;
import com.mall.model.Store;
import com.mall.model.User;
import com.yanbao.vo.CartVo;
import com.yanbao.vo.OrderPurchaseVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/3.
 */
public interface OrderService {

    /**直接购买*/
    GoodsWin addOrderByApp(User user, List<Goods> goodsList, OrderPurchaseVo orderPurchaseVo,Store store) throws Exception;

    /***
     * 根据商品idlist，商品数量重新计算应付总金额，优惠后实付金额，EP总折扣金额
     * @param cartList
     * @param user
     * @return
     * @throws Exception
     */
    Map<String,Double> countMoney(List<CartVo> cartList,User user,String goodWinId) throws Exception;


    @Transactional
    Map<String,Double> calcMoney(boolean isUseDiscount, User user, String goodWinId) throws Exception;

    /***
     *
     * 待付订单重新购买更新主表和明细表
     *
     * @param cartList
     * @param user
     * @param goodsWin
     * @throws Exception
     */
    void  purchasingUpdate(List<CartVo> cartList,User user,GoodsWin goodsWin,Map<String,Double> map) throws Exception;

    /**订单购买处理*/
    Boolean purchaseHandlerByApp(User user, String orderNo) throws Exception;

}
