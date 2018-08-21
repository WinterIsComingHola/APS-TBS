package com.roli.apsimock.model.project;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/5/11 下午2:54
 */
public class Table implements Serializable{

    private static final long serialVersionUID = -4006817918480211459L;
    private Integer code;
    private String msg;
    private Integer count;
    private List<Map<String,Object>> data;

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

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
