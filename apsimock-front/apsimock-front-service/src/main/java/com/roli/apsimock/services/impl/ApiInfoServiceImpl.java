package com.roli.apsimock.services.impl;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.ApiInfo;
import com.roli.apsimock.model.api.ApiInfoOV;
import com.roli.apsimock.services.ApiInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.model.enums.ErrorsEnum;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/5/22 上午10:12
 */

@Service
public class ApiInfoServiceImpl implements ApiInfoService{

    @Value("${soa.path}")
    public String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @Override
    public ResultSoaRest queryApiByProjectid(String projectid,String page, String limit){
        ApsSoaParam soaParam = new ApsSoaParam();
        Map<String,String> mapParam = new HashMap<>();
        mapParam.put("projectid",projectid);
        mapParam.put("pageNum",page);
        mapParam.put("pageSize",limit);
        soaParam.setBusinessParam(JacksonUtils.toJson(mapParam));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"api/queryApiByProjectid.action",soaParam);
        return resultSoaRest;
    }

    @Override
    public ResultSoaRest addApi(ApiInfoOV apiInfoOV) throws BusinessException {
        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(JacksonUtils.toJson(apiInfoOV));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"api/addApiInfo.action",soaParam);

        if(resultSoaRest.getState()==404){
            BusinessException.throwMessage(ErrorsEnum.API_DUPLICATE);
        }

        if(resultSoaRest.getState()==405){
            BusinessException.throwMessage(ErrorsEnum.URLPATH_DUPLICATE);
        }
        if(resultSoaRest.getState()==102){
            BusinessException.throwMessage(ErrorsEnum.OBJECT_NULL);
        }

        return resultSoaRest;

    }

    @Override
    public ResultSoaRest updateApiInfo(String tag, String field, String apiid)
    {
        ApsSoaParam soaParam = new ApsSoaParam();
        Map<String,String> map = new HashMap<>();
        map.put("tag",tag);
        map.put("field",field);
        map.put("apiid",apiid);

        soaParam.setBusinessParam(JacksonUtils.toJson(map));
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"api/updateApiInfo.action",soaParam);
        return resultSoaRest;
    }

    @Override
    public ApiInfo queryApiInfoByApiid(String apiid){
        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(apiid);

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"api/queryApiInfoByApiId.action",soaParam);

        Map<String,Object> map = (Map)resultSoaRest.getAttribute("apiinfo");
        ApiInfo apiInfo = JacksonUtils.map2obj(map,ApiInfo.class);

        return apiInfo;


    }


}
