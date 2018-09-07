package com.roli.apsimock.services.impl;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.NoticeForAjax;
import com.roli.apsimock.model.api.NoticeForAjaxDetail;
import com.roli.apsimock.model.project.*;
import com.roli.apsimock.model.user.NoticeRecord;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.services.ProjectInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.model.enums.ErrorsEnum;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
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

    private static final Logger logger = LoggerFactory.getLogger(ProjectInfoServiceImpl.class);

    @Value("${soa.path}")
    public String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @Resource(name = "jmsTemplateQueue")
    JmsTemplate jmsTemplate;

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


        //下面的代码开始执行MQ消息的构造和发送
        //获取当前公共projectid的所属的用户的Account信息
        ApsSoaParam soaParam2 = new ApsSoaParam();
        soaParam2.setBusinessParam(String.valueOf(projectid));
        ResultSoaRest resultSoaRest2 = soaRestScheduler.sendPost(SOAPATH+"project/queryuseraccountbyprojectid.action",soaParam2);
        String ownerUserAccount = (String)resultSoaRest2.getAttribute("userAccount");
        if(ownerUserAccount == null){
            BusinessException.throwMessage(ErrorsEnum.PARAM_NULL);
        }

        //构造发生到MQ的数据，主要是发送当前projectId所属的用户信息
        Map<String,Object> mapMessage = new HashMap<>();
        List<String> ownerUserList = new ArrayList<>();
        ownerUserList.add(ownerUserAccount);

        mapMessage.put("tag","projectMessage");
        mapMessage.put("ownUsers",ownerUserList);
        mapMessage.put("message","通知：有用户加入您创建的项目，项目id是"+projectid);
        //向MQ消息中间件进行推送
        logger.info("-------------------------开始进行MQ消息推送-------------------------");
        jmsTemplate.convertAndSend(mapMessage);
        logger.info("-------------------------MQ消息推送执行完毕-------------------------");



        //返回到正常的业务
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

    @Override
    public NoticeForAjax queryNoticeByUser(String userAccount,
                                           String page,
                                           String limit) {
        ApsSoaParam soaParam = new ApsSoaParam();
        Map<String ,String> paramMap = new HashMap<>();
        paramMap.put("userAccount",userAccount);
        paramMap.put("pageNum",page);
        paramMap.put("pageSize",limit);
        soaParam.setBusinessParam(JacksonUtils.toJson(paramMap));
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"project/querynoticebyuser.action",soaParam);
        NoticeForAjax table = new NoticeForAjax();
        List<NoticeForAjaxDetail> mapListTable = new ArrayList<>();

        if(resultSoaRest.getState()==601){
            table.setCode(-1);
            table.setCount(0);
            table.setMsg(resultSoaRest.getMessage());
            return table;
        }


        List<Map> noticeRecords = (List<Map>)
                resultSoaRest.getAttribute("noticeRecords");


        for(Map<String,Object> noticeRecordMap:noticeRecords){

            NoticeRecord noticeRecord = JacksonUtils.map2obj(noticeRecordMap,NoticeRecord.class);


            NoticeForAjaxDetail noticeForAjaxDetail = new NoticeForAjaxDetail();
            noticeForAjaxDetail.setZizeng(noticeRecord.getId());
            noticeForAjaxDetail.setInfo(noticeRecord.getNoticeContent());
            noticeForAjaxDetail.setTime(noticeRecord.getCreateTime());

            mapListTable.add(noticeForAjaxDetail);
        }

        table.setCode(0);
        table.setCount((Integer) resultSoaRest.getAttribute("total"));
        table.setMsg("");
        table.setData(mapListTable);

        return table;

    }
}
