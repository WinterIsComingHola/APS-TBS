package com.roli.apsimock.controller.test;

import com.roli.aps.soa.client.service.ApiInfoClientService;
import com.roli.aps.soa.client.service.FieldInfoClientService;
import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.ApiInfoOV;
import com.roli.apsimock.model.api.FieldInfoOV;
import com.roli.apsimock.services.api.ApiInfoService;
import com.roli.apsimock.services.api.impl.ApiInfoServiceImpl;
import com.roli.common.exception.BusinessException;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/3/28 23:50
 */
@Controller
@RequestMapping("/")
public class ApiInfoTestController {

    @Resource
    ApiInfoClientService apiInfoClientService;

    @Value("${soa.path}")
    String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;


    @RequestMapping(value = "/test/aps/rest/api/addApiInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest addApiInfo(ApiInfoOV apiInfoOV){

        ResultSoaRest result = new ResultSoaRest();

        try {
            ResultSoaRest resultSoaRest = apiInfoClientService.addApi(apiInfoOV);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "/test/aps/rest/api/deleteApiInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest addApiInfo(String apiName){

        ResultSoaRest result = new ResultSoaRest();

        try {
            ResultSoaRest resultSoaRest = apiInfoClientService.deleteApi(apiName);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    // @RequestMapping(value = "/test/aps/rest/api/updateapiinfo",method = RequestMethod.POST)
    // @ResponseBody
    // public ResultSoaRest updateApiInfo(@RequestParam("tag") String tag,
    //                                    @RequestParam("field") String field,
    //                                    @RequestParam("apiid") String apiid){
    //
    //     ApsSoaParam apsSoaParam = new ApsSoaParam();
    //     Map<String,String> strmap = new HashMap<>();
    //     strmap.put("tag",tag);
    //     strmap.put("field",field);
    //     strmap.put("apiid",apiid);
    //     apsSoaParam.setBusinessParam(JacksonUtils.toJson(strmap));
    //
    //     ResultSoaRest resultSoaRest = soaRestScheduler.sendPost(SOAPATH+"api/updateApiInfo.action",apsSoaParam);
    //     return resultSoaRest;
    // }

    @RequestMapping(value = "/test/aps/rest/api/queryAllField/**",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest queryAllFieldByUrlPath(HttpServletRequest request){

        String  urlPath = extractPathFromPattern(request);
        String realPath = "/"+urlPath;

        String[] noPointPath = StringUtils.split(realPath,".");

        String resultUrl = noPointPath[0];

        ResultSoaRest result = new ResultSoaRest();

        try {
            ResultSoaRest resultSoaRest = apiInfoClientService.queryAllFieldByUrlPath(resultUrl);

            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
            result.addAttribute("fieldResponseMap",resultSoaRest.getData().get("fieldResponseMap"));
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    private static String extractPathFromPattern(final HttpServletRequest request){
        String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String)request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }


}
