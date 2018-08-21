package com.roli.apsimock.model.project;

import java.io.Serializable;

/**
 * @author xuxinyu
 * @date 2018/5/4 下午3:19
 */
public class ProjectForAjaxDetail implements Serializable{


    private static final long serialVersionUID = 5602487187702032674L;
    private Integer projectid;
    private String projectname;
    private String createuser;
    private String tag;
    private String desc;

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
