package com.roli.apsimock.services.impl;

import com.roli.apsimock.dao.postman.ReqParamMapper;
import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.*;
import com.roli.apsimock.services.ReqParamInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.model.enums.ErrorsEnum;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/5/24 下午5:04
 */

@Service
public class ReqParamInfoServiceImpl implements ReqParamInfoService{

    @Value("${soa.path}")
    public String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;
    @Resource
    ReqParamMapper reqParamMapper;

    @Override
    public List<ParamInfo> queryParamInfoByApiId(String apiid) throws BusinessException {

        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(apiid);
        List<ParamInfo> paramInfos = new ArrayList<>();

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"reqparam/queryreqparam.action",soaParam);

        if(resultSoaRest.getState()==509){
            BusinessException.throwMessage(ErrorsEnum.FIELD_TYPE_UNEXCEPTION);
        }else if(resultSoaRest.getState()==510){
            BusinessException.throwMessage(ErrorsEnum.REQPARAM_NOT_EXIST);
        }

        List<Map<String,Object>> paramInfoList = (List<Map<String,Object>>)resultSoaRest.getAttribute("reqparams");

        for(Map<String,Object> paramInfoMap:paramInfoList){
            ParamInfo paramInfo = JacksonUtils.map2obj(paramInfoMap,ParamInfo.class);
            paramInfos.add(paramInfo);
        }
        return  paramInfos;

    }

    @Override
    public ResultSoaRest updateParamField(ParamInfo paramInfo) throws BusinessException {

        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(JacksonUtils.toJson(paramInfo));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"reqparam/updatereqparam.action",soaParam);
        if(resultSoaRest.getState()==102){
            BusinessException.throwMessage(ErrorsEnum.OBJECT_NULL);
        }
        return resultSoaRest;
    }

    @Override
    public ResultSoaRest queryParamMaxId(){
        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam("notnull");

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"reqparam/queryparammaxid.action",soaParam);

        return resultSoaRest;

    }

    @Override
    public ResultSoaRest insettParamRow(ReqRowData reqRowData) throws BusinessException {

        LocalDateTime localDateTime = LocalDateTime.now();
        reqRowData.setCreateTime(localDateTime);

        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(JacksonUtils.toJson(reqRowData));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"reqparam/insertparamrow.action",soaParam);

        if(resultSoaRest.getState()==102){
            BusinessException.throwMessage(ErrorsEnum.OBJECT_NULL);
        }

        return resultSoaRest;
    }

    @Override
    public ResultSoaRest deleteParamRow(Integer rowId) throws BusinessException{
        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(String.valueOf(rowId));
        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"reqparam/deleteparamrow.action",soaParam);

        if(resultSoaRest.getState()==102){
            BusinessException.throwMessage(ErrorsEnum.OBJECT_NULL);
        }

        return resultSoaRest;

    }

    @Override
    public ResultSoaRest queryBodyType(Integer apiId) throws BusinessException {

        ApsSoaParam soaParam = new ApsSoaParam();
        soaParam.setBusinessParam(String.valueOf(apiId));

        //调用restful服务
        ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"reqparam/querybodytype.action",soaParam);

        if(resultSoaRest.getState()==102){
            BusinessException.throwMessage(ErrorsEnum.OBJECT_NULL);
        }

        return resultSoaRest;

    }

    @Override
    public ResultSoaRest runPostMan(String url
            ,String httpMethod
            ,Integer apiId
            ,Integer formOrRaw) throws BusinessException {

        if(url == null||httpMethod == null||apiId==null){
            BusinessException.throwMessage(ErrorsEnum.OBJECT_NULL);
        }

        List<ReqRowData> reqRowDatas = reqParamMapper.queryRowFieldDataByApiId(apiId);

        Map<String,Map<String,String>> params = new HashMap<>();
        Map<String,String> fieldMap1 = new HashMap<>();
        Map<String,String> fieldMap2 = new HashMap<>();
        Map<String,String> fieldMap3 = new HashMap<>();
        Map<String,String> fieldMap4 = new HashMap<>();
        for(ReqRowData reqRowData : reqRowDatas){

            String fieldKey = null;
            for(ParamDetail paramDetail:reqRowData.getParamDetails()){
                if(paramDetail.getFieldType()==1){
                    fieldKey = paramDetail.getFieldValue();
                    if(formOrRaw==1&&reqRowData.getParamType()==1){
                        fieldMap1.put(fieldKey,null);
                    }else if(formOrRaw==2&&reqRowData.getParamType()==1){
                        fieldMap2.put(fieldKey,null);
                    }else if(reqRowData.getParamType()==2){
                        fieldMap3.put(fieldKey,null);
                    }else if(reqRowData.getParamType()==3){
                        fieldMap4.put(fieldKey,null);
                    }

                }else if(paramDetail.getFieldType()==2){
                    if(fieldKey ==null){
                        BusinessException.throwMessage(ErrorsEnum.FIELDVALUE_NULL);
                    }
                    if(formOrRaw==1&&reqRowData.getParamType()==1){
                        fieldMap1.put(fieldKey,paramDetail.getFieldValue());
                    }else if(formOrRaw==2&&reqRowData.getParamType()==1){
                        fieldMap2.put(fieldKey,paramDetail.getFieldValue());
                    }else if(reqRowData.getParamType()==2){
                        fieldMap3.put(fieldKey,paramDetail.getFieldValue());
                    }else if(reqRowData.getParamType()==3){
                        fieldMap4.put(fieldKey,paramDetail.getFieldValue());
                    }

                }
            }
        }

        params.put("param",fieldMap1);
        params.put("raw",fieldMap2);
        params.put("header",fieldMap3);
        params.put("cookie",fieldMap4);


        ResultSoaRest resultSoaRest = null;

        if(httpMethod.equals("POST")){
            resultSoaRest = soaRestScheduler.sendPost(url,params,formOrRaw);

        }else if(httpMethod.equals("GET")){
            resultSoaRest = soaRestScheduler.sendGet(url,params);
        }

        return resultSoaRest;

    }

}
