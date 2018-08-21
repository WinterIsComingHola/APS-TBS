package com.roli.apsimock.dao.api;


import com.roli.apsimock.model.api.ParamDetail;
import com.roli.apsimock.model.api.ReqParamInfo;
import com.roli.apsimock.model.api.ReqRowData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/5/24 下午3:59
 */
public interface ReqParamMapper {

    //根据rowid查询所有的请求域值数据
    public ReqRowData queryReqParamField(@Param("paramRowid")Integer paramRowid);

    //根据rowid和fieldType更新对应的参数值
    public void updateReqParamField(@Param("paramRowid")Integer paramRowid,
                                    @Param("fieldType")Integer fieldType,
                                    @Param("fieldValue")String fieldValue);

    //查询rowid的最大值
    public Integer queryReqMaxRowId();

    ///查询当前api的bodyType
    public Integer queryBodyTypeByApiid(@Param("apiid")Integer apiid);

    //新增一条row数据
    public void insertReqRow(ReqRowData reqRowData);

    //新增field数据
    public void insertParamField(
            @Param("fieldTypeList")List<Integer> fieldTypeList,
            @Param("paramDetail")ParamDetail paramDetail);

    //删除一条row数据
    public void deleteReqRow(@Param("rowId") Integer rowId);
    //删除field数据
    public void deleteParamField(@Param("rowId") Integer rowId);
}
