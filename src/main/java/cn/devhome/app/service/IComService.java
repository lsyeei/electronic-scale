package cn.devhome.app.service;

import cn.devhome.app.domain.ComPortConfig;
import cn.devhome.app.domain.ScaleValue;

/**
 * 串口服务
 * @author lishiying
 * @date 2022/8/2 002
 */
public interface IComService {
    /**
     * 打开串口
     * @param config 串口配置
     * @return true 成功， false 失败
     */
    boolean connect(ComPortConfig config);

    /**
     * 串口是否已打开
     * @return true 打开， false 未打开
     */
    boolean isConnected();

    /**
     * 关闭串口
     * @return true 成功， false 失败
     */
    boolean disconnect();

    /**
     * 读取当前秤值
     * @return 秤值
     */
    ScaleValue getScaleValue();

    /**
     * 设置websocket服务
     * @param webSocketService websocket服务
     */
//    void setWebSocketService(IScaleWebSocketService webSocketService);
}
