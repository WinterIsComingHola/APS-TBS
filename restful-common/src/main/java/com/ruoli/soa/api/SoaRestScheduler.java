package com.ruoli.soa.api;

import com.roli.common.exception.JDBCException;
import com.roli.common.exception.SecurityException;
import com.roli.common.utils.security.HmacHa1Handle;
import com.ruoli.soa.model.ResultSoaRest;
import com.ruoli.soa.model.SoaHostAuthModel;
import com.ruoli.soa.model.SoaRestParam;
import com.ruoli.soa.model.enums.SoaRestError;
import com.ruoli.soa.spring.dao.SoaHostAuthDao;
import com.ruoli.soa.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.roli.common.utils.security.HmacHa1Handle.signWithHmacSha1;

/**
 * 本类集中处理接口鉴权、加密、客户端处理等操作
 * @author xuxinyu
 * @date 2018/2/9 下午3:29
 */

@Component
public class SoaRestScheduler {

    private static final Logger logger = LoggerFactory.getLogger(SoaRestScheduler.class);
    private static String HOST_NAME = getHostName();

    //用于全局缓冲器，存储SoaHostAuthModel对象
    private static final Map<Integer,SoaHostAuthModel> MODELMAP =
            new ConcurrentHashMap<>();

    private SoaHostAuthModel secretModel = null;

    private static int timeOut = 60 * 1000 * 3;

    @Resource
    private SoaHostAuthDao soaHostAuthDao;


    /**
    * 获取机器主机名称
    * @return String
    * @throws
    * @author xuxinyu
    * @date 2018/2/9 下午3:43
    */
    private static String getHostName(){

        String hostName = null;
        hostName = System.getenv().get("COMPUTERNAME");
        if(StringUtils.isBlank(hostName)){
            hostName = System.getenv().get("HOSTNAME");
        }
        if(StringUtils.isBlank(hostName)){
            try{
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                System.out.println("xxxxxxx获取主机名失败xxxxxxxxxxxx");
                e.printStackTrace();
            }
        }

        System.out.println("********* current hostname:  " + hostName
                + "   *********");
        return hostName;
    }

    /**
    * 提供给刷新线程使用，主要提供库中主机鉴权表的刷新是否成功的处理
     * 如果刷新到数据（和hostname匹配）且secretkey字段不为空，则返回true
     * 否则返回false
     *
    * @return boolean 布尔值
    * @throws
    * @author xuxinyu
    * @date 2018/2/9 下午4:39
    */
    public boolean refreshSecretKey(){

        try{
            SoaHostAuthModel _authModel = soaHostAuthDao.queryHostAuthConfig(HOST_NAME,1);

            if(_authModel.isSelectTrue()){
                secretModel = _authModel;
                logger.error("refreshSecretKey/"+soaHostAuthDao.getUrl()+"...............................................................");
                if (secretModel != null
                        && StringUtils.isNotBlank(secretModel.getSecretKey())) {
                    return true;
                }
            }
            return false;
        } catch (JDBCException e) {
            System.out.println("xxxxxxx查询服务主机异常xxxxxxxxxxxx");
            e.printStackTrace();
            return false;
        }
    }


    /**
    * 提供给客户端程序使用
     * 对SoaRestParam及其子类中的 可以加密的属性进行加密操作并返回密文
    * @param soaRestParam SoaRestParam对象，用于持有外部调用的参数信息
    * @return String 加密完成后的密文数据
    * @throws SecurityException 加密处理异常，需要外部处理
    * @author xuxinyu
    * @date 2018/2/9 下午4:55
    */
    public String sigWithSoaParam(SoaRestParam soaRestParam) throws SecurityException {

        if(secretModel == null
                ||StringUtils.isEmpty(secretModel.getSecretKey())
                ||secretModel.getAppID()==null){
            logger.error("secretModel is null....................");
            throw new IllegalStateException(
                    "secretModel/getSecretKey/ is null,please  check or wait....................");
        }

        if(soaRestParam == null){
            logger.error("soaRestParam is null....................");
            throw new IllegalStateException(
                    "soaRestParam must not be null,....................");
        }

        soaRestParam.setAppID(secretModel.getAppID());
        String participateEncryptCalc = soaRestParam.getSOAParamStr();
        String encryptByHMACHash = signWithHmacSha1(participateEncryptCalc,secretModel.getSecretKey());
        if(logger.isDebugEnabled()){
            logger.debug("SOA SIG========================\nparamStr:"+participateEncryptCalc+"\n sig:"+encryptByHMACHash+"\nSK:"+secretModel.getSecretKey()+"\n================================");
        }

        soaRestParam.setSig(encryptByHMACHash);

        return encryptByHMACHash;
    }

