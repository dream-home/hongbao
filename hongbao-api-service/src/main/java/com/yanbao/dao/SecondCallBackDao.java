package com.yanbao.dao;

import com.mall.model.Ad;
import com.mall.model.SecondCallBack;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface SecondCallBackDao {
	SecondCallBack getById(String id);
}
