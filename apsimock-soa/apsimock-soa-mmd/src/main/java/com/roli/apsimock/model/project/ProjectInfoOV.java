package com.roli.apsimock.model.project;

/**
 * @author xuxinyu
 * @date 2018/3/20 下午7:53
 */
public class ProjectInfoOV extends ProjectInfo{

    private String userAccount;

    private Integer isMaster;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }
}
