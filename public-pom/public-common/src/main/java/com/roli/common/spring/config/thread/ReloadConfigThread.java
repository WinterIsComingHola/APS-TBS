package com.roli.common.spring.config.thread;

import com.roli.common.model.PropertiesMO;
import com.roli.common.spring.DBPropertyPlaceholderConfigurer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/2/3 22:41
 */
public class ReloadConfigThread extends Thread{

    private final Logger log = LoggerFactory.getLogger(ReloadConfigThread.class);
    private DBPropertyPlaceholderConfigurer dbConfigurer;

    public ReloadConfigThread(DBPropertyPlaceholderConfigurer dbPropertyPlaceholderConfigurer){
        this.dbConfigurer = dbPropertyPlaceholderConfigurer;
    }

    private static boolean isStart;

    public synchronized void start0(){
        if (isStart) {
            return;
        }
        isStart = true;
        System.err.println("已开启配置文件自动刷新模式，刷新间隔："
                + dbConfigurer.getReloadInterval() + "ms");
        start();
    }

    /**
     * 启动一个新的线程，进行无限循环，每次循环都是每隔一段时间，对数据库中的配置信息进行select。
     *如果有数据被检索出，则对这部分配置进行刷新处理
     * 保持配置在一段时候以后一定是最新的
     *
    * @param
    * @return
    * @throws
    * @author xuxinyu
    * @date 2018/2/3 22:46
    */
    @Override
    public void run(){

        while(true){
            try{
                Thread.sleep(dbConfigurer.getReloadInterval());
                long s = System.currentTimeMillis();
                Properties properties = dbConfigurer.getDbProperties(1);
                if (properties.isEmpty()) {
                    continue;
                }

                Map refreshProperties = dbConfigurer.refreshProperties(
                        dbConfigurer.newDBResolver(properties)
                );

                if(!refreshProperties.isEmpty()){
                    StringBuilder result = new StringBuilder("已刷新配置("
                            + (System.currentTimeMillis() - s) + ")，数量："
                            + refreshProperties.size() + "/"
                            + properties.size() + " \t" + dbConfigurer.getUrl());
                    if (dbConfigurer.isDebug()) {
                        result.append("\t参考明细："
                                + PropertiesMO.detail(refreshProperties));
                    }
                    System.err.println(result);
                }

            }catch (InterruptedException e) {
                log.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
            }

        }


    }

}
