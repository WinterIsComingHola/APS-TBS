package com.roli.common.model.enums;

/**
 * @author xuxinyu
 * @date 2018/3/5 下午7:48
 */
public enum ErrorsEnum {
    SUCCESS(1000,"处理成功"),

//============基础异常===================

    COMMON_ERROR(101,"通用错误"),
    OBJECT_NULL(102,"对象为空"),
    PARAM_NULL(103,"参数为空"),
    RESULT_ERROR(104,"返回值异常"),
    DB_ERROR(105,"数据库处理异常"),
    CACHE_ERROR(106,"缓存处理异常"),
    TIME_ERROR(107,"时间错误"),
    TIME_TASK_ERROR(108,"定时任务处理异常"),
    SECURITY_ERROR(109,"安全处理异常"),

    //===========业务类用户数据异常===============
    PASS_NOT_SAME(201,"输入的密码不一致"),
    PASS_CHECK_FAIL(200,"密码鉴权失败"),
    ACCOUNT_NULL(202,"账号为空"),
    USERNAME_NULL(203,"用户名为空"),
    ACCOUNT_DUPLICATE(204,"用户账户已经存在"),
    ACCOUNT_NONE(205,"用户账户不存在"),
    ACCOUNT_REGISTE(206,"用户注册失败请稍后再试"),
    ACCOUNT_DATA_NULL(207,"用户数据不存在"),
    RELATION_DUPLICATE(208,"项目和用户关系重复"),

    //============业务类项目数据异常================
    PROJECTNAME_NULL(301,"项目名称为空"),
    PROJECTPRIVACY_NULL(302,"项目私有归属为空"),
    PROJECT_DUPLICATE(303,"项目名称重复"),
    ISCREATE_ERROR(304,"项目创建者标识错误"),
    USERLIST_NULL(305,"用户列表为空"),

    //============业务类接口数据异常=================
    APINAME_NULL(401,"接口名称为空"),
    APIURLPATH_NULL(402,"接口路径为空"),
    APIHTTPMETHOD_NULL(403,"接口方法为空"),
    API_DUPLICATE(404,"接口名称重复"),
    URLPATH_DUPLICATE(405,"URL路径名称重复"),
    URLPATH_NULL(406,"URL为空"),
    API_NOTFIND(407,"接口不存在"),
    API_HASBEDELETE(408,"接口已删除"),

    //=============业务类接口参数异常==================
    FIELD_NULL(501,"接口字段参数名称为空"),
    FIELDTYPE_NULL(502,"接口字段类型为空"),
    FIELDISROOT_NULL(503,"一级字段标识为空"),
    FIELD_DUPLICATE(504,"字段名称重复"),
    FIELD_NOTNULL(505,"接口字段参数名称不为空"),
    FIELDVALUE_NULL(506,"参数值为空"),
    FIELD_NOT_NULL(507,"接口字段参数名称不为空"),
    FATHER_TYPE_ERROR(508,"父类型不是5或者6"),
    FIELD_TYPE_UNEXCEPTION(509,"不是正常的字段类型"),
    REQPARAM_NOT_EXIST(510,"请求参数不存在")
    ;


    private Integer errorCode;
    private String message;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ErrorsEnum(Integer errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }

}
