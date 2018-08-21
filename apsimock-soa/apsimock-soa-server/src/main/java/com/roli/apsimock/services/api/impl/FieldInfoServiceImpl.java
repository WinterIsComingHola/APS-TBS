package com.roli.apsimock.services.api.impl;

import com.roli.apsimock.dao.api.FieldInfoMapper;
import com.roli.apsimock.dao.api.FieldNewInfoMapper;
import com.roli.apsimock.model.api.FieldInfo;
import com.roli.apsimock.model.api.FieldInfoOV;
import com.roli.apsimock.model.api.FieldNew;
import com.roli.apsimock.model.api.FieldNewParamInfo;
import com.roli.apsimock.services.api.FieldInfoService;
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
 * @date 2018/4/3 下午8:51
 */
@Service
@Transactional
public class FieldInfoServiceImpl implements FieldInfoService{

    @Resource
    FieldInfoMapper fieldInfoMapper;
    @Resource
    FieldNewInfoMapper fieldNewInfoMapper;

    @Override
    public void addField(FieldInfoOV fieldInfoOV) throws BusinessException {

        CustomAssert.isNotNull(fieldInfoOV, ErrorsEnum.OBJECT_NULL);
       // CustomAssert.isNotEmpty(fieldInfoOV.getFieldName(), ErrorsEnum.FIELD_NULL);
        CustomAssert.isNotNull(fieldInfoOV.getFieldType(),ErrorsEnum.FIELDTYPE_NULL);
        CustomAssert.isNotNull(fieldInfoOV.getIsRoot(),ErrorsEnum.FIELDISROOT_NULL);
        CustomAssert.isNotEmpty(fieldInfoOV.getApiName(),ErrorsEnum.APINAME_NULL);


        if(fieldInfoOV.getFieldType()==5||fieldInfoOV.getFieldType()==6){
            CustomAssert.isNull(fieldInfoOV.getFieldValue(),ErrorsEnum.FIELD_NOTNULL);
        }/*else{
            CustomAssert.isNotNull(fieldInfoOV.getFieldValue(),ErrorsEnum.FIELDVALUE_NULL);
        }*/
        if(fieldInfoOV.getIsRoot()==1){
            CustomAssert.isNull(fieldInfoOV.getFatherId(),ErrorsEnum.FIELD_NOTNULL);
            CustomAssert.isNull(fieldInfoOV.getFatherType(),ErrorsEnum.FIELD_NOTNULL);
        }else{
            CustomAssert.isNull(fieldInfoOV.getRootType(),ErrorsEnum.FIELD_NOTNULL);

        }
        if(fieldInfoOV.getFatherType()!=5&&fieldInfoOV.getFatherType()!=6){
                BusinessException.throwMessage(ErrorsEnum.FATHER_TYPE_ERROR);
        }
        //List中的数据是没有Name的
        if(fieldInfoOV.getFatherType()==6){
            CustomAssert.isEmpty(fieldInfoOV.getFieldName(),ErrorsEnum.FIELD_NOT_NULL);
        }

        /*List<FieldInfo> fieldInfos = fieldInfoMapper.queryFieldByName(fieldInfoOV.getFieldName(),fieldInfoOV.getApiName());
        if(fieldInfos.size()!=0){
            BusinessException.throwMessage(ErrorsEnum.FIELD_DUPLICATE);
        }*/

        LocalDateTime localDateTime = LocalDateTime.now();
        fieldInfoOV.setCreateTime(localDateTime);

        fieldInfoMapper.addField(fieldInfoOV);

    }

    @Override
    public List<FieldInfo> queryAllField(String ApiName) throws BusinessException {

        CustomAssert.isNotEmpty(ApiName,ErrorsEnum.APINAME_NULL);

        String fieldName = null;

        List<FieldInfo> fieldInfos = fieldInfoMapper.queryFieldByName(fieldName,ApiName);
        return fieldInfos;
    }

    @Override
    public Integer queryFieldRowMaxId(){
        return fieldNewInfoMapper.queryFieldRowDataMaxId();
    }

    @Override
    public List<Integer> queryAllChildNodeId(Integer currentId) throws BusinessException {

        CustomAssert.isNotNull(currentId,ErrorsEnum.OBJECT_NULL);

        //查询当前节点下所有的子节点，需要明确的是。如果子节点下仍然有子节点，则需要继续查询下去直到子节点下没有任何节点为止
        List<Integer> listId = fieldNewInfoMapper.queryAllChildNodeId(currentId);
        return getAllChildId(listId);

    }

