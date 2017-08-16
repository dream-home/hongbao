package com.yanbao.vo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by summer on 2016-12-16:14:52;
 */
public class BaseVo {

    private String id;

    @JsonIgnore
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fromTime;

    @JsonIgnore
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date stopTime;

    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
