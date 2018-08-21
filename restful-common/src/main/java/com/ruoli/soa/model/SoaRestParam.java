package com.ruoli.soa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roli.common.utils.json.JacksonUtils;
import com.roli.common.utils.serialize.DeserializeUtils;
import com.roli.common.utils.serialize.SerializeUtils;
import com.ruoli.soa.annotation.SoaIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xuxinyu
 * @date 2018/2/8 下午8:17
 */
public class SoaRestParam extends BaseSoaRestParam{

    @SoaIgnore
    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(SoaRestParam.class);

    public SoaRestParam(){}

    /**
     * 从request中获取对象实例
    *
    * @return T
    * @throws
    * @author xuxinyu
    * @date 2018/2/8 23:02
    */
    public static <T> T getInstance(Class<T> clazz){
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return (T)httpServletRequest.getAttribute(PARAM_MARK);
    }

    /**
     * spring拦截器获取request后，得到业务中的参数类的字符串形式
     * 本方法将字符串转换为业务类的一个实例
     *
    * @param str 待处理的字符串
     *@param format 待处理的字符串的格式
     *@param clazz 需要将字符串转换成的类型信息
    * @return format 格式 json kryo
    * @throws
    * @author xuxinyu
    * @date 2018/2/8 23:03
    */
    public static <T> T convertStrToInstance(String str,String format,Class<T> clazz){
        if(FORMAT_JSON.equalsIgnoreCase(format)){
            return JacksonUtils.fromJson(str,clazz);
        }else if(FORMAT_KRYO.equalsIgnoreCase(format)){
            return DeserializeUtils.deserialize(str, clazz);
        }
        return null;
    }

    @Override
    public String toString() throws UnsupportedOperationException{
        if (FORMAT_JSON.equalsIgnoreCase(getFormat())) {
            return JacksonUtils.toJson(this);
        } else if (FORMAT_KRYO.equalsIgnoreCase(getFormat())) {
            return SerializeUtils.serialize2str(this);
        }
        throw new UnsupportedOperationException("unrealized......");
    }

}
