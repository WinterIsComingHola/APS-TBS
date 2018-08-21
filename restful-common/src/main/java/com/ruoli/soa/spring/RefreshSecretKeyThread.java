package com.ruoli.soa.spring;

import com.ruoli.soa.api.SoaRestScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 定时刷新DB中的鉴权数据
 * @author xuxinyu
 * @date 2018/2/11 22:08
 */
@Component
public class RefreshSecretKeyThread extends Thread implements BeanFactoryAware{

    private static final Logger logger = LoggerFactory.getLogger(RefreshSecretKeyThread.class);

    public RefreshSecretKeyThread(){
        this.setDaemon(true);
    }

    private boolean start = false;

    private int interval = 60 * 1 * 1000;

    private int slow_interval=60 * 30 * 1000;

    @Resource
    SoaRestScheduler soaRestScheduler;

    @PostConstruct
    public synchronized void start0(){

        if(start){
            return;
        }
        start = true;
        logger.error("restful oauth:DaemonThread start.....");
        start();
    }

    @Override
    public void run(){
        while (true) {
            try {
                if(soaRestScheduler.refreshSecretKey()){
                    logger.error("刷新本服务器主机名称完成，刷新本机成功，即将进入下一次刷新，间隔30分钟.....................................");
                    Thread.currentThread().sleep(slow_interval);
                }else{
                    logger.error("刷新本服务器主机名称完成，刷新本机失败，即将进入下一次刷新，间隔1分钟.....................................");
                    Thread.currentThread().sleep(interval);
                }
            } catch (Exception e) {
                try {
                    Thread.currentThread().sleep(interval);
                } catch (Exception e2) {
                }
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        start0();
    }

}
