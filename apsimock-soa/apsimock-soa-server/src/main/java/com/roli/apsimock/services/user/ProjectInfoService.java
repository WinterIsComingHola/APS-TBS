package com.roli.apsimock.services.user;

import com.roli.apsimock.model.project.ProjectInfo;
import com.roli.apsimock.model.project.ProjectInfoOOV;
import com.roli.apsimock.model.project.ProjectInfoOV;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.common.exception.BusinessException;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/3/16 下午4:53
 */
public interface ProjectInfoService {

    //新增一个工程
    public void addProject(ProjectInfoOV projectInfoov) throws Exception;

    //加入一个项目
    public void joinProject(ProjectInfoOV projectInfoov) throws BusinessException;

    //根据用户账户查询名下项目
    public List<ProjectInfo> queryProjectsByUserAccount(String userAccount) throws Exception;

    //查询用户加入的项目
    public List<ProjectInfo> queryProjectsUserAdd(String userAccount) throws BusinessException;

    //往项目中拉取人员
    public void makeProjectHavePerson(ProjectInfo projectInfo)throws Exception;
    //根据项目获取项目下的成员信息
    public List<String> queryUserByProjectName(String projectName)throws Exception;

    //查询所有公共项目
    public List<ProjectInfoOOV> queryPublicProject(String projectName,String createUser);

    //删除当前项目
    public void deleteProject(Integer projectid) throws BusinessException;

}
