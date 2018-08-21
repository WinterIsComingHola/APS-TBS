package com.roli.apsimock.controller.test;

import com.roli.aps.soa.client.service.FieldInfoClientService;
import com.roli.apsimock.model.api.FieldInfoOV;
import com.ruoli.soa.model.ResultSoaRest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author xuxinyu
 * @date 2018/4/8 下午4:05
 */
@Controller
@RequestMapping("/")
public class FieldInfoTestController {
    @Resource
    FieldInfoClientService fieldInfoClientService;

    @RequestMapping(value = "/test/aps/rest/api/addFieldInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest addFieldInfo(FieldInfoOV fieldInfoOV){

        ResultSoaRest result = new ResultSoaRest();

        try {
            ResultSoaRest resultSoaRest = fieldInfoClientService.addField(fieldInfoOV);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/test/aps/rest/api/queryAllFieldByApiName",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest queryAllFieldByApiName(String apiName){

        ResultSoaRest result = new ResultSoaRest();

        try {
            ResultSoaRest resultSoaRest = fieldInfoClientService.queryAllFieldByApiName(apiName);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.addAttribute("fieldInfos",resultSoaRest.getData().get("fieldInfos"));
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
