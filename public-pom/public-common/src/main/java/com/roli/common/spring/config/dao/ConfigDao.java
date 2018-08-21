package com.roli.common.spring.config.dao;


import com.roli.common.exception.JDBCException;
import com.roli.common.exception.SecurityException;
import com.roli.common.model.PropertiesMO;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 本类用于对数据库的配置中心表进行读取。配置中心表的表结构要求如下：
 * CREATE TABLE `test_config_profiles` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `profilename` varchar(255) NOT NULL,
 `appname` varchar(255) NOT NULL,
 `ipAuth` int(10) DEFAULT '0',
 `ip` varchar(255) DEFAULT NULL,
 `key` varchar(255) DEFAULT NULL,
 `val` varchar(5000) DEFAULT NULL,
 `encrypt` int(255) DEFAULT '0',
 `reload` int(11) NOT NULL DEFAULT '1',
 `timeout` int(11) DEFAULT NULL,
 `yn` int(11) NOT NULL DEFAULT '1',
 `desc` varchar(255) DEFAULT NULL,
 `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (`id`),
 UNIQUE KEY `config_profiles_profilename_appname_key_uk` (`profilename`,`appname`,`key`,`ip`,`yn`) USING BTREE,
 KEY `config_profiles_appname_index` (`appname`),
 KEY `config_profiles_fname_index` (`profilename`)
 ) ENGINE=InnoDB AUTO_INCREMENT=15376 DEFAULT CHARSET=utf8;

 *
 *
 * @author xuxinyu
 * @Title: ConfigDao
 * @Package com.roli.common.spring.config.dao
 * @Description: ${todo}
 * @date 2018/2/1 下午7:59
 */
public class ConfigDao extends BaseDaoCon{

    private Map<String,String> mulitIp;
    private final String querySql = "SELECT profilename,appname,reload,timeout,ipAuth,ip,encrypt,"
            + "`key`,val FROM test_config_profiles where yn=1 and profilename=? and appname=? ";


    public ConfigDao(String url, String driverClass, String pwd, String name,Map<String,String> mulitIp) {
        super(url, driverClass, pwd, name);
        this.mulitIp = mulitIp;
    }

    /**
     * 查询数据库的配置中心表，将其中的配置数据统一存在Map中提供给spring处理
     * 本配置中心查询支持实时加载机制
     * 系统将启动另外一个进程，实时监控reload=1的所有配置信息
    * @param profileName 环境：dev-开发环境 test-测试环境 production-生产环境
     * @param appName 应用名称
     * @param reload 是否实时加载。reload=1表示实时加载，!=1表示不实时加载
     * @return Map<String,PropertiesMO> 配置项容器
    * @throws JDBCException 外部处理。自定义异常
    * @author xuxinyu
    * @date 2018/2/1 22:42
    */
    public Map<String,PropertiesMO> queryConfig(String profileName,String appName,
                                                Integer reload) throws JDBCException {

        try{

            Map<String,PropertiesMO> mapProperties = new LinkedHashMap<>();
            Connection connection = getConnection();

            if(connection == null){
                System.err.println("con is null.............");
                return mapProperties;
            }

            PreparedStatement preparedStatement  = null;
            String sql = querySql;

            //实时刷新进程将取reload=1的配置数据
            if(reload != null){
                sql = sql + "and reload = ?";
            }

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,profileName);
            preparedStatement.setString(2,appName);

            if(reload != null){
                preparedStatement.setInt(3,reload);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            PropertiesMO propertiesMO = null;
            while (resultSet.next()){

                propertiesMO = new PropertiesMO();
                propertiesMO.setProfileName(resultSet.getString("profilename"));
                propertiesMO.setAppName(resultSet.getString("appname"));
                propertiesMO.setKey(resultSet.getString("key"));
                propertiesMO.setVal(resultSet.getString("val"));

                if(resultSet.getString("ipauth").equals("1")){
                /*
                * IP鉴权逻辑：
                * 1、数据库ipauth字段如果为1，则需要进行ip鉴权，否则不处理ip字段
                * 2、如果进行ip鉴权，则，将会提取ip字段的值和服务器本机值进行比对
                * 3、如果比对成功，则ip字段以及本行配置都将正常处理（放入map）
                * 4、如果比对失败，则ip字段以及本行配置都将废弃，不入map，也无法被读取到
                *
                * 注意：ip字段的格式为  192.168.1.1,192.168.1.2
                * 逗号分隔多个ip地址，用于支持集群方式部署（同一个配置）
                * */

                    String ips = resultSet.getString("ip");
                    if(StringUtils.isNotBlank(ips)){
                        propertiesMO.setiP(ips);

                        boolean flag = false;
                        for(String ip : ips.split(",")){
                            if(mulitIp.containsKey(ip)){
                                flag = true;
                                break;
                            }

                        }
                        if(!flag){
                            continue;//跳出while循环
                        }

                    }

                }

                propertiesMO.setReload(resultSet.getInt("reload"));
                propertiesMO.setEncrypt(resultSet.getInt("encrypt"));
                mapProperties.put(propertiesMO.getKey(),propertiesMO);
            }

            if(connection != null){

                preparedStatement.close();
                closeConnection(connection);
            }

            return mapProperties;
        } catch (SQLException e) {
            logger.error("执行查询数据库异常！！"+e);
            e.printStackTrace();
            throw new JDBCException("执行查询数据库异常！！");
        }

    }


    public static void main(String[] args) throws JDBCException, SecurityException {

        String url = "jdbc:mysql://59.172.38.194:13306/zentao?characterEncoding=utf8";

        String driverclass = "com.mysql.jdbc.Driver";
        //String pwd = "N3hkPttLiw8y9J0lefm7hpBJmzXEW06+38inAUK8L4VzQWTUTopfqSz4N6e+LigtyxF2LJZuK2LASwy0ok/PoA==";
        String pwd = "123456";
        String username = "ddldata";
        Map<String,String> mapip = new HashMap<>();
        //mapip.put("192.168.199.211","192.168.199.211");
        mapip.put("192.168.43.201","192.168.43.201");

        ConfigDao configDAO = new ConfigDao(url,driverclass,pwd,username,mapip);
        Map<String, PropertiesMO> mappro = configDAO.queryConfig("dev","public",null);

        for(Map.Entry<String,PropertiesMO> mapEn:mappro.entrySet()){

            System.out.println(mapEn.getKey());
            System.out.println(mapEn.getValue().getVal());

        }

    }


}
