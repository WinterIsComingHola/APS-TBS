package com.roli.apsimock.model.api;

import java.io.Serializable;
import java.util.List;

public class NoticeForAjax implements Serializable {


    private static final long serialVersionUID = -2858140864271754614L;
    private Integer code;
    private String msg;
    private Integer count;

    private List<NoticeForAjaxDetail> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<NoticeForAjaxDetail> getData() {
        return data;
    }

    public void setData(List<NoticeForAjaxDetail> data) {
        this.data = data;
    }
}
