package com.roli.apsimock.controller.user;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.user.UserFieldForget;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.model.user.UserInfoReturn;
import com.roli.apsimock.model.user.userov.UserInfoOV;
import com.roli.apsimock.services.user.UserInfoService;
import com.roli.common.exception.BaseRunTimeException;
import com.roli.common.exception.BusinessException;
import com.roli.common.exception.SecurityException;
import com.roli.common.model.enums.ErrorsEnum;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.annotation.SoaRestAuth;
import com.ruoli.soa.model.Result;
import com.ruoli.soa.model.ResultSoaRest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuxinyu
 * @date 2018/3/6 下午7:29
 */
@Controller
@RequestMapping("/")
public class UserInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Resource
    UserInfoService userInfoService;

    /**
    * 新增一个用户接口
    * @author xuxinyu
    * @date 2018/3/19 下午7:53
    */
    @RequestMapping(value = "/aps/rest/user/addUserInfo",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest addUserInfo(){
        //返回值
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            //取出业务参数并转换为UserInfoOV
            UserInfoOV userInfoOV = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),UserInfoOV.class);

            try{
                userInfoService.addUser(userInfoOV);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
            } catch (SecurityException e) {
                logger.error("/aps/rest/addUserInfo 安全性处理异常",e);
                resultSoaRest.setState(ErrorsEnum.SECURITY_ERROR.getErrorCode());
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (BusinessException e){
                logger.error("/aps/rest/addUserInfo 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }

        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }

    /**
    * 根据account和pass对账户进行登录，用于登陆接口
    * @author xuxinyu
    * @date 2018/3/18 下午4:09
    */
    @RequestMapping(value = "/aps/rest/user/checkUser",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest checkUser(){
        //返回值
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            //从ApsSoaParam中取出外部的入参
            UserInfo userInfo = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),UserInfo.class);

            try{
                UserInfo returnUser = userInfoService.queryUser(userInfo);
                UserInfoReturn returnUserPass = new UserInfoReturn();
                returnUserPass.setAccount(returnUser.getUserAccount());
                returnUserPass.setName(returnUser.getUserName());
                returnUserPass.setIsActive(returnUser.getIsActive());
                returnUserPass.setCreateTime(returnUser.getCreateTime());
                returnUserPass.setPassWordSer(returnUser.getPassWord());

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
                resultSoaRest.addAttribute("userinfo",returnUserPass);
            } catch (SecurityException e) {
                logger.error("/aps/rest/addUserInfo 安全性处理异常",e);
                resultSoaRest.setState(301);
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (BusinessException e){
                logger.error("/aps/rest/addUserInfo 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }

        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }

    /**
    * 根据用户账户返回id，根据用户账户名，查询用户的id
    * @author xuxinyu
    * @date 2018/3/18 下午4:10
    */
    @RequestMapping(value = "/aps/rest/user/getUseridByAccount",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest getUserId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);

        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            String account = apsSoaParam.getBusinessParam();

            try{
                Integer userid = userInfoService.queryUserIdByAccount(account);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
                resultSoaRest.addAttribute("userid",userid);
            } catch (BusinessException e) {
                logger.error("/aps/rest/getUseridByAccount 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }

        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * 根据用户账户和用户名称，查询数据库内容，若有数据则返回true，否则抛出异常
     * @author xuxinyu
     * @date 2018/3/18 下午4:10
     */
    @RequestMapping(value = "/aps/rest/user/getUserInfoByAccountAndName",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest getUserInfoByAccountAndName(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);

        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            UserFieldForget userFieldForget = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),UserFieldForget.class);

            try{
                Boolean isHaveUser = userInfoService.queryUserInfoByAccountAndName(userFieldForget);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
                resultSoaRest.addAttribute("isHaveUser",isHaveUser);
            } catch (BusinessException e) {
                logger.error("/aps/rest/getUserInfoByAccountAndName 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }

        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * 根据用户账号和密码，进行密码更新服务
     * @author xuxinyu
     * @date 2018/3/18 下午4:10
     */
    @RequestMapping(value = "/aps/rest/user/resetpass",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest resetpass(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);

        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            Map<String,String> userMap = JacksonUtils.str2map(apsSoaParam.getBusinessParam());

            try{
                userInfoService.updateUserPass(userMap);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
            } catch (BusinessException e) {
                logger.error("/aps/rest/resetpass 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            } catch (SecurityException e) {
                logger.error("/aps/rest/addUserInfo 安全性处理异常",e);
                resultSoaRest.setState(ErrorsEnum.SECURITY_ERROR.getErrorCode());
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }

        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * 查询全量用户数据
     * @author xuxinyu
     * @date 2018/3/18 下午4:10
     */
    @RequestMapping(value = "/aps/rest/user/getallusers",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryAllUser(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);

        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            Map<String,Object> mapParam =  JacksonUtils.str2map(apsSoaParam.getBusinessParam());
            String projectid = (String)mapParam.get("projectid");
            Integer isInv = (Integer) mapParam.get("isInv");
            String userAccount = (String)mapParam.get("userAccount");
            String userName = (String)mapParam.get("userName");

            try{
                List<UserInfo> userInfoList = userInfoService.queryAllUser(projectid,isInv,userAccount,userName);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
                resultSoaRest.addAttribute("users",userInfoList);
            } catch (BusinessException e) {
                logger.error("/aps/rest/getallusers 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }

        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }

        return resultSoaRest;
    }


}
