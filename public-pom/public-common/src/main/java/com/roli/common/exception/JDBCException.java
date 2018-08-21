package com.roli.common.exception;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/2/1 下午7:40
 */
public class JDBCException  extends Exception{

    private final long serialVersionUID = 1L;

    public JDBCException(String message){
        super(message);
    }
}
