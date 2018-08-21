package com.roli.apsimock.services.api;

import com.roli.apsimock.model.api.ParamInfo;
import com.roli.apsimock.model.api.ReqParamInfo;
import com.roli.apsimock.model.api.ReqRowData;
import com.roli.common.exception.BusinessException;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/5/24 下午4:45
 */
public interface ReqParamInfoService {

    public List<ParamInfo> queryParamInfoByApiId(Integer apiid )throws BusinessException;

    public void updateParamField(ParamInfo paramInfo) throws BusinessException;

    public Integer queryParamMaxId();

    public void insertParamRow(ReqRowData reqRowData) throws BusinessException;

    public void deleteParamRow(Integer rowId) throws BusinessException;

    public Integer queryBodyTypeByApiId(Integer apiId) throws BusinessException;
}
