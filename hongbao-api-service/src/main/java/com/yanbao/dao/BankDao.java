package com.yanbao.dao;

import java.util.List;

import com.mall.model.Bank;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface BankDao {

	List<Bank> getList() throws Exception;

}
