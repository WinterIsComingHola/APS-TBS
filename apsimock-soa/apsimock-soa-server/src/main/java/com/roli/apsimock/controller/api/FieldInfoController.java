package com.roli.apsimock.controller.api;


import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.*;
import com.roli.apsimock.services.api.FieldInfoService;
import com.roli.common.exception.BaseRunTimeException;
import com.roli.common.exception.BusinessException;
import com.roli.common.model.enums.ErrorsEnum;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.annotation.SoaRestAuth;
import com.ruoli.soa.model.ResultSoaRest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class FieldInfoController {

    private static final Logger logger = LoggerFactory.getLogger(FieldInfoController.class);


    @Resource
    FieldInfoService fieldInfoService;

    @RequestMapping(value = "/aps/rest/api/addFieldInfo",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest addFieldInfo(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                FieldInfoOV fieldInfoOV = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),FieldInfoOV.class);
                fieldInfoService.addField(fieldInfoOV);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("新增字段成功");
            }catch (BusinessException e){
                logger.error("/aps/rest/api/addFieldInfo 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (Exception e){
                logger.error("/aps/rest/api/addFieldInfo 内部处理异常",e);
                resultSoaRest.setState(501);
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }

    @RequestMapping(value = "/aps/rest/api/queryAllFieldByApiName",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryAllFieldByApiName(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                String apiName = apsSoaParam.getBusinessParam();
                List<FieldInfo> fieldInfos = fieldInfoService.queryAllField(apiName);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("fieldInfos",fieldInfos);
            } catch (Exception e){
                logger.error("/aps/rest/api/queryAllFieldByApiName 内部处理异常",e);
                resultSoaRest.setState(501);
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * @Description 查询Field行数据的id最大值
     * **/
    @RequestMapping(value = "/aps/rest/api/queryFieldRowMaxId",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryFieldRowMaxId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                Integer maxId = fieldInfoService.queryFieldRowMaxId();

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("maxId",maxId);
            } catch (Exception e){
                logger.error("/aps/rest/api/queryFieldRowMaxId 内部处理异常",e);
                resultSoaRest.setState(501);
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * @Description 查询当前节点下的所有字节点id
     * **/
    @RequestMapping(value = "/aps/rest/api/queryAllChildNodeId",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryAllChildNodeId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                Integer currentId = Integer.parseInt(apsSoaParam.getBusinessParam());

                List<Integer> childIds = fieldInfoService.queryAllChildNodeId(currentId);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("childIds",childIds);
            } catch (BusinessException e){
                logger.error("/aps/rest/api/queryAllChildNodeId 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * @Description 查询当前APIID下的root节点id
     * **/
    @RequestMapping(value = "/aps/rest/api/queryrootnodeid",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryRootNodeIdByApiId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                Integer apiId = Integer.parseInt(apsSoaParam.getBusinessParam());

                Integer rootId = fieldInfoService.queryRootNodeIdByApiId(apiId);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("rootId",rootId);
            } catch (BusinessException e){
                logger.error("/aps/rest/api/queryrootnodeid 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * @Description 查询当前APIID下的root节点id
     * **/
    @RequestMapping(value = "/aps/rest/api/queryrootnodetype",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryRootNodeTypeByApiId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                Integer apiId = Integer.parseInt(apsSoaParam.getBusinessParam());

                Integer rootType = fieldInfoService.queryRootNodeTypeByApiId(apiId);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("rootType",rootType);
            } catch (BusinessException e){
                logger.error("/aps/rest/api/queryrootnodetype 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }

    /**
     * @Description 新增Node节点
     * **/
    @RequestMapping(value = "/aps/rest/api/insertnodedata",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest insertNodeData(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                FieldNew fieldNew = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),FieldNew.class);

                fieldInfoService.insertNodeData(fieldNew);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("新增节点成功");
            } catch (BusinessException e){
                logger.error("/aps/rest/api/insertnodedata 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }

    /**
     * @Description 查询当前节点的父节点
     * **/
    @RequestMapping(value = "/aps/rest/api/queryfatherid",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryFatherId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                Integer currentId = Integer.parseInt(apsSoaParam.getBusinessParam());

                Integer fatherId = fieldInfoService.queryFatherNodeId(currentId);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("fatherId",fatherId);
            } catch (BusinessException e){
                logger.error("/aps/rest/api/queryfatherid 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * @Description 根据Apiid查询当前接口下的所有的mock字段
     * **/
    @RequestMapping(value = "/aps/rest/api/queryallfield",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryAllFieldByApiId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                Integer apiId = Integer.parseInt(apsSoaParam.getBusinessParam());

                List<FieldNew> fieldNews = fieldInfoService.queryAllFieldByApiID(apiId);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("fieldNews",fieldNews);
            } catch (BusinessException e){
                logger.error("/aps/rest/api/queryallfield 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * @Description 根据url查询当前接口下的所有的mock字段
     * **/
    @RequestMapping(value = "/aps/rest/api/queryallfieldByUrl",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryAllFieldByUrl(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                String url = apsSoaParam.getBusinessParam();

                List<FieldNew> fieldNews = fieldInfoService.queryAllFieldByUrl(url);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("fieldNews",fieldNews);
            } catch (BusinessException e){
                logger.error("/aps/rest/api/queryallfieldByUrl 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


    /**
     * @Description 更新Field的域值
     * **/
    @RequestMapping(value = "/aps/rest/api/updatefieldvalue",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest updateFieldValue(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                FieldNewParamInfo fieldNewParamInfo = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),FieldNewParamInfo.class);

                fieldInfoService.updateFieldValue(fieldNewParamInfo);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("更新域值数据成功");
            } catch (BusinessException e){
                logger.error("/aps/rest/api/updatefieldvalue 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }

    /**
     * @Description 更新Row的行数据类型
     * **/
    @RequestMapping(value = "/aps/rest/api/updatefieldrowdatatype",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest updateFieldRowDataType(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                FieldNewParamInfo fieldNewParamInfo = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),FieldNewParamInfo.class);

                fieldInfoService.updateFileRowDataType(fieldNewParamInfo);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("更新域值数据成功");
            } catch (BusinessException e){
                logger.error("/aps/rest/api/updatefieldrowdatatype 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }

    /**
     * @Description 删除数据
     * **/
    @RequestMapping(value = "/aps/rest/api/deletemockdata",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest deleteMockData(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                List<Integer> rowIds = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),List.class);

                fieldInfoService.deleteMockFieleData(rowIds);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("删除数据成功");
            } catch (BusinessException e){
                logger.error("/aps/rest/api/deletemockdata 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }

    /**
     * @Description 根据URL查询当前APIID下的root节点id
     * **/
    @RequestMapping(value = "/aps/rest/api/queryrootnodeidByUrl",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryrootnodeidByUrl(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                Map<String,String> strMap = JacksonUtils.str2map(apsSoaParam.getBusinessParam());

                String url = strMap.get("url");
                Integer isRootId =  Integer.parseInt(strMap.get("isRootId"));

                Integer rootInfo = fieldInfoService.queryRootInfoByApiUrl(url,isRootId);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("rootInfo",rootInfo);
            } catch (BusinessException e){
                logger.error("/aps/rest/api/queryrootnodeidByUrl 处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else{
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }
        return resultSoaRest;
    }


}
