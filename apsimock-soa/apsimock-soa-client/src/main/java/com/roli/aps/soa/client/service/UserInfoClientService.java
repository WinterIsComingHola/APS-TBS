package com.roli.aps.soa.client.service;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.model.user.userov.UserInfoOV;
import com.ruoli.soa.model.ResultSoaRest;

/**
 * 用于restful接口内部调试
 * 调用方controller统一放在server包的testsoa目录
 * @author xuxinyu
 * @date 2018/3/7 21:58
 */
public interface UserInfoClientService {
    public ResultSoaRest addUserClient(UserInfoOV userInfoOV);
    public ResultSoaRest checkUser(UserInfo userInfo);
    public ResultSoaRest queryUseridByAccount(String account);
}
