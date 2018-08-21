package com.roli.apsimock.model.project;

import com.roli.apsimock.model.user.UserInfo;

/**
 * @author xuxinyu
 * @date 2018/5/4 下午4:14
 */
public class ProjectInfoOOV extends ProjectInfo{
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
