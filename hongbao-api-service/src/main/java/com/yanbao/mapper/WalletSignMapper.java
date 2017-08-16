package com.yanbao.mapper;

import com.yanbao.core.page.Page;
import com.mall.model.WalletSign;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3 0003.
 */
public interface WalletSignMapper {
    List<WalletSign> getList(@Param("userid") String userid,@Param("page") Page page);
    
   Integer count(@Param("userid") String userid);

   Integer add(@Param("model") WalletSign model);
   
   Double signTotal(@Param("model") WalletSign model,@Param("countNo") Integer countNo);

    Integer getSubsidyCount(@Param("userId") String userId,@Param("grade") String grade);

    Integer getSignCount(@Param("userId") String userId);

    List<WalletSign> getCommonSignList(@Param("userId") String userId,@Param("page") Page page);

    List<WalletSign> getMyDoudouList(@Param("userId") String userId,@Param("page") Page page);

    Integer countMyDoudouListSize(@Param("userId") String userId);

    Integer countCommonSignListSize(@Param("userId") String userId);
}
