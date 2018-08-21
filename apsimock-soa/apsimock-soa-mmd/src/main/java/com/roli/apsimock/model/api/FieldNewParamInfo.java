package com.roli.apsimock.model.api;

import java.io.Serializable;

public class FieldNewParamInfo implements Serializable {
    private static final long serialVersionUID = 1304083852212642524L;

    private Integer dataType;
    private Integer fatherId;
    private Integer rowWidth;

    private Integer fieldType;
    private String fieldName;
    private String fieldValue;
    private String fieldDesc;
    private Integer rowId;

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public Integer getRowWidth() {
        return rowWidth;
    }

    public void setRowWidth(Integer rowWidth) {
        this.rowWidth = rowWidth;
    }

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
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

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }
}
