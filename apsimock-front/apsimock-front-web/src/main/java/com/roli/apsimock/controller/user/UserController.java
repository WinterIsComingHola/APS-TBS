package com.roli.apsimock.controller.user;

import com.roli.apsimock.model.project.Table;
import com.roli.apsimock.model.user.UserFieldForget;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.model.user.UserInfoOV;
import com.roli.apsimock.model.user.UserInfoReturn;
import com.roli.apsimock.services.UserInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.model.enums.ErrorsEnum;
import com.roli.common.utils.json.JacksonUtils;
import com.roli.common.utils.servlet.CookieUtils;
import com.ruoli.soa.model.ResultSoaRest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${soa.path}")
    private String soaPath;
    @Resource
    UserInfoService userInfoService;

    @RequestMapping(value = "/aps/user/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "login/register";
    }

    @RequestMapping(value = "/aps/front/user/addUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest addUser(UserInfoOV userInfoOV) {
        ResultSoaRest result = new ResultSoaRest();

        try{

            ResultSoaRest resultSoaRest = userInfoService.addUserClient(userInfoOV);

            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());

        }catch (BusinessException e){
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 登陆页入口
     * **/
    @RequestMapping(value = "/aps/user/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "login/login";
    }

    /**
     * 校验用户信息接口
     * **/
    @RequestMapping(value = "/aps/user/checkUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest checkUser(UserInfo userInfo
            ,HttpSession session
            ,HttpServletRequest request) {
        ResultSoaRest result = new ResultSoaRest();

        try{

            ResultSoaRest resultSoaRest = userInfoService.checkUser(userInfo);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("userinfo",resultSoaRest.getData().get("userinfo"));

            //取的用户账户名数据，作为session中的key
            UserInfoReturn userInfoReturn = JacksonUtils.map2obj((Map)resultSoaRest.getAttribute("userinfo"),UserInfoReturn.class);
            String userAccountSession = userInfoReturn.getAccount();

            if(request.getSession().getAttribute(userAccountSession)==null){
                session.setMaxInactiveInterval(691200);//设置session最大存活时间8天，要求必须比cookie要久(cookie设置7天)
                session.setAttribute(userAccountSession,userInfoReturn);
            }

        }catch (BusinessException e){
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }


    //home页处理，用于从login页提交的已经经过校验的get访问请求
    @RequestMapping(value = "/aps/user/homehandle",method = RequestMethod.GET)
    public String homePageHandle(HttpServletRequest request,
            HttpServletResponse response
            ,HttpSession session
            ,RedirectAttributes model
            ,String remeberMe
            ,String userAccount) throws UnsupportedEncodingException, BusinessException {

        //从/aps/front/user/checkUserInfo接口的session中获取数据
        UserInfoReturn userInfoInSession = (UserInfoReturn)request.getSession().getAttribute(userAccount);
        if(userInfoInSession==null){
            BusinessException.throwMessage(ErrorsEnum.CACHE_ERROR);
        }

        //设置cookie
        addCookie(response,"_useraccount",userInfoInSession.getAccount(),remeberMe,true);
        addCookie(response,"_usersecret",userInfoInSession.getPassWordSer(),remeberMe,true);
        addCookie(response,"_username",userInfoInSession.getName(),remeberMe,true);

        //注意：需要对原JSESSIONID进行覆盖，因为原JSESSIONID的失效期是临时的
        //当用户登陆后cookie形成，一旦关闭浏览器，JSESSIONID便失效导致浏览器无法找到当前session，而是新建了sessionid
        //最终使页面展示/auth/advtestman的路径异常
        //这里人为重新设置失效期
        addCookie(response,"JSESSIONID",session.getId(),remeberMe,true);

        //做一步跳转，用于保护被登录的主页页面直接被访问
        model.addFlashAttribute("userAccount", userAccount);
        return "redirect:/aps/user/home";

    }

    @RequestMapping(value = "/aps/user/home",method = RequestMethod.GET)
    public String getHomePage(HttpServletRequest request
    , @ModelAttribute("userAccount")String userAccount){

        //Cookie[] cookies = CookieUtils.getCookies(request);

        //如果是直接访问本页面，则session中没有数据
        if(request.getSession().getAttribute(userAccount)==null){
            return "login/login";
        }/*else if(cookies.length>1){

            for (Cookie cookie : cookies) {
					*//*
					 * 遍历cookie数组，如果有用户名和session中的user对象的用户名一致的，
					 * 如果密码和session中user对象的密码一致的，认为是已登陆用户 直接转入index页面
					 *//*
                if (cookie.getValue()
                        .equals(((UserInfoReturn) request.getSession().getAttribute(userAccount)).getPassWordSer())) {
                    return "index";
                }
            }

        }else{
            return "login/login";
        }*/
        return "redirect:/index";
    }

    /**
     * 默认页，如果有cookie，则校验，校验成功返回到主页，校验失败则返回到登陆页
     * **/
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String getIndexPage(HttpServletRequest request,Model model) throws UnsupportedEncodingException {

        Cookie[] cookies = CookieUtils.getCookies(request);
        String userAccount = URLDecoder.decode(CookieUtils.getCookieValue("_useraccount",request),"utf-8");
        String userName = CookieUtils.getCookieValue("_username",request);
        if(userAccount==null||userName==null){
            return "login/login";
        }else if(request.getSession().getAttribute(userAccount)==null){
            return "login/login";
        }

        //校验cookie
        if(cookies.length>1){

            for (Cookie cookie : cookies) {
					/*
					 * 遍历cookie数组，如果有用户名和session中的user对象的用户名一致的，
					 * 如果密码和session中user对象的密码一致的，认为是已登陆用户 直接转入index页面
					 */
                if (URLDecoder.decode(cookie.getValue(),"utf-8")
                        .equals(((UserInfoReturn) request.getSession().getAttribute(userAccount)).getPassWordSer())) {

                    model.addAttribute("userName",URLDecoder.decode(userName,"utf-8"));
                    model.addAttribute("userAccount",userAccount);

                    return "index";
                }
            }

        }else{
            return "login/login";
        }
        return "login/login";
    }

    /**
     * 点击退出后进来登录页面
     * **/
    @RequestMapping(value = "/aps/user/logout",method = RequestMethod.GET)
    public String logOut(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = CookieUtils.getCookies(request);
        String domain=soaPath.replace("http://","").split(":")[0];
        if(cookies!=null){
            for(Cookie cookie:cookies){
                CookieUtils.deleteCookie(cookie,response,domain,"/");
            }
        }
        return "login/login";
    }

    /**
     * 进入忘记密码页面
     * **/
    @RequestMapping(value = "/aps/user/forget",method = RequestMethod.GET)
    public String forgetPage(){
        return "login/forget";
    }

    /**
     * 校验忘记密码页面接口
     * **/
    @RequestMapping(value = "/aps/user/checkforget",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest checkForget(UserFieldForget userFieldForget){
        ResultSoaRest result = new ResultSoaRest();
        try {
            ResultSoaRest resultSoaRest = userInfoService.queryUserByAccountAndName(userFieldForget);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("isHaveUser",resultSoaRest.getAttribute("isHaveUser"));

        } catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 进入重置密码页面
     * **/
    @RequestMapping(value = "/aps/user/reset",method = RequestMethod.GET)
    public String resetPage(String userAccount,Model model){
        model.addAttribute("userAccount",userAccount);
        return "login/resetpass";
    }

    /**
     * 修改密码页面
     * **/
    @RequestMapping(value = "/aps/user/pagereset",method = RequestMethod.GET)
    public String resetPass(String userAccount,Model model){
        model.addAttribute("userAccount",userAccount);
        return "login/pageresetpass";
    }
    /**
     * 修改密码成功返回页面
     * **/
    @RequestMapping(value = "/aps/user/pageresetsucess",method = RequestMethod.GET)
    public String resetPassSucess(){
      return "login/pageresetpasssucess";
    }

    /**
     * 重置密码接口
     * **/
    @RequestMapping(value = "/aps/user/resetpass",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest checkForget(String userAccount,String password){

        Map<String,String> userMap = new HashMap<>();
        userMap.put("userAccount",userAccount);
        userMap.put("newPass",password);

        ResultSoaRest result = new ResultSoaRest();
        try {
            ResultSoaRest resultSoaRest = userInfoService.resetUserPass(userMap);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());

        } catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/aps/user/getallusers",method = RequestMethod.POST)
    @ResponseBody
    public Table queryAllUser(Integer projectid,Integer isInv,
                              String page,
                              String limit,
                              String userAccount,
                              String userName){

        if(StringUtils.isEmpty(userAccount)){
            userAccount = null;
        }
        if(StringUtils.isEmpty(userName)){
            userName = null;
        }

        Table table = userInfoService.queryAllUser(projectid,isInv,userAccount,userName, page, limit);
        return table;
    }


    /**
     * addcookie方法
     * **/
    private void addCookie(HttpServletResponse response
            ,String name
            ,String value
            ,String remember
            ,boolean login) throws UnsupportedEncodingException {

        if(login){
            if(remember.equals("on")){
                //如果是记住我登陆，则设置cookie最大存活时间7天
                CookieUtils.addCookie(response,URLEncoder.encode(name, "UTF-8")
                        ,URLEncoder.encode(value, "UTF-8"),604800,"/");
            }else{
                //如果不是记住我登陆，则设置cookie最大存活时间未临时
                CookieUtils.addCookie(response,URLEncoder.encode(name, "UTF-8")
                        ,URLEncoder.encode(value, "UTF-8"),-1,"/");
            }
        }else{
            //如果不是登录，则是退出，删除cookie
            CookieUtils.addCookie(response,URLEncoder.encode(name, "UTF-8")
                    ,URLEncoder.encode(value, "UTF-8"),0,"/");

        }
    }

    /**
     * 获得用户信息
     * **/
    @RequestMapping(value = "/aps/user/userinfo",method = RequestMethod.GET)
    public String getUserInfo(String userAccount,Model model){
        ResultSoaRest resultSoaRest = queryUserInfoByAccount(userAccount);
        model.addAttribute("userId",((LinkedHashMap)resultSoaRest.getAttribute("UserInfo")).get("id"));
        model.addAttribute("userAccount",((LinkedHashMap)resultSoaRest.getAttribute("UserInfo")).get("userAccount"));
        model.addAttribute("userName",((LinkedHashMap)resultSoaRest.getAttribute("UserInfo")).get("userName"));
        int isActive =(int)((LinkedHashMap)resultSoaRest.getAttribute("UserInfo")).get("isActive");
        String isActiveAttr = "";
        if(isActive==1){
            isActiveAttr="激活状态";
        }
        if(isActive==0){
            isActiveAttr="失效状态";
        }
        model.addAttribute("isActive",isActiveAttr);
        return "login/userinfo";
    }


    /**
     * 查询用户信息接口
     * **/
    @ResponseBody
    public ResultSoaRest queryUserInfoByAccount(String account){
        ResultSoaRest result = new ResultSoaRest();
        try {
            ResultSoaRest resultSoaRest = userInfoService.queryUserByAccount(account);

            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("UserInfo",resultSoaRest.getAttribute("UserInfo"));

        } catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    /**
     * 修改用户账号
     * **/
    @RequestMapping(value = "/aps/user/updateAccount",method = RequestMethod.GET)
    public String updateAccount(String userAccount,String userId,Model model){
        ResultSoaRest result = new ResultSoaRest();
        try {
            ResultSoaRest resultSoaRest0 = userInfoService.resetAccount(userAccount, userId);
        }catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return "login/userinfo";
    }

    /**
     * 根据用户id修改用户名
     * @param request
     * @return
     */
    @RequestMapping(value = "/aps/user/updateUserNameById",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest updateUserNameById(HttpServletRequest request){
        ResultSoaRest result = new ResultSoaRest();
        String userName=request.getParameter("userName");
        String userId=request.getParameter("userId");;
        try{
            result= userInfoService.resetUserName(userName ,userId);
        }catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 查询是否存在相同用户名的用户
     * @param request
     * @return
     */
    @RequestMapping(value = "/aps/user/queryAccountCount",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest queryAccountCount(HttpServletRequest request){
        ResultSoaRest result = new ResultSoaRest();
        String newUserAccount=request.getParameter("newUserAccount");
        String userId=request.getParameter("userId");;
        try{
            result= userInfoService.queryAccountCount(newUserAccount);
        }catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

}
