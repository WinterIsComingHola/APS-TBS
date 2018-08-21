package com.roli.apsimock.model.api;

import java.io.Serializable;

/**
 * Created by 17040386 on 2018/7/5.
 */
public class ParamInfo implements Serializable{


    private static final long serialVersionUID = -7005194748683371257L;

    private Integer botyType;
    private Integer paramType;
    private String fieldName;
    private String fieldValue;
    private String fieldDesc;
    private String rawBody;
    private Integer rowId;
    private Integer fieldType;

    public Integer getBotyType() {
        return botyType;
    }

    public void setBotyType(Integer botyType) {
        this.botyType = botyType;
    }

    public Integer getParamType() {
        return paramType;
    }

    public void setParamType(Integer paramType) {
        this.paramType = paramType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getRawBody() {
        return rawBody;
    }

    public void setRawBody(String rawBody) {
        this.rawBody = rawBody;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }
}
