package com.roli.apsimock.services;

import com.roli.apsimock.model.api.ApiInfo;
import com.roli.apsimock.model.api.ApiInfoOV;
import com.roli.common.exception.BusinessException;
import com.ruoli.soa.model.ResultSoaRest;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/5/22 上午10:12
 */
public interface ApiInfoService {
    public ResultSoaRest queryApiByProjectid(String projectid,String page,String limit);

    //新增Api
    public ResultSoaRest addApi(ApiInfoOV apiInfoOV) throws BusinessException;

    //根据apiid查询api
    public ApiInfo queryApiInfoByApiid(String apiid);

    //更新Api
    public ResultSoaRest updateApiInfo(String tag, String field, String apiid);
}
