package com.roli.aps.soa.client.service.impl;

import com.roli.aps.soa.client.service.FieldInfoClientService;
import com.roli.apsimock.model.ApsSoaParam;
import com.roli.apsimock.model.api.FieldInfo;
import com.roli.apsimock.model.api.FieldInfoOV;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/4/4 下午4:22
 */

@Service
public class FieldInfoClientServiceImpl implements FieldInfoClientService{

    @Value("${soa.path}")
    String SOAPATH;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @Override
    public ResultSoaRest addField(FieldInfoOV fieldInfoOV){

        ResultSoaRest result = new ResultSoaRest();

        try{
            ApsSoaParam apsSoaParam = new ApsSoaParam();
            apsSoaParam.setBusinessParam(JacksonUtils.toJson(fieldInfoOV));
            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH+"api/addFieldInfo.action",apsSoaParam);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;

    }

    @Override
    public ResultSoaRest queryAllFieldByApiName(String apiName){
        ResultSoaRest result = new ResultSoaRest();

        try{
            ApsSoaParam apsSoaParam = new ApsSoaParam();
            apsSoaParam.setBusinessParam(apiName);
            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH+"api/queryAllFieldByApiName.action",apsSoaParam);

            List<FieldInfo> fieldInfos = (List<FieldInfo>)resultSoaRest.getData().get("fieldInfos");

            if(fieldInfos.size()==0){
                result.setState(-1);
                result.setSuccess(false);
                result.setMessage("数据为空");
            }else{
                result.setState(resultSoaRest.getState());
                result.setSuccess(resultSoaRest.isSuccess());
                result.addAttribute("fieldInfos",fieldInfos);
            }

        }catch (Exception e){
            result.setState(-1);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


}
