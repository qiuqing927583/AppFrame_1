package com.android.yawei.jhoa.bean;

/**
 * 设备信息
 * Created by Yusz on 2018-5-10.
 */

public class DevicesInfoBean {

    public String deve_brand;//设备品牌
    public String deve_version;//设备系统版本
    public String deve_model;//设备型号
    public String deve_imei;//设备的唯一标示

    public String getDeve_brand() {
        return deve_brand;
    }

    public void setDeve_brand(String deve_brand) {
        this.deve_brand = deve_brand;
    }

    public String getDeve_version() {
        return deve_version;
    }

    public void setDeve_version(String deve_version) {
        this.deve_version = deve_version;
    }

    public String getDeve_model() {
        return deve_model;
    }

    public void setDeve_model(String deve_model) {
        this.deve_model = deve_model;
    }

    public String getDeve_imei() {
        return deve_imei;
    }

    public void setDeve_imei(String deve_imei) {
        this.deve_imei = deve_imei;
    }
}
