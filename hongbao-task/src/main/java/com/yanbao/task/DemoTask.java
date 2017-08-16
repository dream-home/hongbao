package com.yanbao.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DemoTask extends BaseScheduleTask {

    public static Log log = LogFactory.getLog(DemoTask.class);

    public void demoTask() {
        //TODO  do someting.....
    }

    @Override
    protected void doSpecificTask() {
        log.error("doSpecificTask  START");
        this.demoTask();
        log.error("doSpecificTask STOP");
    }
}
