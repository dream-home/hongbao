package com.yanbao.vo;

import java.io.Serializable;

/**
 * @author zzwei
 * @date 2017年06月27日
 */
public class PayPasswordVo implements Serializable {

    private static final long serialVersionUID = 8315942462674630954L;

    private String phone;
    private String smsCode;
    private String newPayPass;
    private String newPayPassConfirm;

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNewPayPass() {
        return newPayPass;
    }

    public void setNewPayPass(String newPayPass) {
        this.newPayPass = newPayPass;
    }

    public String getNewPayPassConfirm() {
        return newPayPassConfirm;
    }

    public void setNewPayPassConfirm(String newPayPassConfirm) {
        this.newPayPassConfirm = newPayPassConfirm;
    }
}
