package com.roli.apsimock.model.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class FieldNew implements Serializable {

    private static final long serialVersionUID = -1551744188396446211L;

    private Integer id;
    private Integer dataType;
    private Integer isRoot;
    private Integer fatherId;
    private Integer apiId;
    private Integer rowWidth;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    private List<FieldDetail> fieldDetailList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Integer isRoot) {
        this.isRoot = isRoot;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

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

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Integer getRowWidth() {
        return rowWidth;
    }

    public void setRowWidth(Integer rowWidth) {
        this.rowWidth = rowWidth;
    }

    public List<FieldDetail> getFieldDetailList() {
        return fieldDetailList;
    }

    public void setFieldDetailList(List<FieldDetail> fieldDetailList) {
        this.fieldDetailList = fieldDetailList;
    }
}
