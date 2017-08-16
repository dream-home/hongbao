package com.yanbao.vo;

/**
 * Created by summer on 2016-12-30:11:02;
 * <p>
 * 系统的财务统计类
 */
public class FinancialStatistics {

    //充值订单数量
    Integer rechargeCount;
    //提现订单数量
    Integer exchangeCount;
    //充值总金额
    Double rechaScoreCount;
    //提现总金额
    Double exchangeScoreCount;
    //未审核提现订单
    Integer unCheckCount;
    //系统当前收益
    Double totalProfit;
    
    //Ep总数
    Double exchangeEP;
    
    
    //系统ep收入
    Double conSumeEPSUM;
    
    //ep兑换商品数量
    Integer conSumeCount;
    
    
    //系统ep支出
    Double sign;
    
    //积分转换ep
    
    Double sysRecord;
    
    
    
    //用户总余额
    Double scoreSUM;
    
    
    
    
    //系统账户积分余额
    Double  sysScoreBalance;
    //系统账户ep余额；
    Double  sysEPBalance;
    
    
    
    
    
    
    
    
    

    public Double getSysScoreBalance() {
		return sysScoreBalance;
	}

	public void setSysScoreBalance(Double sysScoreBalance) {
		this.sysScoreBalance = sysScoreBalance;
	}

	public Double getSysEPBalance() {
		return sysEPBalance;
	}

	public void setSysEPBalance(Double sysEPBalance) {
		this.sysEPBalance = sysEPBalance;
	}

	public Double getScoreSUM() {
		return scoreSUM;
	}

	public void setScoreSUM(Double scoreSUM) {
		this.scoreSUM = scoreSUM;
	}

	public Double getSysRecord() {
		return sysRecord;
	}

	public void setSysRecord(Double sysRecord) {
		this.sysRecord = sysRecord;
	}

	public Double getConSumeEPSUM() {
		return conSumeEPSUM;
	}

	public void setConSumeEPSUM(Double conSumeEPSUM) {
		this.conSumeEPSUM = conSumeEPSUM;
	}

	public Integer getConSumeCount() {
		return conSumeCount;
	}

	public void setConSumeCount(Integer conSumeCount) {
		this.conSumeCount = conSumeCount;
	}

	public Double getSign() {
		return sign;
	}

	public void setSign(Double sign) {
		this.sign = sign;
	}

	public Double getExchangeEP() {
		return exchangeEP;
	}

	public void setExchangeEP(Double exchangeEP) {
		this.exchangeEP = exchangeEP;
	}

	public Double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(Double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Integer getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Integer rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public Integer getExchangeCount() {
        return exchangeCount;
    }

    public void setExchangeCount(Integer exchangeCount) {
        this.exchangeCount = exchangeCount;
    }

    public Double getRechaScoreCount() {
        return rechaScoreCount;
    }

    public void setRechaScoreCount(Double rechaScoreCount) {
        this.rechaScoreCount = rechaScoreCount;
    }

    public Double getExchangeScoreCount() {
        return exchangeScoreCount;
    }

    public void setExchangeScoreCount(Double exchangeScoreCount) {
        this.exchangeScoreCount = exchangeScoreCount;
    }

    public Integer getUnCheckCount() {
        return unCheckCount;
    }

    public void setUnCheckCount(Integer unCheckCount) {
        this.unCheckCount = unCheckCount;
    }
}
