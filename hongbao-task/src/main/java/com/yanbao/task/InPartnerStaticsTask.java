package com.yanbao.task;

import com.mall.model.Parameter;
import com.yanbao.service.InAgentStaticsService;
import com.yanbao.service.PartnerBillService;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.ParamUtil;
import com.yanbao.util.ToolUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 合伙人业绩结算
 * Created by Administrator on 2017/6/19.
 */
@Component
public class InPartnerStaticsTask extends BaseScheduleTask {

    public static Log log = LogFactory.getLog(InPartnerStaticsTask.class);

    @Autowired
    private PartnerBillService partnerBillService;

    @Override
    protected void doSpecificTask() {
        log.error("----------------------------------------------InPartnerStaticsTask  START---------------------------");
        try {
            int nowDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int partnerDate = ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.BILLDAY,"15"));
            if(nowDate != partnerDate){
                log.info("当前不是合伙人结算日期！！！");
                log.error("----------------------------------------------当前不是合伙人结算日期------------------------------------------");
                return;
            }
            //获取上月时间
            Calendar startcalendar = Calendar.getInstance();
            startcalendar.add(Calendar.MONTH, -1);
            startcalendar.set(Calendar.DAY_OF_MONTH, 1);
            startcalendar.set(Calendar.HOUR_OF_DAY, 0);
            startcalendar.set(Calendar.MINUTE, 0);
            startcalendar.set(Calendar.SECOND, 0);
            Calendar endcalendar = Calendar.getInstance();
            endcalendar.set(Calendar.DAY_OF_MONTH, 1);
            endcalendar.set(Calendar.HOUR_OF_DAY, 0);
            endcalendar.set(Calendar.MINUTE, 0);
            endcalendar.set(Calendar.SECOND, 0);
            System.out.println("-------------------");
            /**时间格式为YYYY-MM-DD*/
            partnerBillService.inPartnerStatistics(DateTimeUtil.formatDate(startcalendar.getTime(),DateTimeUtil.PATTERN_C),DateTimeUtil.formatDate(endcalendar.getTime(),DateTimeUtil.PATTERN_C));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("----------------------------------------------InPartnerStaticsTask STOP------------------------------------------");
    }

    public static void main(String[] args) {
        Calendar startcalendar = Calendar.getInstance();
        startcalendar.add(Calendar.MONTH, -1);
        startcalendar.set(Calendar.DAY_OF_MONTH, 1);
        startcalendar.set(Calendar.HOUR_OF_DAY, 0);
        startcalendar.set(Calendar.MINUTE, 0);
        startcalendar.set(Calendar.SECOND, 0);
        Calendar endcalendar = Calendar.getInstance();
        endcalendar.set(Calendar.DAY_OF_MONTH, 1);
        endcalendar.set(Calendar.HOUR_OF_DAY, 0);
        endcalendar.set(Calendar.MINUTE, 0);
        endcalendar.set(Calendar.SECOND, 0);
        System.out.println(startcalendar.getTimeInMillis() > endcalendar.getTimeInMillis());

    }
}
