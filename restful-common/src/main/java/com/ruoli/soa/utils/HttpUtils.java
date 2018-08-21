package com.ruoli.soa.utils;

import com.roli.common.utils.base.RegexpUtils;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.model.ResultSoaRest;
import com.ruoli.soa.model.SoaHttpRespResult;
import com.ruoli.soa.model.SoaRestParam;
import com.ruoli.soa.model.enums.SoaRestError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.*;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/2/12 下午6:58
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static int connectionTimeOut = 20000;
    private static int socketTimeOut = 5000;
    private static int maxConnectionPerHost = 500;
    private static int maxTotalConnections = 10000;

    private static CloseableHttpClient httpClient;
    private static CloseableHttpAsyncClient asyncClient;

    static{
        try{
            //设置ssl不进行证书验证
            SSLContext sslContext = SSLContexts.custom().build();
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {

                public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, null);
            NoopHostnameVerifier noopHostnameVerifier = new NoopHostnameVerifier();

            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.INSTANCE)
                            .register("https",new SSLConnectionSocketFactory(sslContext, noopHostnameVerifier))
                            .build();
            //定义并设置http连接池
            PoolingHttpClientConnectionManager poolingConnManager =
                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).setSoKeepAlive(true).build();
            poolingConnManager.setDefaultSocketConfig(socketConfig);

            MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
                    .setMaxLineLength(2000).build();
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints).build();

            poolingConnManager.setDefaultConnectionConfig(connectionConfig);
            poolingConnManager.setMaxTotal(maxTotalConnections);
            poolingConnManager.setDefaultMaxPerRoute(maxConnectionPerHost);
            poolingConnManager.setValidateAfterInactivity(1000);

            //定义并设置httpclientbuild
            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setConnectionManager(poolingConnManager);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectionTimeOut)
                    .setConnectionRequestTimeout(connectionTimeOut)
                    .setSocketTimeout(socketTimeOut)
                    .setCookieSpec(CookieSpecs.STANDARD).build();
            httpClientBuilder.setDefaultRequestConfig(requestConfig);

            ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {

                @Override
                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    return 65 * 1000;
                }
            };
            httpClientBuilder.setKeepAliveStrategy(myStrategy);

            List<Header> headers = new ArrayList<>();
            headers.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) soa-restful-httpclient"));
            headers.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate"));
            headers.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN"));
            headers.add(new BasicHeader(HttpHeaders.CONNECTION, "Keep-Alive"));
            headers.add(new BasicHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString()));

            httpClientBuilder.setDefaultHeaders(headers);

            httpClient = httpClientBuilder.build();

            //设置asyncClient
            asyncClient = HttpAsyncClients.createDefault();
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
            PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(
                    ioReactor);
            cm.setMaxTotal(maxTotalConnections);
            cm.setDefaultMaxPerRoute(maxConnectionPerHost);
            RequestConfig asyncrequestConfig = RequestConfig.custom()
                    .setSocketTimeout(socketTimeOut)
                    .setConnectTimeout(connectionTimeOut).build();
            asyncClient = HttpAsyncClients.custom().setConnectionManager(cm)
                    .setDefaultRequestConfig(asyncrequestConfig).build();
            asyncClient.start();

        } catch (NoSuchAlgorithmException |KeyManagementException | IOReactorException e) {
            e.printStackTrace();
            logger.error("URLConnectionHelper is error ", ExceptionUtils.getStackTrace(e));
        }


    }


    public static ResultSoaRest HttpPost(String url,SoaRestParam params,Map<String,String> mapParams,Charset charset){

        ResultSoaRest resultSoaRest = new ResultSoaRest();
        HttpEntityEnclosingRequestBase httpPost = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;

        try{
            httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE,ContentType.APPLICATION_FORM_URLENCODED.withCharset(charset==null?Consts.UTF_8:charset).toString());
            httpPost.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
            //将表单的值放入postmethod中
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair(SoaRestParam.PARAM_MARK,params.toString()));
            nvps.add(new BasicNameValuePair(SoaRestParam.FORMAT_PREFIX,params.getFormat()));
            if(mapParams!=null){
                for(Map.Entry<String,String> mapSet:mapParams.entrySet()){
                    nvps.add(new BasicNameValuePair(mapSet.getKey(),mapSet.getValue()));
                }
            }

            StringEntity stringEntity = new UrlEncodedFormEntity(nvps,Consts.UTF_8);
            httpPost.setEntity(stringEntity);

            if (logger.isDebugEnabled()) {
                logger.debug("\n\n\n%%%%%%%%%%%%%%%http send: " + url
                        + "\n%%%%%%%%%%%%%%%params:" + params.toString()
                        + "\n%%%%%%%%%%%%%%%paramMap:" + mapParams);
            }

            if (params.getConfiguration() != null) {
                httpPost.setConfig(params.getConfiguration());
            }

            response = httpClient.execute(httpPost);
            entity = response.getEntity();

            int stateCode = response.getStatusLine().getStatusCode();
            String responseBodyAsString = EntityUtils.toString(entity,Consts.UTF_8);

            if (stateCode == HttpStatus.SC_OK) {
                resultSoaRest = ResultSoaRest.strToResultSoaRest(responseBodyAsString);
            }else if(stateCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                resultSoaRest.setResultSoaRestError(SoaRestError.SERVER_INNER_ERROR);
                logger.error(stateCode + ":" + responseBodyAsString);
            } else {
                resultSoaRest.setResultSoaRestError(SoaRestError.HTTP_STATUS_ERROR);
                //resultSoaRest.addAttribute("statusCode", stateCode);
                logger.error(stateCode + ":" + responseBodyAsString);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("\n%%%%%%%%%%%%%%%http state: " + stateCode
                        + "\n%%%%%%%%%%%%%%%response:" + responseBodyAsString);
            }


        }catch (ClientProtocolException e) {
            logger.error("soa exception   %%%%% http url:" + url);
            logger.error(e.getMessage(), e);
            resultSoaRest.setResultSoaRestError(SoaRestError.GENERAL_ERROR);
            resultSoaRest.addAttribute("stacktrace",
                    ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            logger.error("soa exception   %%%%% http url:" + url);
            logger.error(e.getMessage(), e);
            resultSoaRest.setResultSoaRestError(SoaRestError.IO_ERROR);
            resultSoaRest.addAttribute("stacktrace",
                    ExceptionUtils.getStackTrace(e));
        }

        return resultSoaRest;

    }

    private static void close(HttpEntity entity, HttpRequestBase request, CloseableHttpResponse response) {
        try {
            if (request != null)
                request.releaseConnection();
            if (entity != null)
                entity.getContent().close();
            if (response != null)
                response.close();
        } catch (IllegalStateException | IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * GET方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static ResultSoaRest URLGet(String url, SoaRestParam params,
                                       Map<String, String> paramMap, Charset charset) {
        return HttpPost(url, params, paramMap, charset);

    }


    /**
     * 异步方式提交http
    * @param url http接口url地址
     *@param params 业务参数
     *@param paramMap 其他参数，map
     * @param charset 字符编码
     * @param futureCallback futureCallback对象
    * @author xuxinyu
    * @date 2018/2/26 21:42
    */
    public static void AsyncURLPost(final String url, SoaRestParam params,
                                    Map<String, String> paramMap, final Charset charset,
                                    final FutureCallback<HttpResponse> futureCallback) {
        //TimeWatch.start("OUT_" + TimeWatch.spcURL(url));
        final HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=" + charset.toString());
        // 将表单的值放入postMethod中

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(SoaRestParam.PARAM_MARK, params.toString()));
        nvps.add(new BasicNameValuePair(SoaRestParam.FORMAT_PREFIX, params.getFormat()));
        if (paramMap != null) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, charset.toString());
            urlEncodedFormEntity
                    .setContentType("application/x-www-form-urlencoded");
            post.setEntity(urlEncodedFormEntity);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("\n\n\n%%%%%%%%%%%%%%%AsyncURLPost send: " + url
                    + "\n%%%%%%%%%%%%%%%params:" + params.toString()
                    + "\n%%%%%%%%%%%%%%%paramMap:" + paramMap);
        }
        asyncClient.execute(post, new FutureCallback<HttpResponse>() {
            public void failed(Exception ex) {
                //TimeWatch.stop("OUT_" + TimeWatch.spcURL(url), true);
                if (logger.isDebugEnabled()) {
                    logger.error("\n%%%%%%%%%%%%%%%AsyncURLPost url: " + url
                            + "\n%%%%%%%%%%%%%%%ex:"
                            + ExceptionUtils.getStackTrace(ex));
                }
                if (futureCallback != null) {
                    futureCallback.failed(ex);
                }
                post.releaseConnection();
            }

            public void completed(HttpResponse result) {
               // TimeWatch.stop("OUT_" + TimeWatch.spcURL(url), false);
                if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    try {
                        String string = EntityUtils.toString(
                                result.getEntity(), Consts.UTF_8.toString());
                        if (logger.isDebugEnabled()) {
                            logger.debug("\n%%%%%%%%%%%%%%%AsyncURLPost url: "
                                    + url + "\n%%%%%%%%%%%%%%%status:" + string);
                        }
                    } catch (Exception e) {
                        logger.error(url + e.getMessage(), e);
                    }
                } else {
                    logger.error("\n%%%%%%%%%%%%%%%AsyncURLPost url: " + url
                            + "\n%%%%%%%%%%%%%%%status:"
                            + result.getStatusLine().getStatusCode());
                }
                if (futureCallback != null) {
                    futureCallback.completed(result);
                }
                post.releaseConnection();
            }

            public void cancelled() {
                logger.error("\n%%%%%%%%%%%%%%%AsyncURLPost state:cancelled ");
                if (futureCallback != null) {
                    futureCallback.cancelled();
                }
                post.releaseConnection();
            }
        });
    }

    /**
     * 以通用的方式进行httpPost连接，请求方式为POST，如果请求的contextType 为www-form表单形式
     * @param url http接口url地址
     *@param params 参数对象
     * @Param formOrRaw  如果为1，则请求的contextType 为www-form表单形式，如果为2，则为raw形式
     * @author xuxinyu
     * @date 2018/7/20 21:42
     */
    public static ResultSoaRest HttpPost(String url
            , Map<String,Map<String,String>> params
            , Integer formOrRaw){

        ResultSoaRest resultSoaRest = new ResultSoaRest();
        CookieStore cookieStore = new BasicCookieStore();
        HttpEntityEnclosingRequestBase httpPost = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;

        try{

            httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<>();
            if(formOrRaw == 1){

                for(Map.Entry<String,Map<String,String>> mapEntry : params.entrySet()){
                    if(mapEntry.getKey().equals("header")){
                        Map<String,String> headers = mapEntry.getValue();
                        for(Map.Entry<String,String> headMap:headers.entrySet()){
                            httpPost.addHeader(headMap.getKey(),headMap.getValue());
                        }

                    }else if(mapEntry.getKey().equals("cookie")){

                        for(Map.Entry<String,String> cookieMap:mapEntry.getValue().entrySet()){

                            BasicClientCookie cookie = new BasicClientCookie(cookieMap.getKey(), cookieMap.getValue());
                            String doMain = RegexpUtils.getSubStr("(?<=://)[A-Za-z0-9.]+(?=[^:0-9]?)",url);
                            /*if(doMain.contains("http")){
                                cookie.setDomain(doMain.substring(7));
                            }else if(doMain.contains("https")){
                                cookie.setDomain(doMain.substring(8));
                            }*/
                            cookie.setDomain(doMain);
                            cookie.setPath("/");
                            cookieStore.addCookie(cookie);
                        }
                        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

                    }else if(mapEntry.getKey().equals("param")){
                        for(Map.Entry<String,String> paramMap:mapEntry.getValue().entrySet()){
                            nvps.add(new BasicNameValuePair(paramMap.getKey(),paramMap.getValue()));
                        }
                        StringEntity stringEntity = new UrlEncodedFormEntity(nvps,Consts.UTF_8);
                        httpPost.setEntity(stringEntity);
                    }
                }

            }else if(formOrRaw == 2){

                for(Map.Entry<String,Map<String,String>> mapEntry : params.entrySet()){
                    if(mapEntry.getKey().equals("header")){
                        Map<String,String> headers = mapEntry.getValue();
                        for(Map.Entry<String,String> headMap:headers.entrySet()){
                            httpPost.addHeader(headMap.getKey(),headMap.getValue());
                        }

                    }else if(mapEntry.getKey().equals("cookie")){

                        for(Map.Entry<String,String> cookieMap:mapEntry.getValue().entrySet()){

                            BasicClientCookie cookie = new BasicClientCookie(cookieMap.getKey(), cookieMap.getValue());
                            cookie.setDomain(RegexpUtils.getSubStr("((\\w+(\\.)?)+)[^/]",url));
                            cookie.setPath("/");
                            cookieStore.addCookie(cookie);
                        }
                        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

                    }else if(mapEntry.getKey().equals("raw")){

                        String body = null;
                        for(Map.Entry<String,String> rawMap:mapEntry.getValue().entrySet()){
                            body = rawMap.getValue();
                        }
                        StringEntity stringEntity = new StringEntity(body,ContentType.create("application/json", Consts.UTF_8));
                        httpPost.setEntity(stringEntity);
                    }
                }

            }



            response = httpClient.execute(httpPost);
            entity = response.getEntity();

            Header headers[] = response.getAllHeaders();
            Map<String,String> mapHeader = new HashMap<>();
            for(Header header:headers){
                mapHeader.put(header.getName(),header.getValue());
            }

            String responseHeader = JacksonUtils.toJson(mapHeader);

            int stateCode = response.getStatusLine().getStatusCode();
            String responseBodyAsString = EntityUtils.toString(entity,Consts.UTF_8);




            if (stateCode == HttpStatus.SC_OK) {

                resultSoaRest.addAttribute("header",responseHeader);
                resultSoaRest.addAttribute("respBody",responseBodyAsString);
                resultSoaRest.setState(1000);
                resultSoaRest.setSuccess(true);

                //resultSoaRest = ResultSoaRest.strToResultSoaRest(responseBody);
            }else if(stateCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                resultSoaRest.setResultSoaRestError(SoaRestError.SERVER_INNER_ERROR);
                logger.error(stateCode + ":" + responseBodyAsString);
            } else {
                resultSoaRest.setResultSoaRestError(SoaRestError.HTTP_STATUS_ERROR);
                //resultSoaRest.addAttribute("statusCode", stateCode);
                logger.error(stateCode + ":" + responseBodyAsString);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("\n%%%%%%%%%%%%%%%http state: " + stateCode
                        + "\n%%%%%%%%%%%%%%%response:" + responseBodyAsString);
            }

        } catch (ClientProtocolException e) {
            logger.error("soa exception   %%%%% http url:" + url);
            logger.error(e.getMessage(), e);
            resultSoaRest.setResultSoaRestError(SoaRestError.GENERAL_ERROR);
            resultSoaRest.addAttribute("stacktrace",
                    ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            logger.error("soa exception   %%%%% http url:" + url);
            logger.error(e.getMessage(), e);
            resultSoaRest.setResultSoaRestError(SoaRestError.IO_ERROR);
            resultSoaRest.addAttribute("stacktrace",
                    ExceptionUtils.getStackTrace(e));
        }

        return resultSoaRest;
    }

    public static ResultSoaRest HttpGet(String url,Map<String,Map<String,String>> params ){

        ResultSoaRest resultSoaRest = new ResultSoaRest();
        CookieStore cookieStore = new BasicCookieStore();
        HttpRequestBase httpGet = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;

        try{

            httpGet = new HttpGet(url);
            for(Map.Entry<String,Map<String,String>> mapEntry : params.entrySet()){
                    if(mapEntry.getKey().equals("header")){
                        Map<String,String> headers = mapEntry.getValue();
                        for(Map.Entry<String,String> headMap:headers.entrySet()){
                            httpGet.addHeader(headMap.getKey(),headMap.getValue());
                        }

                    }else if(mapEntry.getKey().equals("cookie")){
                        for(Map.Entry<String,String> cookieMap:mapEntry.getValue().entrySet()){

                            BasicClientCookie cookie = new BasicClientCookie(cookieMap.getKey(), cookieMap.getValue());
                            cookie.setDomain(RegexpUtils.getSubStr("((\\w+(\\.)?)+)[^/]",url));
                            cookie.setPath("/");
                            cookieStore.addCookie(cookie);
                        }
                        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

                    }
                }


            response = httpClient.execute(httpGet);
            entity = response.getEntity();

            Header headers[] = response.getAllHeaders();
            Map<String,String> mapHeader = new HashMap<>();
            for(Header header:headers){
                mapHeader.put(header.getName(),header.getValue());
            }

            String responseHeader = JacksonUtils.toJson(mapHeader);

            int stateCode = response.getStatusLine().getStatusCode();
            String responseBodyAsString = EntityUtils.toString(entity,Consts.UTF_8);

            if (stateCode == HttpStatus.SC_OK) {

                resultSoaRest.addAttribute("header",responseHeader);
                resultSoaRest.addAttribute("respBody",responseBodyAsString);
                resultSoaRest.setState(1000);
                resultSoaRest.setSuccess(true);

                //resultSoaRest = ResultSoaRest.strToResultSoaRest(responseBodyAsString);
            }else if(stateCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                resultSoaRest.setResultSoaRestError(SoaRestError.SERVER_INNER_ERROR);
                logger.error(stateCode + ":" + responseBodyAsString);
            } else {
                resultSoaRest.setResultSoaRestError(SoaRestError.HTTP_STATUS_ERROR);
                //resultSoaRest.addAttribute("statusCode", stateCode);
                logger.error(stateCode + ":" + responseBodyAsString);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("\n%%%%%%%%%%%%%%%http state: " + stateCode
                        + "\n%%%%%%%%%%%%%%%response:" + responseBodyAsString);
            }

        } catch (ClientProtocolException e) {
            logger.error("soa exception   %%%%% http url:" + url);
            logger.error(e.getMessage(), e);
            resultSoaRest.setResultSoaRestError(SoaRestError.GENERAL_ERROR);
            resultSoaRest.addAttribute("stacktrace",
                    ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            logger.error("soa exception   %%%%% http url:" + url);
            logger.error(e.getMessage(), e);
            resultSoaRest.setResultSoaRestError(SoaRestError.IO_ERROR);
            resultSoaRest.addAttribute("stacktrace",
                    ExceptionUtils.getStackTrace(e));
        }

        return resultSoaRest;

    }


}
