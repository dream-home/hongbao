package com.yanbao.service.impl;

import com.yanbao.dao.IndexAdDao;
import com.mall.model.IndexAd;
import com.yanbao.service.IndexAdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2017年05月18日
 */
@Service
public class IndexAdServiceImpl implements IndexAdService {

	private static final Logger logger = LoggerFactory.getLogger(IndexAdServiceImpl.class);

	@Autowired
	private IndexAdDao indexAdDao;

	@Override
	public List<IndexAd> getIndexAdList() throws Exception {
		return indexAdDao.getList();
	}

}
