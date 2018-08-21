package com.roli.aps.soa.client.service.impl;

import com.roli.aps.soa.client.service.ApiInfoClientService;
import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.ApiInfoOV;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/3/28 22:42
 */
@Service
public class ApiInfoClientServiceImpl implements ApiInfoClientService{

    @Value("${soa.path}")
    public String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @Override
    public ResultSoaRest addApi(ApiInfoOV apiInfoOV){

        ResultSoaRest result = new ResultSoaRest();

        try{

            ApsSoaParam apsSoaParam = new ApsSoaParam();
            apsSoaParam.setBusinessParam(JacksonUtils.toJson(apiInfoOV));

            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH+"api/addApiInfo.action",apsSoaParam);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());

        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @Override
    public ResultSoaRest deleteApi(String apiName){
        ResultSoaRest result = new ResultSoaRest();

        try{

            ApsSoaParam apsSoaParam = new ApsSoaParam();
            apsSoaParam.setBusinessParam(apiName);

            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH+"api/deleteApiInfo.action",apsSoaParam);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());

        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultSoaRest queryAllFieldByUrlPath(String urlPath){
        ResultSoaRest result = new ResultSoaRest();

        try{

            ApsSoaParam apsSoaParam = new ApsSoaParam();
            apsSoaParam.setBusinessParam(urlPath);

            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH+"api/queryAllFieldByUrlPath.action",apsSoaParam);

            Map<String,Object> resultMap = (Map<String,Object>)resultSoaRest.getData().get("fieldResponseMap");
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("fieldResponseMap",resultMap);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
