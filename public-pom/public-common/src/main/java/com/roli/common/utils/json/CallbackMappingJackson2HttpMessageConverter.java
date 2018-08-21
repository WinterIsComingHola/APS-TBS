package com.roli.common.utils.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author xuxinyu
 * @date 2018/8/15 下午3:58
 */
public class CallbackMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private String callBackFunctionName;

    /**
     * 复写MappingJackson2HttpMessageConverter的writeInternal方法
     * 支持jsonp方式返回
     * 如果请求消息中带有callback参数，则返jsonp
     * 否则返回json
     * @param object 待处理对象
     * @param type java类型
     * @param httpOutputMessage HTTP的响应对象
     *
     * **/
    @Override
    public void writeInternal(Object object, Type type, HttpOutputMessage httpOutputMessage) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

        String callBackVal = request.getParameter(callBackFunctionName);

        //如果callback值为空，返回正常的json
        if(StringUtils.isEmpty(callBackVal)){
            super.writeInternal(object,type, httpOutputMessage);
        }else{
            //组装jsonp数据
            JsonEncoding encoding = getJsonEncoding(httpOutputMessage.getHeaders().getContentType());
            try {
                String result =callBackVal+"("+super.getObjectMapper().writeValueAsString(object)+");";
                IOUtils.write(result, httpOutputMessage.getBody(),encoding.getJavaName());
            }
            catch (JsonProcessingException ex) {
                throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
            }
        }
    }


    public String getCallBackFunctionName() {
        return callBackFunctionName;
    }

    public void setCallBackFunctionName(String callBackFunctionName) {
        this.callBackFunctionName = callBackFunctionName;
    }
}
