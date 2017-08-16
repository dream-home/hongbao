package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.mall.model.WalletDonate;
import com.yanbao.vo.WalletDonateVo;

/**
 * Created by summer on 2016-12-13:16:30;
 */
public interface WalletDonateDao extends CommonDao<WalletDonate> {

    Double getIncome();

    List<WalletDonateVo> readVoList(@Param("model") WalletDonateVo walletDonateVo, @Param("page") Page page);

    Integer readVoCount(@Param("model") WalletDonateVo walletDonateVo);
}
