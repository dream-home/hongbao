package com.yanbao.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mall.model.Bank;

@Repository
public interface BankMapper {  

	List<Bank> getList();
	  
}
