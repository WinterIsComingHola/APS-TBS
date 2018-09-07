package com.roli.apsimock.services.user.impl;

import com.github.pagehelper.PageHelper;
import com.roli.apsimock.dao.project.ProjectInfoMapper;
import com.roli.apsimock.model.project.ProUserRel;
import com.roli.apsimock.model.project.ProjectInfo;
import com.roli.apsimock.model.project.ProjectInfoOOV;
import com.roli.apsimock.model.project.ProjectInfoOV;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.services.user.ProjectInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.exception.CustomAssert;
import com.roli.common.model.enums.ErrorsEnum;
import com.ruoli.soa.model.Datagrid;
import com.ruoli.soa.utils.PageFenYeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/3/16 下午4:54
 */
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService
{

    @Resource
    ProjectInfoMapper projectInfoMapper;

    @Override
    @Transactional
    public void addProject(ProjectInfoOV projectInfoov) throws BusinessException
    {
        CustomAssert.isNotNull(projectInfoov, ErrorsEnum.OBJECT_NULL);
        CustomAssert.isNotEmpty(projectInfoov.getProjectName(), ErrorsEnum.PROJECTNAME_NULL);
        CustomAssert.isNotEmpty(projectInfoov.getUserAccount(), ErrorsEnum.ACCOUNT_NULL);
        CustomAssert.isNotNull(projectInfoov.getPrivacy(), ErrorsEnum.PROJECTPRIVACY_NULL);
        CustomAssert.isNotNull(projectInfoov.getIsMaster(), ErrorsEnum.ISCREATE_ERROR);


        ProjectInfo projectFromDB = projectInfoMapper.queryProjectByName(projectInfoov.getProjectName());
        if (projectFromDB != null)
        {
            BusinessException.throwMessage(ErrorsEnum.PROJECT_DUPLICATE);
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        projectInfoov.setCreateTime(localDateTime);
        //新增项目
        projectInfoMapper.addProject(projectInfoov);
        //新增关系
        if (projectInfoov.getIsMaster() != 1)
        {
            BusinessException.throwMessage(ErrorsEnum.ISCREATE_ERROR);
        }
        projectInfoMapper.addProjectAndUserRelation(projectInfoov);


    }


    @Override
    @Transactional
    public void joinProject(ProjectInfoOV projectInfov) throws BusinessException
    {
        CustomAssert.isNotNull(projectInfov, ErrorsEnum.OBJECT_NULL);

        String userAccount = projectInfov.getUserAccount();
        Integer projectId = projectInfov.getId();

        ProUserRel proUserRel = projectInfoMapper.queryRelation(projectId, userAccount);
        if (proUserRel != null)
        {
            BusinessException.throwMessage(ErrorsEnum.RELATION_DUPLICATE);
        }

        projectInfoMapper.addProjectAndUserRelation(projectInfov);
    }

    @Override
    public List<ProjectInfo> queryProjectsByUserAccount(String userAccount) throws BusinessException
    {

        CustomAssert.isNotEmpty(userAccount, ErrorsEnum.ACCOUNT_NULL);

        List<ProjectInfo> projectInfos = projectInfoMapper.queryProjectsByUserAccount(userAccount);

        return projectInfos;

    }

    @Override
    public List<ProjectInfo> queryProjectsUserAdd(String userAccount) throws BusinessException
    {
        CustomAssert.isNotEmpty(userAccount, ErrorsEnum.ACCOUNT_NULL);
        List<ProjectInfo> projectInfos = projectInfoMapper.queryProjectsUserAdd(userAccount);
        return projectInfos;

    }

    @Override
    @Transactional
    public void makeProjectHavePerson(ProjectInfo projectInfo) throws BusinessException
    {

        CustomAssert.isNotNull(projectInfo, ErrorsEnum.OBJECT_NULL);
        CustomAssert.notEmpty(projectInfo.getUserInfoList(), ErrorsEnum.USERLIST_NULL);
        projectInfoMapper.makeProjectAndUserRelation(projectInfo);

    }

    @Override
    public List<String> queryUserByProjectName(String projectName) throws BusinessException
    {
        CustomAssert.isNotEmpty(projectName, ErrorsEnum.PROJECTNAME_NULL);
        ProjectInfo projectInfo = projectInfoMapper.queryUsersByProjectName(projectName);
        List<UserInfo> userInfoList = projectInfo.getUserInfoList();

        List<String> userNames = new ArrayList<>();

        for (UserInfo userInfo : userInfoList)
        {
            userNames.add(userInfo.getUserName());
        }

        return userNames;
    }

    @Override
    @Transactional
    public List<ProjectInfoOOV> queryPublicProject(String projectName, String createUser)
    {
        return projectInfoMapper.queryPublicProject(projectName, createUser);
    }


    /*
     * 对于增删改类操作，需要进行事务管理
     * */
    @Override
    @Transactional
    public void deleteProject(Integer projectid) throws BusinessException
    {
        CustomAssert.isNotNull(projectid, ErrorsEnum.PARAM_NULL);
        projectInfoMapper.deleteProject(projectid);
        projectInfoMapper.deleteRelationProAndUser(projectid);
    }

    @Override
    public String queryUserAccountByProjectId(Integer projectId) throws BusinessException {

        CustomAssert.isNotNull(projectId, ErrorsEnum.PARAM_NULL);
        String userAccount = projectInfoMapper.queryUserAccountByProjectId(projectId);
        return userAccount;
    }

}
