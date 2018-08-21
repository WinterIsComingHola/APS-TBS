package com.roli.common.exception;

import com.roli.common.model.enums.ErrorsEnum;

/**
 * 业务通用异常处理，基于RuntimeException
 * @author xuxinyu
 * @date 2018/3/5 下午6:01
 */
public class BusinessException extends BaseRunTimeException{

    public BusinessException(String errCode){
        super(errCode);
    }

    public BusinessException(String errCode, String message){
        super(errCode,message);
    }

    public BusinessException(String errCode,String... params){
        super(errCode,params);
    }

    public static void throwMessage(String errCode,String... params) throws BusinessException {
        throw new BusinessException(errCode,params);
    }

    public static void throwMessage(String errCode,String message) throws BusinessException {
        throw new BusinessException(errCode,message);
    }

    public static void throwMessage(ErrorsEnum errorsEnum) throws BusinessException {
        String errorCode = String.valueOf(errorsEnum.getErrorCode());
        String message = errorsEnum.getMessage();
        throw new BusinessException(errorCode,message);
    }

}
