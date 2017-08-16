package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 代理月结账单汇总po类(月度结算)
 * Created by Administrator on 2017/6/15.
 */
public class AgentBill extends SimpleModel {

        private String agentId;      //代理商id
        private String type;            //结算类型：1：提现业绩结算  2：ep业绩结算',
        private String orderNo;      // '结算单号',
        private Double scale;        //结算比例
        private Double amount;      //结算金额：ep或余额',
        private String company;     //收款公司名称',
        private String userName;    //负责人姓名',
        private String phone;       //手机号',
        private String bankCard;    //'银行账户',
        private String bankTypeCode;// '银行类型',
        private String bankType;    // '银行中文名',
        private String bankBranch;  // '开户支行中文名',
        private String agentAreaId; //代理地区id',
        private String agentProvince;   //'省代',
        private String agentCity;   //'市代',
        private String agentCountry;    //'县代',
        private String address;         //'地址',
        private Integer agentLevel;      //'代理等级',
        private Double totalAmount;     //'结算总金额',
        private Double EP;              //'结算ep',
        private Double EPScale;         //结算ep比例',
        private Double balanceScale;        //结算销售比例',
        private Double balance;             //'结算销售',
        private String billday;         //月结日201706150000',

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public Double getScale() {
            return scale;
        }

        public void setScale(Double scale) {
            this.scale = scale;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public String getBankTypeCode() {
            return bankTypeCode;
        }

        public void setBankTypeCode(String bankTypeCode) {
            this.bankTypeCode = bankTypeCode;
        }

        public String getBankType() {
            return bankType;
        }

        public void setBankType(String bankType) {
            this.bankType = bankType;
        }

        public String getBankBranch() {
            return bankBranch;
        }

        public void setBankBranch(String bankBranch) {
            this.bankBranch = bankBranch;
        }

        public String getAgentAreaId() {
            return agentAreaId;
        }

        public void setAgentAreaId(String agentAreaId) {
            this.agentAreaId = agentAreaId;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Integer getAgentLevel() {
            return agentLevel;
        }

        public void setAgentLevel(Integer agentLevel) {
            this.agentLevel = agentLevel;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Double getEP() {
            return EP;
        }

        public void setEP(Double EP) {
            this.EP = EP;
        }

        public Double getEPScale() {
            return EPScale;
        }

        public void setEPScale(Double EPScale) {
            this.EPScale = EPScale;
        }

        public Double getBalanceScale() {
            return balanceScale;
        }

        public void setBalanceScale(Double balanceScale) {
            this.balanceScale = balanceScale;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }

        public String getBillday() {
            return billday;
        }

        public void setBillday(String billday) {
            this.billday = billday;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
}
