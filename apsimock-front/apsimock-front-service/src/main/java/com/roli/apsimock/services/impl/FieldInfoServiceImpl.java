package com.roli.apsimock.services.impl;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.FieldDetail;
import com.roli.apsimock.model.api.FieldNew;
import com.roli.apsimock.model.api.FieldNewParamInfo;
import com.roli.apsimock.services.FieldInfoService;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FieldInfoServiceImpl implements FieldInfoService {


    @Value("${soa.path}")
    String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @Override
    public ResultSoaRest queryFieldRowMaxId(){

        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam("notnull");
        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/queryFieldRowMaxId.action",apsSoaParam);
        return resultSoaRest;
    }

    @Override
    public ResultSoaRest queryAllChildNodeId(Integer currentId){

        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam(String.valueOf(currentId));

        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/queryAllChildNodeId.action",apsSoaParam);

        return resultSoaRest;

    }

    @Override
    public ResultSoaRest queryRootNodeIdByApiId(Integer apiId){
        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam(String.valueOf(apiId));

        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/queryrootnodeid.action",apsSoaParam);

        return resultSoaRest;
    }

    @Override
    public ResultSoaRest queryRootNodeTypeByApiId(Integer apiId){
        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam(String.valueOf(apiId));

        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/queryrootnodetype.action",apsSoaParam);

        return resultSoaRest;
    }

    @Override
    public ResultSoaRest insertNodeData(FieldNew fieldNew){
        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam(JacksonUtils.toJson(fieldNew));

        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/insertnodedata.action",apsSoaParam);

        return resultSoaRest;

    }

    @Override
    public ResultSoaRest queryFatherNodeId(Integer currentId){
        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam(String.valueOf(currentId));

        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/queryfatherid.action",apsSoaParam);

        return resultSoaRest;

    }

    @Override
    public List<FieldNewParamInfo> queryAllFieldByApiId(Integer apiId){

        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam(String.valueOf(apiId));
        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/queryallfield.action",apsSoaParam);

        List<Map<String,Object>> fieldNews = (List<Map<String,Object>>)resultSoaRest.getAttribute("fieldNews");
        //将List<Field>转换为List<FieldNewParamInfo>
        List<FieldNewParamInfo> fieldNewParamInfos = convertField2FieldParamInfo(fieldNews);

        //需要对数据结果按照父子关系进行重新排序
        ResultSoaRest resultSoaRest2 =
                soaRestScheduler.sendPost(SOAPATH+"api/queryrootnodeid.action",apsSoaParam);
        Integer rootId = (Integer)resultSoaRest2.getAttribute("rootId");
        //调用递归方法实现数组重排
        List<FieldNewParamInfo> fieldNewParamInfosOrder = sortFieldParam(fieldNewParamInfos,rootId);

        return fieldNewParamInfosOrder;
    }

    @Override
    public ResultSoaRest updateFieldValue(FieldNewParamInfo fieldNewParamInfo){

        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam(JacksonUtils.toJson(fieldNewParamInfo));

        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/updatefieldvalue.action",apsSoaParam);

        return resultSoaRest;
    }

    @Override
    public ResultSoaRest updateFieldRowDataType(FieldNewParamInfo fieldNewParamInfo){
        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam(JacksonUtils.toJson(fieldNewParamInfo));

        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/updatefieldrowdatatype.action",apsSoaParam);

        return resultSoaRest;
    }

    @Override
    public ResultSoaRest deleteMockData(List<String> rowIds){
        ApsSoaParam apsSoaParam = new ApsSoaParam();

        List<Integer> rowIdsInt = new ArrayList<>();
        for(String rowid:rowIds){
            rowIdsInt.add(Integer.parseInt(rowid));
        }

        apsSoaParam.setBusinessParam(JacksonUtils.toJson(rowIdsInt));
        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/deletemockdata.action",apsSoaParam);

        return resultSoaRest;
    }


    @Override
    public Object runMockServer(String url){
        ApsSoaParam apsSoaParam = new ApsSoaParam();
        apsSoaParam.setBusinessParam(url);
        ResultSoaRest resultSoaRest =
                soaRestScheduler.sendPost(SOAPATH+"api/queryallfieldByUrl.action",apsSoaParam);

        List<Map<String,Object>> fieldNews = (List<Map<String,Object>>)resultSoaRest.getAttribute("fieldNews");
        //将List<Field>转换为List<FieldNewParamInfo>
        List<FieldNewParamInfo> fieldNewParamInfos = convertField2FieldParamInfo(fieldNews);

        ApsSoaParam apsSoaParam2 = new ApsSoaParam();
        Map<String,String> mapStr = new HashMap<>();
        mapStr.put("url",url);
        mapStr.put("isRootId","1");
        apsSoaParam2.setBusinessParam(JacksonUtils.toJson(mapStr));
        ResultSoaRest resultSoaRest2 =
                soaRestScheduler.sendPost(SOAPATH+"api/queryrootnodeidByUrl.action",apsSoaParam2);
        Integer rootId = (Integer)resultSoaRest2.getAttribute("rootInfo");
        //调用递归方法实现数组重排
        List<FieldNewParamInfo> fieldNewParamInfosOrder = sortFieldParam(fieldNewParamInfos,rootId);


        //获取root节点的datatype，如果是5，则为Map类型，如果是6，则为List类型
        mapStr.put("isRootId","0");
        apsSoaParam2.setBusinessParam(JacksonUtils.toJson(mapStr));
        ResultSoaRest resultSoaRest3 =
                soaRestScheduler.sendPost(SOAPATH+"api/queryrootnodeidByUrl.action",apsSoaParam2);
        Integer dataType = (Integer)resultSoaRest3.getAttribute("rootInfo");

        //调用递归方法实现json体构造，如果根节点是对象，则返回map，如果根节点是数组，则返回List
        if(dataType == 5){
            Map<String,Object> resultMap = (Map<String,Object>)mockHandler(fieldNewParamInfosOrder,rootId,1);
            return resultMap;
        }else if(dataType == 6){
            List<Object> resultList = (List<Object>)mockHandler(fieldNewParamInfosOrder,rootId,2);
            return resultList;
        }else{
            return null;
        }

    }

    /**
     * 用于将SOA返回的List<FieldInfo>转换为List<FieldNewParamInfo>
     * **/
    private List<FieldNewParamInfo> convertField2FieldParamInfo(List<Map<String,Object>> fieldNews){
        List<FieldNewParamInfo> fieldNewParamInfos = new ArrayList<>();

        for(Map<String,Object> fieldNewMap:fieldNews){

            FieldNew fieldNew = JacksonUtils.map2obj(fieldNewMap,FieldNew.class);

            FieldNewParamInfo fieldNewParamInfo = new FieldNewParamInfo();
            fieldNewParamInfo.setDataType(fieldNew.getDataType());
            fieldNewParamInfo.setFatherId(fieldNew.getFatherId());
            fieldNewParamInfo.setRowWidth(fieldNew.getRowWidth());
            fieldNewParamInfo.setRowId(fieldNew.getId());

            List<FieldDetail> fieldDetails = fieldNew.getFieldDetailList();

            for(FieldDetail fieldDetail:fieldDetails){
                if(fieldDetail.getFieldType()==1){
                    fieldNewParamInfo.setFieldName(fieldDetail.getFieldValue());
                }else if(fieldDetail.getFieldType()==2){
                    fieldNewParamInfo.setFieldValue(fieldDetail.getFieldValue());
                }else if(fieldDetail.getFieldType()==3){
                    fieldNewParamInfo.setFieldDesc(fieldDetail.getFieldValue());
                }

            }

            fieldNewParamInfos.add(fieldNewParamInfo);

        }
        return fieldNewParamInfos;
    }

    /**
     * 本方法实现数据库返回的FieldNewParamInfo数组重排序
     * 数据库返回的数组在父子关系上没有按照前端的顺序进行排列，本方法实现按照前端的顺序排列数据
     * **/
    private List<FieldNewParamInfo> sortFieldParam(List<FieldNewParamInfo> sourceList,
                                                   Integer sourceId){
        List<FieldNewParamInfo> destList = new ArrayList<>();
        for(FieldNewParamInfo fieldNewParamInfo:sourceList){
            if(fieldNewParamInfo.getFatherId().intValue()==sourceId.intValue()){
                destList.add(fieldNewParamInfo);
                destList.addAll(sortFieldParam(sourceList,fieldNewParamInfo.getRowId()));
            }

        }
        return destList;
    }


    private Object mockHandler(List<FieldNewParamInfo> sourceList,
                            Integer parentId,int ParentDataType){

        if(ParentDataType == 1){

            Map<String,Object> mockChildFieldMap = new HashMap<>();
            for(FieldNewParamInfo fieldNewParamInfo : sourceList){
                if(fieldNewParamInfo.getFatherId().intValue()==parentId.intValue()){

                    switch (fieldNewParamInfo.getDataType()){
                        case 1:
                            mockChildFieldMap.put(fieldNewParamInfo.getFieldName(),fieldNewParamInfo.getFieldValue());
                            break;
                        case 2:
                            mockChildFieldMap.put(fieldNewParamInfo.getFieldName(),Integer.parseInt(fieldNewParamInfo.getFieldValue()));

                            break;
                        case 3:
                            mockChildFieldMap.put(fieldNewParamInfo.getFieldName(),Float.parseFloat(fieldNewParamInfo.getFieldValue()));

                            break;
                        case 4:
                            mockChildFieldMap.put(fieldNewParamInfo.getFieldName(),Boolean.parseBoolean(fieldNewParamInfo.getFieldValue()));

                            break;
                        case 5:
                            mockChildFieldMap.put(fieldNewParamInfo.getFieldName(),mockHandler(sourceList,fieldNewParamInfo.getRowId(),1));
                            break;
                        case 6:
                            mockChildFieldMap.put(fieldNewParamInfo.getFieldName(),mockHandler(sourceList,fieldNewParamInfo.getRowId(),2));
                            break;
                    }

                    /*if(fieldNewParamInfo.getDataType() == 1||
                            fieldNewParamInfo.getDataType() == 2||
                            fieldNewParamInfo.getDataType() == 3||
                            fieldNewParamInfo.getDataType() == 4){
                        mockChildFieldMap.put(fieldNewParamInfo.getFieldName(),fieldNewParamInfo.getFieldValue());
                    }else if(fieldNewParamInfo.getDataType() == 5){
                        mockChildFieldMap.put(fieldNewParamInfo.getFieldName(),mockHandler(sourceList,fieldNewParamInfo.getRowId(),1));
                    }else if(fieldNewParamInfo.getDataType() == 6){
                        mockChildFieldMap.put(fieldNewParamInfo.getFieldName(),mockHandler(sourceList,fieldNewParamInfo.getRowId(),2));
                    }*/
                }
            }

            return mockChildFieldMap;

        }else if(ParentDataType == 2){
            List<Object> objects = new ArrayList<>();
            for(FieldNewParamInfo fieldNewParamInfo : sourceList){

                if(fieldNewParamInfo.getFatherId().intValue()==parentId.intValue()){
                    switch (fieldNewParamInfo.getDataType()){
                        case 1:
                            objects.add(fieldNewParamInfo.getFieldValue());
                            break;
                        case 2:

                            objects.add(Integer.parseInt(fieldNewParamInfo.getFieldValue()));
                            break;
                        case 3:

                            objects.add(Float.parseFloat(fieldNewParamInfo.getFieldValue()));
                            break;
                        case 4:

                            objects.add(Boolean.parseBoolean(fieldNewParamInfo.getFieldValue()));

                            break;
                        case 5:
                            objects.add(mockHandler(sourceList,fieldNewParamInfo.getRowId(),1));
                            break;
                        case 6:
                            objects.add(mockHandler(sourceList,fieldNewParamInfo.getRowId(),2));
                            break;
                    }
                }

            }
            return objects;
        }else {
            return null;
        }

    }

}
