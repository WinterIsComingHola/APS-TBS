package com.roli.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/3/5 下午5:59
 */
public class BaseRunTimeException extends Exception{


    private static final long serialVersionUID = -2960073620806529618L;

    private String            errCode;

    private List<String> params           = new ArrayList<String>(0);

    private String            message;

    public BaseRunTimeException() {

    }

    public BaseRunTimeException(String errCode) {
        this.errCode = errCode;
        if (message == null) {
            this.message = errCode;
        }
    }

    public BaseRunTimeException(String errCode, List<String> params) {
        this.errCode = errCode;
        this.params = params;
        if (message == null) {
            this.message = errCode;
        }
    }

    public BaseRunTimeException(String errCode, String[] params) {
        this.errCode = errCode;
        for (String s : params) {
            this.params.add(s);
        }
        if (message == null) {
            this.message = errCode;
        }
    }

    public BaseRunTimeException(String errCode, String message) {
        this.errCode = errCode;
        this.message = message;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
        if (message == null) {
            this.message = errCode;
        }
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addParam(String p) {
        params.add(p);
    }

}
