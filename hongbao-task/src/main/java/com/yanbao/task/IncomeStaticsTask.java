package com.yanbao.task;

import com.yanbao.service.IncomeStatisticsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
@Component
public class IncomeStaticsTask extends BaseScheduleTask {

    public static Log log = LogFactory.getLog(IncomeStaticsTask.class);
    
    @Autowired
	private IncomeStatisticsService incomeStatistics;

    @Override
    protected void doSpecificTask() {
        log.error("doSpecificTask  START");
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
  		Date starttime=startcalendar.getTime();
  		Date endtime=endcalendar.getTime();		
        try {
        	System.out.println("-------------------");
        	incomeStatistics.IncomeStatistics(starttime,endtime);	
		} catch (Exception e) {
			e.printStackTrace();
		}
        log.error("doSpecificTask STOP");
    }
}
