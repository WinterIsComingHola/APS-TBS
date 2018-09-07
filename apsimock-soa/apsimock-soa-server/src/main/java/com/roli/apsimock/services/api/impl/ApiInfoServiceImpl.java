package com.roli.apsimock.services.api.impl;

import com.roli.apsimock.common.utils.MapMultiAnalysis;
import com.roli.apsimock.dao.api.ApiInfoMapper;
import com.roli.apsimock.dao.api.FieldInfoMapper;
import com.roli.apsimock.dao.api.FieldNewInfoMapper;
import com.roli.apsimock.model.api.ApiInfo;
import com.roli.apsimock.model.api.ApiInfoOV;
import com.roli.apsimock.model.api.FieldInfo;
import com.roli.apsimock.model.api.MockRunResultInfo;
import com.roli.apsimock.services.api.ApiInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.exception.CustomAssert;
import com.roli.common.model.enums.ErrorsEnum;
import com.ruoli.soa.model.ResultSoaRest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/3/27 下午8:53
 */
@Service

public class ApiInfoServiceImpl implements ApiInfoService{

    @Resource
    ApiInfoMapper apiInfoMapper;
    @Resource
    FieldInfoMapper fieldInfoMapper;
    @Resource
    FieldNewInfoMapper fieldNewInfoMapper;

