package com.roli.apsimock.controller.api;

import com.roli.apsimock.controller.project.ProjectController;
import com.roli.apsimock.model.api.ApiInfo;
import com.roli.apsimock.model.api.ParamInfo;
import com.roli.apsimock.model.api.ReqPostMan;
import com.roli.apsimock.model.api.ReqRowData;
import com.roli.apsimock.services.ApiInfoService;
import com.roli.apsimock.services.ReqParamInfoService;
import com.roli.common.exception.BusinessException;
import com.ruoli.soa.model.ResultSoaRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17040386 on 2018/7/6.
 */


@Controller
@RequestMapping("/")
public class ParamFieldController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Resource
    ReqParamInfoService reqParamInfoService;

    @Resource
    ApiInfoService apiInfoService;

    /**
     * @Description: 根据apiid查询请求参数的具体数据
     * @param projectid
     * @param userAccount
     * @param apiId
     * @param model
     * @return String
     * @throws
     * @author xuxinyu
     * @date 2018/7/6 14:53
     */
    @RequestMapping(value = "/aps/interface/getpostmanconfig",method = RequestMethod.GET)
    public String getPostManConfigPage(String projectid,String userAccount ,String apiId,String userRole,Model model){

        ApiInfo apiInfo = apiInfoService.queryApiInfoByApiid(apiId);

        //设置api基本信息
        String apiName = apiInfo.getApiName();
        String createrName = apiInfo.getUserInfo().getUserName();

        String apiMethod = null;

        switch (apiInfo.getHttpMethod())
        {
            case 1:
                apiMethod = "GET";
                break;
            case 2:
                apiMethod = "POST";
                break;
            case 3:
                apiMethod = "PUT";
                break;
            case 4:
                apiMethod = "DELETE";
                break;
            case 5:
                apiMethod = "HEAD";
                break;
            case 6:
                apiMethod = "OPTION";
                break;
        }

        String apiPath = apiInfo.getUrlPath();
        String isActive = "";
        if(apiInfo.getIsActive()==1){
            isActive = "可用";
        }else if(apiInfo.getIsActive()==0){
            isActive = "不可用";
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String createTime = df.format(apiInfo.getCreateTime());


        model.addAttribute("projectid",projectid);
        model.addAttribute("userAccount",userAccount);
        model.addAttribute("apiId",apiId);
        model.addAttribute("userRole",userRole);
        model.addAttribute("apiName",apiName);
        model.addAttribute("createrName",createrName);
        model.addAttribute("apiMethod",apiMethod);
        model.addAttribute("apiPath",apiPath);
        model.addAttribute("isActive",isActive);
        model.addAttribute("createTime",createTime);
        //对请求参数数据进行遍历。重新按照参数属性进行分类

        try {
            List<ParamInfo> paramInfos = reqParamInfoService.queryParamInfoByApiId(apiId);
            model.addAttribute("reqParams",paramInfos);
        } catch (BusinessException e) {
            List<ParamInfo> paramInfos = new ArrayList<>();
            model.addAttribute("reqparams",paramInfos);
        }


        /*List<ParamInfo> paramInfos = null;
        List<ReqParamInfoOV> reqParamovInfos1 = new ArrayList<>();
        String rawJsonBody = null;//rawJsonBody消息体
        List<ReqParamInfoOV> reqParamovInfos2 = new ArrayList<>();
        List<ReqParamInfoOV> reqParamovInfos3 = new ArrayList<>();
        List<ReqParamInfoOV> reqParamovInfos4 = new ArrayList<>();
        List<ReqParamInfoOV> reqParamovInfos5 = new ArrayList<>();

        try{
            reqParamInfos = reqParamInfoService.queryReqParamInfoByApiid(apiId);
            Random random = new Random();
            for(ReqParamInfo reqParamInfo:reqParamInfos){

                if(reqParamInfo.getBodyType()==1){
                    //www-form
                    ReqParamInfoOV reqParamInfoOV = new ReqParamInfoOV();
                    reqParamInfoOV.setParamName(reqParamInfo.getParamName());
                    reqParamInfoOV.setParamValue(reqParamInfo.getParamValue());
                    reqParamInfoOV.setDesc(reqParamInfo.getDesc());
                    reqParamInfoOV.setBodyType(reqParamInfo.getBodyType());
                    reqParamInfoOV.setParamType(reqParamInfo.getParamType());
                    reqParamInfoOV.setRadnum(random.nextInt(900000)+100000);

                    if(reqParamInfo.getParamType()==1){
                        //body
                        reqParamovInfos1.add(reqParamInfoOV);
                    }else if(reqParamInfo.getParamType()==2){
                        //head
                        reqParamovInfos2.add(reqParamInfoOV);
                    }else if(reqParamInfo.getParamType()==3){
                        //cookie
                        reqParamovInfos3.add(reqParamInfoOV);
                    }

                }else if(reqParamInfo.getBodyType()==2){
                    //raw
                    ReqParamInfoOV reqParamInfoOV = new ReqParamInfoOV();
                    reqParamInfoOV.setParamName(reqParamInfo.getParamName());
                    reqParamInfoOV.setParamValue(reqParamInfo.getParamValue());
                    reqParamInfoOV.setDesc(reqParamInfo.getDesc());
                    reqParamInfoOV.setBodyType(reqParamInfo.getBodyType());
                    reqParamInfoOV.setParamType(reqParamInfo.getParamType());
                    reqParamInfoOV.setRadnum(random.nextInt(900000)+100000);

                    if(reqParamInfo.getParamType()==1){
                        //body
                        rawJsonBody = reqParamInfo.getRawBody();
                    }else if(reqParamInfo.getParamType()==2){
                        //head
                        reqParamovInfos2.add(reqParamInfoOV);
                    }else if(reqParamInfo.getParamType()==3){
                        //cookie
                        reqParamovInfos3.add(reqParamInfoOV);
                    }
                }

            }

        }catch (BusinessException e){
        }*/
/*        model.addAttribute("reqParamovInfos1",reqParamovInfos1);
        model.addAttribute("reqrawbody",rawJsonBody);
        model.addAttribute("reqParamovInfos2",reqParamovInfos2);
        model.addAttribute("reqParamovInfos3",reqParamovInfos3);
        model.addAttribute("reqParamovInfos4",reqParamovInfos4);
        model.addAttribute("reqParamovInfos5",reqParamovInfos5);*/

        return "interface/postmanconfig";
    }

    /**
     * @Description: 更新请求参数值接口，提供前台ajax调用
     * @param paramInfo
     * @return ResultSoaRest
     * @throws
     * @author xuxinyu
     * @date 2018/7/6 14:52
     */
    @RequestMapping(value = "/aps/interface/updatereqfield",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest updateParamField(ParamInfo paramInfo){

        ResultSoaRest result = new ResultSoaRest();
        try {
            return reqParamInfoService.updateParamField(paramInfo);
        } catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    /**
     * @Description: 查询数据库中rowid最大值,提供前台ajax调用
     * @param
     * @return Integer
     * @throws
     * @author xuxinyu
     * @date 2018/7/10 17:33
     */
    @RequestMapping(value = "/aps/interface/queryparammaxid",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest queryParamMaxId(){
        return reqParamInfoService.queryParamMaxId();
    }


    /**
     * @Description: 新增row数据,提供前台ajax调用
     * @param reqRowData reqRowData对象
     * @return ResultSoaRest
     * @throws
     * @author xuxinyu
     * @date 2018/7/17 17:33
     */
    @RequestMapping(value = "/aps/interface/insertparamrow",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest insertParamRow(ReqRowData reqRowData){

        ResultSoaRest result = new ResultSoaRest();
        try {
            return reqParamInfoService.insettParamRow(reqRowData);
        } catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    /**
     * @Description: 删除row数据,提供前台ajax调用
     * @param rowId reqRowData对象
     * @return ResultSoaRest
     * @throws
     * @author xuxinyu
     * @date 2018/7/17 17:33
     */
    @RequestMapping(value = "/aps/interface/deleteparamrow",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest deleteParamRow(@RequestParam("rowId")Integer rowId){

        ResultSoaRest result = new ResultSoaRest();
        try {
            return reqParamInfoService.deleteParamRow(rowId);
        } catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    /**
     * @Description: 查询bodyType数据,提供前台ajax调用
     * @param apiId apiID
     * @return ResultSoaRest
     * @throws
     * @author xuxinyu
     * @date 2018/7/18 15:33
     */
    @RequestMapping(value = "/aps/interface/querybody",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest queryBody(@RequestParam("apiId")Integer apiId){

        ResultSoaRest result = new ResultSoaRest();
        try {
            return reqParamInfoService.queryBodyType(apiId);
        } catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    @RequestMapping(value = "/aps/interface/runpostman",method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest runPostMan(@RequestParam("url") String url
            ,@RequestParam("reqMethod")String httpMethod
            ,@RequestParam("apiId")Integer apiId
            ,@RequestParam("formOrRaw")Integer formOrRaw){

        ResultSoaRest result = new ResultSoaRest();
        try {
            return reqParamInfoService.runPostMan(url,httpMethod,apiId,formOrRaw);
        } catch (BusinessException e) {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }

    }

}
