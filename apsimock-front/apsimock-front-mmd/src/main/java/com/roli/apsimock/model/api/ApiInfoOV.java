package com.roli.apsimock.model.api;

/**
 * @author xuxinyu
 * @date 2018/3/27 下午7:58
 */
public class ApiInfoOV extends ApiInfo{

    private String userAccount;
    private String projectName;


    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
