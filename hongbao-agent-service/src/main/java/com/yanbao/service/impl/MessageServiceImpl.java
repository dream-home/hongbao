package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.Message;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.MessageMapper;
import com.yanbao.service.MessageService;

/**
 * Created by summer on 2016-12-09:12:03;
 */
@Service
public class MessageServiceImpl extends CommonServiceImpl<Message> implements MessageService {

    @Autowired
    MessageMapper messageDao;

    @Override
    protected CommonDao<Message> getDao() {
        return messageDao;
    }

    @Override
    protected Class<Message> getModelClass() {
        return Message.class;
    }
    @Override
    public void insertAll(Message message){
    	
    	
    	messageDao.insertAll(message);
    }
    
}
