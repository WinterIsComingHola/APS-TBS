package com.roli.apsimock.controller.api;

import com.roli.apsimock.controller.project.ProjectController;
import com.roli.apsimock.model.api.*;
import com.roli.apsimock.model.project.Table;
import com.roli.apsimock.services.ApiInfoService;
import com.roli.apsimock.services.FieldInfoService;
import com.roli.apsimock.services.ReqParamInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.model.ResultSoaRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author xuxinyu
 * @date 2018/5/22 上午10:14
 */

@Controller
@RequestMapping("/")
public class ApiController
{

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);


    @Resource
    ApiInfoService apiInfoService;
    @Resource
    ReqParamInfoService reqParamInfoService;
    @Resource
    FieldInfoService fieldInfoService;

    @RequestMapping(value = "/aps/interface/myinterface", method = RequestMethod.GET)
    public String getInterfacePage(String projectid, String userAccount,String userRole, Model model,String page, String limit)
    {

        ResultSoaRest resultSoaRest = apiInfoService.queryApiByProjectid(projectid,page,limit);
        List<Map<String,Object>> apiInfoList = (List<Map<String,Object>>)resultSoaRest.getAttribute("apiinfos");

        if (apiInfoList.size() == 0)
        {
            model.addAttribute("apiInfos", 0);
        } else
        {
            model.addAttribute("apiInfos", 1);
        }

        model.addAttribute("projectid", projectid);
        model.addAttribute("userAccount", userAccount);
        model.addAttribute("userRole",userRole);


        return "interface/myinterface";

    }

    @RequestMapping(value = "/aps/interface/addApi", method = RequestMethod.POST)
    @ResponseBody
    public ResultSoaRest addProjectInfo(ApiInfoOV apiInfoOV)
    {

        ResultSoaRest result = new ResultSoaRest();
        try
        {

            ResultSoaRest resultSoaRest = apiInfoService.addApi(apiInfoOV);
            result.setState(resultSoaRest.getState());
            result.setSuccess(resultSoaRest.isSuccess());
            result.setMessage(resultSoaRest.getMessage());

        } catch (BusinessException e)
        {
            result.setState(Integer.parseInt(e.getErrCode()));
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;

    }

    @RequestMapping(value = "/aps/interface/getApiByProject", method = RequestMethod.POST)
    @ResponseBody
    public Table getApiByProject(String projectid, String page,String limit)
    {

        ResultSoaRest resultSoaRest = apiInfoService.queryApiByProjectid(projectid,page,limit);
        List<Map<String, Object>> apiInfoList = (List<Map<String, Object>>)resultSoaRest.getAttribute("apiinfos");

        Table table = new Table();
        List<Map<String, Object>> mapList = new ArrayList<>();

        if (apiInfoList.size() == 0)
        {
            table.setCode(0);
            table.setMsg("暂无数据");
            table.setCount(0);
            table.setData(mapList);
        } else
        {
            for (Map<String, Object> apiMap : apiInfoList)
            {

                ApiInfo apiInfo = JacksonUtils.map2obj(apiMap, ApiInfo.class);

                Map<String, Object> map = new HashMap<>();
                map.put("interid", apiInfo.getId());
                map.put("intername", apiInfo.getApiName());

                String method = "";

                switch (apiInfo.getHttpMethod())
                {
                    case 1:
                        method = "GET";
                        break;
                    case 2:
                        method = "POST";
                        break;
                    case 3:
                        method = "PUT";
                        break;
                    case 4:
                        method = "DELETE";
                        break;
                    case 5:
                        method = "HEAD";
                        break;
                    case 6:
                        method = "OPTION";
                        break;

                }

                map.put("intermethod", method);
                map.put("interpath", apiInfo.getUrlPath());
                map.put("interdesc", apiInfo.getDesc());

                mapList.add(map);
            }

            table.setCode(0);
            table.setMsg("");
            table.setCount(Integer.parseInt(resultSoaRest.getAttribute("total").toString()));
            table.setData(mapList);
        }
        return table;

    }

    @RequestMapping(value = "/aps/interface/updateapiinfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> updateApiInfo(@RequestParam("tag") String tag,
                              @RequestParam("field") String field,
                              @RequestParam("apiid") String apiid)
    {
        ResultSoaRest result = apiInfoService.updateApiInfo(tag, field, apiid);
        Map<String, String > map = new HashMap<>();
        map.put("state",String.valueOf(result.getState()));
        map.put("message", result.getMessage());
        return map;
    }


    @RequestMapping(value = "/aps/interface/getinterconfig", method = RequestMethod.GET)
    public String getInterfaceConfigPage(String projectid, String userAccount, String apiId,String userRole, Model model)
    {

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
        if (apiInfo.getIsActive() == 1)
        {
            isActive = "可用";
        } else if (apiInfo.getIsActive() == 0)
        {
            isActive = "不可用";
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String createTime = df.format(apiInfo.getCreateTime());

        Integer rootId = (Integer) fieldInfoService.queryRootNodeIdByApiId(Integer.parseInt(apiId)).getAttribute("rootId");

        //返回全量Mock字段
        List<FieldNewParamInfo> fieldNewParamInfos = fieldInfoService.queryAllFieldByApiId(Integer.parseInt(apiId));

        model.addAttribute("projectid", projectid);
        model.addAttribute("userAccount", userAccount);
        model.addAttribute("userRole",userRole);
        model.addAttribute("apiId", apiId);
        model.addAttribute("apiName", apiName);
        model.addAttribute("createrName", createrName);
        model.addAttribute("apiMethod", apiMethod);
        model.addAttribute("apiPath", apiPath);
        model.addAttribute("isActive", isActive);
        model.addAttribute("createTime", createTime);
        model.addAttribute("rootId", rootId);
        model.addAttribute("fieldNewParamInfos", fieldNewParamInfos);

        return "interface/interconfig";

    }

}
