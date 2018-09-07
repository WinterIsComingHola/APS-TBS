package com.ruoli.soa.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;


/**
 * @author xuxinyu
 * @date 2018/8/30 上午11:37
 */
public class WebSocketInterceptor implements HandshakeInterceptor {


    @Value("${websocket.sessionid}")
    private String webSessUid;

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest,
                                   ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler,
                                   Map<String, Object> map) throws Exception {
        String uid = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getParameter(webSessUid);
        if(uid!=null){
            map.put(webSessUid,uid);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
