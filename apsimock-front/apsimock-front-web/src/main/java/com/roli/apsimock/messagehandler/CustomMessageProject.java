package com.roli.apsimock.messagehandler;

import com.roli.apsimock.model.ApsSoaParam;
import com.roli.common.utils.json.JacksonUtils;
import com.ruoli.soa.annotation.MqHander;
import com.ruoli.soa.api.SoaRestScheduler;
import com.ruoli.soa.model.ResultSoaRest;
import com.ruoli.soa.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.support.JmsUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinyu
 * @date 2018/8/28 上午11:37
 */


@Component
public class CustomMessageProject {

    @Value("${soa.path}")
    public String SOAPATH;


    @Resource
    SoaRestScheduler soaRestScheduler;
    @Resource
    WebSocketHandler webSocketHandler;

    private static final Logger logger = LoggerFactory.getLogger(CustomMessageProject.class);

    public CustomMessageProject(){}

    @MqHander("projectMessage")
    public void addProjectNotification(Message message){

        MapMessage mapMessage = (MapMessage)message;
        Map<String,Object> mapNotify = new HashMap<>();
        ApsSoaParam apsSoaParam = new ApsSoaParam();

        try {
            logger.info("-------------------------开始接收通知消息-------------------------");
            //执行数据入库
            List<String> userAccounts = (List<String>)mapMessage.getObject("ownUsers");
            mapNotify.put("ownUsers",userAccounts);
            mapNotify.put("message",mapMessage.getString("message"));
            apsSoaParam.setBusinessParam(JacksonUtils.toJson(mapNotify));

            ResultSoaRest resultSoaRest =
                    soaRestScheduler.sendPost(SOAPATH + "project/addnotify.action", apsSoaParam);


            //执行socket推送
            for(String userAccount:userAccounts){
                TextMessage pushMessage = new TextMessage(mapMessage.getString("message"));
                webSocketHandler.sendMessage(pushMessage,userAccount);
            }

            logger.info("-------------------------接收并处理通知消息完成-------------------------");

        } catch (JMSException e) {
            throw JmsUtils.convertJmsAccessException(e);
        }
    }

}
