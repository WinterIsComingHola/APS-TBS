package com.roli.apsimock.model.project;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/5/13 下午2:38
 */
public class RequestProject implements Serializable{


    private static final long serialVersionUID = -4644844942107620616L;
    private String projectName;
    private List<Integer> userIdList;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Integer> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }
}
