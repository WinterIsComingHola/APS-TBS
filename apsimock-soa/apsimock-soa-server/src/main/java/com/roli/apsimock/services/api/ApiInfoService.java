package com.roli.apsimock.services.api;

import com.roli.apsimock.model.api.ApiInfo;
import com.roli.apsimock.model.api.ApiInfoOV;
import com.roli.common.exception.BusinessException;
import com.ruoli.soa.model.ResultSoaRest;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/3/27 下午8:52
 */

public interface ApiInfoService {

    public void addApi(ApiInfoOV apiInfoOV) throws BusinessException;

    public void deleteApi(String apiName) throws BusinessException;

    public Map<String,Object> queryAllFieldByUrlPath(String urlPath) throws BusinessException;

    public List<ApiInfo> queryApiByProjectid(Integer projectid) throws BusinessException;

    public ApiInfo queryApiInfoByApiId (Integer apiid) throws BusinessException;

    public void updateApiInfo(String tag, String field, Integer apiid) throws BusinessException;

    public ResultSoaRest queryAppointFieldByApiId(String tag, String field,Integer apiid) throws BusinessException;

}
