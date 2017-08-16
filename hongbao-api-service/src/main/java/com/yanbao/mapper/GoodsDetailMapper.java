package com.yanbao.mapper;

import com.mall.model.GoodsDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List; 

@Repository 
public interface GoodsDetailMapper {

    List<GoodsDetail> getList();
    
    GoodsDetail getById(@Param("id") String id);
    
    List<GoodsDetail> getByGoodsId(@Param("id") String id,@Param("status") Integer status);
    
    List<GoodsDetail> getInLikeId(@Param("ids") List<String> ids);
    
    Integer delId(@Param("id") String id);
    
    Integer delInId(@Param("ids") List<String> ids);
    
    Integer add(@Param("model") GoodsDetail model);
    
    List<GoodsDetail> getPoList(@Param("model") GoodsDetail model);
}
