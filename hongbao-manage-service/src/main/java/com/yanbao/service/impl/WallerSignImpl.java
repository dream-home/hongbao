package com.yanbao.service.impl;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.WalletSignDao;
import com.mall.model.WalletSign;
import com.yanbao.service.WalletSignService;
import com.yanbao.vo.WalletSignVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WallerSignImpl extends CommonServiceImpl<WalletSign> implements WalletSignService  {
	
	@Autowired
	WalletSignDao signDao;
	
	 @Override
	    protected CommonDao<WalletSign> getDao() {
	        return signDao;
	    }

	    @Override
	    protected Class<WalletSign> getModelClass() {
	        return WalletSign.class;
	    }
	
	@Override
	public double signSUM(){
		return signDao.signSUM();
		
	}
	
	
	@Override
	public int readSubsidyCount(WalletSignVo walletSignVo){
		return signDao.readSubsidyCount(walletSignVo);
		
	}
	
	@Override
	public List<WalletSignVo> readSubsidyList(WalletSignVo walletSignVo,Page page){
		return signDao.readSubsidyList(walletSignVo,page);
		
	}
	

	@Override
	public List<WalletSignVo> readList1(WalletSignVo walletSignVo,Page page){
		return signDao.readList1(walletSignVo,page);
		
	}
	
	

}
