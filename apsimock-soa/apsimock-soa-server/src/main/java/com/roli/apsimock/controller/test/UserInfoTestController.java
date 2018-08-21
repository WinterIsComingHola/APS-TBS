package com.roli.apsimock.controller.test;

import com.roli.aps.soa.client.service.UserInfoClientService;
import com.roli.apsimock.controller.user.UserInfoController;
import com.roli.apsimock.model.user.userov.UserInfoOV;
import com.ruoli.soa.model.ResultSoaRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xuxinyu
 * @date 2018/3/7 23:07
 */
@Controller
@RequestMapping("/")
public class UserInfoTestController {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Resource
    UserInfoClientService userInfoClientService;

    @RequestMapping(value = "/test/aps/rest/user/addUserInfo")
    @ResponseBody
    public ResultSoaRest addUserInfo(UserInfoOV userInfoOV, HttpServletResponse response){

        //设置消息头，支持js跨域访问
        response.setHeader("Access-Control-Allow-Origin","*");
        ResultSoaRest result = new ResultSoaRest();

        try{
            ResultSoaRest resultSoaRest = userInfoClientService.addUserClient(userInfoOV);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/test/aps/rest/user/checkUser",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest checkUser(UserInfoOV userInfoOV){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ResultSoaRest resultSoaRest = userInfoClientService.checkUser(userInfoOV);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("userinfo",resultSoaRest.getData().get("userinfo"));
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/test/aps/rest/user/queryUserByAccount",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest queryUseridByAccount(String account){
        ResultSoaRest result = new ResultSoaRest();

        try{
            ResultSoaRest resultSoaRest = userInfoClientService.queryUseridByAccount(account);
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
