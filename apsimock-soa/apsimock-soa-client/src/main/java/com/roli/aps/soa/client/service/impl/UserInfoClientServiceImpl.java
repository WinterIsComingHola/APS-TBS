package com.roli.aps.soa.client.service.impl;

import com.roli.aps.soa.client.service.UserInfoClientService;
import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.model.user.userov.UserInfoOV;
import com.roli.common.exception.BaseRunTimeException;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import com.sun.org.apache.regexp.internal.RE;
import jdk.nashorn.internal.runtime.ECMAException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/3/7 22:00
 */

@Service
public class UserInfoClientServiceImpl implements UserInfoClientService{

    @Value("${soa.path}")
    public String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @Override
    public ResultSoaRest addUserClient(UserInfoOV userInfoOV){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ApsSoaParam soaParam = new ApsSoaParam();
            soaParam.setBusinessParam(JacksonUtils.toJson(userInfoOV));
            //调用restful服务
            ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/addUserInfo.action",soaParam);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return  result;
    }

    @Override
    public ResultSoaRest checkUser(UserInfo userInfo){
        ResultSoaRest result = new ResultSoaRest();

        try{
            ApsSoaParam soaParam = new ApsSoaParam();
            soaParam.setBusinessParam(JacksonUtils.toJson(userInfo));
            //调用restful服务
            ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/checkUser.action",soaParam);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("userinfo",resultSoaRest.getData().get("userinfo"));
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return  result;
    }

    @Override
    public ResultSoaRest queryUseridByAccount(String account){
        ResultSoaRest result = new ResultSoaRest();
        try{
            ApsSoaParam soaParam = new ApsSoaParam();
            soaParam.setBusinessParam(account);
            ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/getUseridByAccount.action",soaParam);

            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("userid",resultSoaRest.getData().get("userid"));
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
