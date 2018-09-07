package com.ruoli.soa.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author xuxinyu
 * @date 2018/8/29 上午11:37
 */
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Value("${websocket.sessionid}")
    private String webSessUid;

    private static Map<String,WebSocketSession> cacheWebSocketSession;

    static {
        cacheWebSocketSession = new HashMap<>();
    }

    /*
    * 链接成功以后，即开始将session存入map
    * */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Map<String,Object> handshakeAttributes = session.getAttributes();
        String uid = (String)handshakeAttributes.get(webSessUid);

        cacheWebSocketSession.put(uid,session);

        logger.info("-------------------------建立websocket链接成功-------------------------");
        logger.info("链接id为："+session.getId());

    }


    /*
    * 链接失败后，Map中的session删除
    * */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        Iterator<Map.Entry<String,WebSocketSession>> it = cacheWebSocketSession.entrySet().iterator();

        while (it.hasNext()){

            Map.Entry<String,WebSocketSession> entry = it.next();
            if(entry.getValue().getId().equals(session.getId())){
                cacheWebSocketSession.remove(entry.getKey());

                logger.info("-------------------------断开websocket链接-------------------------");
                logger.info("链接ID为："+session.getId());
            }

        }

    }


    /*
    * 执行消息发送。根据外部传入的uid进行session匹配，支持模糊匹配
    * */
    public void sendMessage(TextMessage message, String uid){
        Iterator<Map.Entry<String,WebSocketSession>> it = cacheWebSocketSession.entrySet().iterator();

        while (it.hasNext()){

            Map.Entry<String,WebSocketSession> entry = it.next();
            if(entry.getKey().contains(uid)){
                try {
                    entry.getValue().sendMessage(message);
                    logger.info("-------------------------发送消息成功-------------------------");
                    logger.info("链接ID为："+entry.getValue().getId());
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }

        }
    }

}
