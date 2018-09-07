package com.roli.apsimock.services;

import com.roli.apsimock.model.MockRunResultInfo;
import com.roli.apsimock.model.api.ApiInfo;
import com.roli.apsimock.model.api.ApiInfoOV;
import com.roli.apsimock.model.api.MockRunResultForAjax;
import com.roli.common.exception.BusinessException;
import com.ruoli.soa.model.ResultSoaRest;

/**
 * @author xuxinyu
 * @date 2018/5/22 上午10:12
 */
public interface ApiInfoService
{
    public ResultSoaRest queryApiByProjectid(String projectid, String page, String limit);

    //新增Api
    public ResultSoaRest addApi(ApiInfoOV apiInfoOV) throws BusinessException;

    //根据apiid查询api
    public ApiInfo queryApiInfoByApiid(String apiid);

    //更新Api
    public ResultSoaRest updateApiInfo(String tag, String field, String apiid);

    //新增mock运行结果
    public void addMockRunResult(MockRunResultInfo mockRunResultInfo);

    //查询Mock运行结果
    public MockRunResultForAjax queryMockRunResult(String urlpath, String pageNum, String pageSize, String starttime, String endtime);
}
