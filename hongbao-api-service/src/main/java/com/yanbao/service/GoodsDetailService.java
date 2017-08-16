package com.yanbao.service;

import java.util.List;

import com.mall.model.GoodsDetail;

/**
 * <p>Title:GoodsDetailService</p>
 * <p>Description:商品简介表服务接口类</p> 
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 炎宝科技</p>
 * @author Test - 1091945691@qq.com
 * @version v1.0 2017-05-17
 */
public interface GoodsDetailService {
	
	List<GoodsDetail> getList()throws Exception;
    
    GoodsDetail getById(String id)throws Exception;
    
    List<GoodsDetail> getByGoodsId(String id)throws Exception;
    
    List<GoodsDetail> getByGoodsId(String id,Integer status)throws Exception;
    
    List<GoodsDetail> getInLikeId(List<String> ids)throws Exception;
    
    Integer delId(String id)throws Exception;
    
    Integer delInId(List<String> ids)throws Exception;
    
    Integer add(GoodsDetail model)throws Exception;
    
    List<GoodsDetail> getPoList(GoodsDetail model)throws Exception;
    
}