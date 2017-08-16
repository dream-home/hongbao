package com.yanbao.core.service;

import java.util.List;

import com.yanbao.core.model.SimpleModel;

/**
 * Created by summer on 2016-12-02:09:35;
 */
public interface CommonService<M extends SimpleModel> {

	// C
	void create(M model);

	void createWithUUID(M model);

	// R
	M readById(String id);


	M readOne(M model);

	// pageNo 业务上的起始页，一般从1开始
	List<M> readList(M model, int pageNo, int pageSize, int totalRow);

	List<M> readAll(M model);

	int readCount(M model);

	// U
	void updateById(String id, M model);

	// D
	void deleteById(String id);

	

}
