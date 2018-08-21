package com.roli.aps.soa.client.service;

import com.roli.apsimock.model.api.FieldInfoOV;
import com.ruoli.soa.model.ResultSoaRest;

/**
 * @author xuxinyu
 * @date 2018/4/4 下午4:21
 */
public interface FieldInfoClientService {

    public ResultSoaRest addField(FieldInfoOV fieldInfoOV);

    public ResultSoaRest queryAllFieldByApiName(String apiName);


}
