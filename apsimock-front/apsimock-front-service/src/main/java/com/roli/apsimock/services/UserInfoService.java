package com.roli.apsimock.services;

import com.roli.apsimock.model.project.Table;
import com.roli.apsimock.model.user.UserFieldForget;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.model.user.UserInfoOV;
import com.roli.common.exception.BusinessException;
import com.ruoli.soa.model.ResultSoaRest;

import java.util.Map;

public interface UserInfoService {

    public ResultSoaRest addUserClient(UserInfoOV userInfoOV) throws BusinessException;
    public ResultSoaRest checkUser(UserInfo userInfo) throws BusinessException;
    public ResultSoaRest queryUseridByAccount(String account);

    public ResultSoaRest queryUserByAccountAndName(UserFieldForget userFieldForget) throws BusinessException;

    public ResultSoaRest resetUserPass(Map<String,String> userMap) throws BusinessException;

    public Table queryAllUser(Integer projectid,
                              Integer isInv,
                              String userAccount,
                              String userName,
                              String page,
                              String limit);

    public ResultSoaRest queryUserByAccount(String account) throws BusinessException;

    public ResultSoaRest resetAccount(String account,String userId) throws BusinessException;

    public ResultSoaRest resetUserName(String userName,String userId) throws BusinessException;

    public ResultSoaRest queryAccountCount(String newUserAccount) throws BusinessException;
}
