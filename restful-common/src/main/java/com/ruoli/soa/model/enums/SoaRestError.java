package com.ruoli.soa.model.enums;

/**
 * @author xuxinyu
 * @date 2018/2/9 下午3:21
 */
public enum SoaRestError {

    SIG_ERROR(-10000, "sig签名错误"),

    APPID_NULL(-10001, "appID不能为空"),

    APPID_OBJ_NULL(-100020, "参数对象不能为空"),

    HOSTNAME_NULL(-10002, "主机名不能为空"),

    AUTHINFO_NULL(-10003, "授权信息为空"),

    SECRETKEY_NULL(-10004, "SecretKey为空"),

    SOARESTPARAM_NULL(-10005, "SOARestParam为空"),

    SIGNATURE_CALCULATE_ERROR(-10006, "计算签名时发生错误"),

    SIG_NULL(-10007, "sig签名为空"),

    TIMESTAMP_NULL(-10008, "timestamp为空"),

    ANN_SOARESTOAUTH_NULL(-10009, "服务端未正确标注该方法"),

    SIG_CHECK_FAIL(-10010, "签名验证失败"),

    HANDLERMETHOD_ERROR(-10015, " is not  instanceof HandlerMethod...."),

    RETURNTYPE_NOTVOID(-10011, "返回值不能为void"),

    RETURNTYPE_NOTSUPPORT(-10012, "不支持的返回值类型"),

    RETURNTYPE_ERROR(-10013, "服务端方法返回值必须为SOARestResult"),

    HTTP_STATUS_ERROR(-11000, "http状态码错误"),

    IO_ERROR(-11001, "操作失败，请稍后再试"),

    GENERAL_ERROR(-12001,"内部错误，请核查异常码"),
    ENORDECODE_ERROR(-12002,"内部编解码错误"),
    SERVER_INNER_ERROR(-12003,"服务器内部HTTP500错误")
    ;

    SoaRestError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    };

    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }

    private Integer code;

    private String msg;



}
