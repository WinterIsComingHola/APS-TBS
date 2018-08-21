package com.roli.apsimock.model.api;

import java.io.Serializable;
import java.util.Map;

public class ReqPostMan implements Serializable {

    private static final long serialVersionUID = 4546122638345750968L;

    private String httpMethod;
    private Integer formOrRaw;
    private String url;
    private Map<String,Map<String,String>> params;

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Integer getFormOrRaw() {
        return formOrRaw;
    }

    public void setFormOrRaw(Integer formOrRaw) {
        this.formOrRaw = formOrRaw;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Map<String, String>> getParams() {
        return params;
    }

    public void setParams(Map<String, Map<String, String>> params) {
        this.params = params;
    }
}
