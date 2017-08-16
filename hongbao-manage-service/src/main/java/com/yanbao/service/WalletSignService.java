package com.yanbao.service;

import java.util.List;

import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.mall.model.WalletSign;
import com.yanbao.vo.WalletSignVo;


public interface WalletSignService extends CommonService<WalletSign> {
		
	double signSUM();
	
	int readSubsidyCount(WalletSignVo walletSignVo);
	List<WalletSignVo> readSubsidyList(WalletSignVo walletSignVo,Page page);
	List<WalletSignVo> readList1(WalletSignVo walletSignVo,Page page);
	

}
