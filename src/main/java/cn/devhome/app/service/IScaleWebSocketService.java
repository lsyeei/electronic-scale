package cn.devhome.app.service;

import cn.devhome.app.domain.ScaleValue;

/**
 * websocket 接口
 * @author lishiying
 * @date 2022/8/2 002
 */
public interface IScaleWebSocketService {
    /**
     * 发送电子秤重量值
     * @param data
     */
    void sendScaleValue(ScaleValue data);
}
