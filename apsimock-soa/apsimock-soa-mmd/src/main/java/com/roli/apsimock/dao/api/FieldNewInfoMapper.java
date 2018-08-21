package com.roli.apsimock.dao.api;

import com.roli.apsimock.model.api.FieldDetail;
import com.roli.apsimock.model.api.FieldNew;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface FieldNewInfoMapper {

    //新增root节点，传入apiName为了获取对应api的id值
    public void insertRootField(@Param("apiName")String apiName);

    //查询行数据的最大节点
    public Integer queryFieldRowDataMaxId();

    //查询当前节点id下面的所有子节点
    public List<Integer> queryAllChildNodeId(@Param("currentId")Integer currentId);

    //查询当前apiId下的root节点id
    public Integer queryRootNodeIdByApiId(@Param("apiId")Integer apiId);

    //查询当前apiId下的root节点type
    public Integer queryRootNodeTypeByApiId(@Param("apiId")Integer apiId);

    //新增行数据
    public void insertRowData(FieldNew fieldNew);
    //新增行数据的每个域值
    public void insertRowDataDetail(@Param("fieldTypeList")List<Integer> fieldTypeList,
                                    @Param("fieldRowId")Integer fieldRowId);

    //查询当前节点的父节点Id
    public Integer queryFatherNodeId(@Param("currentId")Integer currentId);

    //根据Apiid查询当前接口下的所有mock字段
    public List<FieldNew> queryAllFieldInfoByApiId(@Param("apiId")Integer apiId);

    //更新field的最新的域值
    public void updateFieldValue(@Param("newValue")String newValue,
                                 @Param("rowDataId")Integer rowDataId,
                                 @Param("fieldType")Integer fieldType);

    //更新MOCK字段的数据类型（匹配前台的下拉列表）
    public void updateFileRowDataType(@Param("rowDataId")Integer rowDataId,
                                      @Param("rowDataType")Integer rowDataType);


    //删除一条MockRow数据
    public void deleteFieldRowData(@Param("rowIds")List<Integer> rowIds);

    //删除Row对应的Field值数据
    public void deleteFieldValueData(@Param("rowIds")List<Integer> rowIds);

    //查询当前url路径下所有的MOCK字段
    public List<FieldNew> queryAllFieldInfoByUrl(@Param("url")String url);

    //根據接口的url查詢对应的rootid
    public Integer queryRootInfoByApiUrl(@Param("url")String url,
                                         @Param("isRootId")Integer isRootId);



}
