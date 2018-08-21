package com.roli.apsimock.model.project;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/5/4 下午3:16
 */
public class ProjectForAjax implements Serializable{


    private static final long serialVersionUID = -1873752327325662120L;
    private Integer code;
    private String msg;
    private Integer count;
    private List<ProjectForAjaxDetail> data;

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

    public List<ProjectForAjaxDetail> getData() {
        return data;
    }

    public void setData(List<ProjectForAjaxDetail> data) {
        this.data = data;
    }
}
