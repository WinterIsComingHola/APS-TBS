package com.roli.apsimock.model.user;

import java.io.Serializable;
/**
 * @author xuxinyu
 * @date 2018/5/01 上午10:39
 */
public class UserFieldForget  implements Serializable {

    private static final long serialVersionUID = -4349818104276011568L;
    private String userAccount;
    private String userName;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
