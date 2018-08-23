package com.roli.apsimock.controller.api;

import com.github.pagehelper.PageHelper;
import com.roli.apsimock.common.utils.MapMultiAnalysis;
import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.ApiInfo;
import com.roli.apsimock.model.api.ApiInfoOV;
import com.roli.apsimock.services.api.ApiInfoService;
import com.roli.common.exception.BaseRunTimeException;
import com.roli.common.exception.BusinessException;
import com.roli.common.model.enums.ErrorsEnum;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.annotation.SoaRestAuth;
import com.ruoli.soa.model.Datagrid;
import com.ruoli.soa.model.ResultSoaRest;
import com.ruoli.soa.utils.PageFenYeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.codecs.perfield.PerFieldDocValuesFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/3/27 下午9:22
 */
@Controller
@RequestMapping("/")
public class ApiInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ApiInfoController.class);

    @Resource
    ApiInfoService apiInfoService;

    /**
     * 新增一个接口
    * @author xuxinyu
    * @date 2018/3/27 下午9:24
    */
    @RequestMapping(value = "/aps/rest/api/addApiInfo",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest addProjectInfo(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                ApiInfoOV apiInfoOV = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),ApiInfoOV.class);
                apiInfoService.addApi(apiInfoOV);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("新增接口成功");
            }catch (BusinessException e){
                logger.error("/aps/rest/api/addApiInfo 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (Exception e){
                logger.error("/aps/rest/api/addApiInfo 内部数据库处理异常",e);
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


    @RequestMapping(value = "/aps/rest/api/deleteApiInfo",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest deleteProjectInfo(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                String apiName = apsSoaParam.getBusinessParam();
                apiInfoService.deleteApi(apiName);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("删除接口成功");
            }catch (BusinessException e){
                logger.error("/aps/rest/api/addApiInfo 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (Exception e){
                logger.error("/aps/rest/api/addApiInfo 内部数据库处理异常",e);
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


    @RequestMapping(value = "/aps/rest/api/queryAllFieldByUrlPath",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryAllFieldByUrlPath(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                String urlPath = apsSoaParam.getBusinessParam();
                Map<String,Object> fieldMap = apiInfoService.queryAllFieldByUrlPath(urlPath);

                Map<String,Object> newFieldMap = new HashMap<>();
                Map<String,Object> targetMap = MapMultiAnalysis.handleMapWithNumKey(fieldMap,newFieldMap);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("fieldResponseMap",targetMap);
            }catch (BusinessException e){
                logger.error("/aps/rest/api/queryAllFieldByUrlPath 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }catch (Exception e){
                logger.error("/aps/rest/api/queryAllFieldByUrlPath 内部数据库处理异常",e);
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

    @RequestMapping(value = "/aps/rest/api/queryApiByProjectid",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryApiByProjectid(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                Map<String, String> mapParam = JacksonUtils.str2map(apsSoaParam.getBusinessParam());
                String projectid = mapParam.get("projectid");
                int pageNum = Integer.parseInt(mapParam.get("pageNum"));
                int pageSize = Integer.parseInt(mapParam.get("pageSize"));

                PageHelper.startPage(pageNum,pageSize);
                List<ApiInfo> apiInfos = apiInfoService.queryApiByProjectid(Integer.parseInt(projectid));
                PageFenYeUtils<ApiInfo> pageFenYeUtils = new PageFenYeUtils<>();
                Datagrid datagrid = pageFenYeUtils.pageFenYeHandle(apiInfos);

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("apiinfos",datagrid.getList());
                resultSoaRest.addAttribute("total",datagrid.getTotal());
            }catch (BusinessException e){
                logger.error("/aps/rest/api/queryApiByProjectid 业务处理异常",e);
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

    @RequestMapping(value = "/aps/rest/api/queryApiInfoByApiId",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryApiInfoByApiId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                String apiid = apsSoaParam.getBusinessParam();
                ApiInfo apiInfo = apiInfoService.queryApiInfoByApiId(Integer.parseInt(apiid));

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("apiinfo",apiInfo);
            }catch (BusinessException e){
                logger.error("/aps/rest/api/queryApiInfoByApiId 业务处理异常",e);
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

    @RequestMapping(value = "/aps/rest/api/updateApiInfo", method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest updateApiInfo(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();

        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{

                Map<String,String> map = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),Map.class);
                String tag = map.get("tag");
                String field = map.get("field");
                String apiid = map.get("apiid");
                if(tag.equals("1")||tag.equals("2")){
                    apiInfoService.queryAppointFieldByApiId(tag, field, Integer.parseInt(apiid));
                }
                apiInfoService.updateApiInfo(tag,field,Integer.parseInt(apiid));

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("更新接口成功");
            }catch (BusinessException e){
                logger.error("/aps/rest/api/updateApiInfo 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());

            }catch (Exception e){
                logger.error("/aps/rest/api/updateApiInfo 内部数据库处理异常",e);
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
}
