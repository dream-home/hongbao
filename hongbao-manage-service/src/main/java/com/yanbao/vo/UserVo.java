package com.yanbao.vo;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import com.yanbao.constant.GradeType;
import com.yanbao.core.model.SimpleModel;

/**
 * Created by summer on 2016-12-14:16:16;
 */
public class UserVo extends SimpleModel implements Serializable{

    /** UID */
    private Integer uid;

    private String userName;
    /** 昵称 */
    private String nickName;
    /** 手机号 */
    private String phone;
    /** 微信openId */
    private String weixin;
    /** 可用积分（总积分=可用积分+冻结积分） */
    private Double score;
    /** 冻结积分（总积分=可用积分+冻结积分 */
    private Double freezeScore;
    /** 银行卡号/支付宝账号/微信支付账号 */
    private String cardNo;
    /** 银行ID */
    private String bankId;
    /** 银行名称*/
    private String bankName;
    /**
     * 邀请人uid
     */
    private String firstReferrer;

    private String addr;
    /**
     * 用户头像
     */
    private String headImgUrl;
    /**
     * 银行卡类型
     */
    /** 卡类型：0：银行卡，1：支付宝，2：微信支付 */
    private Integer type;
    @JsonIgnore
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fromTime;
    @JsonIgnore
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date stopTime;
	/** 是否客服帐号：0：否，1：是 */
	private Integer isKF;
	/** 用户分组：A/B/C */
	private String groupType;
	/** A组下线统计 */
	private Integer groupChildCountA;
	/** B组下线统计 */
	private Integer groupChildCountB;
	/** C组下线统计 */
	private Integer groupChildCountC;
	
	/** 累计最大赠送EP */
	private Double bindEP;
	

	private Double exchangeEP;
	private Double performanceOne;
	private Double performanceTwo;
	private Double performanceThree;
	
	
	private Integer levles;
	private String gradeName;
	private Integer grade;
	
	
	
	
	

	public Integer getLevles() {
		return levles;
	}

	public void setLevles(Integer levles) {
		this.levles = levles;
	}

	

	public Double getExchangeEP() {
		return exchangeEP;
	}

	public void setExchangeEP(Double exchangeEP) {
		this.exchangeEP = exchangeEP;
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

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public Double getBindEP() {
		return bindEP;
	}

	public void setBindEP(Double bindEP) {
		this.bindEP = bindEP;
	}

	public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getFirstReferrer() {
        return firstReferrer;
    }

    public void setFirstReferrer(String firstReferrer) {
        this.firstReferrer = firstReferrer;
    }

	public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getFreezeScore() {
        return freezeScore;
    }

    public void setFreezeScore(Double freezeScore) {
        this.freezeScore = freezeScore;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

	public Integer getIsKF() {
		return isKF;
	}

	public void setIsKF(Integer isKF) {
		this.isKF = isKF;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public Integer getGroupChildCountA() {
		return groupChildCountA;
	}

	public void setGroupChildCountA(Integer groupChildCountA) {
		this.groupChildCountA = groupChildCountA;
	}

	public Integer getGroupChildCountB() {
		return groupChildCountB;
	}

	public void setGroupChildCountB(Integer groupChildCountB) {
		this.groupChildCountB = groupChildCountB;
	}

	public Integer getGroupChildCountC() {
		return groupChildCountC;
	}

	public void setGroupChildCountC(Integer groupChildCountC) {
		this.groupChildCountC = groupChildCountC;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(Integer grade) {
		this.gradeName = GradeType.fromCode(grade).getMsg();
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
		setGradeName(grade);
	}
    
    
}