    /**
    * 提供给服务端拦截器使用，用于检查request中的SoaRestParam对象
    * @param soaRestParam 待检查的soaRestParam对象
    * @return ResultSoaRest 检查后返回ResultSoaRest
    * @throws JDBCException
     *@throws SecurityException
    * @author xuxinyu
    * @date 2018/2/11 下午7:36
    */
    public ResultSoaRest checkRequest(SoaRestParam soaRestParam) throws JDBCException, SecurityException {

        ResultSoaRest resultSoaRest = new ResultSoaRest();
        if(soaRestParam == null){
            resultSoaRest.setResultSoaRestError(SoaRestError.APPID_OBJ_NULL);
            return resultSoaRest;
        }

        Integer appID = soaRestParam.getAppID();
        if(appID == null){
            logger.error("appID is null....................");
            resultSoaRest.setResultSoaRestError(SoaRestError.APPID_NULL);
            return resultSoaRest;
        }else if(StringUtils.isEmpty(soaRestParam.getSig())){
            logger.error("sig is null.......................");
            resultSoaRest.setResultSoaRestError(SoaRestError.SIG_NULL);
            return resultSoaRest;
        }else if(soaRestParam.getT() == null){
            logger.error("timestamp is null.......................");
            resultSoaRest.setResultSoaRestError(SoaRestError.TIMESTAMP_NULL);
            return resultSoaRest;
        }

        /*
		* 这里解释一下modelMap的作用：
		* modelMap是这里的一个缓冲存储器
		* modelMap如果没有数据，则从数据库中取得
		* 数据库取得以后，将数据加入modelMap，以便下次直接从modelMap中取得
		* 但是这个modelMap不能无限制增长，所以，在操作modelMap之前要检测一下modelMap的长度
		* 如果长度超过规定限制，则将modelMap清空
		*
		* 这里需要设计这个modelMap的初衷是因为，soa鉴权这一块启动了一个新的线程在持续不断的读取数据库
		* 如果不这么做，将加大数据库的读取压力
		* */
        if(MODELMAP.size()>10000){
            logger.error("cache is too larger,will clear it.................");
            MODELMAP.clear();
        }
        SoaHostAuthModel soaHostAuthModel = MODELMAP.get(appID);
        if(soaHostAuthModel == null
                ||(System.currentTimeMillis() - soaHostAuthModel.getT()) > timeOut){
            soaHostAuthModel = soaHostAuthDao.queryHostAuthConfig(String.valueOf(appID),2);
            if(soaHostAuthModel.isSelectTrue()){
                MODELMAP.put(appID,soaHostAuthModel);
            }
        }
        if(StringUtils.isEmpty(soaHostAuthModel.getSecretKey())){
            resultSoaRest.setResultSoaRestError(SoaRestError.SECRETKEY_NULL);
            logger.error(appID + ": SecretKey not find....................");
            return resultSoaRest;
        }
        String willBeEncryptStr = soaRestParam.megreField();
        if(logger.isDebugEnabled()){
            logger.debug("SOA checkRequest========================\nmergeFieldStr:"+willBeEncryptStr+"\n sig:"+soaRestParam.getSig()+"\nSK:"+soaHostAuthModel.getSecretKey()+"\n================================");
        }
        boolean verify = HmacHa1Handle.verify(willBeEncryptStr
                ,soaRestParam.getSig()
                ,soaHostAuthModel.getSecretKey());

        if(!verify){
            resultSoaRest.setResultSoaRestError(SoaRestError.SIG_CHECK_FAIL);
            logger.error(" SecretKey is checked failure....................");
        }else{
            resultSoaRest.setSuccess(true);
        }
        return resultSoaRest;
    }

