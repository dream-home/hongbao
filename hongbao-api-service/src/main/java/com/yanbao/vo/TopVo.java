package com.yanbao.vo;

import java.io.Serializable;

/**
 * 竞拍排行榜
 * 
 * @author zhuzh
 * @date 2016年12月7日
 */
public class TopVo implements Serializable {

	private static final long serialVersionUID = -2191914319879790665L;

	/** 中奖积分 */
	private Double score;
	/** 真实姓名 */
	private String userName;
	/** 昵称 */
	private String nickName;

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
