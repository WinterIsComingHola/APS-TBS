package com.roli.common.exception;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/2/1 上午11:14
 */
public class SecurityException extends Exception{

    private final long serialVersionUID = 1L;

    /**
     * 构造一个基本的 安全异常
    * @Description:
    * @param message 异常信息
    * @return
    * @throws
    * @author xuxinyu
    * @date 2018/2/1 上午11:22
    */
    public SecurityException(String message){
        super(message);
    }



}
