package com.roli.apsimock.services.impl;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.project.Table;
import com.roli.apsimock.model.user.UserFieldForget;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.model.user.UserInfoOV;
import com.roli.apsimock.services.UserInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.model.enums.ErrorsEnum;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService{

    @Value("${soa.path}")
    public String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @Override
    public ResultSoaRest addUserClient(UserInfoOV userInfoOV) throws BusinessException {

        ResultSoaRest result = new ResultSoaRest();

        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(JacksonUtils.toJson(userInfoOV));
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/addUserInfo.action",soaParam);

        if(resultSoaRest.getState()==109){
            BusinessException.throwMessage(ErrorsEnum.SECURITY_ERROR);
        }else if(resultSoaRest.getState()==201){
            BusinessException.throwMessage(ErrorsEnum.PASS_NOT_SAME);
        }else if(resultSoaRest.getState()==204){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_DUPLICATE);
        }

        result.setState(resultSoaRest.getState());
        result.setSuccess(resultSoaRest.isSuccess());
        result.setMessage(resultSoaRest.getMessage());

        return result;

    }

    @Override
    public ResultSoaRest checkUser(UserInfo userInfo) throws BusinessException {
        ResultSoaRest result = new ResultSoaRest();

        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(JacksonUtils.toJson(userInfo));
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/checkUser.action",soaParam);

        if(resultSoaRest.getState()==205){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_NONE);
        }else if(resultSoaRest.getState()==200){
            BusinessException.throwMessage(ErrorsEnum.PASS_CHECK_FAIL);
        }

        result.setState(resultSoaRest.getState());
        result.setSuccess(resultSoaRest.isSuccess());
        result.setMessage(resultSoaRest.getMessage());
        result.addAttribute("userinfo",resultSoaRest.getData().get("userinfo"));
        return  result;
    }

    @Override
    public ResultSoaRest queryUseridByAccount(String account){
        ResultSoaRest result = new ResultSoaRest();

        return result;
    }

    @Override
    public ResultSoaRest queryUserByAccountAndName(UserFieldForget userFieldForget) throws BusinessException {

        ResultSoaRest result = new ResultSoaRest();

        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(JacksonUtils.toJson(userFieldForget));
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/getUserInfoByAccountAndName.action",soaParam);

        if(resultSoaRest.getState()==207){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_DATA_NULL);
        }

        result.setState(resultSoaRest.getState());
        result.setSuccess(resultSoaRest.isSuccess());
        result.setMessage(resultSoaRest.getMessage());
        result.addAttribute("isHaveUser",resultSoaRest.getAttribute("isHaveUser"));
        return result;
    }

    @Override
    public ResultSoaRest resetUserPass(Map<String,String> userMap) throws BusinessException {
        ResultSoaRest result = new ResultSoaRest();

        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(JacksonUtils.toJson(userMap));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/resetpass.action",soaParam);

        if(resultSoaRest.getState()==207){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_DATA_NULL);
        }

        result.setState(resultSoaRest.getState());
        result.setSuccess(resultSoaRest.isSuccess());
        result.setMessage(resultSoaRest.getMessage());
        return result;
    }

    @Override
    public Table queryAllUser(Integer projectid,Integer isInv,
                              String userAccount,
                              String userName,
                              String page, String limit){

        ApsSoaParam soaParam = new ApsSoaParam();
        Map<String,Object> mapParam = new HashMap<>();

        Table table = new Table();
        List<Map<String,Object>> listMap = new ArrayList<>();
        String sprojectid  = String.valueOf(projectid);
        mapParam.put("projectid",sprojectid);
        mapParam.put("isInv",isInv);
        mapParam.put("userAccount",userAccount);
        mapParam.put("userName",userName);
        mapParam.put("pageNum", page);
        mapParam.put("pageSize", limit);

        soaParam.setBusinessParam(JacksonUtils.toJson(mapParam));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/getallusers.action",soaParam);
        List<Map<String,Object>> userInfoList = (List<Map<String,Object>>)resultSoaRest.getAttribute("users");

        for(Map<String,Object> usermap:userInfoList){

            UserInfo userInfo = JacksonUtils.map2obj(usermap,UserInfo.class);
            Map<String,Object> mapUser = new HashMap<>();

            mapUser.put("userid",userInfo.getId());
            mapUser.put("username",userInfo.getUserName());
            mapUser.put("userAccount",userInfo.getUserAccount());
            listMap.add(mapUser);

        }

        table.setCode(0);
        table.setMsg("");
        table.setCount(Integer.parseInt(resultSoaRest.getAttribute("total").toString()));
        table.setData(listMap);
        return table;
    }

    /**
     *
     * @param account
     * @return
     * @throws BusinessException
     */
    @Override
    public ResultSoaRest queryUserByAccount(String account) throws BusinessException {

        ResultSoaRest result = new ResultSoaRest();

        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(account);
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/getUserInfoByAccount.action",soaParam);

        if(resultSoaRest.getState()==207){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_DATA_NULL);
        }

        result.setState(resultSoaRest.getState());
        result.setSuccess(resultSoaRest.isSuccess());
        result.setMessage(resultSoaRest.getMessage());
        result.addAttribute("UserInfo",resultSoaRest.getAttribute("UserInfo"));
        return result;
    }


    @Override
    public ResultSoaRest resetAccount(String account,String userId) throws BusinessException {
        ResultSoaRest result = new ResultSoaRest();

        ApsSoaParam soaParam = new ApsSoaParam();
        Map<String,Object> userMap = new HashMap();
        userMap.put("newAccount",account);
        userMap.put("UserId",userId);
        soaParam.setBusinessParam(JacksonUtils.toJson(userMap));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/updateAccountById.action",soaParam);

        if(resultSoaRest.getState()==207){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_DATA_NULL);
        }

        result.setState(resultSoaRest.getState());
        result.setSuccess(resultSoaRest.isSuccess());
        result.setMessage(resultSoaRest.getMessage());
        return result;
    }

    public ResultSoaRest resetUserName(String userName,String userId) throws BusinessException{
        ApsSoaParam soaParam = new ApsSoaParam();
        Map<String,Object> userMap = new HashMap();
        userMap.put("newUserName",userName);
        userMap.put("UserId",userId);
        soaParam.setBusinessParam(JacksonUtils.toJson(userMap));
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/updateUserNameById.action",soaParam);
        return resultSoaRest;
    }

    public ResultSoaRest queryAccountCount(String newUserAccount) throws BusinessException{
        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(newUserAccount);
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"user/queryAccountCount.action",soaParam);
        return resultSoaRest;
    }

}
