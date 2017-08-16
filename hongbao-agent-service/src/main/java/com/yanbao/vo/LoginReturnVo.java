package com.yanbao.vo;

import com.mall.model.AgentInfo;
import com.mall.model.AgentStaff;

/**
 * Created by Administrator on 2017/6/22.
 */
public class LoginReturnVo {

    private String token;
    private Integer errorCode;   //错误码
    private String errorMessage;//错误信息
    private Integer errorCount;
    private AgentInfo agentInfo;//代理信息
    private AgentStaff agentStaff;//代理信息

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public AgentInfo getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(AgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }

    public AgentStaff getAgentStaff() {
        return agentStaff;
    }

    public void setAgentStaff(AgentStaff agentStaff) {
        this.agentStaff = agentStaff;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }
}
