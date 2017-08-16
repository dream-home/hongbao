package com.yanbao.mapper;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.mall.model.WalletSign;
import com.yanbao.vo.WalletSignVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WalletSignDao extends  CommonDao<WalletSign>{
	
	
	double signSUM();
	
	int readSubsidyCount(@Param("model") WalletSignVo  walletSignVo);
	
	List<WalletSignVo> readSubsidyList(@Param("model") WalletSignVo  walletSignVo,@Param("page") Page page);
	List<WalletSignVo> readList1(@Param("model") WalletSignVo  walletSignVo,@Param("page") Page page);
	
}
