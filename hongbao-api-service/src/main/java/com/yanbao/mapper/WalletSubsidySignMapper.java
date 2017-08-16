package com.yanbao.mapper;


import com.mall.model.WalletSubsidySign;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author zcj
 * @date 2017年06月05日
 */
public interface WalletSubsidySignMapper {

    Integer add(@Param("model") WalletSubsidySign model);

    Integer update(@Param("model") WalletSubsidySign model,@Param("id") String id);

    WalletSubsidySign getByUserId(@Param("userid") String userid,@Param("status") Integer status);
}
