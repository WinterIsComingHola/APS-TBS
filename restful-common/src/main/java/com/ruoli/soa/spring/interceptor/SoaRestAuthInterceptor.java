package com.ruoli.soa.spring.interceptor;

import com.roli.common.exception.JDBCException;
import com.roli.common.exception.SecurityException;
import com.ruoli.soa.annotation.SoaRestAuth;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.BaseSoaRestParam;
import com.ruoli.soa.model.ResultSoaRest;
import com.ruoli.soa.model.SoaRestParam;
import com.ruoli.soa.model.enums.SoaRestError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现springmvc的拦截器，如果需要进行参数校验的接口，需要使用本拦截器进行校验
 * @author xuxinyu
 * @date 2018/2/11 下午7:40
 */
public class SoaRestAuthInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SoaRestAuthInterceptor.class);

    @Resource
    private SoaRestScheduler soaRestScheduler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        boolean doing = doCheckRequest(request, response, handler);
        /*if(doing){
            TimeWatch.start("IN_"+request.getRequestURI());
        }*/
        return doing;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
        //TimeWatch.stop("IN_"+request.getRequestURI());
    }


    /**
    * 拦截器鉴权的核心方法，对request中的所有数据进行鉴权校验
    * @param request request请求
     * @param response response响应
     * @param handler 表示controller中的持有方法的实体
    * @return boolean
    * @throws
    * @author xuxinyu
    * @date 2018/2/11 下午8:10
    */
    private boolean doCheckRequest(HttpServletRequest request
            , HttpServletResponse response
            , Object handler){

        if(handler instanceof HandlerMethod){
            //HandlerMethod表示一个controller方法的实体对象，可以转换为Object的handler
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            SoaRestAuth methedAnnotation = handlerMethod.getMethodAnnotation(SoaRestAuth.class);

            /*
                * 要求服务端的controller方法必须要加上@SOARestOauth注解
                * 如果没有加，则返回："服务端未正确标注该方法"
                * 要求服务端的controller方法必须要返回SOARestResult类型
                * 如果不是，则返回："服务端方法返回值必须为SOARestResult"
                * */
            if(methedAnnotation == null ){
                logger.error(handlerMethod.getMethod().getName() + ": SOARestOauth  not found   ");
                new ResultSoaRest(SoaRestError.ANN_SOARESTOAUTH_NULL).renderingByJsonData(response);
                return false;
            }else{

                if(handlerMethod.getReturnType().getParameterType() != ResultSoaRest.class){
                    logger.error(handlerMethod.getMethod().getName()+" : "+
                            handlerMethod.getReturnType().getParameterName() + "  not found   ");
                    new ResultSoaRest(SoaRestError.RETURNTYPE_ERROR).renderingByJsonData(response);
                    return false;
                }

                Class<? extends SoaRestParam> anntationValue = methedAnnotation.value();

                SoaRestParam willBeCheckParam = SoaRestParam.convertStrToInstance(
                        request.getParameter(BaseSoaRestParam.PARAM_MARK)
                        ,request.getParameter(BaseSoaRestParam.FORMAT_PREFIX)
                        ,anntationValue);

                try {
                    ResultSoaRest result = soaRestScheduler.checkRequest(willBeCheckParam);

                    if(result.isSuccess()){
                        request.setAttribute(BaseSoaRestParam.PARAM_MARK, willBeCheckParam);
                        return true;
                    }
                    result.renderingByJsonData(response);
                    return false;

                } catch (JDBCException e) {
                    e.printStackTrace();
                    return false;
                } catch (SecurityException e) {
                    e.printStackTrace();
                    return false;
                }

            }

        }else{
            logger.error(handler.getClass().getSimpleName()
                    + " is not  instanceof HandlerMethod....");
            new ResultSoaRest(SoaRestError.HANDLERMETHOD_ERROR).renderingByJsonData(response);
            return false;
        }

    }

}
