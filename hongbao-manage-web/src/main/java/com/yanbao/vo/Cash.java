package com.yanbao.vo;

import java.io.Serializable;

/***
 * json对象返回参数
 *
 */
public class Cash implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String tranId;
	private String sysTranId;
	private String sysDate;
	private String respCode;
	private String respMsg;
	private String sign;
	private String rcvAcctNo;
	private String rcvAcctName;
	private String tranAmt;
	private String remark;
	private String origRespCode;
	private String origRespMsg;
	
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getRcvAcctNo() {
		return rcvAcctNo;
	}
	public void setRcvAcctNo(String rcvAcctNo) {
		this.rcvAcctNo = rcvAcctNo;
	}
	public String getRcvAcctName() {
		return rcvAcctName;
	}
	public void setRcvAcctName(String rcvAcctName) {
		this.rcvAcctName = rcvAcctName;
	}
	public String getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSysTranId() {
		return sysTranId;
	}
	public void setSysTranId(String sysTranId) {
		this.sysTranId = sysTranId;
	}
	public String getSysDate() {
		return sysDate;
	}
	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}
	public String getOrigRespCode() {
		return origRespCode;
	}
	public void setOrigRespCode(String origRespCode) {
		this.origRespCode = origRespCode;
	}
	public String getOrigRespMsg() {
		return origRespMsg;
	}
	public void setOrigRespMsg(String origRespMsg) {
		this.origRespMsg = origRespMsg;
	}
}
