package com.roli.aps.soa.client.service;

import com.roli.apsimock.model.project.ProjectInfo;
import com.roli.apsimock.model.project.ProjectInfoOV;
import com.ruoli.soa.model.ResultSoaRest;

/**
 * @author xuxinyu
 * @date 2018/3/18 上午9:48
 */
public interface ProjectInfoClientService {
    public ResultSoaRest addProject(ProjectInfoOV projectInfoOv);

    public ResultSoaRest queryProjectsByUserAccount(String account);
    public ResultSoaRest makeProjectHavePerson(ProjectInfo projectInfo);
    public ResultSoaRest queryUserByProjectName(String projectName);

    public ResultSoaRest queryPublicProject();
}
