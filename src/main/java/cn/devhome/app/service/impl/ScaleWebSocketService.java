package cn.devhome.app.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import cn.devhome.app.domain.EventData;
import cn.devhome.app.domain.ScaleValue;
import cn.devhome.app.enums.EventDataType;
import cn.devhome.app.service.IScaleWebSocketService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 称重Websocket接口
 * @author lishiying
 * @date 2022/8/2 002
 */
@Slf4j
@Service
@ServerEndpoint(value = "/material/scale")
public class ScaleWebSocketService implements IScaleWebSocketService {
    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    /**
     * 存放所有在线的客户端
     */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    /**
     * 建立连接的方法
     * @param session 会话对象
     */
    @OnOpen
    public void onOpen(Session session) {
        // 在线数加1
        onlineCount.incrementAndGet();
        clients.put(session.getId(), session);
        log.trace("新连接加入：{}，当前在线数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 连接关闭
     * @param session 会话对象
     */
    @OnClose
    public void onClose(Session session) {
        // 在线数减1
        onlineCount.decrementAndGet();
        clients.remove(session.getId());
        log.trace("关闭一个连接：{}，当前在线数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.trace("收到客户端[{}]的消息:{}", session.getId(), message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket接口发生错误，异常代码：{}", ExceptionUtils.getStackTrace(error));
    }

    @Override
    public void sendScaleValue(ScaleValue data){
        // 广播计数值
        clients.forEach((id,sess)->{
            try {
                sess.getAsyncRemote().sendText(JSON.toJSONString(data));
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * 处理通知数据
     * @param event 通知事件
     */
    @EventListener(EventData.class)
    public void receiveEvent(EventData event){
        if (EventDataType.SCALE_VALUE.equals(event.getType())) {
            sendScaleValue((ScaleValue) event.getData());
        }
    }
}
