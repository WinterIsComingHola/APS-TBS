package com.roli.apsimock.controller.test;

import com.roli.aps.soa.client.service.ProjectInfoClientService;
import com.roli.apsimock.model.project.ProjectInfo;
import com.roli.apsimock.model.project.ProjectInfoOV;
import com.roli.apsimock.model.user.userov.UserInfoOV;
import com.ruoli.soa.model.ResultSoaRest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author xuxinyu
 * @date 2018/3/18 上午10:01
 */

@Controller
@RequestMapping("/")
public class ProjectInfoTestController {
    @Resource
    ProjectInfoClientService projectInfoClientService;

    /**
     * 新增项目接口
    * @author xuxinyu
    * @date 2018/3/19 下午8:40
    */
    @RequestMapping(value = "/test/aps/rest/project/addProjectInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest addProjectInfo(ProjectInfoOV projectInfoOv){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ResultSoaRest resultSoaRest = projectInfoClientService.addProject(projectInfoOv);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/test/aps/rest/project/getProjectInfosByUserAccount",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest getProjectByUserAccount(String account){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ResultSoaRest resultSoaRest = projectInfoClientService.queryProjectsByUserAccount(account);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("projects",resultSoaRest.getData().get("projects"));
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;

    }

    @RequestMapping(value = "/test/aps/rest/project/makeProjectInfoHavePerson",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest makeProjectInfoHavePerson(@RequestBody ProjectInfo projectInfo){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ResultSoaRest resultSoaRest = projectInfoClientService.makeProjectHavePerson(projectInfo);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;

    }

    @RequestMapping(value = "/test/aps/rest/project/queryUserByProjectName",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest queryUserByProjectName(String projectName){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ResultSoaRest resultSoaRest = projectInfoClientService.queryUserByProjectName(projectName);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.addAttribute("users",resultSoaRest.getData().get("users"));
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;

    }


    @RequestMapping(value = "/test/aps/rest/project/queryPublicProject",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest queryPublicProject(){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ResultSoaRest resultSoaRest = projectInfoClientService.queryPublicProject();
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.addAttribute("projects",resultSoaRest.getAttribute("projects"));
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;

    }

}
