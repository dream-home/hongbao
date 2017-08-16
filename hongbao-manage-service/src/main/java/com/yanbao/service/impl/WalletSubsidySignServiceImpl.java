package com.yanbao.service.impl;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.WalletSubsidySignDao;
import com.mall.model.WalletSubsidySign;
import com.yanbao.service.WalletSubsidySignService;
import com.yanbao.vo.WalletSubsidySignVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author zcj
 * @date 2017年06月05日
 */
@Service
public class WalletSubsidySignServiceImpl extends CommonServiceImpl<WalletSubsidySign> implements WalletSubsidySignService {

    @Autowired
    WalletSubsidySignDao walletSubsidySignDao;

    @Override
    protected CommonDao<WalletSubsidySign> getDao() {
        return walletSubsidySignDao;
    }

    @Override
    protected Class<WalletSubsidySign> getModelClass() {
        return WalletSubsidySign.class;
    }

    @Override
    public Integer count(String userid, Integer status) throws Exception {
        if (StringUtils.isEmpty(userid) || status == null) {
            return 0;
        }
        Integer count = walletSubsidySignDao.count(userid, status);
        if (count == null) {
            count = 0;
        }
        return count;
    }

    @Override
    public List<WalletSubsidySign> getList(Page page, String userid, Integer status) throws Exception {
        if (StringUtils.isEmpty(userid) || status == null) {
            return Collections.EMPTY_LIST;
        }
        List<WalletSubsidySign> list = walletSubsidySignDao.getList(page, userid, status);
        if (list == null || list.size() == 0) {
            list = Collections.EMPTY_LIST;
        }
        return list;
    }
    
    
	@Override
	public int readSubsidyCount(WalletSubsidySignVo walletSubsidySignVo){
		return walletSubsidySignDao.readSubsidyCount(walletSubsidySignVo);
		
	}
	
	@Override
	public List<WalletSubsidySignVo> readList1(WalletSubsidySignVo walletSubsidySignVo,Page page){
		return walletSubsidySignDao.readList1(walletSubsidySignVo,page);
		
	}
    
    
    
    
}
