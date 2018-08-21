package com.roli.apsimock.common;

import com.roli.common.exception.BusinessException;
import com.ruoli.soa.model.ResultSoaRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xuxinyu
 * @date 2018/4/23 下午8:21
 */

@ControllerAdvice
public class BusinessExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResultSoaRest userExceptionHandler(BusinessException bussinessException){

        ResultSoaRest result = new ResultSoaRest();
        result.setState(Integer.parseInt(bussinessException.getErrCode()));
        result.setSuccess(false);
        result.setMessage(bussinessException.getMessage());
        return result;
    }

}
