package com.roli.apsimock.services.impl;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.project.*;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.services.ProjectInfoService;
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

/**
 * @author xuxinyu
 * @date 2018/5/4 下午3:50
 */
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService{

    @Value("${soa.path}")
    public String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @Override
    public ProjectForAjax queryPublicProjectInfos(String projectName,String createUser,String pageNum, String pageSize){
        ApsSoaParam soaParam = new ApsSoaParam();
        ProjectForAjax projectForAjax = new ProjectForAjax();
        List<ProjectForAjaxDetail> list = new ArrayList<>();

        Map<String,String> mapParam = new HashMap<>();
        mapParam.put("projectName",projectName);
        mapParam.put("createUser",createUser);
        mapParam.put("pageNum", pageNum);
        mapParam.put("pageSize", pageSize);
        soaParam.setBusinessParam(JacksonUtils.toJson(mapParam));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/queryPublicProject.action",soaParam);

        List<Map<String,Object>> projectInfos = (List<Map<String,Object>>)resultSoaRest.getAttribute("projects");

        for(Map<String,Object> projectInfoMap :projectInfos){

            ProjectInfoOOV projectInfo= JacksonUtils.map2obj(projectInfoMap,ProjectInfoOOV.class);

            ProjectForAjaxDetail projectForAjaxDetail = new ProjectForAjaxDetail();

            projectForAjaxDetail.setProjectid(projectInfo.getId());
            projectForAjaxDetail.setProjectname(projectInfo.getProjectName());
            projectForAjaxDetail.setCreateuser(projectInfo.getUserInfo().getUserName());
            projectForAjaxDetail.setTag(projectInfo.getTag());
            projectForAjaxDetail.setDesc(projectInfo.getDesc());
            list.add(projectForAjaxDetail);
        }

        projectForAjax.setCode(0);
        projectForAjax.setMsg("");
        projectForAjax.setCount(Integer.valueOf(resultSoaRest.getAttribute("total").toString()));
        projectForAjax.setData(list);

        return projectForAjax;

    }

    @Override
    public ResultSoaRest joinProject(String userAccount,Integer projectid,Integer isMaster) throws BusinessException {


        ApsSoaParam soaParam = new ApsSoaParam();
        Map<String,Object> mapParam = new HashMap<>();
        mapParam.put("projectid",projectid);
        mapParam.put("isMaster",isMaster);
        mapParam.put("userAccount",userAccount);

        soaParam.setBusinessParam(JacksonUtils.toJson(mapParam));
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/joinProject.action",soaParam);

        if(resultSoaRest.getState()==208){
            BusinessException.throwMessage(ErrorsEnum.RELATION_DUPLICATE);
        }

        return resultSoaRest;
    }

    @Override
    public List<ProjectInfo> getProjectInfosByUserAccount(String userAccount,String userRole,String page, String limit) throws BusinessException {
        ApsSoaParam soaParam = new ApsSoaParam();
        Map<String,String> mapParam = new HashMap<>();
        mapParam.put("userAccount",userAccount);
        mapParam.put("pageNum",page);
        mapParam.put("pageSize",limit);
        soaParam.setBusinessParam(JacksonUtils.toJson(mapParam));
        ResultSoaRest resultSoaRest = null;
        if(userRole.equals("1")){
            //为1时。调用的是getProjectInfosByUserAccount
            resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/getProjectInfosByUserAccount.action",soaParam);

        }else if(userRole.equals("0")){
            //为0时。调用的是getProjectInfosUserAdd
            resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/getProjectInfosUserAdd.action",soaParam);

        }

        if(resultSoaRest.getState()==202){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_NULL);
        }

        List<ProjectInfo> projectInfoList = (List<ProjectInfo>)resultSoaRest.getAttribute("projects");

        return projectInfoList;
    }

    @Override
    public ProjectForAjax getMyProjectForAjax(String userAccount,String userRole, String pageNum, String pageSize) throws BusinessException {
        ApsSoaParam soaParam = new ApsSoaParam();

        ProjectForAjax projectForAjax = new ProjectForAjax();
        List<ProjectForAjaxDetail> list = new ArrayList<>();
        ResultSoaRest resultSoaRest = null;
        Map<String,String> mapParam = new HashMap<>();
        mapParam.put("userAccount",userAccount);
        mapParam.put("pageNum", pageNum);
        mapParam.put("pageSize", pageSize);
        soaParam.setBusinessParam(JacksonUtils.toJson(mapParam));

        if(userRole .equals("1")){
            //为1时。调用的是getProjectInfosByUserAccount
             resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/getProjectInfosByUserAccount.action",soaParam);

        }else if(userRole .equals("0")){
            //为0时。调用的是getProjectInfosUserAdd
             resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/getProjectInfosUserAdd.action",soaParam);

        }

        if(resultSoaRest.getState()==202){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_NULL);
        }

        List<Map<String,Object>> projectInfos = (List<Map<String,Object>>)resultSoaRest.getAttribute("projects");

        for(Map<String,Object> projectInfoMap :projectInfos){

            ProjectInfo projectInfo= JacksonUtils.map2obj(projectInfoMap,ProjectInfo.class);

            ProjectForAjaxDetail projectForAjaxDetail = new ProjectForAjaxDetail();

            projectForAjaxDetail.setProjectid(projectInfo.getId());
            projectForAjaxDetail.setProjectname(projectInfo.getProjectName());
            projectForAjaxDetail.setTag(projectInfo.getTag());
            projectForAjaxDetail.setDesc(projectInfo.getDesc());
            list.add(projectForAjaxDetail);
        }

        projectForAjax.setCode(0);
        projectForAjax.setMsg("");
        projectForAjax.setCount(Integer.parseInt(resultSoaRest.getAttribute("total").toString()));
        projectForAjax.setData(list);

        return projectForAjax;

    }

    @Override
    public ResultSoaRest addProjectInfo(ProjectInfoOV projectInfoOV) throws BusinessException {
        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(JacksonUtils.toJson(projectInfoOV));
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/addProjectInfo.action",soaParam);

        if(resultSoaRest.getState()==303){
            BusinessException.throwMessage(ErrorsEnum.PROJECT_DUPLICATE);
        }

        return resultSoaRest;

    }

    @Override
    public ResultSoaRest makeProjectHavePerson(String projectName,List<Integer> userIdList){

        ApsSoaParam soaParam = new ApsSoaParam();
        ProjectInfo projectInfo = new ProjectInfo();

        List<UserInfo> userInfoList = new ArrayList<>();

        for(Integer userId:userIdList){
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userId);

            userInfoList.add(userInfo);
        }

        projectInfo.setProjectName(projectName);
        projectInfo.setUserInfoList(userInfoList);

        soaParam.setBusinessParam(JacksonUtils.toJson(projectInfo));
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/makeProjectInfoHavePerson.action",soaParam);
        return resultSoaRest;
    }

    @Override
    public ResultSoaRest deleteProject(Integer projectid) throws BusinessException {
        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(String.valueOf(projectid));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/deleteProject.action",soaParam);

        if(resultSoaRest.getState()==103){
            BusinessException.throwMessage(ErrorsEnum.PARAM_NULL);
        }
        return resultSoaRest;
    }

}
