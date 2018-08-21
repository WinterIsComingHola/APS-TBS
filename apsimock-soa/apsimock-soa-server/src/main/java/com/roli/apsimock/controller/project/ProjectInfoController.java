package com.roli.apsimock.controller.project;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.project.ProjectInfo;
import com.roli.apsimock.model.project.ProjectInfoOOV;
import com.roli.apsimock.model.project.ProjectInfoOV;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.services.user.ProjectInfoService;
import com.roli.common.exception.BaseRunTimeException;
import com.roli.common.exception.BusinessException;
import com.roli.common.model.enums.ErrorsEnum;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.annotation.SoaRestAuth;
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
 * @author xuxinyu
 * @date 2018/3/16 下午5:59
 */
@Controller
@RequestMapping("/")
public class ProjectInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectInfoController.class);

    @Resource
    ProjectInfoService projectInfoService;


    /**
    * 新增一个项目
    * @author xuxinyu
    * @date 2018/3/19 下午7:54
    */
    @RequestMapping(value = "/aps/rest/project/addProjectInfo",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest addProjectInfo(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            try{
               ProjectInfoOV projectInfoOV = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),ProjectInfoOV.class);

                projectInfoService.addProject(projectInfoOV);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
            }catch (BusinessException e){
                logger.error("/aps/rest/project/addProjectInfo 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (Exception e){
                logger.error("/aps/rest/project/addProjectInfo 内部数据库处理异常",e);
                resultSoaRest.setState(501);
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
    * join the project
    * @throws
    * @author xuxinyu
    * @date 2018/5/6 下午2:32
    */
    @RequestMapping(value = "/aps/rest/project/joinProject",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest joinProject(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            try{
                Map<String,Object> mapParam = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),Map.class);
                ProjectInfoOV projectInfoOV = new ProjectInfoOV();
                projectInfoOV.setId((Integer) mapParam.get("projectid"));
                projectInfoOV.setIsMaster((Integer) mapParam.get("isMaster"));
                projectInfoOV.setUserAccount((String) mapParam.get("userAccount"));

                projectInfoService.joinProject(projectInfoOV);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
            }catch (BusinessException e){
                logger.error("/aps/rest/project/joinProject 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (Exception e){
                logger.error("/aps/rest/project/joinProject 内部数据库处理异常",e);
                resultSoaRest.setState(501);
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

    @RequestMapping(value = "/aps/rest/project/getProjectInfosByUserAccount",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest getProjectInfos(){

        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);

        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            try{

                String userAccount = apsSoaParam.getBusinessParam();
                List<ProjectInfo> projectInfos = projectInfoService.queryProjectsByUserAccount(userAccount);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
                resultSoaRest.addAttribute("projects",projectInfos);
            }catch (BusinessException e){
                logger.error("/aps/rest/project/getProjectInfosByUserAccount 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (Exception e){
                logger.error("/aps/rest/project/getProjectInfosByUserAccount 内部数据库处理异常",e);
                resultSoaRest.setState(501);
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


    @RequestMapping(value = "/aps/rest/project/getProjectInfosUserAdd",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest getProjectInfosUserAdd(){

        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);

        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            try{

                String userAccount = apsSoaParam.getBusinessParam();
                List<ProjectInfo> projectInfos = projectInfoService.queryProjectsUserAdd(userAccount);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
                resultSoaRest.addAttribute("projects",projectInfos);
            }catch (BusinessException e){
                logger.error("/aps/rest/project/getProjectInfosUserAdd 业务处理异常",e);
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

    @RequestMapping(value = "/aps/rest/project/makeProjectInfoHavePerson",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest makeProjectInfoHavePerson(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            try{
                ProjectInfo projectInfo = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),ProjectInfo.class);

                projectInfoService.makeProjectHavePerson(projectInfo);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
            }catch (BusinessException e){
                logger.error("/aps/rest/project/makeProjectInfoHavePerson 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (Exception e){
                logger.error("/aps/rest/project/makeProjectInfoHavePerson 内部数据库处理异常",e);
                resultSoaRest.setState(501);
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

    @RequestMapping(value = "/aps/rest/project/getUserByProjectName",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest getUserByProjectName(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            try{
                String projectName = apsSoaParam.getBusinessParam();

                List<String> userInfoList = projectInfoService.queryUserByProjectName(projectName);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
                resultSoaRest.addAttribute("users",userInfoList);
            }catch (BusinessException e){
                logger.error("/aps/rest/project/makeProjectInfoHavePerson 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (Exception e){
                logger.error("/aps/rest/project/makeProjectInfoHavePerson 内部数据库处理异常",e);
                resultSoaRest.setState(501);
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

    @RequestMapping(value = "/aps/rest/project/queryPublicProject",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryPublicProject(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);

        Map<String,String> mapParam = JacksonUtils.str2map(apsSoaParam.getBusinessParam());
        String projectName = mapParam.get("projectName");
        String createUser = mapParam.get("createUser");

        if(apsSoaParam != null){
            List<ProjectInfoOOV> projectInfos = projectInfoService.queryPublicProject(projectName,createUser);
            resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
            resultSoaRest.setSuccess(true);
            resultSoaRest.setMessage("success");
            resultSoaRest.addAttribute("projects",projectInfos);

        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }

    @RequestMapping(value = "/aps/rest/project/deleteProject",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest deleteProject(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);

        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            try{
                Integer projectId = Integer.parseInt(apsSoaParam.getBusinessParam());

                projectInfoService.deleteProject(projectId);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("success");
            }catch (BusinessException e){
                logger.error("/aps/rest/project/deleteProject 业务处理异常",e);
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
