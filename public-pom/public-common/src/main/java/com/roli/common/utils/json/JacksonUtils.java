package com.roli.common.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.roli.common.model.PropertiesMO;
import com.roli.common.utils.security.EncodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/2/8 下午3:58
 */
public class JacksonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);

    /*
    * 默认日期格式
    * */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /*生成单例
    * 分别处理json-对象序列化
    * xml-对象序列化
    * */
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final XmlMapper XML_MAPPER = new XmlMapper();

    static{

        //设置日期格式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_PATTERN));

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性，主要为null的属性
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 设置有属性不能映射成POJO时不报错
        OBJECT_MAPPER.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //配置为true表示mapper接受只有一个元素的数组的反序列化
        OBJECT_MAPPER.configure(
                DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        //支持JAVA8的Localtime相关时间类
        OBJECT_MAPPER.registerModule(new ParameterNamesModule()).registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        //新增自定义处理器
        OBJECT_MAPPER.addHandler(new DeserializationProblemHandler());

        XML_MAPPER.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_PATTERN));

        XML_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        XML_MAPPER.addHandler(new DeserializationProblemHandler());

    }

    /**
     * xml字符串序列化成对象
     *
     * @param str
     * @param type
     * @return
     * @throws Exception
     */
    public static <T> T deserializeXML(String str, Class<T> type) {
        try {
            return (T) XML_MAPPER.readValue(str, type);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象序列化成xml字符串
     *
     * @param object
     * @return
     * @throws Exception
     */
    public static String serializeXML(Object object) {
        try {
            return "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + XML_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换为json.可以传入任意对象包括pojo、map等
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (null == object) {
            return "";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 转换为jsonp.
     *
     * @param functionName
     * @param object
     * @return
     */
    public static String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(EncodeUtils.urlEncode(functionName),
                object));
    }

    /**
     * json转换为对象.
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.error("parse json string error:" + jsonString, e);
            return null;
        }
    }

    public static Map str2map(String jsonStr){
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        //return obj2map(fromJson(jsonStr,Object.class));
        return fromJson(jsonStr,HashMap.class);
    }

    /**
     * Description ： 将map转换为对象<br>
     *
     * @param map
     * @param clazz
     * @return
     * @since
     *
     */
    public static <T> T map2obj(Map map, Class<T> clazz) {
        return fromJson(toJson(map), clazz);
    }

    /**
     *将对象转换为Map
     * @param obj
     * @return
     */
    public static Map obj2map(Object obj) {
        return fromJson(toJson(obj), HashMap.class);
    }

    /**
     *
     * @param jsonString
     * @param parametrized
     *            泛型类
     * @param parameterClasses
     *            泛型参
     * @return
     */
    public static <T> T fromJson(String jsonString, Class<?> parametrized,
                                 Class<?>... parameterClasses) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        return (T) fromJson(jsonString,
                constructParametricType(parametrized, parameterClasses));
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函数constructParametricType构造类型.
     *
     * @see #constructParametricType(Class, Class...)
     */
    public static <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.error("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 构造泛型的Type如List<MyBean>,
     * 则调用constructParametricType(ArrayList.class,MyBean.class)
     * Map<String,MyBean>则调用(HashMap.class,String.class, MyBean.class)
     */
    private static JavaType constructParametricType(Class<?> parametrized,
                                                   Class<?>... parameterClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(
                parametrized, parameterClasses);
    }

    public static void main(String[] args){

        PropertiesMO propertiesMO = new PropertiesMO();
        propertiesMO.setiD(1);
        propertiesMO.setAppName("front");
        propertiesMO.setEncrypt(0);
        propertiesMO.setiP("192.168.199.111");
        propertiesMO.setIpAuth(1);
        propertiesMO.setKey("name");
        propertiesMO.setVal("xuxinyu");
        propertiesMO.setProfileName("hah");
        propertiesMO.setReload(1);

        String spropertiesMO = toJson(propertiesMO);

        Map<String,Object> mapr = str2map(spropertiesMO);

        String ssjsonp = toJsonP("callback",propertiesMO);
        Map<String,Object> map = new HashMap<>();
        map.put("name","xuxinyu");
        map.put("age",20);
        map.put("shu",propertiesMO);
        map.put("shuu",null);
        String jsonmap = toJson(map);
        List<String> listStr = new ArrayList<>();
        listStr.add("1");
        listStr.add("3");
        listStr.add("11");
        String jsonlist = toJson(listStr);


        System.out.println(spropertiesMO);
        System.out.println(ssjsonp);
        System.out.println(jsonmap);
        System.out.println(jsonlist);
        System.out.println(mapr);

        PropertiesMO propertiesMO1 = new PropertiesMO();
        propertiesMO1 = fromJson(spropertiesMO,PropertiesMO.class);



    }


}
