package com.ruoli.soa.model;

import java.util.Date;

/**
 * @author xuxinyu
 * @date 2018/2/9 上午10:52
 */
public class SoaHostAuthModel {

    private Integer appID;

    private String hostName;

    private String ip;

    private String secretKey;

    private String scope;

    private String description;

    private Integer yn;

    private Date created;

    private Date updated;

    //select是否成功
    private boolean selectTrue;
    private long t = System.currentTimeMillis();


    //====================setter and getter=================================


    public Integer getAppID() {
        return appID;
    }

    public void setAppID(Integer appID) {
        this.appID = appID;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean isSelectTrue() {
        return selectTrue;
    }

    public void setSelectTrue(boolean selectTrue) {
        this.selectTrue = selectTrue;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }
}
