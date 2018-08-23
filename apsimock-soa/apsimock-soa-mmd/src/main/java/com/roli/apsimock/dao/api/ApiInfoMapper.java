package com.roli.apsimock.dao.api;

import com.roli.apsimock.model.api.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/3/27 下午8:00
 */
public interface ApiInfoMapper
{

    //新增一个接口
    public void addApi(ApiInfoOV apiInfoOV);

    //根据项目id查询接口
    public List<ApiInfo> queryApiInfoByProjectId(@Param("projectid") Integer projectid);

    //根据接口名称查询接口
    public ApiInfo queryApiInfoByName(@Param("apiName") String apiName);

    //根据接口url查询接口
    public ApiInfo queryApiInfoByUrlPath(@Param("urlPath") String urlPath);

    //删除一个接口（软删除，设置接口为非激活状态）
    public void deleteApiInfoByName(@Param("apiName") String apiName);

    //根据APIid查询接口
    public ApiInfo queryApiInfoByApiId(@Param("apiid") Integer apiid);

    //根据apiid查询所有的请求行数据
    public ApiInfo queryReqParamsByApiId(@Param("apiid") Integer apiid);

    //更新API信息
    public void updateApiInfo(@Param("tag") String tag, @Param("field") String field, @Param("apiid") Integer apiid);

    //根据apiid查询指定字段所有数据
    public List<String> queryAppointFieldByApiId(@Param("tag")String tag,@Param("apiid")Integer apiid);

    //查询
}
