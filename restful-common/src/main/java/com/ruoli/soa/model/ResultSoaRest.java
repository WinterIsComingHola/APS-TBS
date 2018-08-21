package com.ruoli.soa.model;

import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.model.enums.SoaRestError;

/**
 * @author xuxinyu
 * @date 2018/2/11 下午3:29
 */
public class ResultSoaRest extends Result{

    public ResultSoaRest(){}

    public ResultSoaRest(SoaRestError errEnums){
        setState(errEnums.getCode());
        setMessage(errEnums.getMsg());
        setSuccess(false);
    }

    public void setResultSoaRestError(SoaRestError errEnums){
        setState(errEnums.getCode());
        setMessage(errEnums.getMsg());
        setSuccess(false);
    }

    /**
    * ResultSoaRest的形式转换，从json格式转换为ResultSoaRest
    * @param jsonStr ResultSoaRest的json形式。
    * @return ResultSoaRest
    * @throws
    * @author xuxinyu
    * @date 2018/2/11 下午3:34
    */
    public static ResultSoaRest strToResultSoaRest(String jsonStr){
        return JacksonUtils.fromJson(jsonStr,ResultSoaRest.class);
    }


    /**
     * Description ： 根据key获取data结构中的数据。这种key对应的数据结构一般为业务的数据模型。
     *
     * @param key
     * @param T
     * @return
     * @since
     * @author xuxinyu
     */
    public <T> T getObject(Object key, Class<T> T) {
        if (getData() == null) {
            return null;
        }
        Object object = getData().get(key);
        if (object == null) {
            return null;
        } else {
            return JacksonUtils.fromJson(JacksonUtils.toJson(object), T);
        }
    }
}
