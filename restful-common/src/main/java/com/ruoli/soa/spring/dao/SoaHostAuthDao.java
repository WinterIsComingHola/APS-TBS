package com.ruoli.soa.spring.dao;

import com.roli.common.exception.JDBCException;
import com.roli.common.spring.config.dao.BaseDaoCon;
import com.ruoli.soa.model.SoaHostAuthModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * 本类查询配置中心的soa服务的主机名鉴权表，soa_rest_oauth表结构要求如下：
 *
 CREATE TABLE `soa_rest_oauth` (
 `APP_ID` int(11) NOT NULL AUTO_INCREMENT,
 `HOST_NAME` varchar(100) NOT NULL,
 `IP` varchar(100) DEFAULT NULL,
 `SECRET_KEY` varchar(50) NOT NULL,
 `SCOPE` varchar(255) DEFAULT 'all' COMMENT '权限默认值:all所有  多个授权用逗号隔开',
 `DESCRIPTION` varchar(100) DEFAULT NULL COMMENT '描述信息',
 `YN` tinyint(4) NOT NULL DEFAULT '1',
 `CREATED` datetime NOT NULL,
 `UPDATED` datetime NOT NULL,
 PRIMARY KEY (`APP_ID`),
 UNIQUE KEY `uk_soa_rest_oauth_host_name` (`HOST_NAME`)
 ) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8 COMMENT='SOA认证表';
 *
 *
 * @author xuxinyu
 * @date 2018/2/9 上午10:58
 */
public class SoaHostAuthDao extends BaseDaoCon{

    private static final Logger logger = LoggerFactory.getLogger(SoaHostAuthDao.class);

    private static final String SELECT_BY_HOST_NAME_SQL =
            "SELECT app_id,host_name,ip,secret_key,scope,description,yn,created,updated "
                    + "FROM test_soa_rest_oauth where host_name=?";

    private static final String SELECT_BY_APP_ID_SQL =
            "SELECT app_id,host_name,ip,secret_key,scope,description,yn,created,updated "
                    + "FROM test_soa_rest_oauth where app_id=?";

    public SoaHostAuthDao(String driverClass,String url,String userName,String passWord){
        super(url,driverClass,passWord,userName);
    }

    /**
    * 本方法对库中的soa_rest_oauth表进行查询，并将查询结果以SoaHostAuthModel返回
     *
     *
    * @param condition 数据库查询语句中的where条件，主要为host_name和app_id
     *@param hostNameOrAppId 传入1，则使用hostName进行查询，否则使用AppId查询
    * @return SoaHostAuthModel 查询结果写入SoaHostAuthModel实例并返回待用
    * @throws JDBCException 外部需要处理的异常
    * @author xuxinyu
    * @date 2018/2/9 上午11:37
    */
    public SoaHostAuthModel queryHostAuthConfig(String condition,int hostNameOrAppId)
            throws JDBCException{

        try{
            Connection connection = getConnection();
            SoaHostAuthModel soaHostAuthModel = new SoaHostAuthModel();
            if(connection == null){
                System.err.println("con is null.............");
                return soaHostAuthModel;
            }

            PreparedStatement preparedStatement  = null;
            if(hostNameOrAppId==1){
                preparedStatement = connection.prepareStatement(SELECT_BY_HOST_NAME_SQL);
                preparedStatement.setString(1,condition);
            }else{
                preparedStatement = connection.prepareStatement(SELECT_BY_APP_ID_SQL);
                preparedStatement.setString(1,condition);
            }


            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){

                soaHostAuthModel.setAppID(resultSet.getInt("app_id"));
                soaHostAuthModel.setHostName(resultSet.getString("host_name"));
                soaHostAuthModel.setIp(resultSet.getString("ip"));
                soaHostAuthModel.setSecretKey(resultSet.getString("secret_key"));
                soaHostAuthModel.setScope(resultSet.getString("scope"));
                soaHostAuthModel.setDescription(resultSet.getString("description"));
                soaHostAuthModel.setYn(resultSet.getInt("yn"));
                soaHostAuthModel.setCreated(resultSet.getDate("created"));
                soaHostAuthModel.setUpdated(resultSet.getDate("updated"));

            }

            soaHostAuthModel.setSelectTrue(true);

            if(connection != null){

                preparedStatement.close();
                closeConnection(connection);
            }

            return soaHostAuthModel;

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("执行查询数据库异常！！"+e);
            throw new JDBCException("执行查询数据库异常！！");
        }


    }


}
