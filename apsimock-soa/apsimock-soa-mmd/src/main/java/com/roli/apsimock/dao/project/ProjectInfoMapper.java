package com.roli.apsimock.dao.project;

import com.roli.apsimock.model.project.ProUserRel;
import com.roli.apsimock.model.project.ProjectInfo;
import com.roli.apsimock.model.project.ProjectInfoOOV;
import com.roli.apsimock.model.project.ProjectInfoOV;
import com.roli.apsimock.model.user.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/3/15 下午8:22
 */
public interface ProjectInfoMapper {

    //新增一个项目
    public void addProject(ProjectInfoOV projectInfoOV);
    //新增项目和用户关系，新建项目
    public void addProjectAndUserRelation(ProjectInfoOV projectInfoOV);
    //查询项目和用户关系是否存在
    public ProUserRel queryRelation(@Param("projectId")Integer projectId, @Param("userAccount")String userAccount);


    //根据项目ID查询项目
    public ProjectInfo queryProjectByName(@Param("projectName")String projectName);

    //根据用户账户名查询用户名下的项目
    public List<ProjectInfo> queryProjectsByUserAccount(@Param("userAccount")String userAccount);

    //根据用户账户名查询用户加入的项目
    public List<ProjectInfo> queryProjectsUserAdd(@Param("userAccount")String userAccount);

    //根据用户id和项目名称，进行项目和用户关联（用户拉取进项目），非新建项目
    public void makeProjectAndUserRelation(ProjectInfo projectInfo);

    //根据项目名称，查询项目下的所有用户
    public ProjectInfo queryUsersByProjectName(@Param("projectName")String projectName);

    //查询所有公共项目
    public List<ProjectInfoOOV> queryPublicProject(
            @Param("projectName")String projectName,
            @Param("createUser")String createUser
    );

    //删除项目数据
    public void deleteProject(@Param("projectId") Integer projectid);
    //删除项目和人员的关联数据
    public void deleteRelationProAndUser(@Param("projectId") Integer projectid);

}
