package com.roli.apsimock.services.user;

import com.roli.apsimock.model.user.UserFieldForget;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.model.user.userov.UserInfoOV;
import com.roli.common.exception.BusinessException;
import com.roli.common.exception.SecurityException;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/3/5 下午4:56
 */
public interface UserInfoService {

    public void addUser(UserInfoOV userInfoOV) throws SecurityException, BusinessException;

    public UserInfo queryUser(UserInfo userInfo) throws SecurityException, BusinessException;

    public Integer queryUserIdByAccount(String account) throws BusinessException;

    public boolean queryUserInfoByAccountAndName(UserFieldForget userFieldForget) throws BusinessException;

    public void updateUserPass(Map<String,String> userAccountAndNewpass) throws BusinessException, SecurityException;

    public List<UserInfo> queryAllUser(String projectid,Integer isInv,
                                       String userAccount,
                                       String userName) throws BusinessException;

    public UserInfo queryUserInfoByAccount(String account) throws BusinessException;

    public void updateUserAccount(String UserAccount,String UserId) throws BusinessException, SecurityException;

    public void updateUserName(String UserName,String UserId) throws BusinessException, SecurityException;

    public int queryAccountCount(String newUserAccount) throws BusinessException;
}
