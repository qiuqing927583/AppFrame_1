package com.android.yawei.jhoa.bean;

public class ConLogBean {
    private String accountName;//登录账号
    private String mobileBrand;//手机品牌（苹果、xiaomi等）
    private String mobileType;//手机型号（iPhone4、4s、5等）
    private String mobileVersion;//手机品牌（iOS6、android4等）
    private String mobileIEMI;//手机IEMI（手机唯一标识）
    private String mobileNum;//手机号
    private String ipAddress;//手机ip地址
    private String loginTime;//登录时间


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getMobileVersion() {
        return mobileVersion;
    }

    public void setMobileVersion(String mobileVersion) {
        this.mobileVersion = mobileVersion;
    }

    public String getMobileIEMI() {
        return mobileIEMI;
    }

    public void setMobileIEMI(String mobileIEMI) {
        this.mobileIEMI = mobileIEMI;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
