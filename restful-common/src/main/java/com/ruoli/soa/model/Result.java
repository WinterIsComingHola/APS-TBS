package com.ruoli.soa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roli.common.utils.json.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 本类定义SOA返回的响应体模型
 * 基本响应体的结构为：
 * root
 * --state  类型：Integer 定义：响应消息码，只有状态为1表示成功，其余的都为失败
 * --success 类型：boolean 定义：是否成功，true表示成功，false表示失败
 * --message 类型：String 定义：对于错误码的简单描述，若为成功状态则为""
 * --data   类型：HashMap<String, Object> 定义：承载任意的业务模型
 *
 *
 * @author xuxinyu
 * @date 2018/2/11 上午11:47
 */
public class Result {

    private final static Logger logger = LoggerFactory.getLogger(Result.class);

    /** state 常量 默认状态设置**/
    /** 登陆超时状态 **/
    public static final Integer SESSION_TIMEOUT = -1;
    /** 失败状态 **/
    public static final Integer FAILTURE = 0;
    /** 成功状态 **/
    public static final Integer SUCCESS = 1;
    /** 失败状态使用全局提示 **/
    public static final Integer FAILTURE_AUTO = 2;
    /** 成功状态使用全局提示 **/
    public static final Integer SUCCESS_AUTO = 3;

    /**
     * [异常状态码]登陆已过期，需刷新页面
     */
    public static final Integer LOGIN_OUT_DATE = -996;
    /**
     * SOA运行时业务异常
     */
    public static final Integer SOA_BUSINESS_EXCPTION = -995;
    /**
     * SOA运行时非业务异常
     */
    public static final Integer SOA_RUNTIME_EXCPTION = -1001;

    private Integer state;
    private boolean success;
    private String message;
    private final Map<String,Object> data = new HashMap<>();

    //构造函数
    public Result(){}

    public Result(Integer state,boolean success,String message){
        this.state = state;
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, Integer state) {
        this.success = success;
        this.state = state;
    }

    public Result(boolean success, String msg) {
        this.success = success;
        this.message = msg;
    }

    public Result(Integer state, String msg) {
        this.state = state;
        this.message = msg;
    }

    public Result(boolean success) {
        this.success = success;
    }


    //将业务属性绑定到request
    public void boundRequest(HttpServletRequest request){

        if(request == null){
            request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }

        Set<String> keySet = data.keySet();
        for(String key: keySet){
            request.setAttribute(key,data.get(key));
        }

    }


    //将业务属性绑定到MOdel
    public void boundModel(Model model){
        model.addAllAttributes(data);
    }

    //将业务属性绑定到ModeAndView
    public void boundModel(ModelAndView modelAndView){
        modelAndView.addAllObjects(data);
    }


    // ------------------------------------------------
    private static final String JSON_CONTENT_TYPE = "text/html; charset=UTF-8";

    /**
     *
     * 输出result中的键值只response中，值需为json格式
     *
     * @param response
     */
    public void renderingByJsonData(HttpServletResponse response){

        if(response.getContentType() == null){
            response.setContentType(JSON_CONTENT_TYPE);
        }

        PrintWriter writer = null;

        try {
            Map<String,Object> map = new HashMap<>();
            map.put("state", this.getState());
            map.put("success", this.isSuccess());
            map.put("data", this.data);
            map.put("message", this.message);

            String str = JacksonUtils.toJson(map);
            writer = response.getWriter();
            writer.print(str);
            writer.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if(writer!=null){
                writer.close();
            }
        }
    }

    /**
     * 输出result中的键值只response中，值需为jsonp格式
     *
     * @param response
     * @param functionName
     */
    public void renderingByJsonPData(HttpServletResponse response,
                                     String functionName) {
        if (response.getContentType() == null)
            response.setContentType(JSON_CONTENT_TYPE);

        PrintWriter writer = null;
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("state", this.getState());
            map.put("success", this.isSuccess());
            map.put("data", this.data);
            map.put("message", this.message);
            String str = JacksonUtils.toJsonP(functionName, map);
            writer = response.getWriter();
            writer.print(str);
            writer.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    //=============操作data======================================
    public Map<String, Object> getData() {
        return data;
    }


    /**
     * 增加属性
     *
     * @param key
     * @param obj
     */
    public void addAttribute(String key, Object obj) {
        data.put(key, obj);
    }

    /**
     * 追加所有属性
     *
     * @param result
     */
    public void addAttrbutes(Result result) {
        data.putAll(result.getData());
    }

    public void addAttributes(Map<String,Object> map) {
        data.putAll(map);
    }

    /**
     * 获得属性
     *
     * @param key
     */
    @JsonIgnore
    public Object getAttribute(String key) {
        return data.get(key);
    }

    /**
     * 获得属性
     *
     * @param key
     * @param type
     * @return
     */
    @JsonIgnore
    public <T> T getAttribute(String key, Class<T> type) {
        if (data.get(key) == null) {
            return null;
        }
        return (T) data.get(key);
    }

    /**
     * 检查key是否存在
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    /**
     * 检查value是否存在
     *
     * @param value
     * @return
     */
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    /**
     * 判断result是否存在键值
     *
     * @return
     */
    @JsonIgnore
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * 获取所有属性
     *
     * @return
     */
    @JsonIgnore
    public Map<String, ?> getAttribute() {
        return data;
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    public String toJsonString() {
        return JacksonUtils.toJson(this);
    }


    //================setter and getter==========================


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
