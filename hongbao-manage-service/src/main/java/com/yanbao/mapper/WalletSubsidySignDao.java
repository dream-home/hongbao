package com.yanbao.mapper;


import com.mall.model.WalletSubsidySign;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.vo.WalletSubsidySignVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zcj
 * @date 2017年06月05日
 */


public interface WalletSubsidySignDao extends CommonDao<WalletSubsidySign> {

    Integer count(@Param("userid") String userid, @Param("status") Integer status);

    List<WalletSubsidySign> getList(@Param("page") Page page, @Param("userid") String userid, @Param("status") Integer status);
    
    int readSubsidyCount(@Param("model") WalletSubsidySignVo  walletSubsidySignVo);
    List<WalletSubsidySignVo> readList1(@Param("model") WalletSubsidySignVo  walletSubsidySignVo,@Param("page") Page page);
    
}
