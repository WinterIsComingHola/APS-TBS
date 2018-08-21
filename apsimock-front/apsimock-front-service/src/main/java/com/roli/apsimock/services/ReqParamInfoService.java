package com.roli.apsimock.services;

import com.roli.apsimock.model.api.ParamInfo;
import com.roli.apsimock.model.api.ReqParamInfo;
import com.roli.apsimock.model.api.ReqPostMan;
import com.roli.apsimock.model.api.ReqRowData;
import com.roli.common.exception.BusinessException;
import com.ruoli.soa.model.ResultSoaRest;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/5/24 下午5:02
 */
public interface ReqParamInfoService {

    //根据apiid查询请求参数
    public List<ParamInfo> queryParamInfoByApiId(String apiid) throws BusinessException;

    //根据rowid和fieldType更新对应的参数值
    public ResultSoaRest updateParamField(ParamInfo paramInfo) throws BusinessException;

    //查询最大的rowid
    public ResultSoaRest queryParamMaxId();

    //新增row数据
    public ResultSoaRest insettParamRow(ReqRowData reqRowData) throws BusinessException;

    //删除row数据
    public ResultSoaRest deleteParamRow(Integer rowId) throws BusinessException;

    //查询bodyType数据
    public ResultSoaRest queryBodyType(Integer apiId) throws BusinessException;

    //POSTMAN运行
    public ResultSoaRest runPostMan(String url,String httpMethod,Integer apiId,Integer formOrRaw) throws BusinessException;
}
