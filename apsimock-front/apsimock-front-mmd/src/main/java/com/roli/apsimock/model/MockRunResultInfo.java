package com.roli.apsimock.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hzr/18060832
 * @date 2018/8/27 11:14
 */
public class MockRunResultInfo implements Serializable
{
    private static final long serialVersionUID = 201808271400L;

    private Integer id;
    private Integer apiId;
    private String requestSource;
    private String requestMethod;
    private String requestFormat;
    private String requestResult;
    private String failedReason;
    private String urlPath;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getApiId()
    {
        return apiId;
    }

    public void setApiId(Integer apiId)
    {
        this.apiId = apiId;
    }

    public String getRequestSource()
    {
        return requestSource;
    }

    public void setRequestSource(String requestSource)
    {
        this.requestSource = requestSource;
    }

    public String getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public String getRequestFormat()
    {
        return requestFormat;
    }

    public void setRequestFormat(String requestFormat)
    {
        this.requestFormat = requestFormat;
    }

    public String getRequestResult()
    {
        return requestResult;
    }

    public void setRequestResult(String requestResult)
    {
        this.requestResult = requestResult;
    }

    public String getFailedReason()
    {
        return failedReason;
    }

    public void setFailedReason(String failedReason)
    {
        this.failedReason = failedReason;
    }

    public LocalDateTime getRequestTime()
    {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime)
    {
        this.requestTime = requestTime;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
}
