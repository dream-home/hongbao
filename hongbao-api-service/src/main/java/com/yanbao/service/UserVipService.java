package com.yanbao.service;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface UserVipService {

	List<String> getVipList(List<String> ids) throws Exception;
}
