package com.yanbao.dao;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface UserVipDao {

	List<String> getVipList(List<String> ids) throws Exception;
}
