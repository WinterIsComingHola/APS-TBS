package com.roli.aps.soa.client.service.impl;

import com.roli.aps.soa.client.service.ProjectInfoClientService;
import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.project.ProjectInfo;
import com.roli.apsimock.model.project.ProjectInfoOV;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/3/18 上午9:51
 */
@Service
public class ProjectInfoClientServiceImpl implements ProjectInfoClientService{

    @Value("${soa.path}")
    public String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @Override
    public ResultSoaRest addProject(ProjectInfoOV projectInfoOV){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ApsSoaParam apsSoaParam1 = new ApsSoaParam();
            apsSoaParam1.setBusinessParam(projectInfoOV.getUserAccount());
            //先获取userid
            ResultSoaRest resultSoaRestUserid =
                    soaRestScheduler.sendPost(SOAPATH+"user/getUseridByAccount.action",apsSoaParam1);
            Integer userid = (Integer)resultSoaRestUserid.getData().get("userid");
            if(userid==null){
                result.setSuccess(false);
                result.setMessage("用户账号数据异常");
            }else{
                ApsSoaParam apsSoaParam2 = new ApsSoaParam();
                apsSoaParam2.setBusinessParam(JacksonUtils.toJson(projectInfoOV));
                //再进行新增项目操作
                ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/addProjectInfo.action",apsSoaParam2);

                result.setState(resultSoaRest.getState());
                result.setSuccess(resultSoaRest.isSuccess());
                result.setMessage(resultSoaRest.getMessage());
            }
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultSoaRest queryProjectsByUserAccount(String account){

        ResultSoaRest result = new ResultSoaRest();
        try{

            ApsSoaParam apsSoaParam = new ApsSoaParam();
            apsSoaParam.setBusinessParam(account);

            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH+"project/getProjectInfosByUserAccount.action",apsSoaParam);

            List<ProjectInfo> projectInfos = (List<ProjectInfo>)resultSoaRest.getData().get("projects");
            if(projectInfos.size() == 0){
                result.setState(-1);
                result.setSuccess(false);
                result.setMessage("数据为空");
            }else{
                result.setState(resultSoaRest.getState());
                result.setSuccess(resultSoaRest.isSuccess());
                result.setMessage(resultSoaRest.getMessage());
                result.addAttribute("projects",projectInfos);
            }

        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultSoaRest makeProjectHavePerson(ProjectInfo projectInfo){
        ResultSoaRest result = new ResultSoaRest();
        try{

            ApsSoaParam apsSoaParam = new ApsSoaParam();
            apsSoaParam.setBusinessParam(JacksonUtils.toJson(projectInfo));
            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH+"project/makeProjectInfoHavePerson.action",apsSoaParam);

            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());

        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultSoaRest queryUserByProjectName(String projectName){
        ResultSoaRest result = new ResultSoaRest();
        try{

            ApsSoaParam apsSoaParam = new ApsSoaParam();
            apsSoaParam.setBusinessParam(projectName);
            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH+"project/getUserByProjectName.action",apsSoaParam);

            List<UserInfo> userInfoList = (List<UserInfo>)resultSoaRest.getData().get("users");

            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.addAttribute("users",userInfoList);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultSoaRest queryPublicProject(){
        ResultSoaRest result = new ResultSoaRest();
        try{

            ApsSoaParam apsSoaParam = new ApsSoaParam();
            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH+"project/queryPublicProject.action",apsSoaParam);

            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("projects",resultSoaRest.getAttribute("projects"));

        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
