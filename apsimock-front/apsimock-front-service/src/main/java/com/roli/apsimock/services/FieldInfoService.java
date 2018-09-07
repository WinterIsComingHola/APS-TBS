package com.roli.apsimock.services;

import com.roli.apsimock.model.api.FieldNew;
import com.roli.apsimock.model.api.FieldNewParamInfo;
import com.roli.common.exception.BusinessException;
import com.ruoli.soa.model.ResultSoaRest;

import java.util.List;
import java.util.Map;

public interface FieldInfoService {


    //查询行数据的id最大值
    public ResultSoaRest queryFieldRowMaxId();

    //查询当前节点的所有子节点
    public ResultSoaRest queryAllChildNodeId(Integer currentId);

    //查询当前apiid下的root节点的id
    public ResultSoaRest queryRootNodeIdByApiId(Integer apiId);

    //查询当前apiid下的root节点type
    public ResultSoaRest queryRootNodeTypeByApiId(Integer apiId);

    //新增节点数据
    public ResultSoaRest insertNodeData(FieldNew fieldNew);

    //查询当前节点的父节点
    public ResultSoaRest queryFatherNodeId(Integer currentId);

    //根据当前的apiid查询所有的mock字段
    public List<FieldNewParamInfo> queryAllFieldByApiId(Integer apiId);

    //更新域值信息
    public ResultSoaRest updateFieldValue(FieldNewParamInfo fieldNewParamInfo);

    //更新RowId数据信息
    public ResultSoaRest updateFieldRowDataType(FieldNewParamInfo fieldNewParamInfo);

    //删除数据信息
    public ResultSoaRest deleteMockData(List<String> rowIds);

    //执行mockserver
    public Object runMockServer(String url) throws BusinessException;

}
