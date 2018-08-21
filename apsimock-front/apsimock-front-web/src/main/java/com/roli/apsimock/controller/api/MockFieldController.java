package com.roli.apsimock.controller.api;


import com.roli.apsimock.model.api.FieldNew;
import com.roli.apsimock.model.api.FieldNewParamInfo;
import com.roli.apsimock.services.FieldInfoService;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.model.ResultSoaRest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MockFieldController {

    @Resource
    FieldInfoService fieldInfoService;

    @RequestMapping(value = "/aps/interface/getfieldrowmaxid",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest getFieldRowMaxId(){
        return fieldInfoService.queryFieldRowMaxId();
    }

    @RequestMapping(value = "/aps/interface/getallchildnodeid",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest getAllChildNodeId(@RequestParam("currentNodeId")Integer currentNodeId){
        return fieldInfoService.queryAllChildNodeId(currentNodeId);
    }

    @RequestMapping(value = "/aps/interface/insertnodedata",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest insertNodeData(FieldNew fieldNew){
        return fieldInfoService.insertNodeData(fieldNew);
    }

    @RequestMapping(value = "/aps/interface/getroottype",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest getRootTypeByApiId(@RequestParam("apiId")Integer apiId){
        return fieldInfoService.queryRootNodeTypeByApiId(apiId);
    }


    @RequestMapping(value = "/aps/interface/getfatherid",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest insertNodeData(@RequestParam("currentNodeId")Integer currentNodeId){
        return fieldInfoService.queryFatherNodeId(currentNodeId);
    }

    @RequestMapping(value = "/aps/interface/updatefieldvalue",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest updateFieldValue(FieldNewParamInfo fieldNewParamInfo){
        return fieldInfoService.updateFieldValue(fieldNewParamInfo);
    }

    @RequestMapping(value = "/aps/interface/updatefieldRowDataType",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest updateFieldRowDataType(FieldNewParamInfo fieldNewParamInfo){
        return fieldInfoService.updateFieldRowDataType(fieldNewParamInfo);
    }

    @RequestMapping(value = "/aps/interface/deletemockdata",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest deleteMockData(@RequestParam("rowIds")List<String> rowIds){
        return fieldInfoService.deleteMockData(rowIds);
    }

    @RequestMapping(value = "/mockserver/**",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ResponseEntity mockServer(HttpServletRequest request){

        String  urlPath = extractPathFromPattern(request);
        String realPath = "/"+urlPath;
        String[] noPointPath = StringUtils.split(realPath,".");
        String resultUrl = noPointPath[0];

        return ResponseEntity.ok(fieldInfoService.runMockServer(resultUrl));
    }

    private String extractPathFromPattern(final HttpServletRequest request){
        String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String)request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }

}
