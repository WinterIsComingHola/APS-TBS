package com.roli.aps.soa.client.service;

import com.roli.apsimock.model.api.ApiInfoOV;
import com.ruoli.soa.model.ResultSoaRest;


/**
 * @author xuxinyu
 * @date 2018/3/28 下午8:28
 */
public interface ApiInfoClientService {

    public ResultSoaRest addApi(ApiInfoOV apiInfoOV);
    public ResultSoaRest deleteApi(String apiName);
    public ResultSoaRest queryAllFieldByUrlPath(String urlPath);
}
