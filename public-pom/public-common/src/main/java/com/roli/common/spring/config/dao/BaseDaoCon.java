package com.roli.common.spring.config.dao;

import com.roli.common.exception.JDBCException;
import com.roli.common.exception.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/2/1 下午7:34
 */
public class BaseDaoCon {

    protected final Logger logger = LoggerFactory.getLogger(BaseDaoCon.class);

    private String url;
    private String driverClass;
    private String pwd;
    private String name;


    public BaseDaoCon(String url, String driverClass, String pwd, String name){
        this.url = url;
        this.driverClass = driverClass;
        this.pwd = pwd;
        this.name = name;
    }

    /**
     * 获取到jdbc连接
    * @Description:
    * @param
    * @return Connection jdbc连接
    * @throws JDBCException jdbc异常
    * @author xuxinyu
    * @date 2018/2/1 下午7:45
    */
    public Connection getConnection() throws JDBCException{

        try{
            Class.forName(driverClass);
            Connection conn = DriverManager.getConnection(url,name,pwd);
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("连接数据库异常！！"+e);
            throw new JDBCException("连接数据库异常！！");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("连接数据库异常！！"+e);
            throw new JDBCException("连接数据库异常！！");
        }

    }


    public void closeConnection(Connection connection) throws JDBCException{

        if(!(connection == null)){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("关闭数据库异常！！"+e);
                throw new JDBCException("关闭数据库异常！！");
            }

        }

    }


    //==================getter========================


    public String getUrl() {
        return url;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getPwd() {
        return pwd;
    }

    public String getName() {
        return name;
    }
}