    @Override
    public Integer queryRootNodeIdByApiId(Integer apiId) throws BusinessException {

        CustomAssert.isNotNull(apiId,ErrorsEnum.OBJECT_NULL);
        Integer rootId = fieldNewInfoMapper.queryRootNodeIdByApiId(apiId);
        return rootId;
    }

    @Override
    public Integer queryRootNodeTypeByApiId(Integer apiId) throws BusinessException{
        CustomAssert.isNotNull(apiId,ErrorsEnum.OBJECT_NULL);
        Integer rootType = fieldNewInfoMapper.queryRootNodeTypeByApiId(apiId);
        return rootType;
    }

    @Override
    @Transactional
    public void insertNodeData(FieldNew fieldNew) throws BusinessException {
        CustomAssert.isNotNull(fieldNew, ErrorsEnum.OBJECT_NULL);

        fieldNewInfoMapper.insertRowData(fieldNew);

        List<Integer> fieldTypeList = new ArrayList<>();
        fieldTypeList.add(1);
        fieldTypeList.add(2);
        fieldTypeList.add(3);

        Integer fieldRowId = fieldNew.getId();

        fieldNewInfoMapper.insertRowDataDetail(fieldTypeList,fieldRowId);

    }

    @Override
    public Integer queryFatherNodeId(Integer currenrId) throws BusinessException {

        CustomAssert.isNotNull(currenrId, ErrorsEnum.OBJECT_NULL);
        Integer fatherId = fieldNewInfoMapper.queryFatherNodeId(currenrId);
        return  fatherId;

    }

    @Override
    public List<FieldNew> queryAllFieldByApiID(Integer apiId) throws BusinessException {
        CustomAssert.isNotNull(apiId, ErrorsEnum.OBJECT_NULL);
        List<FieldNew> fieldNews = fieldNewInfoMapper.queryAllFieldInfoByApiId(apiId);
        return fieldNews;

    }

    @Override
    @Transactional
    public void updateFieldValue(FieldNewParamInfo fieldNewParamInfo) throws BusinessException {
        CustomAssert.isNotNull(fieldNewParamInfo, ErrorsEnum.OBJECT_NULL);

        Integer rowId = fieldNewParamInfo.getRowId();
        Integer fieldType = fieldNewParamInfo.getFieldType();
        String newFieldValue = null;
        switch (fieldType){
            case 1:
                newFieldValue = fieldNewParamInfo.getFieldName();
                break;
            case 2:
                newFieldValue = fieldNewParamInfo.getFieldValue();
                break;
            case 3:
                newFieldValue = fieldNewParamInfo.getFieldDesc();
                break;
            default:
                BusinessException.throwMessage(ErrorsEnum.FIELD_TYPE_UNEXCEPTION);
        }

        fieldNewInfoMapper.updateFieldValue(newFieldValue,rowId,fieldType);
    }

    @Override
    @Transactional
    public void updateFileRowDataType(FieldNewParamInfo fieldNewParamInfo) throws BusinessException {
        CustomAssert.isNotNull(fieldNewParamInfo, ErrorsEnum.OBJECT_NULL);
        Integer rowId = fieldNewParamInfo.getRowId();
        Integer dataType = fieldNewParamInfo.getDataType();

        fieldNewInfoMapper.updateFileRowDataType(rowId,dataType);
    }

    @Override
    @Transactional
    public void deleteMockFieleData(List<Integer> rowIds) throws BusinessException {
        CustomAssert.isNotNull(rowIds, ErrorsEnum.OBJECT_NULL);
        fieldNewInfoMapper.deleteFieldRowData(rowIds);
        fieldNewInfoMapper.deleteFieldValueData(rowIds);
    }

    @Override
    public List<FieldNew> queryAllFieldByUrl(String url) throws BusinessException {
        CustomAssert.isNotNull(url, ErrorsEnum.OBJECT_NULL);
        List<FieldNew> fieldNews = fieldNewInfoMapper.queryAllFieldInfoByUrl(url);
        return fieldNews;
    }

    @Override
    public Integer queryRootInfoByApiUrl(String url,Integer isRootId) throws BusinessException {
        CustomAssert.isNotNull(url, ErrorsEnum.OBJECT_NULL);
        return fieldNewInfoMapper.queryRootInfoByApiUrl(url,isRootId);
    }




    private List<Integer> getAllChildId(List<Integer> handleList){

        List<Integer> resultList = new ArrayList<>();

        if(handleList.size()==0){
            return handleList;
        }else{
            for (Integer id: handleList){
                resultList.add(id);
                resultList.addAll(getAllChildId(fieldNewInfoMapper.queryAllChildNodeId(id)));
            }
            return resultList;
        }

    }


}
