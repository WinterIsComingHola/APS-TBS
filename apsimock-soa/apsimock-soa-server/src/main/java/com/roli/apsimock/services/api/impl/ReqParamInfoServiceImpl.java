package com.roli.apsimock.services.api.impl;

import com.roli.apsimock.dao.api.ApiInfoMapper;
import com.roli.apsimock.dao.api.ReqParamMapper;
import com.roli.apsimock.model.api.*;
import com.roli.apsimock.services.api.ReqParamInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.exception.CustomAssert;
import com.roli.common.model.enums.ErrorsEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/5/24 下午4:46
 */

@Service
public class ReqParamInfoServiceImpl implements ReqParamInfoService{

    @Resource
    ReqParamMapper reqParamMapper;
    @Resource
    ApiInfoMapper apiInfoMapper;

    /**
     * @Description: 根据apiid查询请求参数的数据
     * @param  apiid
     * @return List
     * @throws  BusinessException
     * @author xuxinyu
     * @date 2018/7/6 10:59
     */
    @Override
    public List<ParamInfo> queryParamInfoByApiId(Integer apiid )throws BusinessException{

        CustomAssert.isNotNull(apiid, ErrorsEnum.OBJECT_NULL);

        List<ParamInfo> paramInfos = new ArrayList<>();
        ApiInfo apiInfo = apiInfoMapper.queryReqParamsByApiId(apiid);

        if(apiInfo == null){
            BusinessException.throwMessage(ErrorsEnum.REQPARAM_NOT_EXIST);
        }

        List<ReqRowData> reqRowDatas = apiInfo.getReqRowDatas();

        for(ReqRowData reqRowData:reqRowDatas){
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setBotyType(reqRowData.getBodyType());
            paramInfo.setParamType(reqRowData.getParamType());
            paramInfo.setRowId(reqRowData.getId());

            ReqRowData newReqRowData =  reqParamMapper.queryReqParamField(reqRowData.getId());

            List<ParamDetail> paramDetails = newReqRowData.getParamDetails();
            for(ParamDetail paramDetail:paramDetails){

                switch(paramDetail.getFieldType())
                {
                    case 1:paramInfo.setFieldName(paramDetail.getFieldValue());
                        break;
                    case 2:paramInfo.setFieldValue(paramDetail.getFieldValue());
                        break;
                    case 3:paramInfo.setFieldDesc(paramDetail.getFieldValue());
                        break;
                    case 4:paramInfo.setRawBody(paramDetail.getFieldValue());
                        break;

                    default:
                        BusinessException.throwMessage(ErrorsEnum.FIELD_TYPE_UNEXCEPTION);
                }

            }
            //paramInfos组装完毕，存入队列
            paramInfos.add(paramInfo);
        }

        return paramInfos;

    }


    /**
     * @Description: 根据rowid和fieldType更新对应的参数值
     * @param paramInfo
     * @return
     * @throws BusinessException
     * @author xuxinyu
     * @date 2018/7/6 11:09
     */
    @Override
    @Transactional
    public void updateParamField(ParamInfo paramInfo) throws BusinessException {

        CustomAssert.isNotNull(paramInfo, ErrorsEnum.OBJECT_NULL);
        Integer rowId = paramInfo.getRowId();
        Integer fieldType = paramInfo.getFieldType();

        String newField = null;

        switch (fieldType)
        {
            case 1:
                newField = paramInfo.getFieldName();
                break;
            case 2:
                newField = paramInfo.getFieldValue();
                break;
            case 3:
                newField = paramInfo.getFieldDesc();
                break;
            case 4:
                newField = paramInfo.getRawBody();
                break;
            default:
                BusinessException.throwMessage(ErrorsEnum.FIELD_TYPE_UNEXCEPTION);
        }


        reqParamMapper.updateReqParamField(rowId,fieldType,newField);
    }

    @Override
    public Integer queryParamMaxId(){

        if(reqParamMapper.queryReqMaxRowId() != null){
            return reqParamMapper.queryReqMaxRowId();
        }else{
            return 31014;
        }
    }

    @Override
    @Transactional
    public void insertParamRow(ReqRowData reqRowData) throws BusinessException {
        CustomAssert.isNotNull(reqRowData, ErrorsEnum.OBJECT_NULL);
        reqParamMapper.insertReqRow(reqRowData);

        ParamDetail paramDetail = new ParamDetail();
        paramDetail.setRowdataId(reqRowData.getId());
        paramDetail.setCreateTime(LocalDateTime.now());

        List<Integer> listNumber = new ArrayList<>();


        if(reqRowData.getBodyType()==1){

            if(reqRowData.getParamType()==1||reqRowData.getParamType()==2){
                listNumber.add(1);
                listNumber.add(2);
                listNumber.add(3);
            }else if(reqRowData.getParamType()==3){
                listNumber.add(1);
                listNumber.add(2);
            }

        }else if(reqRowData.getBodyType()==2){
            if(reqRowData.getParamType()==1){
                listNumber.add(4);
            }else if(reqRowData.getParamType()==2){
                listNumber.add(1);
                listNumber.add(2);
                listNumber.add(3);
            }else if(reqRowData.getParamType()==3){
                listNumber.add(1);
                listNumber.add(2);
            }
        }


        reqParamMapper.insertParamField(listNumber,paramDetail);

    }

    @Override
    @Transactional
    public void deleteParamRow(Integer rowId) throws BusinessException {

        CustomAssert.isNotNull(rowId, ErrorsEnum.OBJECT_NULL);
        reqParamMapper.deleteReqRow(rowId);
        reqParamMapper.deleteParamField(rowId);

    }

    @Override
    public Integer queryBodyTypeByApiId(Integer apiId) throws BusinessException{

        CustomAssert.isNotNull(apiId, ErrorsEnum.OBJECT_NULL);
        return reqParamMapper.queryBodyTypeByApiid(apiId);

    }

}
