package com.roli.apsimock.services.api;

import com.roli.apsimock.model.api.FieldInfo;
import com.roli.apsimock.model.api.FieldInfoOV;
import com.roli.apsimock.model.api.FieldNew;
import com.roli.apsimock.model.api.FieldNewParamInfo;
import com.roli.common.exception.BusinessException;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/4/3 下午8:50
 */
public interface FieldInfoService {

    public void addField(FieldInfoOV fieldInfoOV) throws BusinessException;

    public List<FieldInfo> queryAllField(String ApiName) throws BusinessException;

    //查询field行数据的最大值
    public Integer queryFieldRowMaxId();

    //查询当前节点下的所有节点
    public List<Integer> queryAllChildNodeId(Integer currentId) throws BusinessException;

    //查询当前APIId下的root节点
    public Integer queryRootNodeIdByApiId(Integer apiId) throws BusinessException;

    //查询当前APIID下的root；类型
    public Integer queryRootNodeTypeByApiId(Integer apiId) throws BusinessException;

    //新增节点
    public void insertNodeData(FieldNew fieldNew) throws BusinessException;

    //查询当前节点的父节点
    public Integer queryFatherNodeId(Integer currenrId) throws BusinessException;

    //根据apiid查询所有的mock字段
    public List<FieldNew> queryAllFieldByApiID(Integer apiId) throws BusinessException;

    //更新对应域值的字段值
    public void updateFieldValue(FieldNewParamInfo fieldNewParamInfo) throws BusinessException;

    //更新row行数据的数据类型
    public void updateFileRowDataType(FieldNewParamInfo fieldNewParamInfo) throws BusinessException;

    //删除Row数据的数据类型
    public void deleteMockFieleData(List<Integer> rowIds) throws BusinessException;

    //根据APIUrl查询所有的Mock字段
    public List<FieldNew> queryAllFieldByUrl(String url) throws BusinessException;

    //根据APIURL查询对应接口的rootid
    public Integer queryRootInfoByApiUrl(String url,Integer isRootId) throws BusinessException;
}
