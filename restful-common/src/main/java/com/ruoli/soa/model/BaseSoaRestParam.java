package com.ruoli.soa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoli.soa.annotation.SoaIgnore;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * soa调用时的参数模型。所有业务相关的模型需要继承本类
 * @author xuxinyu
 * @date 2018/2/7 下午2:55
 */
public class BaseSoaRestParam {

    @JsonIgnore
    @SoaIgnore
    private static final Logger logger = LoggerFactory.getLogger(BaseSoaRestParam.class);

    /*公共属性，权限public，不参与签名计算
    * @JsonIgnore不参与json处理
    * @SoaIgnore不参与签名计算
    * */
    @JsonIgnore
    @SoaIgnore
    public static final String PARAM_MARK = "SOARestParam_0_0_1";
    @JsonIgnore
    @SoaIgnore
    public static final String FORMAT_PREFIX = "format";
    @JsonIgnore
    @SoaIgnore
    public static final String FORMAT_JSON = "json";
    @JsonIgnore
    @SoaIgnore
    public static final String FORMAT_KRYO = "kryo";

    /*内部属性
    * @JsonIgnore不参与json处理
    * @SoaIgnore不参与签名计算*/
    @JsonIgnore
    @SoaIgnore
    private RequestConfig configuration;

    //用于将目标参数转为签名密文
    @SoaIgnore
    private String sig;

    private Long t;
    private Integer appID;


    @JsonIgnore
    @SoaIgnore
    private String format = FORMAT_JSON;

    @JsonIgnore
    @SoaIgnore
    private SortedMap<String, Object> paramsMap = new TreeMap<>();


    /**
     *获取restful 请求参数sig计算所需的字符串<br>
     * 每次都是全新生成<br>
    * @param
    * @return String 需要参与签名的属性转换成字符串
    * @throws
    * @author xuxinyu
    * @date 2018/2/8 0:04
    */
    public String getSOAParamStr(){
        paramsMap.clear();
        // 先设置时间戳再生成fields字符串
        t = System.nanoTime();
        return megreField();
    }

    /**
     * 将需要处理的全局MAP中的值转换为字符串
    * @param
    * @return String 需要参与签名的属性转换成字符串
    * @throws
    * @author xuxinyu
    * @date 2018/2/8 0:01
    */
    public String megreField(){
        //注意本处的getClass将首先获取的是外部调用的对应实例的Class对象
        getFields(paramsMap,getClass());

        StringBuilder builder = new StringBuilder();
        for (Object key : paramsMap.keySet()) {
            builder.append("\n" + key + "@" + paramsMap.get(key));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(builder.toString());
        }
        return builder.toString();

    }

    /**
     * 获取需要处理的参数类中的用于签名的所有属性
     * 处理对象包括参数类的父类，直至Object
     * 所有的处理结果都将存储在全局Map中
    * @param sortedMapField 用于存储参与签名计算的属性，需要传入一个全局map
     *@param clazz 需要处理的参数类
    * @return
    * @throws
    * @author xuxinyu
    * @date 2018/2/7 23:56
    */
    private void getFields(SortedMap<String,Object> sortedMapField ,Class clazz){
        //这个if用于跳出递归调用
        if(clazz == Object.class){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        Map<String,String> ignoreFieldsMap = getIgnoreField(clazz);
        putFields(sortedMapField,fields,ignoreFieldsMap);
        //不断递归执行本方法，从当前class一直执行到Object，将所有本继承关系链下的属性全部遍历一遍
        getFields(sortedMapField,clazz.getSuperclass());
    }


    /**
     * 本类主要对传入的属性中需要忽略的属性进行过滤
     * 未被过滤的属性将统一存入全局Map中待处理
     *
    * @param sortedMapField 用于存储参与签名计算的属性，需要传入一个全局map
     *@param fields 所有的属性（包括本类下的所有子类的属性）
     *@param ignoreFieldsMap 需要忽略的属性
     * @return
    * @throws
    * @author xuxinyu
    * @date 2018/2/7 23:46
    */
    private void putFields(SortedMap<String,Object> sortedMapField,Field[] fields,
                           Map<String,String> ignoreFieldsMap){
        for(Field field:fields){
            if(!isPrimitive(field)){
                continue;
            }
            if(ignoreFieldsMap.containsKey(field.getName())){
                continue;
            }

            try{
                //设置为true后，私有field也将会被访问
                field.setAccessible(true);
                //获取到field对应的值，以Object类型存入map
                Object objectWithField = field.get(this);
                if(objectWithField != null){
                    sortedMapField.put(field.getName(),objectWithField);
                }
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
            }

        }

    }


    /**
    * @param clazz 待处理的类，本方法主要用于找到待处理类中带有 SoaIgnore注解的属性
     * 这种属性将被在签名计算时候忽略处理
     * 注意：SoaIgnore有两种标注方式：
     * 1、直接标注于类上，需要把待处理的属性名称写在注解的字符串数组内
     * 2、标注于属性上，此时注解不需要任何值
     * 建议使用第二种方法
    * @return Map
    * @throws
    * @author xuxinyu
    * @date 2018/2/7 22:49
    */
    private Map<String,String> getIgnoreField(Class<? extends BaseSoaRestParam> clazz){
        Map<String,String> mapField = new HashMap<>();

        //处理类上的注解
        SoaIgnore soaIgnoreClass = clazz.getAnnotation(SoaIgnore.class);
        {
            /*类上的SoaIgnore注解处理，如果要在类上使用，需要把将要忽略的域成员名称在
            * 注解上的String数组填充正确，比如：
            * @SoaIgnore({"field1","field2"})
            * */
            if (soaIgnoreClass != null) {
                String[] values = soaIgnoreClass.value();
                for (String value : values) {
                    mapField.put(value, value);
                }
            }
        }

        //处理属性上的注解
        Field[] fileds = clazz.getDeclaredFields();
        for(Field field:fileds) {

            SoaIgnore soaIgnoreField = field.getAnnotation(SoaIgnore.class);
            if(soaIgnoreField!= null){
                mapField.put(field.getName(), field.getName());
            }

        }

        return mapField;

    }

    /**
     * 判断传入的域成员类型的数据类型
     * 如果是原始数据类型，返回true
     * 如果是原始类型的包装类型包括String，返回true
     * 如果上述都不是，返回false
     *
     * 上述处理，将非基础数据类型的数据进行了分类，非基础数据类的域成员将不参与签名计算
     *
    *
    * @param field 域成员类型
    * @return boolean 布尔值
    * @throws
    * @author xuxinyu
    * @date 2018/2/7 下午4:45
    */
    private boolean isPrimitive(Field field){

        Class clazzField = field.getType();
        if(!clazzField.isPrimitive()){

            if(clazzField==String.class||clazzField==Integer.class
                    ||clazzField==Float.class||clazzField==Boolean.class
                    ||clazzField==Double.class||clazzField==Character.class
                    ||clazzField==Byte.class||clazzField==Short.class
                    ||clazzField==Long.class){
                return true;
            }else{
                return false;
            }

        }else{
            return true;
        }

    }


    //=================setter and getter==================================

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public Integer getAppID() {
        return appID;
    }

    public void setAppID(Integer appID) {
        this.appID = appID;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public RequestConfig getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RequestConfig configuration) {
        this.configuration = configuration;
    }
}