    /**
     * http的get方法1
    * @param url
     * @param params
     * @param paramMap
    * @return ResultSoaRest
    * @throws
    * @author xuxinyu
    * @date 2018/2/26 22:03
    */
    public ResultSoaRest sendGet(String url, SoaRestParam params,
                                 Map<String, String> paramMap){
        ResultSoaRest resultSoaRest = null;
        try{
            sigWithSoaParam(params);

            resultSoaRest = HttpUtils.URLGet(url,params,paramMap, Consts.UTF_8);

        } catch (SecurityException e) {
            logger.error("inner encode exception   %%%%% http url:" + url);
            logger.error(e.getMessage(), e);
            resultSoaRest.setResultSoaRestError(SoaRestError.ENORDECODE_ERROR);
            resultSoaRest.addAttribute("stacktrace",
                    ExceptionUtils.getStackTrace(e));
        }
        return resultSoaRest;
    }

    //http的get方法2
    public ResultSoaRest sendGet(String url, SoaRestParam params){
        return sendGet(url,params,null);
    }
    /*//http的get方法3
    public ResultSoaRest sendGet(String url, Map<String, String> paramMap){
        return sendGet(url,new SoaRestParam(),paramMap);
    }
    //http的get方法4
    public ResultSoaRest sendGet(String url){
        return sendGet(url,new SoaRestParam());
    }*/

    /**
     * http的post方法1
     * @param url
     * @param params
     * @param mapParams
     * @return ResultSoaRest
     * @throws
     * @author xuxinyu
     * @date 2018/2/26 22:03
     */
    public ResultSoaRest sendPost(String url,SoaRestParam params,Map<String,String> mapParams){
        ResultSoaRest resultSoaRest = null;
        try{
            sigWithSoaParam(params);

            resultSoaRest = HttpUtils.HttpPost(url,params,mapParams, Consts.UTF_8);

        } catch (SecurityException e) {
            logger.error("inner encode exception   %%%%% http url:" + url);
            logger.error(e.getMessage(), e);
            resultSoaRest.setResultSoaRestError(SoaRestError.ENORDECODE_ERROR);
            resultSoaRest.addAttribute("stacktrace",
                    ExceptionUtils.getStackTrace(e));
        }
        return resultSoaRest;
    }
    //http的post方法2
    public ResultSoaRest sendPost(String url,SoaRestParam params){
        return sendPost(url,params,null);
    }
    //http的post方法3
    public ResultSoaRest sendPost(String url,Map<String,String> mapParams){
        return sendPost(url,new SoaRestParam(),mapParams);
    }
    //http的post方法4
    public ResultSoaRest sendPost(String url){
        return sendPost(url,new SoaRestParam());
    }


    /**
     * 执行POSTMAN的post方法
     * **/
    public ResultSoaRest sendPost(String url
            ,Map<String,Map<String,String>> params
            ,Integer formOrRaw){
        return HttpUtils.HttpPost(url,params,formOrRaw);
    }

    /**
     * 执行POSTMAN的方get法
     * **/
    public ResultSoaRest sendGet(String url
            ,Map<String,Map<String,String>> params){
        return HttpUtils.HttpGet(url,params);
    }


    public void sendAsyncPost(String url, Map<String, String> paramMap) {
        sendAsyncPost(url, new SoaRestParam(), paramMap);
    }

    public void sendAsyncPost(String url, Map<String, String> paramMap,
                              final FutureCallback<HttpResponse> futureCallback) {
        sendAsyncPost(url, new SoaRestParam(), paramMap, futureCallback);
    }

    public void sendAsyncPost(String url, SoaRestParam param,
                              Map<String, String> paramMap) {
        sendAsyncPost(url, param, paramMap, null);
    }

    public void sendAsyncPost(String url, SoaRestParam param,
                              final FutureCallback<HttpResponse> futureCallback) {
        sendAsyncPost(url, param, null, futureCallback);
    }

    public void sendAsyncPost(String url, SoaRestParam param) {
        sendAsyncPost(url, param, null, null);
    }

    public void sendAsyncGet(String url) {
        sendAsyncPost(url, new SoaRestParam(), null, null);
    }

    public void sendAsyncPost(String url, SoaRestParam param,
                              Map<String, String> paramMap,
                              final FutureCallback<HttpResponse> futureCallback) {
        try{
            sigWithSoaParam(param);
            HttpUtils.AsyncURLPost(url, param, paramMap, Consts.UTF_8,
                    futureCallback);
        } catch (SecurityException e) {
            logger.error("inner encode exception   %%%%% http url:" + url);
            logger.error(e.getMessage(), e);
        }
    }


}