    @Override
    @Transactional
    public void addApi(ApiInfoOV apiInfoOV) throws BusinessException {

        CustomAssert.isNotNull(apiInfoOV, ErrorsEnum.OBJECT_NULL);
        CustomAssert.isNotEmpty(apiInfoOV.getApiName(),ErrorsEnum.APINAME_NULL);
        CustomAssert.isNotEmpty(apiInfoOV.getUrlPath(),ErrorsEnum.APIURLPATH_NULL);
        CustomAssert.isNotNull(apiInfoOV.getHttpMethod(),ErrorsEnum.APIHTTPMETHOD_NULL);
        CustomAssert.isNotEmpty(apiInfoOV.getUserAccount(),ErrorsEnum.ACCOUNT_NULL);
        CustomAssert.isNotNull(apiInfoOV.getProjectId(), ErrorsEnum.OBJECT_NULL);

        String apiName = apiInfoOV.getApiName();
        ApiInfo apiInfo = apiInfoMapper.queryApiInfoByName(apiName);

        if(apiInfo!=null){
            BusinessException.throwMessage(ErrorsEnum.API_DUPLICATE);
        }

        String urlPath = apiInfoOV.getUrlPath();
        ApiInfo apiInfo2 = apiInfoMapper.queryApiInfoByUrlPath(urlPath);

        if(apiInfo2!=null){
            BusinessException.throwMessage(ErrorsEnum.URLPATH_DUPLICATE);
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        apiInfoOV.setCreateTime(localDateTime);

        apiInfoMapper.addApi(apiInfoOV);

        //新增接口的同时，需要把root节点新增到Field表
        fieldNewInfoMapper.insertRootField(apiName);

    }

    @Override
    public void deleteApi(String apiName) throws BusinessException {

        CustomAssert.isNotEmpty(apiName,ErrorsEnum.PROJECTNAME_NULL);

        ApiInfo apiInfo = apiInfoMapper.queryApiInfoByName(apiName);

        if(apiInfo==null){
            BusinessException.throwMessage(ErrorsEnum.API_NOTFIND);
        }

        Integer isActive = apiInfo.getIsActive();
        if(isActive==0){
            BusinessException.throwMessage(ErrorsEnum.API_HASBEDELETE);
        }
        apiInfoMapper.deleteApiInfoByName(apiName);

    }

    @Override
    public List<ApiInfo> queryApiByProjectid(Integer projectid) throws BusinessException {
        CustomAssert.isNotNull(projectid, ErrorsEnum.OBJECT_NULL);

        List<ApiInfo> apiInfos = apiInfoMapper.queryApiInfoByProjectId(projectid);

        return apiInfos;

    }

    @Override
    public ApiInfo queryApiInfoByApiId (Integer apiid) throws BusinessException {
        CustomAssert.isNotNull(apiid, ErrorsEnum.OBJECT_NULL);

        ApiInfo apiInfo = apiInfoMapper.queryApiInfoByApiId(apiid);
        return apiInfo;
    }

    @Override
    public Map<String,Object> queryAllFieldByUrlPath(String urlPath) throws BusinessException {

        CustomAssert.isNotEmpty(urlPath,ErrorsEnum.URLPATH_NULL);

        List<FieldInfo> fieldInfoList = fieldInfoMapper.queryAllFieldByUrlPath(urlPath);

        Map<String,Object> fieldMap = new HashMap<>();


        for(FieldInfo fieldInfo:fieldInfoList){
            if(fieldInfo.getIsActive()!=1) {
                continue;
            }
            if(fieldInfo.getIsRoot()==1){

                switch (fieldInfo.getFieldType()){
                    case 1:
                    {
                        String fieldValue = fieldInfo.getFieldValue();
                        fieldMap.put(fieldInfo.getFieldName(),fieldValue);
                        break;
                    }
                    case 2:
                    {
                        Integer fieldValue = Integer.parseInt(fieldInfo.getFieldValue());
                        fieldMap.put(fieldInfo.getFieldName(),fieldValue);
                        break;
                    }
                    case 3:
                    {

                        Float fieldValue = Float.parseFloat(fieldInfo.getFieldValue());
                        fieldMap.put(fieldInfo.getFieldName(),fieldValue);
                        break;
                    }
                    case 4:
                    {
                        //要求前端入库时严格按照true和false写入
                        Boolean fieldValue = Boolean.parseBoolean(fieldInfo.getFieldValue());
                        fieldMap.put(fieldInfo.getFieldName(),fieldValue);
                        break;
                    }
                    case 5:
                    {
                        Map<String,Object> tmap = new HashMap<>();
                        //为了保证key的全局唯一性，对于Object型的key，需要加上字段的id作为前缀
                        fieldMap.put(fieldInfo.getId()+fieldInfo.getFieldName(),tmap);
                        break;
                    }
                    case 6:
                    {
                        List<Object> tlist = new ArrayList<>();
                        //为了保证key的全局唯一性，对于List型的key，需要加上字段的id作为前缀
                        fieldMap.put(fieldInfo.getId()+fieldInfo.getFieldName(),tlist);
                        break;
                    }
                    default:
                        {
                            BusinessException.throwMessage(ErrorsEnum.RESULT_ERROR);
                        }
                }
            }else{
                FieldInfo fatherFieldInfo = fieldInfoMapper.queryFatherFieldByFid(fieldInfo.getFatherId());
                if(fatherFieldInfo.getFieldType()==5){
                    String fatherKey = null;
                    if(StringUtils.isEmpty(fatherFieldInfo.getFieldName())){
                        //对于父级是Object型的情况，可能存在父级的名称为空的情况，则将父级的id作为key进行处理
                        fatherKey = String.valueOf(fatherFieldInfo.getId());
                    }else{
                        fatherKey = fatherFieldInfo.getId()+fatherFieldInfo.getFieldName();
                    }
                    switch (fieldInfo.getFieldType()){
                        case 1:
                        {
                            fieldMap = MapMultiAnalysis.convertMap(fieldMap,fatherKey,fieldInfo.getFieldName(),fieldInfo.getFieldValue(),false);
                            break;
                        }
                        case 2:
                        {
                            fieldMap = MapMultiAnalysis.convertMap(fieldMap,fatherKey,fieldInfo.getFieldName(),Integer.parseInt(fieldInfo.getFieldValue()),false);
                            break;
                        }
                        case 3:
                        {
                            fieldMap = MapMultiAnalysis.convertMap(fieldMap,fatherKey,fieldInfo.getFieldName(),Float.parseFloat(fieldInfo.getFieldValue()),false);
                            break;
                        }
                        case 4:
                        {
                            fieldMap = MapMultiAnalysis.convertMap(fieldMap,fatherKey,fieldInfo.getFieldName(),Boolean.parseBoolean(fieldInfo.getFieldValue()),false);
                            break;
                        }
                        case 5:
                        {
                            Map<String,Object> tmap = new HashMap<>();
                            fieldMap = MapMultiAnalysis.convertMap(fieldMap,fatherKey,fieldInfo.getId()+fieldInfo.getFieldName(),tmap,false);
                            break;
                        }
                        case 6:
                        {
                            List<Object> tlist = new ArrayList<>();
                            fieldMap = MapMultiAnalysis.convertMap(fieldMap,fatherKey,fieldInfo.getId()+fieldInfo.getFieldName(),tlist,false);
                            break;
                        }
                        default:
                        {
                            BusinessException.throwMessage(ErrorsEnum.RESULT_ERROR);
                        }
                    }
                }else if(fatherFieldInfo.getFieldType()==6){
                    switch (fieldInfo.getFieldType()){
                        case 1:
                        {
                            fieldMap = MapMultiAnalysis.convertList(fieldMap,fatherFieldInfo.getId()+fatherFieldInfo.getFieldName(),fieldInfo.getFieldValue());
                            break;
                        }
                        case 2:
                        {
                            fieldMap = MapMultiAnalysis.convertList(fieldMap,fatherFieldInfo.getId()+fatherFieldInfo.getFieldName(),Integer.parseInt(fieldInfo.getFieldValue()));
                            break;
                        }
                        case 3:
                        {
                            fieldMap = MapMultiAnalysis.convertList(fieldMap,fatherFieldInfo.getId()+fatherFieldInfo.getFieldName(),Float.parseFloat(fieldInfo.getFieldValue()));
                            break;
                        }
                        case 4:
                        {
                            fieldMap = MapMultiAnalysis.convertList(fieldMap,fatherFieldInfo.getId()+fatherFieldInfo.getFieldName(),Boolean.parseBoolean(fieldInfo.getFieldValue()));
                            break;
                        }
                        case 5:
                        {
                            Map<String,Object> tmap = new HashMap<>();
                            fieldMap = MapMultiAnalysis.convertList(fieldMap,fatherFieldInfo.getId()+fatherFieldInfo.getFieldName(),tmap);
                            break;
                        }
                        /*case 6:
                        {
                            List<Object> tlist = new ArrayList<>();
                            fieldMap = MapMultiAnalysis.convertMap(fieldMap,fatherFieldInfo.getFieldName(),fieldInfo.getId()+fieldInfo.getFieldName(),tlist);
                            break;
                        }*/
                        default:
                        {
                            BusinessException.throwMessage(ErrorsEnum.RESULT_ERROR);
                        }
                    }
                }
            }
        }
        return fieldMap;
    }

    @Override
    @Transactional
    public void updateApiInfo(String tag, String field, Integer apiid) throws BusinessException
    {
        CustomAssert.isNotNull(apiid, ErrorsEnum.OBJECT_NULL);
        apiInfoMapper.updateApiInfo(tag,field,apiid);
    }

    @Override
    public ResultSoaRest queryAppointFieldByApiId(String tag, String field, Integer apiid) throws BusinessException
    {
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        CustomAssert.isNotNull(apiid, ErrorsEnum.OBJECT_NULL);
        List<String> fieldValues = apiInfoMapper.queryAppointFieldByApiId(tag, apiid);
        if(tag.equals("1")){
            if(fieldValues.contains(field)){
                throw new BusinessException(String.valueOf(ErrorsEnum.API_DUPLICATE.getErrorCode()),ErrorsEnum.API_DUPLICATE.getMessage());
            }
        }
        if(tag.equals("2")){
            if(fieldValues.contains(field)){
                throw new BusinessException(String.valueOf(ErrorsEnum.URLPATH_DUPLICATE.getErrorCode()),ErrorsEnum.URLPATH_DUPLICATE.getMessage());
            }
        }
        return resultSoaRest;
    }

    @Override
    public void addMockRunResult(MockRunResultInfo mockRunResultInfo) throws BusinessException
    {
        CustomAssert.isNotNull(mockRunResultInfo,ErrorsEnum.OBJECT_NULL);
        CustomAssert.isNotEmpty(mockRunResultInfo.getRequestSource(),ErrorsEnum.FIELDVALUE_NULL);
        CustomAssert.isNotEmpty(mockRunResultInfo.getRequestMethod(),ErrorsEnum.FIELDVALUE_NULL);
        CustomAssert.isNotEmpty(mockRunResultInfo.getRequestFormat(),ErrorsEnum.FIELDVALUE_NULL);
        CustomAssert.isNotEmpty(mockRunResultInfo.getRequestResult(),ErrorsEnum.FIELDVALUE_NULL);
        CustomAssert.isNotEmpty(mockRunResultInfo.getUrlPath(),ErrorsEnum.FIELDVALUE_NULL);

        LocalDateTime localDateTime = LocalDateTime.now();
        mockRunResultInfo.setRequestTime(localDateTime);
        apiInfoMapper.addMockRunResult(mockRunResultInfo);
    }

    @Override
    public List<MockRunResultInfo> queryMockRunResultInfo(String urlpath, String starttime, String endtime) throws BusinessException
    {
        CustomAssert.isNotEmpty(urlpath,ErrorsEnum.API_NOTFIND);
        return apiInfoMapper.queryMockRunResultInfo(urlpath,starttime,endtime);
    }
}
