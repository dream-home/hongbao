package com.yanbao.vo;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */
public class AgentBillVo implements Serializable {

    //手机号
    private String phone;
    //代理等级
    private Integer agentLevel;
    private String agentProvince;   //'省代',
    private String agentCity;   //'市代',
    private String agentCountry;    //'县代',
    @JsonIgnore
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fromTime;
    @JsonIgnore
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date stopTime;
    private List<String> ids;       //多个id
    
    //0,未领取，1,已领取
    private Integer status;
    //代理id
    private String agentId;
    
    
    
    
    
    public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(Integer agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getAgentProvince() {
        return agentProvince;
    }

    public void setAgentProvince(String agentProvince) {
        this.agentProvince = agentProvince;
    }

    public String getAgentCity() {
        return agentCity;
    }

    public void setAgentCity(String agentCity) {
        this.agentCity = agentCity;
    }

    public String getAgentCountry() {
        return agentCountry;
    }

    public void setAgentCountry(String agentCountry) {
        this.agentCountry = agentCountry;
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

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
