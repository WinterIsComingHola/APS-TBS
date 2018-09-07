package com.roli.apsimock.services;

import com.roli.apsimock.model.api.NoticeForAjax;
import com.roli.apsimock.model.project.ProjectForAjax;
import com.roli.apsimock.model.project.ProjectInfo;
import com.roli.apsimock.model.project.ProjectInfoOV;
import com.roli.apsimock.model.project.Table;
import com.roli.common.exception.BusinessException;
import com.ruoli.soa.model.ResultSoaRest;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/5/4 下午3:22
 */
public interface ProjectInfoService {

    //查询公共项目
    public ProjectForAjax queryPublicProjectInfos(String projectName,String createUser, String pageNum, String pageSize);
    //加入项目
    public ResultSoaRest joinProject(String userAccount, Integer projectid, Integer isMaster) throws BusinessException;
    //根据用户账户查询项目信息
    public List<ProjectInfo> getProjectInfosByUserAccount(String userAccount,String userRole,String page, String limit) throws BusinessException;

    public ProjectForAjax getMyProjectForAjax(String userAccount,String userRole, String pageNum, String pageSize) throws BusinessException;


    //新增项目
    public ResultSoaRest addProjectInfo(ProjectInfoOV projectInfoOV) throws BusinessException;

    //拉取人员到项目
    public ResultSoaRest makeProjectHavePerson(String projectName,List<Integer> userIdList);

    //删除项目
    public ResultSoaRest deleteProject(Integer projectid) throws BusinessException;

    //查询通知记录
    public NoticeForAjax queryNoticeByUser(String userAccount,
                                           String page,
                                           String limit);

}
