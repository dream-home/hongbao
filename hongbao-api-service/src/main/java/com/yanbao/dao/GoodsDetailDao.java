package com.yanbao.dao;

import java.util.List;
import com.mall.model.GoodsDetail;

public interface GoodsDetailDao { 
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
