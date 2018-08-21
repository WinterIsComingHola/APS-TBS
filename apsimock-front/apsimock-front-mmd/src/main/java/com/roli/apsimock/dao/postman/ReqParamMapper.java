package com.roli.apsimock.dao.postman;

import com.roli.apsimock.model.api.ReqRowData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReqParamMapper {

    //根据传入的apiId，查询对应的请求参数数据

    public List<ReqRowData> queryRowFieldDataByApiId(@Param("apiId")Integer apiId);


}
