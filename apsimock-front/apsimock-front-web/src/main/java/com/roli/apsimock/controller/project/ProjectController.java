package com.roli.apsimock.controller.project;

import com.roli.apsimock.model.project.*;
import com.roli.apsimock.services.ProjectInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.model.enums.ErrorsEnum;
import com.ruoli.soa.model.ResultSoaRest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/5/4 上午11:00
 */
@Controller
@RequestMapping("/")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Resource
    ProjectInfoService projectInfoService;

    @RequestMapping(value = "/aps/project/public",method = RequestMethod.GET)
    public String getPublicProject(String userAccount,Model model){
        model.addAttribute("userAccount",userAccount);
        return "project/publicproject";
    }

    @RequestMapping(value = "/aps/project/getpub",method = RequestMethod.POST)
    @ResponseBody
    public ProjectForAjax getPublicProject(String projectName,String createUser){

        if(StringUtils.isEmpty(projectName)&&StringUtils.isEmpty(createUser)){
            projectName = null;
            createUser = null;
        }else if(StringUtils.isEmpty(projectName)){
            projectName = null;
        }else if(StringUtils.isEmpty(createUser)){
            createUser = null;
        }
        ProjectForAjax projectForAjax = projectInfoService.queryPublicProjectInfos(projectName,createUser);
        return projectForAjax;
    }

    @RequestMapping(value = "/aps/project/joinProject",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest joinProject(String userAccount,Integer projectid,Integer isMaster){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ResultSoaRest resultSoaRest = projectInfoService.joinProject(userAccount,projectid,isMaster);
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

    @RequestMapping(value = "/aps/project/mypublic",method = RequestMethod.GET)
    public String getMyPublicProject(String userAccount,String userRole,Model model){

        try{
            List<ProjectInfo> projectInfoList = projectInfoService.getProjectInfosByUserAccount(userAccount,userRole);
            if(projectInfoList.size()==0){
                model.addAttribute("projects",0);
            }else{
                model.addAttribute("projects",1);
            }

        }catch (BusinessException e){
            model.addAttribute("projects",null);
        }
        model.addAttribute("userAccount",userAccount);
        if(userRole.equals("1")){
            return "project/myproject";
        }else{
            return "project/addproject";
        }
    }

    @RequestMapping(value = "/aps/project/myprojects",method = RequestMethod.POST)
    @ResponseBody
    public ProjectForAjax getMyPublicProjectForAjax(String userAccount,Integer userRole){

        ProjectForAjax projectForAjax = null;
        try {
            projectForAjax = projectInfoService.getMyProjectForAjax(userAccount,userRole);
        } catch (BusinessException e) {
            projectForAjax.setCode(-1);
            projectForAjax.setMsg("failed");
        }
        return projectForAjax;

    }

    @RequestMapping(value = "/aps/project/addproject",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest addProjectInfo(ProjectInfoOV projectInfoOV){

        ResultSoaRest result = new ResultSoaRest();
        projectInfoOV.setIsMaster(1);
        try {

            ResultSoaRest resultSoaRest = projectInfoService.addProjectInfo(projectInfoOV);
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

    @RequestMapping(value = "/aps/project/makeprohaveper",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest makeProjectHavePerson(@RequestBody RequestProject requestProject){

        String projectName = requestProject.getProjectName();
        List<Integer> userIdList = requestProject.getUserIdList();

        ResultSoaRest resultSoaRest = projectInfoService.makeProjectHavePerson(projectName,userIdList);
        return resultSoaRest;
    }

    @RequestMapping(value = "/aps/project/deleteproject",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest deleteProject(Integer projectid){

        ResultSoaRest result = new ResultSoaRest();
        if(projectid ==null){

            result.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            result.setSuccess(false);
            result.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }else{
            try{
                ResultSoaRest resultSoaRest = projectInfoService.deleteProject(projectid);
                result.setState(resultSoaRest.getState());
                result.setSuccess(resultSoaRest.isSuccess());
                result.setMessage(resultSoaRest.getMessage());
            }catch (BusinessException e){
                result.setState(Integer.parseInt(e.getErrCode()));
                result.setSuccess(false);
                result.setMessage(e.getMessage());
            }
        }

        return result;

    }

}
