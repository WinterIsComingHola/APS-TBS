package com.roli.apsimock.controller.api;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.ParamInfo;
import com.roli.apsimock.model.api.ReqParamInfo;
import com.roli.apsimock.model.api.ReqRowData;
import com.roli.apsimock.services.api.ReqParamInfoService;
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

/**
 * @author xuxinyu
 * @date 2018/5/24 下午4:54
 */

@Controller
@RequestMapping("/")
public class ReqParamController {

    private static final Logger logger = LoggerFactory.getLogger(ApiInfoController.class);

    @Resource
    ReqParamInfoService reqParamInfoService;


    /**
     * @Description: 根据apiid查询所有的请求参数数据
     * @param
     * @return ResultSoaRest
     * @throws
     * @author xuxinyu
     * @date 2018/7/5 11:57
     */
    @RequestMapping(value = "/aps/rest/reqparam/queryreqparam",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryParamByApiId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                String apiid = apsSoaParam.getBusinessParam();
                List<ParamInfo> paramInfos = reqParamInfoService.queryParamInfoByApiId(Integer.parseInt(apiid));

                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("reqparams",paramInfos);
            }catch (BusinessException e){
                logger.error("/aps/rest/api/queryreqparam 业务处理异常",e);
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

    @RequestMapping(value = "/aps/rest/reqparam/updatereqparam",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest updateParamField(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            try{
                ParamInfo paramInfo = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),ParamInfo.class);
                reqParamInfoService.updateParamField(paramInfo);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("更新参数值成功");
            }catch (BusinessException e){
                logger.error("/aps/rest/reqparam/updatereqparam 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else {
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }

        return resultSoaRest;

    }



    @RequestMapping(value = "/aps/rest/reqparam/queryparammaxid",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryParamMaxId(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){
            Integer maxId = reqParamInfoService.queryParamMaxId();

            resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
            resultSoaRest.setSuccess(true);
            resultSoaRest.addAttribute("maxId",maxId);
        }else {
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }

        return resultSoaRest;

    }


    @RequestMapping(value = "/aps/rest/reqparam/insertparamrow",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest insertParamRow(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            ReqRowData reqRowData = JacksonUtils.fromJson(apsSoaParam.getBusinessParam(),ReqRowData.class);

            try {
                reqParamInfoService.insertParamRow(reqRowData);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("添加数据成功");
            } catch (BusinessException e) {
                logger.error("/aps/rest/reqparam/insertparamrow 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else {
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }

        return resultSoaRest;

    }

    @RequestMapping(value = "/aps/rest/reqparam/deleteparamrow",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest deleteParamRow(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            Integer reqRowId = Integer.parseInt(apsSoaParam.getBusinessParam());

            try {
                reqParamInfoService.deleteParamRow(reqRowId);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.setMessage("删除数据成功");
            } catch (BusinessException e) {
                logger.error("/aps/rest/reqparam/deleteparamrow 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else {
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }

        return resultSoaRest;

    }


    @RequestMapping(value = "/aps/rest/reqparam/querybodytype",method = RequestMethod.POST)
    @ResponseBody
    @SoaRestAuth(ApsSoaParam.class)
    public ResultSoaRest queryBodyType(){
        ResultSoaRest resultSoaRest = new ResultSoaRest();
        ApsSoaParam apsSoaParam = ApsSoaParam.getInstance(ApsSoaParam.class);
        if(apsSoaParam != null && StringUtils.isNoneBlank(apsSoaParam.getBusinessParam())){

            Integer apiId = Integer.parseInt(apsSoaParam.getBusinessParam());

            try {
                Integer bodyType =  reqParamInfoService.queryBodyTypeByApiId(apiId);
                resultSoaRest.setState(ErrorsEnum.SUCCESS.getErrorCode());
                resultSoaRest.setSuccess(true);
                resultSoaRest.addAttribute("bodyType",bodyType);
            } catch (BusinessException e) {
                logger.error("/aps/rest/reqparam/querybodytype 业务处理异常",e);
                resultSoaRest.setState(Integer.parseInt(e.getErrCode()));
                resultSoaRest.setSuccess(false);
                resultSoaRest.setMessage(e.getMessage());
            }
        }else {
            resultSoaRest.setState(ErrorsEnum.PARAM_NULL.getErrorCode());
            resultSoaRest.setSuccess(false);
            resultSoaRest.setMessage(ErrorsEnum.PARAM_NULL.getMessage());
        }

        return resultSoaRest;

    }

}
