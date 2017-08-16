package com.yanbao.task;

import com.yanbao.service.InAgentStaticsService;
import com.yanbao.util.DateTimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 区域代理业绩结算
 * Created by Administrator on 2017/6/19.
 */
@Component
public class InAgentStaticsTask extends BaseScheduleTask {

    public static Log log = LogFactory.getLog(InAgentStaticsTask.class);

    @Autowired
    private InAgentStaticsService inAgentStaticsService;

    @Override
    protected void doSpecificTask() {
        log.error("InAgentStaticsTask  START");
        try {
            System.out.println("-------------------");
            inAgentStaticsService.inAgentStatistics(DateTimeUtil.getDayMinus(1),DateTimeUtil.getDayFirst());
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("InAgentStaticsTask STOP");
    }
    

    public void TestTask() {
        System.out.println("-------------------------"+new Date().toLocaleString());
    }
}
