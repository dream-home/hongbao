package com.mall.model;

import java.util.Date;

import com.yanbao.core.model.SimpleModel;

/**
 * 消费EP流水表(签到和兑换)
 *
 * @author zcj
 * @date 2017年03月03日
 */
public class ConsumeEPRecord extends SimpleModel {

    private static final long serialVersionUID = 1555367273865792610L;

    private String userId;
    private String orderNo;
    private Double consumeEp;
    private Integer grade;
    private Integer type;
    private String uid;
    private String phone;
    private Date fromTime;
    
    private Date stopTime;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getConsumeEp() {
        return consumeEp;
    }

    public void setConsumeEp(Double consumeEp) {
        this.consumeEp = consumeEp;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
    
}
