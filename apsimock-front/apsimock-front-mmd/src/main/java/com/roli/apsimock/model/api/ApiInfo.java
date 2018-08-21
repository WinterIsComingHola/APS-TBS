package com.roli.apsimock.model.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roli.apsimock.model.user.UserInfo;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/3/27 下午7:20
 */
public class ApiInfo implements Serializable{


    private static final long serialVersionUID = -3102569548637950720L;

    private Integer id;
    private String apiName;
    private String urlPath;
    private Integer httpMethod;
    private Integer projectId;
    private Integer createUserId;
    private Integer isActive;
    private String desc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonIgnore
    private LocalDateTime updateTime;

    private List<FieldInfo> fieldInfos;
    private UserInfo userInfo;

    private List<ReqRowData> reqRowDatas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public Integer getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(Integer httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }


    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    /*public Integer getBakField() {
        return bakField;
    }

    public void setBakField(Integer bakField) {
        this.bakField = bakField;
    }*/

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<ReqRowData> getReqRowDatas() {
        return reqRowDatas;
    }

    public void setReqRowDatas(List<ReqRowData> reqRowDatas) {
        this.reqRowDatas = reqRowDatas;
    }
}
