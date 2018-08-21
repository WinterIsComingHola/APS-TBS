package com.roli.apsimock.model.user.userov;

import com.roli.apsimock.model.user.UserInfo;

/**
 * @author xuxinyu
 * @date 2018/3/5 下午5:31
 */
public class UserInfoOV extends UserInfo{

    private String passConfirm;

    public String getPassConfirm() {
        return passConfirm;
    }

    public void setPassConfirm(String passConfirm) {
        this.passConfirm = passConfirm;
    }
}
