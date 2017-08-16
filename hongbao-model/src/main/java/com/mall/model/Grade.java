package com.mall.model;

import java.util.Date;

import com.yanbao.core.model.SimpleModel;

/**
 * 会员等级表
 *
 * @author zcj
 * @date 2017年03月02日
 */
public class Grade extends SimpleModel {
    private static final long serialVersionUID = -6966654808156525194L;
    /*等级编号 */
    private Integer grade;
    /*EP数量*/
    private Double EP;
    /*销售一部业绩*/
    private Double performanceOne;
    /*销售二部业绩*/
    private Double performanceTwo;
    /*销售三部业绩*/
    private Double performanceThree;
    /*日计月薪*/
    private Integer daily;
    /*领取次数*/
    private Integer totalSignNo;
    
    private Date fromTime;
    
    private Date stopTime;

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Double getEP() {
        return EP;
    }

    public void setEP(Double EP) {
        this.EP = EP;
    }

    public Double getPerformanceOne() {
        return performanceOne;
    }

    public void setPerformanceOne(Double performanceOne) {
        this.performanceOne = performanceOne;
    }

    public Double getPerformanceTwo() {
        return performanceTwo;
    }

    public void setPerformanceTwo(Double performanceTwo) {
        this.performanceTwo = performanceTwo;
    }

    public Double getPerformanceThree() {
        return performanceThree;
    }

    public void setPerformanceThree(Double performanceThree) {
        this.performanceThree = performanceThree;
    }

    public Integer getDaily() {
        return daily;
    }

    public void setDaily(Integer daily) {
        this.daily = daily;
    }

    public Integer getTotalSignNo() {
        return totalSignNo;
    }

    public void setTotalSignNo(Integer totalSignNo) {
        this.totalSignNo = totalSignNo;
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
